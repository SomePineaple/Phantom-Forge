package dev.somepineaple.phantom.main.modules;

public enum Category {
	COMBAT("Combat"), MOVEMENT("Movement"), RENDER("Render"), MISC("Misc");
	
	private String name;
	Category(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
