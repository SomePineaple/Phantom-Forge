package dev.somepineaple.phantom.main.gui;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import dev.somepineaple.phantom.Phantom;
import dev.somepineaple.phantom.main.gui.components.CategoryRenderer;
import dev.somepineaple.phantom.main.modules.Category;
import net.minecraft.client.gui.GuiScreen;

public class ClickGui extends GuiScreen {
	private final ArrayList<CategoryRenderer> categories;
	
	public ClickGui() {
		categories = new ArrayList<>();
		
		int xOffset = 10;
		int counter = 0;
		for (Category category : Category.values()) {
			categories.add(new CategoryRenderer(category, xOffset, 10, 120));
			xOffset += categories.get(counter).getWidth() + 10;
			counter++;
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		for (CategoryRenderer renderer : categories) {
			renderer.render();
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			Phantom.getModuleManager().getModuleWithName("Click GUI").toggle();
			return;
		}

		categories.forEach(categoryRenderer -> categoryRenderer.processKey(keyCode));
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (CategoryRenderer category : categories) {
			if (mouseX > category.getX() && mouseX < category.getX() + category.getWidth() &&
				mouseY > category.getY() && mouseY < category.getY() + category.getHeight()) {
				category.processClick(mouseX, mouseY, mouseButton);
			}
		}
	}
	
	@Override
	public void onGuiClosed() {
		if (Phantom.getModuleManager().getModuleWithName("Click GUI").isEnabled())
			Phantom.getModuleManager().getModuleWithName("Click GUI").setEnabed(false);
		
		Phantom.getModuleManager().saveBinds();
	}
}
