package pixel.gui;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import pixel.Pixel;
import pixel.mod.Mod;
import pixel.mod.ModHandler;
import pixel.util.ColorManager;

public class GuiModList extends GuiScreen {
	private GuiScreen prevGuiScreen;
	private int page;
	
	public GuiModList(GuiScreen prevGuiScreen, int page) {
		this.prevGuiScreen = prevGuiScreen;
		this.page = page;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		Gui.drawText(fontRendererObj, "Mod List", (width - mc.fontRendererObj.getStringWidth("Mod List")) / 2, 15, ColorManager.WHITE.getARGB(), true, false);
	}
	
	@Override
	public void initGui() {
		buttonList.clear();
		
		List<Mod> mods = ModHandler.getModsList();
		mods.sort(Comparator.comparing(Mod::getName));
		
		int rows = 4;
		int cols = 3;
		int modsCanBeDrawn = Math.min(mods.size(), rows * cols);
		
		int j = 0;
		int k = 0;
		
		int start = page * modsCanBeDrawn;
		int end = Math.min(start + modsCanBeDrawn, mods.size());

		for (int i = start; i < end; i++) {
			Mod mod = mods.get(i);
			
			if (j >= cols) {
				j = 0;
				
				k++;
			}
			
			String modName = mod.getName();
			
			if (fontRendererObj.getStringWidth(modName) > 70) {
				int l = 0;
				
				for (int h = 0; h < 70; l++) {					
					h += fontRendererObj.getStringWidth(modName.substring(l, l + 1));
				}
				
				modName = modName.substring(0, l);
			}
			
			buttonList.add(new GuiButton(i + 1, width / 2 - 80 / 2 + ((2 + 80) * (j - 1)), height / 6 + 32 + (2 + 20) * k, 80, 20, modName));
			
			j++;
		}
		
		buttonList.add(new GuiButton(0, (width - 200) / 2, height / 6 + 168, I18n.format("gui.done")));
		
		int totPages = mods.size() / modsCanBeDrawn;
		
		if (totPages + 1 > 1 && mods.size() != modsCanBeDrawn) {
			if (page > 0) {
				buttonList.add(new GuiButton(-1, (width - 200) / 2 - 2 - 20, height / 6 + 168, 20, 20, "<"));
			}
			
			if (page < totPages) {
				buttonList.add(new GuiButton(-2, (width + 200) / 2 + 2, height / 6 + 168, 20, 20, ">"));
			}
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case -2:
			mc.displayGuiScreen(new GuiModList(prevGuiScreen, page + 1));
			break;
		case -1:
			mc.displayGuiScreen(new GuiModList(prevGuiScreen, page - 1));
			break;
		case 0:
			mc.displayGuiScreen(prevGuiScreen);
		}
		
		int i = 1;
		
		for (Mod mod : ModHandler.getModsList()) {
			if (button.id == i) {
				mc.displayGuiScreen(mod.getGuiOptions(this));
			}
			
			i++;
		}
	}
}
