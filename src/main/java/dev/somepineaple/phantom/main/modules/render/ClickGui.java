package dev.somepineaple.phantom.main.modules.render;

import org.lwjgl.input.Keyboard;

import dev.somepineaple.phantom.main.modules.Category;
import dev.somepineaple.phantom.main.modules.Module;
import dev.somepineaple.phantom.main.modules.Setting;

public class ClickGui extends Module {
	public ClickGui() {
		super("Click GUI", Category.RENDER, Keyboard.KEY_RSHIFT);
		
		addSetting(new Setting("R", 50, 0, 255));
		addSetting(new Setting("G", 150, 0, 255));
		addSetting(new Setting("B", 250, 0, 255));
		addSetting(new Setting("A", 255, 0, 255));
		
		addSetting(new Setting("R2", 50, 0, 255));
		addSetting(new Setting("G2", 100, 0, 255));
		addSetting(new Setting("B2", 200, 0, 255));
		addSetting(new Setting("A2", 255, 0, 255));
		
		addSetting(new Setting("BD R", 255, 0, 255));
		addSetting(new Setting("BD G", 255, 0, 255));
		addSetting(new Setting("BD B", 255, 0, 255));
		addSetting(new Setting("BD A", 255, 0, 255));
		
		addSetting(new Setting("BG R", 50, 0, 255));
		addSetting(new Setting("BG G", 50, 0, 255));
		addSetting(new Setting("BG B", 50, 0, 255));
		addSetting(new Setting("BG A", 150, 0, 255));
		
		addSetting(new Setting("Text R", 255, 0, 255));
		addSetting(new Setting("Text G", 255, 0, 255));
		addSetting(new Setting("Text B", 255, 0, 255));
		addSetting(new Setting("Text A", 255, 0, 255));
	}
	
	@Override
	protected void onEnable() {
		mc.displayGuiScreen(new dev.somepineaple.phantom.main.gui.ClickGui());
	}
	
	@Override
	protected void onDisable() {
		mc.displayGuiScreen(null);
	}
}
