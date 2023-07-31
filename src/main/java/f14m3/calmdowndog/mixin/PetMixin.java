package f14m3.calmdowndog.mixin;

import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnimalEntity.class)
public class PetMixin {
	@Inject(method="interactMob(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
	public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cil) {
		World world = player.getWorld();
		AnimalEntity entity = ((AnimalEntity)(Object)this);
		if (!(entity instanceof TameableEntity || entity instanceof ParrotEntity) ) {
			return;
		}
		TameableEntity tameableEntity = ((TameableEntity)(Object)this);
		if (player.isSneaking() && tameableEntity.isTamed()) {
			world.sendEntityStatus(tameableEntity, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
			if (tameableEntity.isOwner(player) && tameableEntity instanceof Angerable && !world.isClient()) {
				((Angerable)tameableEntity).stopAnger();
			}
			player.swingHand(Hand.MAIN_HAND, !world.isClient);
			cil.setReturnValue(ActionResult.CONSUME);
		}
	}
}