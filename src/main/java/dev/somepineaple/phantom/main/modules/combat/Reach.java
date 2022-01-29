package dev.somepineaple.phantom.main.modules.combat;

import dev.somepineaple.phantom.main.modules.Category;
import dev.somepineaple.phantom.main.modules.Module;
import dev.somepineaple.phantom.main.modules.Setting;

public class Reach extends Module {
    private final Setting reach = addSetting(new Setting("Hits", 3.2f, 3.0f, 6.0f));
    private final Setting buildReach = addSetting(new Setting("Blocks", 4.5f, 4.5f, 6.0f));

    public Reach() {
        super("Reach", Category.COMBAT, -1);
    }

    public float getMaxReach() {
        return Math.max(reach.floatVal(), buildReach.floatVal());
    }

    public float getBuildReach() {
        return buildReach.floatVal();
    }

    public float getCombatReach() {
        return reach.floatVal();
    }
}
