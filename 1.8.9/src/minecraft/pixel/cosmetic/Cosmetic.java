package pixel.cosmetic;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.gui.GuiScreen;
import pixel.cosmetic.option.CosmeticOption;
import pixel.cosmetic.option.type.CosmeticOptionColor;
import pixel.cosmetic.option.type.CosmeticOptionScale;
import pixel.gui.GuiCosmeticOptions;

public abstract class Cosmetic {
	private final Class<? extends CosmeticBase> clazz;
	
	private boolean enabled;
	private CosmeticOption[] options;
	
	private final CosmeticFile file;
	
	public Cosmetic(Class<? extends CosmeticBase> clazz) {
		this.clazz = clazz;
		file = new CosmeticFile(this);
		
		enable((boolean) file.safeGet("enabled", enabled));
	}
	
	public String getName() {
		return StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(getClass().getSimpleName().replaceAll("\\d+", "")), " ");
	}
	
	public GuiScreen getGuiOptions(GuiScreen prevGuiScreen) {
		return new GuiCosmeticOptions(prevGuiScreen, this, 0);
	}
	
	public Class<? extends CosmeticBase> getClazz() {
		return clazz;
	}
	
	public void enable(boolean enabled) {
		this.enabled = enabled;
		
		file.set("enabled", enabled);
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public CosmeticFile getFile() {
		return file;
	}
	
	public CosmeticOption[] getOptions() {
		return options;
	}
	
	public CosmeticOption getOption(String key) {
		for (CosmeticOption option : options) {			
			if (key.equals(option.getKey())) {
				return option;
			}
		}
		
		return null;
	}
	
	public void loadOptions(CosmeticOption... options) {
		this.options = options;
		
		for (CosmeticOption option : options) {
			if (option.getValue() instanceof Boolean) {
				option.setValue((boolean) file.safeGet(option.getKey(), (boolean) option.getValue()));
				option.saveIn(this);
			} else if (option instanceof CosmeticOptionColor) {
				CosmeticOptionColor optionColor = (CosmeticOptionColor) option;
				
				int argb = (int) ((long) file.safeGet(optionColor.getKeyARGB(), optionColor.getARGB()));
				boolean rainbow = (boolean) file.safeGet(optionColor.getKeyRainbow(), optionColor.isRainbowEnabled());
				
				optionColor.setARGB(argb);
				optionColor.enableRainbow(rainbow);
				optionColor.saveIn(this);
			} else if (option instanceof CosmeticOptionScale) {
				option.setValue((int) ((long) file.safeGet(option.getKey(), (int) option.getValue())));
				option.saveIn(this);
			}
		}
	}
	
	public boolean castOptionValueIntoBoolean(String key) {
		return (boolean) getOption(key).getValue();
	}
	
	public int castOptionValueIntoInt(String key) {
		return (int) getOption(key).getValue();
	}
	
	public CosmeticOptionColor getOptionColor(String key) {
		return (CosmeticOptionColor) getOption(key);
	}
}
