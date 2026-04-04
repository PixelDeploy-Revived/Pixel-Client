package pixel.cosmetic.option.type;

import pixel.cosmetic.Cosmetic;
import pixel.cosmetic.option.CosmeticOption;
import pixel.cosmetic.option.CosmeticOption.InGuiSettings;

public class CosmeticOptionColor extends CosmeticOption {
	private int argb;
	private boolean rainbow;
	private int rainbowSpeed;
	
	public CosmeticOptionColor(String key, int argb, boolean rainbow, int rainbowSpeed, InGuiSettings inGuiSettings) {
		super(key, null, inGuiSettings);
		
		this.argb = argb;
		this.rainbow = rainbow;
		this.rainbowSpeed = rainbowSpeed;
	}
	
	public String getKeyARGB() {
		return key + ".argb";
	}
	
	public String getKeyRainbow() {
		return key + ".rainbow";
	}
	
	public String getKeyRainbowSpeed() {
		return key + ".rainbowSpeed";
	}
	
	public void setARGB(int argb) {
		this.argb = argb;
	}
	
	public int getARGB() {
		return argb;
	}
	
	public void enableRainbow(boolean enabled) {
		rainbow = enabled;
	}
	
	public boolean isRainbowEnabled() {
		return rainbow;
	}
	
	public void setRainbowSpeed(int rainbowSpeed) {
		this.rainbowSpeed = rainbowSpeed;
	}
	
	public int getRainbowSpeed() {
		return rainbowSpeed;
	}
	
	@Override
	public void saveIn(Cosmetic cosmetic) {
		cosmetic.getFile().set(getKeyARGB(), argb);
		cosmetic.getFile().set(getKeyRainbow(), rainbow);
		cosmetic.getFile().set(getKeyRainbowSpeed(), rainbowSpeed);
	}
	
	public InGuiSettings getInGuiSettings() {
		return (InGuiSettings) inGuiSettings;
	}
}
