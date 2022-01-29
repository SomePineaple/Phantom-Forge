package dev.somepineaple.phantom.main.modules.movement;

import dev.somepineaple.phantom.main.modules.Category;
import dev.somepineaple.phantom.main.modules.Module;
import dev.somepineaple.phantom.main.modules.Setting;
import net.minecraft.client.settings.KeyBinding;

public class Sprint extends Module {
    private final Setting rage = addSetting(new Setting("Rage", false));

    public Sprint() {
        super("Sprint", Category.MOVEMENT, -1);
    }

    @Override
    public void update() {
        if (rage.booleanVal())
            mc.thePlayer.setSprinting(true);
        else {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
            KeyBinding.onTick(mc.gameSettings.keyBindSprint.getKeyCode());
        }
    }

    @Override
    protected void onDisable() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
        KeyBinding.onTick(mc.gameSettings.keyBindSprint.getKeyCode());
    }
}
