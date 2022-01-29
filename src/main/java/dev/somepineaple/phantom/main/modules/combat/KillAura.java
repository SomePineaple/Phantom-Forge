package dev.somepineaple.phantom.main.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

import dev.somepineaple.phantom.main.events.EventMotion;
import dev.somepineaple.phantom.main.modules.Category;
import dev.somepineaple.phantom.main.modules.Module;
import dev.somepineaple.phantom.main.modules.Setting;
import dev.somepineaple.phantom.main.utils.MSTimer;
import dev.somepineaple.phantom.main.utils.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class KillAura extends Module {
	private final Setting range = addSetting(new Setting("Range", 4.5f, 0f, 6f));
	private final Setting cps = addSetting(new Setting("CPS", 10, 0, 20));
	private final Setting rotate = addSetting(new Setting("Rotate", true));
	private final Setting monsters = addSetting(new Setting("Monsters", false));
	private final Setting dead = addSetting(new Setting("Dead", false));

	private final ArrayList<EntityLivingBase> targets = new ArrayList<>();

	private final MSTimer timer = new MSTimer();

	public KillAura() {
		super("Kill Aura", Category.COMBAT, -1);
	}

	@Override
	public void update() {
		targets.clear();
		for (Entity e : mc.theWorld.loadedEntityList) {
			if ((e instanceof EntityPlayer || (e instanceof EntityMob && monsters.booleanVal())) &&
				 e != mc.thePlayer && !(!dead.booleanVal() && e.isDead) &&
				 e.getDistanceToEntity(mc.thePlayer) < range.floatVal()) {
				
				if (e instanceof EntityPlayer && !PlayerUtils.teamsCheck((EntityPlayer) e))
					continue;
				
				targets.add((EntityLivingBase) e);
			}
		}

		targets.sort(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer)));

		if (!targets.isEmpty()) {
			EntityLivingBase target = targets.get(0);

			if (timer.hasTimePassed((long) (1000 / cps.intVal() * ThreadLocalRandom.current().nextDouble(0.8, 1.2)))) {
				mc.thePlayer.swingItem();
				mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook());
				timer.reset();
			}
		}
	}

	@SubscribeEvent
	public void onPacketSent(EventMotion event) {
		if (rotate.booleanVal() && targets.size() > 0) {
			float rotations[] = getRotations(targets.get(0));
			event.setYaw(rotations[0]);
			event.setPitch(rotations[1]);
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
