package hibi.blahaj.mixin;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import hibi.blahaj.CuddlyItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

@Mixin(PlayerRenderer.class)
public class PlayerEntityRendererMixin {
	@Inject(
		method = "getArmPose",
		at = @At("TAIL"),
		cancellable = true
	)
	private static void cuddleBlahaj(AbstractClientPlayer abstractClientPlayer, InteractionHand interactionHand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
		ItemStack lv = abstractClientPlayer.getItemInHand(interactionHand);
		if(lv.getItem() instanceof CuddlyItem) {
			cir.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_HOLD); // Hold crossbow
			cir.cancel();
		}
	}
}
