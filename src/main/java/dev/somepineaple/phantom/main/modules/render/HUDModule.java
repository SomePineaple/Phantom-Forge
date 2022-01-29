package dev.somepineaple.phantom.main.modules.render;

import java.awt.Color;

import dev.somepineaple.phantom.main.modules.Category;
import dev.somepineaple.phantom.main.modules.Module;
import dev.somepineaple.phantom.main.modules.Setting;

public class HUDModule extends Module {
	
	private final Setting rainbow = addSetting(new Setting("Rainbow", false));
	private final Setting textR = addSetting(new Setting("Text R", 255, 0, 255));
	private final Setting textG = addSetting(new Setting("Text G", 255, 0, 255));
	private final Setting textB = addSetting(new Setting("Text B", 255, 0, 255));
	private final Setting textA = addSetting(new Setting("Text A", 255, 0, 255));
	
	public HUDModule() {
		super("HUD", Category.RENDER, -1);

		addSetting(new Setting("ArrayList", true));
		addSetting(new Setting("Watermark", true));
		addSetting(new Setting("Keystrokes", false));
		addSetting(new Setting("Target HUD", false));
	}
	
	float currentHue = 0;
	
	@Override
	public void update() {
		if (rainbow.booleanVal()) {
			currentHue += 0.008;
			if (currentHue > 255)
				currentHue = 0;
			
			Color nextColor = Color.getHSBColor(currentHue, 0.4f, 1f);
			textR.setVal(nextColor.getRed());
			textG.setVal(nextColor.getGreen());
			textB.setVal(nextColor.getBlue());
		}
	}
	
	public int getTextColor() {
		return new Color(textR.intVal(), textG.intVal(), textB.intVal(), textA.intVal()).getRGB();
	}
}
