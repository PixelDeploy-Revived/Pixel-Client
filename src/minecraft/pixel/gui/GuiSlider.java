package pixel.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class GuiSlider extends GuiButton {
	public boolean isMouseDown;
	protected String name;
	protected final int decimals;
	private final float min;
	private final float max;
	private float sliderPosition = 1.0F;

	public GuiSlider(int id, int x, int y, String name, int decimals, float min, float max, float defaultValue) {
		super(id, x, y, 150, 20, "");
		
		this.name = name;
		this.decimals = decimals;
		this.min = min;
		this.max = max;
		sliderPosition = (defaultValue - min) / (max - min);
		displayString = getDisplayString();
	}
	
	public float getValue() {
		return min + (max - min) * sliderPosition;
	}
	
	public float getSliderPosition() {
		return sliderPosition;
	}
	
	private String getDisplayString() {
		return name + ": " + String.format("%." + decimals + "f", getValue());
	}
	
	@Override
	protected int getHoverState(boolean mouseOver) {
		return 0;
	}
	
	@Override
	protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
		if (visible) {
			if (isMouseDown) {
				sliderPosition = (float) (mouseX - (xPosition + 4)) / (float) (width - 8);

				if (sliderPosition < 0.0F) {
					sliderPosition = 0.0F;
				}

				if (sliderPosition > 1.0F) {
					sliderPosition = 1.0F;
				}

				displayString = getDisplayString();
			}

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			drawTexturedModalRect(xPosition + (int) (sliderPosition * (float) (width - 8)), yPosition, 0, 66, 4, 20);
			drawTexturedModalRect(xPosition + (int) (sliderPosition * (float) (width - 8)) + 4, yPosition, 196, 66, 4, 20);
		}
	}
	
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		if (super.mousePressed(mc, mouseX, mouseY)) {
			sliderPosition = (float) (mouseX - (xPosition + 4)) / (float) (width - 8);
			
			if (sliderPosition < 0.0F) {
				sliderPosition = 0.0F;
			}
			
			if (sliderPosition > 1.0F) {
				sliderPosition = 1.0F;
			}
			
			displayString = getDisplayString();
			isMouseDown = true;
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		isMouseDown = false;
	}
}
