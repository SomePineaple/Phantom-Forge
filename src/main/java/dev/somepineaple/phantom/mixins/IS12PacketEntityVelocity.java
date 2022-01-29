package dev.somepineaple.phantom.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.play.server.S12PacketEntityVelocity;

@Mixin(S12PacketEntityVelocity.class)
public interface IS12PacketEntityVelocity {
	@Accessor
	void setMotionX(int motionX);
	
	@Accessor
	void setMotionY(int motionY);
	
	@Accessor
	void setMotionZ(int motionZ);
}
