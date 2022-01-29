package dev.somepineaple.phantom.main.modules.movement;

import dev.somepineaple.phantom.main.modules.Category;
import dev.somepineaple.phantom.main.modules.Module;
import dev.somepineaple.phantom.main.modules.Setting;
import dev.somepineaple.phantom.main.utils.MSTimer;

public class Flight extends Module {
	private final Setting vanillaKickBypass = addSetting(new Setting("Kick Bypass", true));
	
	private final MSTimer kickBypassTimer;
	
	public Flight() {
		super("Flight", Category.MOVEMENT, -1);
		kickBypassTimer = new MSTimer();
	}
	
	@Override
	public void update() {
		mc.thePlayer.capabilities.isFlying = true;
		
		if (vanillaKickBypass.booleanVal() && kickBypassTimer.hasTimePassed(1500)) {
			mc.thePlayer.capabilities.isFlying = false;
			kickBypassTimer.reset();
		}
	}
}
