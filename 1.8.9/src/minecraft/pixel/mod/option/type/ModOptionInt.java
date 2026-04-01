package pixel.mod.option.type;

import net.minecraft.client.Minecraft;
import pixel.mod.Mod;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOptionParent;

public class ModOptionInt extends ModOption {
	public static class GuiSlider extends pixel.gui.GuiSlider {
		private Mod mod;
		private ModOptionInt option;
		
		public GuiSlider(Mod mod, ModOptionInt option, int id, int x, int y, int min, int max, int defaultValue) {
			super(id, x, y, option.getInGuiSettings().getName(), 0, min, max, defaultValue);
			
			this.mod = mod;
			this.option = option;
		}
		
		private String getDisplayString() {
			return name + ": " + (int) getValue();
		}
		
		@Override
		protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
			super.mouseDragged(mc, mouseX, mouseY);
			
			option.setValue((int) getValue());
		}
		
		@Override
		public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
			option.setValue((int) getValue());
			
			return super.mousePressed(mc, mouseX, mouseY);
		}
		
		@Override
		public void mouseReleased(int mouseX, int mouseY) {
			super.mouseReleased(mouseX, mouseY);
			
			option.saveIn(mod);
		}
	}
	
	private final int min;
	private final int max;
	
	public ModOptionInt(ModOptionParent optionParent, String key, int value, int min, int max, InGuiSettings inGuiSettings) {
		super(optionParent, key, value, inGuiSettings);
		
		this.min = min;
		this.max = max;
	}
	
	public ModOptionInt(String key, int value, int min, int max, InGuiSettings inGuiSettings) {
		super(key, value, inGuiSettings);
		
		this.min = min;
		this.max = max;
	}
	
	public int getMinValue() {
		return min;
	}
	
	public int getMaxValue() {
		return max;
	}
}
