package pixel.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.resources.I18n;
import pixel.Pixel;
import pixel.cosmetic.Cosmetic;
import pixel.cosmetic.option.CosmeticOption;
import pixel.cosmetic.option.type.CosmeticOptionColor;
import pixel.cosmetic.option.type.CosmeticOptionScale;
import pixel.util.ColorManager;

public class GuiCosmeticOptions extends GuiScreen {
	private GuiScreen prevGuiScreen;
	private Cosmetic cosmetic;
	private int page;
	
	public GuiCosmeticOptions(GuiScreen prevGuiScreen, Cosmetic cosmetic, int page) {
		this.prevGuiScreen = prevGuiScreen;
		this.cosmetic = cosmetic;
		this.page = page;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		Gui.drawText(fontRendererObj, cosmetic.getName(), (width - mc.fontRendererObj.getStringWidth(cosmetic.getName())) / 2, 15, ColorManager.WHITE.getARGB(), true, false);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void initGui() {		
		buttonList.clear();
		
		buttonList.add(new GuiButton(-1, width / 2 - 150 / 2, height / 6, 150, 20, "Toggled: " + (cosmetic.isEnabled() ? "ON" : "OFF")));
		
		int rows = 6;
		int cols = 2;
		int optionsPerPage = rows * cols;
		
		int i = 0;
		int j = 1;
		int k = 0;
		int h = 1;
		
		for (CosmeticOption option : cosmetic.getOptions()) {
			if (i >= optionsPerPage * page && i < optionsPerPage * page + optionsPerPage) {
				if (k == 2) {
					k = 0;
					h++;
				}
				
				GuiButton optionButton = null;
				
				if (option.getValue() instanceof Boolean) {
					buttonList.add(optionButton = new GuiButton(j, width / 2 - 155 + (155 + 5) * k, height / 6 + 24 * h, 150, 20, option.getInGuiSettings().getName() + ": " + (((boolean) option.getValue()) ? "ON" : "OFF")));
				} else if (option instanceof CosmeticOptionColor) {
					buttonList.add(optionButton = new GuiButton(j, width / 2 - 155 + (155 + 5) * k, height / 6 + 24 * h, 150, 20, option.getInGuiSettings().getName()));
				} else if (option instanceof CosmeticOptionScale) {
					CosmeticOptionScale optionInt = (CosmeticOptionScale) option;
					
					buttonList.add(optionButton = new CosmeticOptionScale.GuiSlider(cosmetic, optionInt, j, width / 2 - 155 + (155 + 5) * k, height / 6 + 24 * h, optionInt.getMinValue(), optionInt.getMaxValue(), (int) optionInt.getValue()));
				}
				
				k++;
			}
			
			j++;
			i++;
		}
		
		int totPages = cosmetic.getOptions().length / optionsPerPage;
		
		if (totPages + 1 > 1) {
			if (page > 0) {
				buttonList.add(new GuiButton(-2, (width - 200) / 2 - 2 - 20, height / 6 + 168, 20, 20, "<"));
			}
			
			if (page < totPages) {
				buttonList.add(new GuiButton(-3, (width + 200) / 2 + 2, height / 6 + 168, 20, 20, ">"));
			}
		}
		
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 6 + 168, I18n.format("gui.done")));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case -3:
			mc.displayGuiScreen(new GuiCosmeticOptions(prevGuiScreen, cosmetic, page + 1));
			break;
		case -2:
			mc.displayGuiScreen(new GuiCosmeticOptions(prevGuiScreen, cosmetic, page - 1));
			break;
		case -1:
			cosmetic.enable(!cosmetic.isEnabled());
			initGui();
			Pixel.getInstance().renderCosmetics();
			break;
		case 0:
			mc.displayGuiScreen(prevGuiScreen);
		}
		
		int i = 1;
		
		for (CosmeticOption option : cosmetic.getOptions()) {
			if (button.id == i) {
				if (option.getValue() instanceof Boolean) {
					option.setValue(!((boolean) option.getValue()));
					option.saveIn(cosmetic);
					
					initGui();
				} else if (option instanceof CosmeticOptionColor) {
					mc.displayGuiScreen(new GuiCosmeticColor(this, cosmetic, (CosmeticOptionColor) option));
				}
			}
			
			i++;
		}
	}
}
