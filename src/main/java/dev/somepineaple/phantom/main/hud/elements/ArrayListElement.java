package dev.somepineaple.phantom.main.hud.elements;

import java.util.ArrayList;
import java.util.Collections;

import dev.somepineaple.phantom.Phantom;
import dev.somepineaple.phantom.main.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class ArrayListElement extends HudElement {
	private int lastWidth;
	
	public ArrayListElement(int x, int y) {
		super(x, y, "ArrayList");
		lastWidth = 0;
	}

	@Override
	public void render() {
		ArrayList<String> enabledModules = new ArrayList<>();
		
		for (Module m : Phantom.getModuleManager().getModules()) {
			if (m.isEnabled())
				enabledModules.add(m.getName());
		}
		
		Collections.sort(enabledModules, (s1, s2) -> fr.getStringWidth(s2) - fr.getStringWidth(s1));
		
		width = fr.getStringWidth(enabledModules.get(0)) + 5;
		height = (fr.FONT_HEIGHT + 2) * enabledModules.size();
		
		if (lastWidth != 0 && lastWidth != width) {
			int diff = lastWidth - width;
			
			x += diff;
		}
		
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		
		x = Math.min(x, sr.getScaledWidth() - width);
		
		int currentY = y;
		for (String module : enabledModules) {
			int mWidth = fr.getStringWidth(module) + 5;
			int mHeight = fr.FONT_HEIGHT + 2;
			Gui.drawRect(x + width - mWidth, currentY, x + width, currentY + mHeight, bgColor);
			Gui.drawRect(x + width - mWidth, currentY, x + 1 + width - mWidth, currentY + mHeight, textColor);
			fr.drawString(module, x + width - mWidth + 3, currentY + (mHeight / 2 - fr.FONT_HEIGHT / 2), textColor);
			currentY += mHeight;
		}
		
		lastWidth = width;
	}
}
