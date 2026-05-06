package at.woodexplosive.noplayername.client.Mixins;

import at.woodexplosive.noplayername.client.NoPlayerNameConfig;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {

    @Inject(method = "renderLabelIfPresent*", at = @At("HEAD"), cancellable = true)
    private void hidePlayerNames(EntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraRenderState, CallbackInfo ci) {
        EntityType<?> entityType = state.entityType;
        if (entityType == EntityType.PLAYER) {
            if (NoPlayerNameConfig.hideNametags) {
                ci.cancel();
            }
        }
    }
}