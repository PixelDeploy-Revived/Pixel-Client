package pixel.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import pixel.mod.Mod;
import pixel.mod.ModHandler;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOptionParent;
import pixel.mod.option.type.ModOptionColor;
import pixel.mod.option.type.ModOptionEnum;
import pixel.mod.option.type.ModOptionFloat;
import pixel.mod.option.type.ModOptionInt;
import pixel.util.ColorManager;

public class GuiModOptions extends GuiScreen {
	private GuiScreen prevGuiScreen;
	private Mod mod;
	private int page;
	
	public GuiModOptions(GuiScreen prevGuiScreen, Mod mod, int page) {
		this.prevGuiScreen = prevGuiScreen;
		this.mod = mod;
		this.page = page;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		Gui.drawText(fontRendererObj, mod.getName(), (width - mc.fontRendererObj.getStringWidth(mod.getName())) / 2, 15, ColorManager.WHITE.getARGB(), true, false);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void initGui() {		
		buttonList.clear();
		
		buttonList.add(new GuiButton(-1, width / 2 - 150 / 2, height / 6, 150, 20, "Toggled: " + (mod.isEnabled() ? "ON" : "OFF")));
		
		int rows = 6;
		int cols = 2;
		int optionsPerPage = rows * cols;
		
		int i = 0;
		int j = 1;
		int k = 0;
		int h = 1;
		
		for (ModOption option : mod.getOptions()) {
			if (option.getInGuiSettings().isVisible()) {
				if (i >= optionsPerPage * page && i < optionsPerPage * page + optionsPerPage) {
					if (k == 2) {
						k = 0;
						h++;
					}
					
					GuiButton optionButton = null;
					
					if (option.getValue() instanceof Boolean) {
						buttonList.add(optionButton = new GuiButton(j, width / 2 - 155 + (155 + 5) * k, height / 6 + 24 * h, 150, 20, option.getInGuiSettings().getName() + ": " + (((boolean) option.getValue()) ? "ON" : "OFF")));
					} else if (option instanceof ModOptionColor) {
						buttonList.add(optionButton = new GuiButton(j, width / 2 - 155 + (155 + 5) * k, height / 6 + 24 * h, 150, 20, option.getInGuiSettings().getName()));
					} else if (option instanceof ModOptionInt) {
						ModOptionInt optionInt = (ModOptionInt) option;
						
						buttonList.add(optionButton = new ModOptionInt.GuiSlider(mod, optionInt, j, width / 2 - 155 + (155 + 5) * k, height / 6 + 24 * h, optionInt.getMinValue(), optionInt.getMaxValue(), (int) optionInt.getValue()));
					} else if (option instanceof ModOptionFloat) {
						ModOptionFloat optionFloat = (ModOptionFloat) option;
						
						buttonList.add(optionButton = new ModOptionFloat.GuiSlider(mod, optionFloat, j, width / 2 - 155 + (155 + 5) * k, height / 6 + 24 * h, optionFloat.getMinValue(), optionFloat.getMaxValue(), (float) optionFloat.getValue()));
					} else if (option instanceof ModOptionEnum) {
						buttonList.add(optionButton = new GuiButton(j, width / 2 - 155 + (155 + 5) * k, height / 6 + 24 * h, 150, 20, option.getInGuiSettings().getName() + ": " + ((ModOptionEnum) option).getEnumList().findElement((int) option.getValue()).getName()));
					}
					
					if (optionButton != null) {
						optionButton.enabled = isOptionEnable(option);
					}
					
					k++;
				}
				
				j++;
				i++;
			}
		}
		
		int totPages = mod.getOptions().length / optionsPerPage;
		
		if (totPages + 1 > 1 && mod.getOptions().length != optionsPerPage) {
			if (page > 0) {
				buttonList.add(new GuiButton(-2, (width - 200) / 2 - 2 - 20, height / 6 + 168, 20, 20, "<"));
			}
			
			if (page < totPages && page + 1 != totPages) {
				buttonList.add(new GuiButton(-3, (width + 200) / 2 + 2, height / 6 + 168, 20, 20, ">"));
			}
		}
		
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 6 + 168, I18n.format("gui.done")));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case -3:
			mc.displayGuiScreen(new GuiModOptions(prevGuiScreen, mod, page + 1));
			break;
		case -2:
			mc.displayGuiScreen(new GuiModOptions(prevGuiScreen, mod, page - 1));
			break;
		case -1:
			mod.enable(!mod.isEnabled());
			initGui();
			break;
		case 0:
			mc.displayGuiScreen(prevGuiScreen);
		}
		
		int i = 1;
		
		for (ModOption option : mod.getOptions()) {
			if (button.id == i) {
				if (option.getValue() instanceof Boolean) {
					option.setValue(!((boolean) option.getValue()));
					option.saveIn(mod);
					
					initGui();
				} else if (option instanceof ModOptionColor) {
					mc.displayGuiScreen(new GuiModColor(this, mod, (ModOptionColor) option));
				} else if (option instanceof ModOptionEnum) {
					ModOptionEnum optionEnum = (ModOptionEnum) option;
					
					int index = (int) optionEnum.getValue();
					int min = optionEnum.getEnumList().getFirst();
					int max = optionEnum.getEnumList().getLast();
					
					option.setValue(index < max ? index + 1 : min);
					option.saveIn(mod);
					
					initGui();
				}
			}
			
			i++;
		}
	}
	
	private boolean isOptionEnable(ModOption option) {
		ModOptionParent optionParent = option.getOptionParent();
		
		if (optionParent == null) {
			return true;
		}
		
		ModOption opt = mod.getOption(optionParent.getKey());
		
		if (!(opt.getValue() instanceof Boolean)) {
			return false;
		}
		
		boolean isParentedOptionToggled = (boolean) opt.getValue();
		boolean enabled = optionParent.isNeeded() ? isParentedOptionToggled : !isParentedOptionToggled;
		
		return enabled && isOptionEnable(opt);
	}
}
