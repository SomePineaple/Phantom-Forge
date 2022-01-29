package dev.somepineaple.phantom.main.gui.components;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import dev.somepineaple.phantom.Phantom;
import dev.somepineaple.phantom.main.modules.Category;
import dev.somepineaple.phantom.main.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class CategoryRenderer {
	private static final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	private final Category category;
	private final ArrayList<ModuleRenderer> modules;
	private final int width;
	private int x, y, height;
	
	private boolean isBeingDragged = false;
	private int dragOffsetX;
	private int dragOffsetY;
	
	public CategoryRenderer(Category category, int x, int y, int width) {
		this.category = category;
		modules = new ArrayList<>();
		this.width = width;
		this.x = x;
		this.y = y;
		
		int yOffset = 20;
		int counter = 0;
		for (Module m : Phantom.getModuleManager().getModulesWithCategory(this.category)) {
			modules.add(new ModuleRenderer(m, x + 1, y + yOffset, width - 2));
			yOffset += modules.get(counter).getHeight();
			counter++;
		}
		
		height = calcHeight();
	}
	
	public void render() {
		if (isBeingDragged) {
			x = (Mouse.getX() - dragOffsetX) / 2;
			y = (Minecraft.getMinecraft().displayHeight - Mouse.getY() - dragOffsetY) / 2;
			
			updateModulePos();
		}
		
		Color color = new Color(
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("BG R").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("BG G").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("BG B").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("BG A").intVal()
		);
		
		int color2 = new Color(
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("R").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("G").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("B").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("A").intVal()
		).getRGB();
		
		Gui.drawRect(x, y, x + width, y + 20, color.getRGB());
		
		Gui.drawRect(x, y, x + width, y + 1, color2);
		Gui.drawRect(x, y, x + 1, y + 20, color2);
		Gui.drawRect(x, y + 19, x + width, y + 20, color2);
		Gui.drawRect(x + width - 1, y, x + width, y + 20, color2);
		
		int nameXOffset = width / 2 - fr.getStringWidth(category.getName()) / 2;
		int nameYOffset = 10 - fr.FONT_HEIGHT / 2;
		fr.drawString(category.getName(), x + nameXOffset, y + nameYOffset, color2);
		
		for (ModuleRenderer renderer : modules) {
			renderer.render();
		}
	}
	
	private void updateModulePos() {
		int yOffset = 20;
		for (ModuleRenderer moduleRenderer : modules) {
			moduleRenderer.setX(x + 1);
			moduleRenderer.setY(y + yOffset);
			yOffset += moduleRenderer.getHeight();
		}
	}
	
	public void processClick(int mouseX, int mouseY, int mouseButton) {
		if (mouseY < y + 20)
			processTopClick(mouseX, mouseY);
		
		for (ModuleRenderer module : modules) {
			if (mouseX > module.getX() && mouseX < module.getX() + module.getWidth() &&
				mouseY > module.getY() && mouseY < module.getY() + module.getHeight()) {
				module.processClick(mouseX, mouseY, mouseButton);
				updateModulePos();
				break;
			}
		}
		height = calcHeight();
	}

	public void processKey(int key) {
		modules.forEach(moduleRenderer -> moduleRenderer.processKey(key));
	}
	
	private void processTopClick(int mouseX, int mouseY) {
		isBeingDragged = !isBeingDragged;
		
		if (isBeingDragged) {
			dragOffsetX = mouseX - x;
			dragOffsetY = mouseY - y;
		}
	}
	
	private int calcHeight() {
		int height = 20;
		for (ModuleRenderer moduleRenderer : modules) {
			height += moduleRenderer.getHeight();
		}
		
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
