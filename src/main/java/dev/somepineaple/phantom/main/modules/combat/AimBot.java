package dev.somepineaple.phantom.main.modules.combat;

import dev.somepineaple.phantom.main.modules.Category;
import dev.somepineaple.phantom.main.modules.Module;
import dev.somepineaple.phantom.main.modules.Setting;
import dev.somepineaple.phantom.main.utils.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class AimBot extends Module {
	private final Setting range = addSetting(new Setting("Range", 4.5f, 3f, 8f));
	private final Setting monsters = addSetting(new Setting("Monsters", false));
	private final Setting passives = addSetting(new Setting("Passives", false));
	private final Setting dead = addSetting(new Setting("Dead", false));
	
	public AimBot() {
		super("Aim Bot", Category.COMBAT, -1);
	}
	
	@Override
	public void update() {
		EntityLivingBase target = null;
		float targetDistance = range.floatVal();

		for (Entity e : mc.theWorld.loadedEntityList) {
			if ((e instanceof EntityPlayer || (e instanceof EntityMob && monsters.booleanVal()) ||
				(e instanceof EntityAnimal && passives.booleanVal())) &&
				 e != mc.thePlayer && !(!dead.booleanVal() && e.isDead) &&
				 e.getDistanceToEntity(mc.thePlayer) < targetDistance) {
				
				if (e instanceof EntityPlayer && !PlayerUtils.teamsCheck((EntityPlayer) e))
					continue;
				
				target = (EntityLivingBase) e;
				targetDistance = e.getDistanceToEntity(mc.thePlayer);
			}
		}

		if (target != null) {
			float[] rotations = getRotations(target);

			mc.thePlayer.rotationYaw = rotations[0];
			mc.thePlayer.rotationPitch = rotations[1];
		}
	}

	public float[] getRotations(EntityLivingBase target) {
		double deltaX = target.posX + target.posX - target.lastTickPosX - mc.thePlayer.posX;
		double deltaY = target.posY - 3.5 + target.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
		double deltaZ = target.posZ + target.posZ - target.lastTickPosZ - mc.thePlayer.posZ;
		double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));

		float yaw = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ));
		float pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));

		double mathStuffs = Math.toDegrees(Math.atan(deltaZ / deltaX));
		if (deltaX < 0 && deltaZ < 0) {
			yaw = (float) (90 + mathStuffs);
		} else if (deltaX > 0 && deltaZ < 0) {
			yaw = (float) (-90 + mathStuffs);
		}

		return new float[] {yaw, pitch};
	}
}
