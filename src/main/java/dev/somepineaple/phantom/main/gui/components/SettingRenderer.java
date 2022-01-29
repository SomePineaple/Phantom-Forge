package dev.somepineaple.phantom.main.gui.components;

import java.awt.Color;

import dev.somepineaple.phantom.Phantom;
import dev.somepineaple.phantom.main.modules.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class SettingRenderer {
	private static int RECTCOLOR = new Color(
			Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("R2").intVal(),
			Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("G2").intVal(),
			Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("B2").intVal(),
			Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("A2").intVal()
	).getRGB();
	
	private static int TEXTCOLOR = new Color(
			Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("Text R").intVal(),
			Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("Text G").intVal(),
			Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("Text B").intVal(),
			Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("Text A").intVal()
	).getRGB();
	
	private static final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	
	private final Setting setting;
	public SettingRenderer(Setting setting) {
		this.setting = setting;
	}
	
	private int x = 0, width = 0, y = 0, height = 0;
	
	public void render(int x, int y, int width, int height) {
		this.x = x;
		this.width = width;
		this.y = y;
		this.height = height;
		
		updateColors();
		
		switch (setting.getType()) {
		case BOOL:
			renderBool(x, y, width, height);
			break;
		case INT:
			renderInt(x, y, width, height);
			break;
		case FLOAT:
			renderFloat(x, y, width, height);
			break;
		case MULTISELECT:
			renderMultiSelect(x, y, width, height);
			break;
		}
	}
	
	public void processClick(int mouseX, int mouseY) {
		switch (setting.getType()) {
		case BOOL:
			clickBool();
			break;
		case INT:
			case FLOAT:
				clickNumber(mouseX, mouseY);
			break;
		case MULTISELECT:
			multiSelectClick();
			break;
		}
	}
	
	private void renderMultiSelect(int x, int y, int width, int height) {
		int yOffset = height / 2 - fr.FONT_HEIGHT / 2;
		
		fr.drawString(setting.getName(), x + 2, y + yOffset, TEXTCOLOR);
		fr.drawString(setting.getCurrentMultiSelect(), x + width - 2 - fr.getStringWidth(setting.getCurrentMultiSelect()), 
				      y + yOffset, TEXTCOLOR);
	}
	
	private void multiSelectClick() {
		setting.nextMultiSelect();
	}
	
	private void clickBool() {
		setting.setVal(!setting.booleanVal());
	}
	
	private void clickNumber(int mouseX, int mouseY) {
		int xOffset = mouseX - x;
		float xOffsetComparedToWidth = (float) xOffset / (float) width;
		setting.setVal((setting.maxVal() - setting.minVal()) * xOffsetComparedToWidth + setting.minVal());
	}
	
	private void renderBool(int x, int y, int width, int height) {
		int nameXOffset = width / 2 - fr.getStringWidth(setting.getName()) / 2;
		int nameYOffset = height / 2 - fr.FONT_HEIGHT / 2;
		
		if (setting.booleanVal()) {
			Gui.drawRect(x, y, x + width, y + height, RECTCOLOR);
			fr.drawString(setting.getName(), x + nameXOffset, y + nameYOffset, TEXTCOLOR);
		} else {
			fr.drawString(setting.getName(), x + nameXOffset, y + nameYOffset, TEXTCOLOR);
		}
	}
	
	private void renderInt(int x, int y, int width, int height) {
		int nameXOffset = width / 2 - fr.getStringWidth(setting.getName()) / 2;
		int nameYOffset = height / 2 - fr.FONT_HEIGHT / 2;
		
		Gui.drawRect(x, y, (int) (x + width * ((setting.intVal() - setting.minVal()) / (setting.maxVal() - setting.minVal()))), y + height, RECTCOLOR);
		
		fr.drawString(setting.getName(), x + nameXOffset, y + nameYOffset, TEXTCOLOR);
		fr.drawString(String.valueOf(setting.intVal()), 
					  x + width - fr.getStringWidth(String.valueOf(setting.intVal())), 
					  y + nameYOffset, 
					  TEXTCOLOR);
		
	}
	
	private void renderFloat(int x, int y, int width, int height) {
		int nameXOffset = width / 2 - fr.getStringWidth(setting.getName()) / 2;
		int nameYOffset = height / 2 - fr.FONT_HEIGHT / 2;
		
		Gui.drawRect(x, y, (int) (x + width * ((setting.floatVal() - setting.minVal()) / (setting.maxVal() - setting.minVal()))), y + height, RECTCOLOR);

		String rawValue =  String.valueOf(setting.floatVal());
		StringBuilder renderValue = new StringBuilder();

		for (int i = 0; i < Math.min(rawValue.length(), 4); i++) {
			renderValue.append(rawValue.charAt(i));
		}

		fr.drawString(setting.getName(), x + nameXOffset, y + nameYOffset, TEXTCOLOR);
		fr.drawString(renderValue.toString(),
					  x + width - fr.getStringWidth(renderValue.toString()),
					  y + nameYOffset, 
					  TEXTCOLOR);
	}
	
	private void updateColors() {
		TEXTCOLOR = new Color(
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("Text R").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("Text G").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("Text B").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("Text A").intVal()
		).getRGB();
		
		RECTCOLOR = new Color(
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("R2").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("G2").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("B2").intVal(),
				Phantom.getModuleManager().getModuleWithName("Click GUI").getSettingWithName("A2").intVal()
		).getRGB();
	}
	
	public int getY() {
		return y;
	}
	
	public int getHeight() {
		return height;
	}
}
