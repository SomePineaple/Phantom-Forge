package dev.somepineaple.phantom.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;

@Mixin(Minecraft.class)
public interface IMinecraft {
	@Accessor
	Timer getTimer();
	
	@Accessor
	void setSession(Session session);
}
