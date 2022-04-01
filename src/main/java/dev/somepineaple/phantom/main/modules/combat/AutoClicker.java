package dev.somepineaple.phantom.main.modules.combat;

import dev.somepineaple.phantom.main.modules.Category;
import dev.somepineaple.phantom.main.modules.Module;
import dev.somepineaple.phantom.main.modules.Setting;
import dev.somepineaple.phantom.main.utils.MSTimer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

import org.lwjgl.input.Mouse;

import java.util.concurrent.ThreadLocalRandom;

public class AutoClicker extends Module {
    private static final int KEY = mc.gameSettings.keyBindAttack.getKeyCode();

    private final Setting cps = addSetting(new Setting("CPS", 12f, 0f, 20f));
    private final Setting breakBlocks = addSetting(new Setting("Break Blocks", false));

    private final MSTimer lastClick;
    private final MSTimer hold;
    private double speed, holdLength, min, max;
    private final MSTimer eventTimer;
    private boolean isSpiking, isDropping;

    public AutoClicker() {
        super("Auto Clicker", Category.COMBAT, -1);
        lastClick = new MSTimer();
        hold = new MSTimer();
        eventTimer = new MSTimer();
        isDropping = false;
        isSpiking = false;
    }

    @Override
    public void render() {
    	if (breakBlocks.booleanVal() && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit.equals(MovingObjectType.BLOCK))
    		return;
    	
        if (Mouse.isButtonDown(0)) {
            if (lastClick.hasTimePassed((long) (speed * 1000))) {
                lastClick.reset();
                if (hold.timePassed() < lastClick.timePassed())
                    hold.setLastMS(lastClick.timePassed());
                KeyBinding.setKeyBindState(KEY, true);
                KeyBinding.onTick(KEY);
                updateVals();
            } else if (hold.hasTimePassed((long) (holdLength * 1000))) {
                KeyBinding.setKeyBindState(KEY, false);
                updateVals();
            }
        }
    }

    @Override
    protected void onEnable() {
        updateVals();
    }

    private void updateVals() {
        max = cps.floatVal() + 2;
        min = cps.floatVal() - 2;

        if (eventTimer.hasTimePassed((long) (3000 * ThreadLocalRandom.current().nextDouble(0.8, 1.2)))) {
            eventTimer.reset();
            int rn = ThreadLocalRandom.current().nextInt(100);
            if (rn < 25) {
                isSpiking = true;
                isDropping = false;
            } else if (rn < 75) {
                isSpiking = false;
                isDropping = false;
            } else {
                isSpiking = false;
                isDropping = true;
            }
        }

        if (isSpiking) {
            max = cps.floatVal() + 4;
            min = cps.floatVal();
        } else if (isDropping) {
            max = cps.floatVal();
            min = cps.floatVal() - 4;
        }

        speed = 1.0 / ThreadLocalRandom.current().nextDouble(min, max);
        holdLength = speed / ThreadLocalRandom.current().nextDouble(min, max);
    }
}
