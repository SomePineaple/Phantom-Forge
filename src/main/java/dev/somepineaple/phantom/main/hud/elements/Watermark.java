package dev.somepineaple.phantom.main.hud.elements;

import dev.somepineaple.phantom.Phantom;

public class Watermark extends HudElement {
	private final String wStr;
	
	public Watermark(int x, int y) {
		super(x, y, "Watermark");
		
		wStr = Phantom.NAME + " " + Phantom.VERSION;
		
		height = fr.FONT_HEIGHT;
		width = fr.getStringWidth(wStr);
	}
	
	@Override
	public void render() {
		fr.drawString(wStr, x, y, hudModule.getTextColor(), false);
	}
}
