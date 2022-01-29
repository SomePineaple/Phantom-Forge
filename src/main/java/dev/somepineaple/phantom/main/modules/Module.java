package dev.somepineaple.phantom.main.modules;

import java.util.ArrayList;

import dev.somepineaple.phantom.main.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class Module {
protected static final Minecraft mc = Minecraft.getMinecraft();
	
	private int bind;
	private final String name;
	private final Category category;
	private final ArrayList<Setting> settings;
	private boolean isEnabled;
	
	public Module(String name, Category category, int bind) {
		this.name = name;
		this.category = category;
		settings = new ArrayList<>();
		this.bind = bind;
		isEnabled = false;
	}
	
	protected void onEnable() {}
	protected void onDisable() {}
	
	public void update() {}
	public void render() {}
	
	public void toggle() {
		setEnabed(!isEnabled);
	}
	
	public void setEnabed(boolean isEnalbed) {
		this.isEnabled = isEnalbed;
		
		if (isEnabled) {
			MinecraftForge.EVENT_BUS.register(this);
			onEnable();
			PlayerUtils.sendMessage(name + " was enabled");
		} else {
			MinecraftForge.EVENT_BUS.unregister(this);
			onDisable();
			PlayerUtils.sendMessage(name + " was disalbed");
		}
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}

	public void setBind(int bind) {
		this.bind = bind;
	}
	
	protected Setting addSetting(Setting s) {
		settings.add(s);
		return s;
	}
	
	public String getName() {
		return name;
	}
	
	public int getBind() {
		return bind;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public ArrayList<Setting> getSettings() {
		return settings;
	}
	
	public Setting getSettingWithName(String name) {
		for (Setting s : settings) {
			if (s.getName().equalsIgnoreCase(name)) {
				return s;
			}
		}
		
		return null;
	}
}
