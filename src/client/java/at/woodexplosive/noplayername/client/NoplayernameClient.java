package at.woodexplosive.noplayername.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Type;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Screenshot;
import net.minecraft.client.KeyMapping.Category;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class NoplayernameClient implements ClientModInitializer {
    private static final KeyMapping.Category CATEGORY = Category.register(Identifier.fromNamespaceAndPath("noplayername", "options"));
    private boolean orignalHideState;
    private boolean isScreenshotScheduled = false;
    private static final KeyMapping toggleKey;
    private static final KeyMapping screenShotKey;

    public NoplayernameClient() {
    }

    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (toggleKey.consumeClick()) {
                NoPlayerNameConfig.hideNametags = !NoPlayerNameConfig.hideNametags;
                Component state = NoPlayerNameConfig.hideNametags ? Component.translatable("noplayername.message.off") : Component.translatable("noplayername.message.on");
                if (client.player != null) {
                    client.player.sendSystemMessage(Component.translatable("noplayername.message", state));
                }
            }

            if (screenShotKey.consumeClick()) {
                this.orignalHideState = NoPlayerNameConfig.hideNametags;
                NoPlayerNameConfig.hideNametags = true;
                this.isScreenshotScheduled = true;
            } else if (this.isScreenshotScheduled) {
                this.isScreenshotScheduled = false;
                Screenshot.grab(client.gameDirectory, client.gameRenderer.mainRenderTarget(), (message) -> client.execute(() -> client.showDebugChat(message)));
                NoPlayerNameConfig.hideNametags = this.orignalHideState;
            }

        });
    }

    static {
        toggleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.noplayername.toggle", Type.KEYBOARD, InputConstants.KEY_N, CATEGORY));
        screenShotKey = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.noplayername.take_screenshot", Type.KEYBOARD, InputConstants.KEY_F10, CATEGORY));
    }
}
