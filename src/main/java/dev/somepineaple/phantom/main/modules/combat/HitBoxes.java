package dev.somepineaple.phantom.main.modules.combat;

import dev.somepineaple.phantom.main.modules.Category;
import dev.somepineaple.phantom.main.modules.Module;
import dev.somepineaple.phantom.main.modules.Setting;

public class HitBoxes extends Module {
    public HitBoxes() {
        super("HitBoxes", Category.COMBAT, -1);
        addSetting(new Setting("Expand", 0.4f, 0.0f, 1f));
    }
}
