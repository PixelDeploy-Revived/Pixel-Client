package pixel.mod;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import pixel.Pixel;
import pixel.event.EventManager;
import pixel.gui.GuiModOptions;
import pixel.mod.option.ModOption;
import pixel.mod.option.type.ModOptionColor;
import pixel.mod.option.type.ModOptionEnum;
import pixel.mod.option.type.ModOptionFloat;
import pixel.mod.option.type.ModOptionInt;
import pixel.util.FileManager;

public abstract class Mod {
	protected boolean enabled;
	protected ModOption[] options;
	
	protected final Pixel pixelClient;
	protected final Minecraft mc;
	protected final FontRenderer font;
	protected final FileManager file;
	
	public Mod(boolean enabled) {
		pixelClient = Pixel.getInstance();
		mc = Minecraft.getMinecraft();
		font = mc.fontRendererObj;
		file = FileManager.create(getClass().getSimpleName());
		
		enable((boolean) file.safeGet("enabled", enabled));
	}
	
	public void enable(boolean enabled) {
		this.enabled = enabled;
		
		if (enabled) {
			EventManager.register(this);
		} else {
			EventManager.unregister(this);
		}
		
		file.set("enabled", enabled);
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public FileManager getFile() {
		return file;
	}
	
	public String getName() {
		return StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(getClass().getSimpleName().replaceAll("\\d+", "")), " ");
	}
	
	public GuiScreen getGuiOptions(GuiScreen prevGuiScreen) {
		return new GuiModOptions(prevGuiScreen, this, 0);
	}
	
	public ModOption[] getOptions() {
		return options;
	}
	
	public ModOption getOption(String key) {
		for (ModOption option : options) {			
			if (key.equals(option.getKey())) {
				return option;
			}
		}
		
		return null;
	}
	
	public void loadOptions(ModOption... options) {
		this.options = options;
		
		for (ModOption option : options) {
			if (option.getValue() instanceof Boolean) {
				option.setValue((boolean) file.safeGet(option.getKey(), (boolean) option.getValue()));
				option.saveIn(this);
			} else if (option instanceof ModOptionColor) {
				ModOptionColor optionColor = (ModOptionColor) option;

				int argb = (int) ((long) file.safeGet(optionColor.getKeyARGB(), optionColor.getARGB()));
				boolean chroma = (boolean) file.safeGet(optionColor.getKeyChroma(), optionColor.isChromaEnabled());
				
				optionColor.setARGB(argb);
				optionColor.enableChroma(chroma);
				optionColor.saveIn(this);
			} else if (option instanceof ModOptionFloat) {
				option.setValue((float) ((double) file.safeGet(option.getKey(), (float) option.getValue())));
				option.saveIn(this);
			} else if (option instanceof ModOptionInt || option instanceof ModOptionEnum) {
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
	
	public float castOptionValueIntoFloat(String key) {
		return (float) getOption(key).getValue();
	}
	
	public ModOptionColor getOptionColor(String key) {
		return (ModOptionColor) getOption(key);
	}
}
