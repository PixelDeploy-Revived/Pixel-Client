package pixel.mod.option;

public class ModOptionParent {
	private String key;
	private boolean needed;
	
	public ModOptionParent(String key, boolean needed) {
		this.key = key;
		this.needed = needed;
	}
	
	public ModOptionParent(String key) {
		this(key, true);
	}
	
	public String getKey() {
		return key;
	}
	
	public boolean isNeeded() {
		return needed;
	}
}
