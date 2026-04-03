package pixel.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import pixel.cosmetics.Cosmetic;
import pixel.cosmetics.option.type.CosmeticOptionColor;
import pixel.gui.hud.ScreenPosition;
import pixel.util.ColorManager;

public class GuiCosmeticColor extends GuiScreen {
	private final GuiScreen prevGuiScreen;
	private final Cosmetic cosmetic;
	private final CosmeticOptionColor option;
	private final ColorManager color;
	
	private GuiSlider sliderRed;
	private GuiSlider sliderGreen;
	private GuiSlider sliderBlue;
	private GuiSlider sliderRainbowSpeed;
	
	public GuiCosmeticColor(GuiScreen prevGuiScreen, Cosmetic cosmetic, CosmeticOptionColor option) {
		this.prevGuiScreen = prevGuiScreen;
		this.cosmetic = cosmetic;
		this.option = option;
		color = new ColorManager(option.getARGB());
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		int i = -16;
		
		Gui.drawText(fontRendererObj, cosmetic.getName(), (width - fontRendererObj.getStringWidth(cosmetic.getName())) / 2, 15, ColorManager.WHITE.getARGB(), true, false);
		Gui.drawRect((width - 150) / 2, height / 4 + i , (width - 150) / 2 + 150, height / 4 + i + 20, color.getARGB());
		Gui.drawHollowRect((width - 150) / 2, height / 4 + i , 150, 20, 1, ColorManager.BLACK.getARGB());
		Gui.drawText(fontRendererObj, option.getInGuiSettings().getName(), (width - fontRendererObj.getStringWidth(option.getInGuiSettings().getName())) / 2, height / 4 + i + (20 - fontRendererObj.FONT_HEIGHT + 1) / 2, ColorManager.WHITE.getARGB(), true, option.isRainbowEnabled());
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void initGui() {
		buttonList.clear();
		
		int i = -16;
		
		buttonList.add(sliderRed = new GuiSlider(1, (width - 150) / 2, height / 4 + 24 + i, "Red", 0, 0.0F, 255.0F, color.getRed()));
		buttonList.add(sliderGreen = new GuiSlider(2, (width - 150) / 2, height / 4 + 48 + i, "Green", 0, 0.0F, 255.0F, color.getGreen()));
		buttonList.add(sliderBlue = new GuiSlider(3, (width - 150) / 2, height / 4 + 72 + i, "Blue", 0, 0.0F, 255.0F, color.getBlue()));
		buttonList.add(new GuiButton(4, (width - 150) / 2, height / 4 + 96 + i, 150, 20, "Rainbow: " + (option.isRainbowEnabled() ? "ON" : "OFF")));
		buttonList.add(sliderRainbowSpeed = new GuiSlider(5, (width - 150) / 2, height / 4 + 120 + i, "Rainbow Speed", 0, 1000.0F, 5000.0F, option.getRainbowSpeed()));
		
		sliderRainbowSpeed.enabled = option.isRainbowEnabled();
		
		buttonList.add(new GuiButton(0, (width - 150) / 2, height / 4 + 144 + i, 150, 20, I18n.format("gui.done")));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case 0:
			mc.displayGuiScreen(prevGuiScreen);
			break;
		case 1:
			color.setRed((int) sliderRed.getValue());
			option.setARGB(color.getARGB());
			option.saveIn(cosmetic);
			break;
		case 2:
			color.setGreen((int) sliderGreen.getValue());
			option.setARGB(color.getARGB());
			option.saveIn(cosmetic);
			break;
		case 3:
			color.setBlue((int) sliderBlue.getValue());
			option.setARGB(color.getARGB());
			option.saveIn(cosmetic);
			break;
		case 4:
			option.enableRainbow(!option.isRainbowEnabled());
			option.saveIn(cosmetic);
			initGui();
			break;
		case 5:
			option.setRainbowSpeed((int) sliderRainbowSpeed.getValue());
			option.saveIn(cosmetic);
		}
	}
	
	@Override
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		color.setRed((int) sliderRed.getValue());
		color.setGreen((int) sliderGreen.getValue());
		color.setBlue((int) sliderBlue.getValue());
		option.setRainbowSpeed((int) sliderRainbowSpeed.getValue());
		
		option.setARGB(color.getARGB());
		option.saveIn(cosmetic);
	}
}
