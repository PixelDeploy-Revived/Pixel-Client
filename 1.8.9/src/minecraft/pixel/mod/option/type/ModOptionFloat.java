package pixel.mod.option.type;

import net.minecraft.client.Minecraft;
import pixel.mod.Mod;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOptionParent;

public class ModOptionFloat extends ModOption {
	public static class GuiSlider extends pixel.gui.GuiSlider {
		private Mod mod;
		private ModOptionFloat option;
		
		public GuiSlider(Mod mod, ModOptionFloat option, int id, int x, int y, float min, float max, float defaultValue) {
			super(id, x, y, option.getInGuiSettings().getName(), option.getInGuiSettings().getDecimals(), min, max, defaultValue);
			
			this.mod = mod;
			this.option = option;
		}
		
		@Override
		protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
			super.mouseDragged(mc, mouseX, mouseY);
					
			option.setValue(getValue());
		}
		
		@Override
		public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
			option.setValue(getValue());
			
			return super.mousePressed(mc, mouseX, mouseY);
		}
		
		@Override
		public void mouseReleased(int mouseX, int mouseY) {
			super.mouseReleased(mouseX, mouseY);
			
			option.saveIn(mod);
		}
	}
	
	public static class InGuiSettings extends ModOption.InGuiSettings {
		private int decimals;
		
		public InGuiSettings(boolean visible) {
			super(visible);
		}
		
		public InGuiSettings(String optionName, int decimals) {
			super(optionName);
			
			this.decimals = decimals;
		}
		
		public int getDecimals() {
			return decimals;
		}
	}
	
	private float min;
	private float max;
	
	public ModOptionFloat(ModOptionParent optionParent, String key, float value, float min, float max, InGuiSettings inGuiSettings) {
		super(optionParent, key, value, inGuiSettings);
		
		this.min = min;
		this.max = max;
	}
	
	public ModOptionFloat(String key, float value, float min, float max, InGuiSettings inGuiSettings) {
		super(key, value, inGuiSettings);
		
		this.min = min;
		this.max = max;
	}

	public float getMinValue() {
		return min;
	}

	public float getMaxValue() {
		return max;
	}
	
	public InGuiSettings getInGuiSettings() {
		return (InGuiSettings) inGuiSettings;
	}
}
