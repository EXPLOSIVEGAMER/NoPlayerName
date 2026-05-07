package at.woodexplosive.noplayername.client;

import at.woodexplosive.noplayername.Noplayername;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class NoplayernameClient implements ClientModInitializer {
    private boolean orignalHideState;
    private boolean isScreenshotScheduled = false;

    private static final KeyBinding.Category CATEGORY =
            KeyBinding.Category.create(Identifier.of(Noplayername.MOD_ID, "options"));

    private static final KeyBinding toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.noplayername.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            CATEGORY
    ));

    private static final KeyBinding screenShotKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.noplayername.take_screenshot",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F10,
            CATEGORY
    ));

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleKey.wasPressed()) {
                NoPlayerNameConfig.HIDE_NAME_TAGS = !NoPlayerNameConfig.HIDE_NAME_TAGS;
                MutableText state = NoPlayerNameConfig.HIDE_NAME_TAGS ? Text.translatable("noplayername.message.off") : Text.translatable("noplayername.message.on");

                if (client.player != null) {
                    client.player.sendMessage(Text.translatable("noplayername.message", state), false);
                }
            }

            if (screenShotKey.wasPressed()) {

                orignalHideState = NoPlayerNameConfig.HIDE_NAME_TAGS;
                NoPlayerNameConfig.HIDE_NAME_TAGS = true;
                isScreenshotScheduled = true;

            } else if (isScreenshotScheduled) {

                isScreenshotScheduled = false;

                ScreenshotRecorder.saveScreenshot(
                        client.runDirectory,
                        client.getFramebuffer(),
                        message -> client.execute(() -> client.inGameHud.getChatHud().addMessage(message))
                );

                NoPlayerNameConfig.HIDE_NAME_TAGS = orignalHideState;
            }

        });
    }
}
