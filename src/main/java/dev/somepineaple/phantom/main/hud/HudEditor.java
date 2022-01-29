package dev.somepineaple.phantom.main.hud;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import dev.somepineaple.phantom.Phantom;
import dev.somepineaple.phantom.main.hud.elements.HudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class HudEditor extends GuiScreen {
	private final ArrayList<HudElement> elements;
	private final ScaledResolution sr;
	
	private HudElement selectedElement;
	private int selectedXOffset, selectedYOffset;
	
	public HudEditor() {
		elements = Phantom.getHud().getElements();
		sr = new ScaledResolution(Minecraft.getMinecraft());
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (!Mouse.isButtonDown(0))
			selectedElement = null;
		
		for (HudElement e : elements) {
			if (Phantom.getModuleManager().getModuleWithName("HUD").getSettingWithName(e.getName()).booleanVal())
				Gui.drawRect(e.getX(), e.getY(), e.getX() + e.getWidth(), e.getY() + e.getHeight(), new Color(0, 0, 0, 100).getRGB());
		}
		
		if (selectedElement != null) {
			selectedElement.setX(Math.min(mouseX - selectedXOffset, sr.getScaledWidth() - selectedElement.getWidth()));
			selectedElement.setY(Math.min(mouseY - selectedYOffset, sr.getScaledHeight() - selectedElement.getWidth()));
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton == 0) {
			for (HudElement e : elements) {
				if (mouseX > e.getX() && mouseX < e.getX() + e.getWidth() && mouseY > e.getY() && mouseY < e.getY() + e.getHeight() && Phantom.getModuleManager().getModuleWithName("HUD").getSettingWithName(e.getName()).booleanVal()) {
					selectedElement = e;
					selectedXOffset = mouseX - e.getX();
					selectedYOffset = mouseY - e.getY();
					return;
				}
			}
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE)
			Phantom.getModuleManager().getModuleWithName("Edit HUD").toggle();
	}
	
	@Override
	public void onGuiClosed() {
		if (Phantom.getModuleManager().getModuleWithName("Edit HUD").isEnabled())
			Phantom.getModuleManager().getModuleWithName("Edit HUD").toggle();
		
		Phantom.getHud().saveHudPositions();
	}
}
