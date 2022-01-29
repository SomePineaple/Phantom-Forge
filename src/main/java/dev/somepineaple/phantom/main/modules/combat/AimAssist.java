package dev.somepineaple.phantom.main.modules.combat;

import dev.somepineaple.phantom.main.modules.Category;
import dev.somepineaple.phantom.main.modules.Module;
import dev.somepineaple.phantom.main.modules.Setting;
import dev.somepineaple.phantom.main.utils.EntityUtils;
import dev.somepineaple.phantom.main.utils.PlayerUtils;
import dev.somepineaple.phantom.main.utils.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;

public class AimAssist extends Module{
	private final Setting range = addSetting(new Setting("Range", 4.5f, 0f, 8f));
	private final Setting FOV = addSetting(new Setting("FOV", 100, 0, 180));
	private final Setting hSpeed = addSetting(new Setting("HSpeed", 10f, 0f, 50f));
	private final Setting vSpeed = addSetting(new Setting("VSpeed", 10f, 0f, 50f));
	private final Setting onlyOnClick = addSetting(new Setting("Only Click", true));
	private final Setting center = addSetting(new Setting("Center", false));
	private final Setting monsters = addSetting(new Setting("Monsters", false));
	private final Setting dead = addSetting(new Setting("Dead", false));
	
	public AimAssist() {
		super("Aim Assist", Category.COMBAT, -1);
	}
	
	@Override
	public void render() {
		if (!center.booleanVal() && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit.equals(MovingObjectPosition.MovingObjectType.ENTITY))
			return;

		if ((onlyOnClick.booleanVal() && !Mouse.isButtonDown(0)) || mc.currentScreen != null) {
			return;
		}

		float closestDistance = range.floatVal();
		EntityLivingBase bestEntity = null;
		
		for (Entity e : mc.theWorld.loadedEntityList) {
			if (e == mc.thePlayer) continue;
			if (e instanceof EntityLivingBase && e.getDistanceToEntity(mc.thePlayer) < closestDistance) {
				if ((e instanceof EntityMob && monsters.booleanVal() || e instanceof EntityPlayer) &&
				    isInFOV((EntityLivingBase) e) && !(!dead.booleanVal() && e.isDead)) {
					if (e instanceof EntityPlayer && !PlayerUtils.teamsCheck((EntityPlayer) e))
						continue;
					bestEntity = (EntityLivingBase) e;
					closestDistance = e.getDistanceToEntity(mc.thePlayer);
				}
			}
		}

		if (bestEntity != null) {
			rotate(bestEntity);
		}
	}
	
	private void rotate(EntityLivingBase e) {
		float[] fullRotations = EntityUtils.getRotations(e);
		float currentYaw = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw);
		float currentPitch = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch);

		int direction = getDirection(fullRotations[0], currentYaw);

		mc.thePlayer.rotationYaw += Math.min(hSpeed.floatVal() / Minecraft.getDebugFPS(), Math.abs(fullRotations[0] - currentYaw)) * direction;
		
		if (fullRotations[1] > currentPitch) {
			mc.thePlayer.rotationPitch += vSpeed.floatVal() / Minecraft.getDebugFPS();
			mc.thePlayer.rotationPitch = Math.min(mc.thePlayer.rotationPitch, fullRotations[1]);
		} else if (fullRotations[1] < currentPitch) {
			mc.thePlayer.rotationPitch -= vSpeed.floatVal() / Minecraft.getDebugFPS();
			mc.thePlayer.rotationPitch = Math.max(mc.thePlayer.rotationPitch, fullRotations[1]);
		}
	}

	public static int getDirection(float targetYaw, float currentYaw) {
		if (currentYaw >= 0) {
			if (targetYaw > currentYaw)
				return 1;
			if (-180 + currentYaw < targetYaw)
				return -1;
			else
				return 1;
		} else {
			if (targetYaw < currentYaw)
				return -1;
			if (180 + currentYaw > targetYaw)
				return 1;
			else
				return -1;
		}
	}

	private boolean isInFOV(EntityLivingBase entity) {
		float playerYaw = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw) + 180;
		float targetYaw = EntityUtils.getRotations(entity)[0] + 180;

		float diff = Math.abs(RotationUtils.getAngleDiff(playerYaw, targetYaw));

		return diff < FOV.floatVal();
	}
}
