package f14m3.calmdowndog.mixin;

import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


// Have to use a seperate mixin for parrots, because they are different.
@Mixin(ParrotEntity.class)
public class PetParrotMixin {
	@Inject(method="interactMob(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
	public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cil) {
		World world = player.getWorld();
		ParrotEntity parrot = ((ParrotEntity)(Object)this);
		if (player.isSneaking() && parrot.isTamed()) {
			world.sendEntityStatus(parrot, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
			player.swingHand(Hand.MAIN_HAND, !world.isClient);
			cil.setReturnValue(ActionResult.CONSUME);
		}
	}
}