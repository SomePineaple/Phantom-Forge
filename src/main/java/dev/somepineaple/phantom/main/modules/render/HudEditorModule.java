package dev.somepineaple.phantom.main.modules.render;

import dev.somepineaple.phantom.main.hud.HudEditor;
import dev.somepineaple.phantom.main.modules.Category;
import dev.somepineaple.phantom.main.modules.Module;

public class HudEditorModule extends Module {
	public HudEditorModule (){
		super("Edit HUD", Category.RENDER, -1);
	}
	
	@Override
	protected void onEnable() {
		mc.displayGuiScreen(new HudEditor());
	}
	
	@Override
	protected void onDisable() {
		mc.displayGuiScreen(null);
	}
}
