package dev.somepineaple.phantom.main.modules.combat;

import dev.somepineaple.phantom.main.events.EventPacket;
import dev.somepineaple.phantom.main.modules.Category;
import dev.somepineaple.phantom.main.modules.Module;
import dev.somepineaple.phantom.main.modules.Setting;
import dev.somepineaple.phantom.mixins.IS12PacketEntityVelocity;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity extends Module {
	private final Setting horizontal = addSetting(new Setting("Horizontal", 0f, 0f, 1f));
	private final Setting vertial = addSetting(new Setting("Vertical", 0f, 0f, 1f));
	
	public Velocity() {
		super("Velocity", Category.COMBAT, -1);
	}
	
	@SubscribeEvent
	public void onPacketSent(EventPacket.ReceivePacket event) {
		if (event.getPacket() instanceof S12PacketEntityVelocity) {
			S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
			IS12PacketEntityVelocity iPacket = (IS12PacketEntityVelocity) event.getPacket();
			
			iPacket.setMotionX((int) (packet.getMotionX() * horizontal.floatVal()));
			iPacket.setMotionY((int) (packet.getMotionY() * vertial.floatVal()));
			iPacket.setMotionZ((int) (packet.getMotionZ() * horizontal.floatVal()));
		}
	}
}
