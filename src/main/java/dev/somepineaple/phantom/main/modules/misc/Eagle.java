package dev.somepineaple.phantom.main.modules.misc;

import dev.somepineaple.phantom.main.modules.Category;
import dev.somepineaple.phantom.main.modules.Module;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;

public class Eagle extends Module {
	public Eagle() {
		super("Eagle", Category.MISC, -1);
	}
	
	@Override
	public void update() {
		if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).down()).getBlock().getMaterial().isReplaceable())
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
		else
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
	}
	
	@Override
	protected void onDisable() {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
	}
}
