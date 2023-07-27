package f14m3.calmdowndog.mixin;

import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WolfEntity.class)
public class PetDogMixin {
	@Inject(method="interactMob(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
	public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cil) {
		World world = player.getWorld();
		WolfEntity wolf = ((WolfEntity)(Object)this);
		if (player.isSneaking() && wolf.isTamed() && !world.isClient()) {
			world.sendEntityStatus(wolf, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
			if (wolf.isOwner(player)) {
				wolf.stopAnger();
			}
			cil.setReturnValue(ActionResult.SUCCESS);
		}
	}
}