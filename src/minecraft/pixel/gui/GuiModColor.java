package pixel.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import pixel.gui.hud.ScreenPosition;
import pixel.mod.Mod;
import pixel.mod.option.type.ModOptionColor;
import pixel.util.ColorManager;

public class GuiModColor extends GuiScreen {
	private final GuiScreen prevGuiScreen;
	private final Mod mod;
	private final ModOptionColor option;
	private final ColorManager color;
	
	private GuiSlider sliderRed;
	private GuiSlider sliderGreen;
	private GuiSlider sliderBlue;
	private GuiSlider sliderAlpha;
	
	public GuiModColor(GuiScreen prevGuiScreen, Mod mod, ModOptionColor option) {
		this.prevGuiScreen = prevGuiScreen;
		this.mod = mod;
		this.option = option;
		color = new ColorManager(option.getARGB());
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		int i = -16;
		
		Gui.drawText(fontRendererObj, mod.getName(), (width - fontRendererObj.getStringWidth(mod.getName())) / 2, 15, ColorManager.WHITE.getARGB(), true, false);
		Gui.drawRect((width - 150) / 2, height / 4 + i , (width - 150) / 2 + 150, height / 4 + i + 20, color.getARGB());
		Gui.drawHollowRect((width - 150) / 2, height / 4 + i , 150, 20, 1, ColorManager.BLACK.getARGB());
		Gui.drawText(fontRendererObj, option.getInGuiSettings().getName(), (width - fontRendererObj.getStringWidth(option.getInGuiSettings().getName())) / 2, height / 4 + i + (20 - fontRendererObj.FONT_HEIGHT + 1) / 2, ColorManager.WHITE.getARGB(), true, option.isChromaEnabled());
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void initGui() {
		buttonList.clear();
		
		int i = -16;
		
		buttonList.add(sliderRed = new GuiSlider(1, (width - 150) / 2, height / 4 + 24 + i, "Red", 0, 0.0F, 255.0F, color.getRed()));
		buttonList.add(sliderGreen = new GuiSlider(2, (width - 150) / 2, height / 4 + 48 + i, "Green", 0, 0.0F, 255.0F, color.getGreen()));
		buttonList.add(sliderBlue = new GuiSlider(3, (width - 150) / 2, height / 4 + 72 + i, "Blue", 0, 0.0F, 255.0F, color.getBlue()));
		
		sliderRed.enabled = !option.isChromaEnabled();
		sliderGreen.enabled = !option.isChromaEnabled();
		sliderBlue.enabled = !option.isChromaEnabled();
		
		int j = 24;
		
		if (option.getInGuiSettings().shouldAlphaSliderBeDrawn()) {
			buttonList.add(sliderAlpha = new GuiSlider(4, (width - 150) / 2, height / 4 + 72 + j + i, "Alpha", 0, 0.0F, 255.0F, color.getAlpha()));
						
			j += 24;
		}
		
		if (option.getInGuiSettings().shouldChromaButtonBeDrawn()) {
			buttonList.add(new GuiButton(5, (width - 150) / 2, height / 4 + 72 + j + i, 150, 20, "Chroma: " + (option.isChromaEnabled() ? "ON" : "OFF")));
			
			j += 24;
		}
		
		buttonList.add(new GuiButton(0, (width - 150) / 2, height / 4 + 72 + j + i, 150, 20, I18n.format("gui.done")));
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
			option.saveIn(mod);
			break;
		case 2:
			color.setGreen((int) sliderGreen.getValue());
			option.setARGB(color.getARGB());
			option.saveIn(mod);
			break;
		case 3:
			color.setBlue((int) sliderBlue.getValue());
			option.setARGB(color.getARGB());
			option.saveIn(mod);
			break;
		case 4:
			color.setAlpha((int) sliderAlpha.getValue());
			option.setARGB(color.getARGB());
			option.saveIn(mod);
			break;
		case 5:
			option.enableChroma(!option.isChromaEnabled());
			option.saveIn(mod);
			initGui();
		}
	}
	
	@Override
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		color.setRed((int) sliderRed.getValue());
		color.setGreen((int) sliderGreen.getValue());
		color.setBlue((int) sliderBlue.getValue());
		
		if (option.getInGuiSettings().shouldAlphaSliderBeDrawn()) {
			color.setAlpha((int) sliderAlpha.getValue());
		}
		
		option.setARGB(color.getARGB());
		option.saveIn(mod);
	}
}
