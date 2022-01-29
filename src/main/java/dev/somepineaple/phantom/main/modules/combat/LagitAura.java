package dev.somepineaple.phantom.main.modules.combat;

import java.util.concurrent.ThreadLocalRandom;

import dev.somepineaple.phantom.main.modules.Category;
import dev.somepineaple.phantom.main.modules.Module;
import dev.somepineaple.phantom.main.modules.Setting;
import dev.somepineaple.phantom.main.utils.EntityUtils;
import dev.somepineaple.phantom.main.utils.MSTimer;
import dev.somepineaple.phantom.main.utils.PlayerUtils;
import dev.somepineaple.phantom.main.utils.RotationUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class LagitAura extends Module {
	private EntityLivingBase target = null;
	private final Setting cps = addSetting(new Setting("CPS", 12.0f, 0.0f, 20.0f));
	private final Setting rotRange = addSetting(new Setting("Rotate Range", 4.5f, 3f, 8f));
	@SuppressWarnings("unused")
	private final Setting hitRange = addSetting(new Setting("Hit Range", 3.2f, 3f, 6f));
	private final Setting rotSpeed = addSetting(new Setting("Speed", 180f, 0f, 180f));
	private final Setting fov = addSetting(new Setting("FOV", 180, 0, 180));
	private final Setting rotateMode = addSetting(new Setting("Rotate Mode", "SlowCenter", "Edge", "Center"));
	private final Setting players = addSetting(new Setting("Players", true));
	private final Setting mobs = addSetting(new Setting("Mobs", false));
	
	public LagitAura() {
		super("LagitAura", Category.COMBAT, -1);
		
		// Some autoclicker shit
		lastClick = new MSTimer();
        hold = new MSTimer();
        eventTimer = new MSTimer();
        isDropping = false;
        isSpiking = false;
	}
	
	// Aiming
	@Override
	public void update() {
		target = null;
		float closest = rotRange.floatVal();
		
		boolean isOverEntity = mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit.equals(MovingObjectType.ENTITY);
		
		if (rotateMode.getCurrentMultiSelect().equalsIgnoreCase("Edge") && isOverEntity)
			return;
		
		for (Entity e : mc.theWorld.loadedEntityList) {
			if ((e instanceof EntityPlayer && players.booleanVal()) || (e instanceof EntityMob && mobs.booleanVal()) ) {
				if (!(e.getDistanceToEntity(mc.thePlayer) < closest) || 
					!(isInFOV((EntityLivingBase)e)) ||
					e == mc.thePlayer)
					continue;
				
				if (e instanceof EntityPlayer && !PlayerUtils.teamsCheck((EntityPlayer)e))
					continue;
				
				closest = e.getDistanceToEntity(mc.thePlayer);
				target = (EntityLivingBase) e;
			}
		}
		
		if (target != null) {
			rotate(target, (isOverEntity && rotateMode.getCurrentMultiSelect().equalsIgnoreCase("SlowCenter")) ? 0.5f : rotSpeed.floatVal());
		}
	}
	
	private void rotate(EntityLivingBase e, float speed) {
		float[] fullRotations = EntityUtils.getRotations(e);
		float currentYaw = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw);
		float currentPitch = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch);

		int direction = AimAssist.getDirection(fullRotations[0], currentYaw);

		mc.thePlayer.rotationYaw += Math.min(speed, Math.abs(fullRotations[0] - currentYaw)) * direction;
		
		if (fullRotations[1] > currentPitch) {
			mc.thePlayer.rotationPitch += speed;
			mc.thePlayer.rotationPitch = Math.min(mc.thePlayer.rotationPitch, fullRotations[1]);
		} else if (fullRotations[1] < currentPitch) {
			mc.thePlayer.rotationPitch -= speed;
			mc.thePlayer.rotationPitch = Math.max(mc.thePlayer.rotationPitch, fullRotations[1]);
		}
	}
	
	private boolean isInFOV(EntityLivingBase entity) {
		float playerYaw = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw) + 180;
		float targetYaw = EntityUtils.getRotations(entity)[0] + 180;

		float diff = Math.abs(RotationUtils.getAngleDiff(playerYaw, targetYaw));

		return diff < fov.floatVal();
	}
	
	// Autoclicker
	@Override
	protected void onEnable() {
		updateVals();
	}
	
	private final MSTimer lastClick;
    private final MSTimer hold;
    private double speed, holdLength, min, max;
    private final MSTimer eventTimer;
    private boolean isSpiking, isDropping;
	
	@Override
	public void render() {
		if (target == null)
			return;
		
		int key = mc.gameSettings.keyBindAttack.getKeyCode();
		
		if (lastClick.hasTimePassed((long) (speed * 1000))) {
            lastClick.reset();
            if (hold.timePassed() < lastClick.timePassed())
                hold.setLastMS(lastClick.timePassed());
            KeyBinding.setKeyBindState(key, true);
            KeyBinding.onTick(key);
            updateVals();
        } else if (hold.hasTimePassed((long) (holdLength * 1000))) {
            KeyBinding.setKeyBindState(key, false);
            updateVals();
        }
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
