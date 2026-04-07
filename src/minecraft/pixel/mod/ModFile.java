package pixel.mod;

import pixel.Pixel;
import pixel.util.FileManager;

public class ModFile {
	private FileManager modsFile;
	private Mod mod;
	
	public ModFile(Pixel pixelClient, Mod mod) {
		modsFile = pixelClient.getModsFile();
		this.mod = mod;
	}
	
	public String getKey(String key) {
		return mod.getClass().getSimpleName() + "." + key;
	}
	
	public void set(String key, Object value) {
		modsFile.put(getKey(key), value);
	}
	
	public Object get(String key) {
		return modsFile.get(getKey(key));
	}
	
	public Object safeGet(String key, Object defaultValue) {
		if (!has(key)) {
			set(key, defaultValue);
		}
		
		return get(key);
	}
	
	public boolean has(String key) {
		return modsFile.containsKey(getKey(key));
	}
}