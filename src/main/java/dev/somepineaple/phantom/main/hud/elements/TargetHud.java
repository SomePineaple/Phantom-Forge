package dev.somepineaple.phantom.main.hud.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;

public class TargetHud extends HudElement {
	public TargetHud(int x, int y) {
		super(x, y, "Target HUD");
		
		width = 120;
		height = 40;
	}

	@Override
	public void render() {
		EntityPlayer target = Minecraft.getMinecraft().thePlayer;
		
		float closest = 16;
		
		for (EntityPlayer p : Minecraft.getMinecraft().theWorld.playerEntities) {
			if (p == Minecraft.getMinecraft().thePlayer)
				continue;
			
			float distance = p.getDistanceToEntity(Minecraft.getMinecraft().thePlayer);
			
			if (distance < closest) {
				target = p;
				closest = distance;
			}
		}
		
		if (target != null) {
			Gui.drawRect(x, y, x + width, y + height, bgColor);
			
			float targetHP = target.getHealth() + target.getAbsorptionAmount();
			fr.drawString(target.getName(), x + 3, y + 3, textColor);
			fr.drawString(String.valueOf((int) targetHP), x + 3, y + (height / 2 - fr.FONT_HEIGHT / 2), textColor);
			
			int barX = x + 20;
			int barY = y + 15;
			int barX2 = x + width - 20;
			int barY2 = y + height - 15;
			int barWidth = barX2 - barX;
			Gui.drawRect(barX, barY, barX2, barY2, bgColor);
			Gui.drawRect(barX, barY, (int) (barX + (barWidth * (target.getHealth() / 20))), barY2, textColor);
		}
	}
}
