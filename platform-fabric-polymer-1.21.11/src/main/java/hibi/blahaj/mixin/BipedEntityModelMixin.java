package hibi.blahaj.mixin;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import hibi.blahaj.CuddlyItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;

@Mixin(HumanoidModel.class)
public abstract class BipedEntityModelMixin<T extends HumanoidRenderState> {

	@Shadow
	public @Final ModelPart rightArm;

	@Shadow
	public @Final ModelPart leftArm;

	@Shadow
	public abstract ModelPart getArm(HumanoidArm humanoidArm);

	@Inject(
		method = {"poseRightArm", "poseLeftArm"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/model/AnimationUtils;animateCrossbowHold(Lnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/model/geom/ModelPart;Z)V",
			shift = Shift.AFTER
		),
		cancellable = true
	)
	public void poseArms(T humanoidRenderState, CallbackInfo ci) {
		if(humanoidRenderState.rightHandItemStack.getItem() instanceof CuddlyItem || humanoidRenderState.leftHandItemStack.getItem() instanceof CuddlyItem) {
			this.rightArm.xRot = -0.95F;
			this.rightArm.yRot = (float) (-Math.PI / 8);
			this.leftArm.xRot = -0.90F;
			this.leftArm.yRot = (float) (Math.PI / 8);
			ci.cancel();
		}
	}
}
