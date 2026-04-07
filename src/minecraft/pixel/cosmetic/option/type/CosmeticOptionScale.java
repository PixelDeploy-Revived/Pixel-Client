package pixel.cosmetic.option.type;

import net.minecraft.client.Minecraft;
import pixel.cosmetic.Cosmetic;
import pixel.cosmetic.option.CosmeticOption;

public class CosmeticOptionScale extends CosmeticOption {
	public static class GuiSlider extends pixel.gui.GuiSlider {
		private Cosmetic cosmetic;
		private CosmeticOptionScale option;
		
		public GuiSlider(Cosmetic cosmetic, CosmeticOptionScale option, int id, int x, int y, int min, int max, int defaultValue) {
			super(id, x, y, "Scale", 0, min, max, defaultValue);
			
			this.cosmetic = cosmetic;
			this.option = option;
		}
		
		private String getDisplayString() {
			return name + ": " + (int) getValue() + "%";
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
			
			option.saveIn(cosmetic);
		}
	}
	
	private final int min;
	private final int max;
	
	public CosmeticOptionScale(String key, int value, int min, int max) {
		super(key, value, null);
		
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
