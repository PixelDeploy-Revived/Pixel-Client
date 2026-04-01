package pixel.mod.option;

import pixel.mod.Mod;

public class ModOption {
	public static class InGuiSettings {
		private boolean visible;
		private String name;
		
		public InGuiSettings(boolean visible) {
			this.visible = visible;
		}
		
		public InGuiSettings(String name) {
			visible = true;
			this.name = name;
		}
		
		public boolean isVisible() {
			return visible;
		}
		
		public String getName() {
			return name;
		}
	}
	
	protected final ModOptionParent optionParent;
	protected final String key;
	protected Object value;
	protected final InGuiSettings inGuiSettings;
	
	public ModOption(ModOptionParent optionParent, String key, Object value, InGuiSettings inGuiSettings) {
		this.optionParent = optionParent;
		this.key = key;
		this.value = value;
		this.inGuiSettings = inGuiSettings;
	}
	
	public ModOption(String key, Object value, InGuiSettings inGuiSettings) {
		this(null, key, value, inGuiSettings);
	}
	
	public ModOptionParent getOptionParent() {
		return optionParent;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public void saveIn(Mod mod) {
		mod.getFile().set(key, value);
	}
	
	public Object getValue() {
		return value;
	}
	
	public InGuiSettings getInGuiSettings() {
		return inGuiSettings;
	}
}
