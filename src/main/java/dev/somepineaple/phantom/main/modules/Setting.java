package dev.somepineaple.phantom.main.modules;

public class Setting {
	private boolean boolVal;
	private float numVal;
	private float maxVal;
	private float minVal;
	private String name;
	private String[] multiSelectVals;
	private int selectedMultiselect;
	private SettingType type;
	
	public Setting(String name, boolean val) {
		boolVal = val;
		this.name = name;
		type = SettingType.BOOL;
	}
	
	public Setting(String name, float val, float minVal, float maxVal) {
		numVal = val;
		this.name = name;
		type = SettingType.FLOAT;
		this.minVal = minVal;
		this.maxVal = maxVal;
	}
	
	public Setting(String name, int val, int minVal, int maxVal) {
		numVal = val;
		this.name = name;
		type = SettingType.INT;
		this.minVal = minVal;
		this.maxVal = maxVal;
	}
	
	public Setting(String name, String ... vals) {
		multiSelectVals = vals;
		selectedMultiselect = 0;
		this.name = name;
		type = SettingType.MULTISELECT;
	}
	
	public String getCurrentMultiSelect() {
		return multiSelectVals[selectedMultiselect];
	}
	
	public void nextMultiSelect() {
		selectedMultiselect++;
		if (selectedMultiselect >= multiSelectVals.length)
			selectedMultiselect = 0;
	}
	
	public boolean booleanVal() {
		return boolVal;
	}
	
	public float floatVal() {
		return numVal;
	}
	
	public float minVal() {
		return minVal;
	}
	
	public float maxVal() {
		return maxVal;
	}
	
	public void setVal(boolean val) {
		boolVal = val;
	}
	
	public void setVal(float val) {
		if (type.equals(SettingType.INT)) {
			val = (int) val;
		}
		
		if (val <= maxVal && val >= minVal) {
			numVal = val;
		}
	}
	
	public void setVal(String val) {
		for (int i = 0; i < multiSelectVals.length; i++) {
			if (multiSelectVals[i].equalsIgnoreCase(val))
				selectedMultiselect = i;
		}
	}
	
	public int intVal() {
		return (int) numVal;
	}
	
	public String getName() {
		return name;
	}
	
	public SettingType getType() {
		return type;
	}
	
	public static enum SettingType {
		BOOL, INT, FLOAT, MULTISELECT
	}
}
