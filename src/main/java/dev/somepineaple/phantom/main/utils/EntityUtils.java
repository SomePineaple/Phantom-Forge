package dev.somepineaple.phantom.main.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

public class EntityUtils {
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	public static float[] getRotations(EntityLivingBase target) {
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
