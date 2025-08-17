package at.woodexplosive.noplayername.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class NoplayernameClient implements ClientModInitializer {

    private static final KeyBinding toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.noplayername.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            "category.noplayername"
    ));

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                NoPlayerNameConfig.hideNametags = !NoPlayerNameConfig.hideNametags;
                client.player.sendMessage(Text.of("Nametags verstecken: " + (NoPlayerNameConfig.hideNametags ? "AN" : "AUS")), false);
            }
        });
    }
}
