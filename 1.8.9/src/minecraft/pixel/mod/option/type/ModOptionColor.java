package pixel.mod.option.type;

import pixel.mod.Mod;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOptionParent;

public class ModOptionColor extends ModOption {
	public static class InGuiSettings extends ModOption.InGuiSettings {
		private boolean drawAlphaSlider;
		private boolean drawChromaButton;
		
		public InGuiSettings(boolean visible) {
			super(visible);
		}
		
		public InGuiSettings(String optionName, boolean drawAlphaSlider, boolean drawChromaButton) {
			super(optionName);
			
			this.drawAlphaSlider = drawAlphaSlider;
			this.drawChromaButton = drawChromaButton;
		}
		
		public boolean shouldAlphaSliderBeDrawn() {
			return drawAlphaSlider;
		}
		
		public boolean shouldChromaButtonBeDrawn() {
			return drawChromaButton;
		}
	}
	
	private int argb;
	private boolean chroma;
	
	public ModOptionColor(ModOptionParent optionParent, String key, int argb, boolean chroma, InGuiSettings inGuiSettings) {
		super(optionParent, key, null, inGuiSettings);
		
		this.argb = argb;
		this.chroma = chroma;
	}
	
	public ModOptionColor(String key, int argb, boolean chroma, InGuiSettings inGuiSettings) {
		super(key, null, inGuiSettings);
		
		this.argb = argb;
		this.chroma = chroma;
	}
	
	public String getKeyARGB() {
		return key + ".argb";
	}
	
	public String getKeyChroma() {
		return key + ".chroma";
	}
	
	public void setARGB(int argb) {
		this.argb = argb;
	}
	
	public int getARGB() {
		return argb;
	}
	
	public void enableChroma(boolean enabled) {
		chroma = enabled;
	}
	
	public boolean isChromaEnabled() {
		return chroma;
	}
	
	@Override
	public void saveIn(Mod mod) {
		mod.getFile().set(getKeyARGB(), argb);
		
		if (getInGuiSettings().shouldChromaButtonBeDrawn()) {
			mod.getFile().set(getKeyChroma(), chroma);
		}
	}
	
	public InGuiSettings getInGuiSettings() {
		return (InGuiSettings) inGuiSettings;
	}
}
