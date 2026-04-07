package pixel.cosmetic.option;

import pixel.cosmetic.Cosmetic;

public class CosmeticOption {
	public static class InGuiSettings {
		private String name;
		
		public InGuiSettings(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
	
	protected final String key;
	protected Object value;
	protected final InGuiSettings inGuiSettings;
	
	public CosmeticOption(String key, Object value, InGuiSettings inGuiSettings) {
		this.key = key;
		this.value = value;
		this.inGuiSettings = inGuiSettings;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public void saveIn(Cosmetic cosmetic) {
		cosmetic.getFile().set(key, value);
	}
	
	public Object getValue() {
		return value;
	}
	
	public InGuiSettings getInGuiSettings() {
		return inGuiSettings;
	}
}
