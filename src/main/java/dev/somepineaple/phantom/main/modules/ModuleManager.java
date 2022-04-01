package dev.somepineaple.phantom.main.modules;

import java.util.ArrayList;

import dev.somepineaple.phantom.main.modules.combat.*;
import org.lwjgl.input.Keyboard;

import dev.somepineaple.phantom.main.modules.misc.Eagle;
import dev.somepineaple.phantom.main.modules.misc.FastPlace;
import dev.somepineaple.phantom.main.modules.movement.Flight;
import dev.somepineaple.phantom.main.modules.movement.Sprint;
import dev.somepineaple.phantom.main.modules.render.ClickGui;
import dev.somepineaple.phantom.main.modules.render.ESP;
import dev.somepineaple.phantom.main.modules.render.HUDModule;
import dev.somepineaple.phantom.main.modules.render.HudEditorModule;
import dev.somepineaple.phantom.main.utils.FileUtils;
import dev.somepineaple.phantom.main.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ModuleManager {
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	private final ArrayList<Module> modules;
	
	public ModuleManager() {
		MinecraftForge.EVENT_BUS.register(this);
		modules = new ArrayList<>();
		
		// Combat
		modules.add(new AimAssist());
		modules.add(new AimBot());
		modules.add(new AutoClicker());
		modules.add(new HitBoxes());
		modules.add(new KillAura());
		modules.add(new LagitAura());
		modules.add(new Reach());
		modules.add(new Teams());
		modules.add(new Velocity());
		
		// Movement
		modules.add(new Flight());
		modules.add(new Sprint());
		
		// Render
		modules.add(new ClickGui());
		modules.add(new ESP());
		modules.add(new HudEditorModule());
		modules.add(new HUDModule());
		
		// Misc
		modules.add(new FastPlace());
		modules.add(new Eagle());
	}
	
	@SubscribeEvent
	public void onTick(ClientTickEvent event) {
		if (mc.theWorld == null || mc.thePlayer == null) return;
		
		for (Module m : modules) {
			if (m.isEnabled())
				m.update();
		}
	}
	
	@SubscribeEvent
	public void onRender(RenderWorldLastEvent event) {
		if (event.isCanceled() || mc.thePlayer == null || mc.theWorld == null)
			return;
		
		for (Module m : modules) {
			if (m.isEnabled())
				m.render();
		}
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (Keyboard.getEventKeyState() && mc.currentScreen == null) {
			for (Module m : modules) {
				if (m.getBind() == Keyboard.getEventKey()) {
					m.toggle();
				}
			}
		}
	}
	
	public void loadConfig(String configName) {
		try {
			String lines[] = FileUtils.loadFileAsString("configs/" + configName + ".txt").split("\n");
			
			for (String line : lines) {
				
				String split[] = line.split(":");
				
				if (split.length == 0)
					continue;
				
				for (Module m : modules) {
					if (m.getName().equalsIgnoreCase(split[0])) {
						Object settings[] = m.getSettings().toArray();
						
						for (int i = 0; i < settings.length; i++) {
							switch (((Setting)settings[i]).getType()) {
							case BOOL:
								((Setting)settings[i]).setVal(Boolean.valueOf(split[i + 1]));
								break;
							case FLOAT:
								((Setting)settings[i]).setVal(Float.valueOf(split[i + 1]));
								break;
							case INT:
								((Setting)settings[i]).setVal(Integer.valueOf(split[i + 1]));
								break;
							case MULTISELECT:
								((Setting)settings[i]).setVal(split[i + 1]);
								break;
							}
						}
						m.setEnabed(Boolean.valueOf(split[split.length - 1]));
					}
				}
			}
			
			PlayerUtils.sendMessage("Loaded config " + configName);
		} catch (Exception e) {
			PlayerUtils.sendMessage("Failed to load config " + configName);
			e.printStackTrace();
		}
	}

	public void saveConfig(String configName) {
		StringBuilder builder = new StringBuilder();
		
		for (Module m : modules) {
			builder.append(m.getName() + ":");
			for (Setting s : m.getSettings()) {
				switch (s.getType()) {
				case BOOL:
					builder.append(s.booleanVal() ? "true" : "false");
					break;
				case FLOAT:
					builder.append(String.valueOf(s.floatVal()));
					break;
				case INT:
					builder.append(String.valueOf(s.intVal()));
					break;
				case MULTISELECT:
					builder.append(s.getCurrentMultiSelect());
					break;
				}
				
				builder.append(":");
			}
			builder.append(m.isEnabled() ? "true" : false);
			builder.append("\n");
		}
	
		FileUtils.writeStringToFile("configs/" + configName + ".txt", builder.toString());
		
		PlayerUtils.sendMessage("Saved config " + configName);
	}
	
	public void saveBinds() {
		StringBuilder builder = new StringBuilder();
		
		for (Module m : modules)
			builder.append(m.getName() + ":" + m.getBind() + "\n");
		
		FileUtils.writeStringToFile("binds.txt", builder.toString());
	}
	
	public void loadBinds() {
		String binds[] = FileUtils.loadFileAsString("binds.txt").split("\n");

		for (String moduleInfo : binds) {
			String split[] = moduleInfo.split(":");
			for (Module m : modules) {
				if (m.getName().equalsIgnoreCase(split[0]))
					m.setBind(Integer.parseInt(split[1]));
			}
		}
	}
	
	public Module getModuleWithName(String name) {
		for (Module m : modules) {
			if (m.getName().equalsIgnoreCase(name)) {
				return m;
			}
		}
		
		return null;
	}
	
	public ArrayList<Module> getModulesWithCategory(Category category) {
		ArrayList<Module> modulesWithCategory = new ArrayList<>();
		
		for (Module m : modules) {
			if (m.getCategory().equals(category)) {
				modulesWithCategory.add(m);
			}
		}
		
		return modulesWithCategory;
	}
	
	public ArrayList<Module> getModules() {
		return modules;
	}
}
