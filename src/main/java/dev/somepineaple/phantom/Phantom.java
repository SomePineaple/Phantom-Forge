package dev.somepineaple.phantom;

import dev.somepineaple.phantom.main.command.CommandManager;
import dev.somepineaple.phantom.main.hud.HUD;
import dev.somepineaple.phantom.main.modules.ModuleManager;
import dev.somepineaple.phantom.main.utils.FileUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Phantom.MODID, name = Phantom.NAME, version = Phantom.VERSION)
public class Phantom {
	public static final String NAME = "Phantom", MODID = "phantom", VERSION = "b1";
	
	private static ModuleManager manager;
	private static CommandManager commandManager;
	private static HUD hud;
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		manager = new ModuleManager();
		commandManager = new CommandManager();
		hud = new HUD();
		FileUtils.init();
		manager.loadBinds();
		hud.loadHudPositions();
	}
	
	public static ModuleManager getModuleManager() {
		return manager;
	}
	
	public static CommandManager getCommandManager() {
		return commandManager;
	}
	
	public static HUD getHud() {
		return hud;
	}
}
