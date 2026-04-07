package pixel.cosmetic;

import pixel.Pixel;
import pixel.util.FileManager;

public class CosmeticFile {
	private FileManager cosmeticsFile;
	private Cosmetic cosmetic;
	
	public CosmeticFile(Cosmetic cosmetic) {
		cosmeticsFile = Pixel.getCosmeticsFile();
		this.cosmetic = cosmetic;
	}
	
	public String getKey(String key) {
		return cosmetic.getClass().getSimpleName() + "." + key;
	}
	
	public void set(String key, Object value) {
		cosmeticsFile.put(getKey(key), value);
	}
	
	public Object get(String key) {
		return cosmeticsFile.get(getKey(key));
	}
	
	public Object safeGet(String key, Object defaultValue) {
		if (!has(key)) {
			set(key, defaultValue);
		}
		
		return get(key);
	}
	
	public boolean has(String key) {
		return cosmeticsFile.containsKey(getKey(key));
	}
}