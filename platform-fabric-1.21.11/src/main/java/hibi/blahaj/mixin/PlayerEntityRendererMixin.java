package hibi.blahaj.mixin;

import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import hibi.blahaj.CuddlyItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

@Mixin(AvatarRenderer.class)
public class PlayerEntityRendererMixin {
	@Inject(
		method = "getArmPose(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;",
		at = @At("TAIL"),
		cancellable = true
	)
	private static void cuddleBlahaj(Avatar avatar, ItemStack itemStack, InteractionHand interactionHand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
		ItemStack lv = avatar.getItemInHand(interactionHand);
		if(lv.getItem() instanceof CuddlyItem) {
			cir.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_HOLD); // Hold crossbow
			cir.cancel();
		}
	}
}
