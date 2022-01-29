package dev.somepineaple.phantom.main.gui.components;

import java.awt.Color;
import java.util.ArrayList;

import dev.somepineaple.phantom.Phantom;
import dev.somepineaple.phantom.main.modules.Module;
import dev.somepineaple.phantom.main.modules.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class ModuleRenderer {
	private static final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	private final Module module;
	private boolean expanded;
	private int x;
	private int y;
	private final int width;
	private int height;
	private final ArrayList<SettingRenderer> settingRenderers;
	private final KeyBindRenderer keyBindRenderer;
	
	public ModuleRenderer(Module module, int x, int y, int width) {
		this.module = module;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = 20;
		keyBindRenderer = new KeyBindRenderer(module);
		settingRenderers = new ArrayList<>();
		for (Setting s : module.getSettings()) {
			settingRenderers.add(new SettingRenderer(s));
		}
	}
	
	public void render() {
		int bgColor = new Color(
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("BG R").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("BG G").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("BG B").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("BG A").intVal()
		).getRGB();
		
		int fgColor = new Color(
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("R").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("G").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("B").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("A").intVal()
		).getRGB();
		
		int bdColor = new Color(
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("BD R").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("BD G").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("BD B").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("BD A").intVal()
		).getRGB();
		
		Gui.drawRect(x, y, x + width, y + height, bgColor);
		
		if (module.isEnabled())
			Gui.drawRect(x, y, x + width, y + 20, fgColor);
		
		int nameXOffset = width / 2 - fr.getStringWidth(module.getName()) / 2;
		int nameYOffset = 10 - fr.FONT_HEIGHT / 2;
		fr.drawString(module.getName(), x + nameXOffset, y + nameYOffset, module.isEnabled() ? bgColor : fgColor);
		
		if (expanded) {
			Gui.drawRect(x, y, x + 1, y + height, bdColor);
			Gui.drawRect(x + width - 1, y, x + width, y + height, bdColor);
			
			int yOffset = 20;
			for (SettingRenderer settingRenderer : settingRenderers) {
				settingRenderer.render(x + 1, y + yOffset, width - 2, 15);
				yOffset += 15;
			}

			keyBindRenderer.render(x + 1, y + yOffset, width - 2, 15);
		}
	}
	
	public void processClick(int mouseX, int mouseY, int mouseButton) {
		if (mouseY < y + 20) {
			if (mouseButton == 1)
				toggleExpand();
			else
				module.toggle();
			return;
		}
		
		for (SettingRenderer settingRenderer : settingRenderers) {
			if (mouseY > settingRenderer.getY() && mouseY < settingRenderer.getY() + settingRenderer.getHeight()) {
				settingRenderer.processClick(mouseX, mouseY);
				break;
			}
		}

		if (mouseY > keyBindRenderer.getY() && mouseY < keyBindRenderer.getY() + keyBindRenderer.getHeight()) {
			keyBindRenderer.processClick();
		}
	}
	
	private int calcExpandedHeight() {
		return 35 + 15 * settingRenderers.size();
	}
	
	private void toggleExpand() {
		expanded = !expanded;
		
		if (expanded) {
			height = calcExpandedHeight();
		} else {
			height = 20;
		}
	}

	public void processKey(int keyCode) {
		keyBindRenderer.processKey(keyCode);
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
}
