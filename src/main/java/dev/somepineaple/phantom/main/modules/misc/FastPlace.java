package dev.somepineaple.phantom.main.modules.misc;

import dev.somepineaple.phantom.main.modules.Category;
import dev.somepineaple.phantom.main.modules.Module;
import dev.somepineaple.phantom.main.modules.Setting;

public class FastPlace extends Module {
	public FastPlace() {
		super("Fast Place", Category.MISC, -1);
		addSetting(new Setting("Delay", 0, 0, 4));
	}
}
