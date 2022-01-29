package dev.somepineaple.phantom.main.hud.elements;

import java.awt.Color;

import dev.somepineaple.phantom.Phantom;
import dev.somepineaple.phantom.main.modules.render.HUDModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public abstract class HudElement {
	protected static final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	protected static int textColor = new Color(255, 255, 255, 255).getRGB();
	protected static int bgColor = new Color(0, 0, 0, 69).getRGB();
	
	protected int x, y, width, height;
	protected static final HUDModule hudModule = (HUDModule) Phantom.getModuleManager().getModuleWithName("HUD");
	private final String name;
	
	public HudElement(int x, int y, String name) {
		this.x = x;
		this.y = y;
		this.name = name;
	}
	
	public static void updateTextColor() {
		HUDModule hudMod = (HUDModule) Phantom.getModuleManager().getModuleWithName("HUD");
		textColor = new Color(
			hudMod.getSettingWithName("Text R").intVal(),
			hudMod.getSettingWithName("Text G").intVal(),
			hudMod.getSettingWithName("Text B").intVal(),
			hudMod.getSettingWithName("Text A").intVal()
		).getRGB();
	}
	
	public abstract void render();
	public int getX() { return x; }
	public int getY() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	public String getName() { return name; }
}
