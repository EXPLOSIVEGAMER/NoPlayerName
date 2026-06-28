package at.woodexplosive.noplayername.client;

import at.woodexplosive.noplayername.Noplayername;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Screenshot;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class NoplayernameClient implements ClientModInitializer {
    private static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath(Noplayername.MOD_ID, "options"));

    private boolean orignalHideState;
    private boolean isScreenshotScheduled = false;

    private static final KeyMapping toggleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.noplayername.toggle",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            CATEGORY
    ));

    private static final KeyMapping screenShotKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.noplayername.take_screenshot",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F10,
            CATEGORY
    ));

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleKey.consumeClick()) {
                NoPlayerNameConfig.hideNametags = !NoPlayerNameConfig.hideNametags;
                Component state = NoPlayerNameConfig.hideNametags ? Component.translatable("noplayername.message.off") : Component.translatable("noplayername.message.on");

                if (client.player != null) {
                    client.player.sendSystemMessage(Component.translatable("noplayername.message", state));
                }
            }

            if (screenShotKey.consumeClick()) {

                orignalHideState = NoPlayerNameConfig.hideNametags;
                NoPlayerNameConfig.hideNametags = true;
                isScreenshotScheduled = true;

            } else if (isScreenshotScheduled) {

                isScreenshotScheduled = false;

                Screenshot.grab(
                        client.gameDirectory,
                        client.gameRenderer.mainRenderTarget(),
                        message -> client.execute(() -> client.showDebugChat(message))
                );

                NoPlayerNameConfig.hideNametags = orignalHideState;
            }
        });
    }
}
