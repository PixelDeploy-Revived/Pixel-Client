package pixel.gui;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import pixel.cosmetic.Cosmetic;
import pixel.cosmetic.CosmeticBase;
import pixel.cosmetic.CosmeticHandler;
import pixel.util.ColorManager;

public class GuiCosmetics extends GuiScreen {
	private GuiScreen prevGuiScreen;
	private int page;
	
	public GuiCosmetics(GuiScreen prevGuiScreen, int page) {
		this.prevGuiScreen = prevGuiScreen;
		this.page = page;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		Gui.drawText(fontRendererObj, "Cosmetics", (width - mc.fontRendererObj.getStringWidth("Cosmetics")) / 2, 15, ColorManager.WHITE.getARGB(), true, false);
	}
	
	@Override
	public void initGui() {
		buttonList.clear();
		
		List<Cosmetic> cosmetics = CosmeticHandler.getCosmetics();
		cosmetics.sort(Comparator.comparing(Cosmetic::getName));
		
		int rows = 4;
		int cols = 3;
		int cosmeticsCanBeDrawn = Math.min(cosmetics.size(), rows * cols);
		
		int j = 0;
		int k = 0;

		for (int i = 0; i < cosmeticsCanBeDrawn; i++) {
			int modIndex = i + (cosmeticsCanBeDrawn * page);
			Cosmetic cosmetic = cosmetics.get(modIndex);
			
			if (j >= cols) {
				j = 0;
				
				k++;
			}
			
			String cosmeticName = cosmetic.getName();
			
			if (fontRendererObj.getStringWidth(cosmeticName) > 70) {
				int l = 0;
				
				for (int h = 0; h < 70; l++) {					
					h += fontRendererObj.getStringWidth(cosmeticName.substring(l, l + 1));
				}
				
				cosmeticName = cosmeticName.substring(0, l);
			}
			
			buttonList.add(new GuiButton(modIndex + 1, width / 2 - 80 / 2 + ((2 + 80) * (j - 1)), height / 6 + 32 + (2 + 20) * k, 80, 20, cosmeticName));
			
			j++;
		}
		
		buttonList.add(new GuiButton(0, (width - 200) / 2, height / 6 + 168, I18n.format("gui.done")));
		
		int totPages = cosmetics.size() / cosmeticsCanBeDrawn;
		
		if (totPages > 1 && cosmetics.size() != cosmeticsCanBeDrawn) {
			if (page > 0) {
				buttonList.add(new GuiButton(-1, (width - 200) / 2 - 2 - 20, height / 6 + 168, 20, 20, "<"));
			}
			
			if (page < totPages && page - 1 != totPages) {
				buttonList.add(new GuiButton(-2, (width + 200) / 2 + 2, height / 6 + 168, 20, 20, ">"));
			}
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case -2:
			mc.displayGuiScreen(new GuiCosmetics(prevGuiScreen, page + 1));
			break;
		case -1:
			mc.displayGuiScreen(new GuiCosmetics(prevGuiScreen, page - 1));
			break;
		case 0:
			mc.displayGuiScreen(prevGuiScreen);
		}
		
		int i = 1;
		
		for (Cosmetic cosmetic : CosmeticHandler.getCosmetics()) {
			if (button.id == i) {
				mc.displayGuiScreen(cosmetic.getGuiOptions(this));
			}
			
			i++;
		}
	}
}
