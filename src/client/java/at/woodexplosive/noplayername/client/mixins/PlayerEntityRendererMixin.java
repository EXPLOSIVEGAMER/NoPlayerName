package at.woodexplosive.noplayername.client.mixins;

import at.woodexplosive.noplayername.client.NoPlayerNameConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class PlayerEntityRendererMixin {

    @Inject(method = "submitNameDisplay*", at = @At("HEAD"), cancellable = true)
    private void hidePlayerNames(AvatarRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
        if (NoPlayerNameConfig.hideNametags) {
            ci.cancel();
        }
    }
}