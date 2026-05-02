package pixel.mod.impl;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDraggable;
import pixel.mod.option.Brackets;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;
import pixel.mod.option.ModOptionParent;
import pixel.mod.option.type.ModOptionColor;
import pixel.mod.option.type.ModOptionEnum;
import pixel.mod.option.type.ModOptionFloat;
import pixel.util.ColorManager;

public class MemoryUsage extends ModDraggable {
	public MemoryUsage() {
		super(false, 0, 0);
		
		loadOptions(
				new ModOptionColor("textColor", ColorManager.WHITE.getARGB(), false, new ModOptionColor.InGuiSettings("Text Color", false, true)),
				new ModOption("textShadow", true, new InGuiSettings("Text Shadow")),
				new ModOptionEnum("brackets", Brackets.toEnumList(), Brackets.SQUARE.getIndex(), new InGuiSettings("Brackets")),
				new ModOption("drawBackground", false, new InGuiSettings("Draw Background")),
				new ModOptionColor(new ModOptionParent("drawBackground"), "backgroundColor", ColorManager.BLACK_66.getARGB(), false, new ModOptionColor.InGuiSettings("Background Color", true, false)),
				new ModOption(new ModOptionParent("drawBackground"), "drawBorder", false, new InGuiSettings("Draw Border")),
				new ModOptionFloat(new ModOptionParent("drawBorder"), "borderThickness", 1.0F, 0.5F, 2.0F, new ModOptionFloat.InGuiSettings("Border Thickness", 1)),
				new ModOptionColor(new ModOptionParent("drawBorder"), "borderColor", ColorManager.BLACK.getARGB(), false, new ModOptionColor.InGuiSettings("Border Color", true, false))
				);
	}
	
	@Override
	public int getWidth() {
		return castOptionValueIntoBoolean("drawBackground") ? 54 : font.getStringWidth(Brackets.fromIndex(castOptionValueIntoInt("brackets")).wrap("Mem: 0%"));
	}
	
	@Override
	public int getHeight() {
		return castOptionValueIntoBoolean("drawBackground") ? 14 : font.FONT_HEIGHT;
	}
	
	@Override
	public void render(ScreenPosition pos) {
		String text = Brackets.fromIndex(castOptionValueIntoInt("brackets")).wrap(String.format("Mem: %1d%%", Long.valueOf((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) * 100L / Runtime.getRuntime().maxMemory())));
		
		if (castOptionValueIntoBoolean("drawBackground")) {
			drawRect(pos.getAbsoluteX(), pos.getAbsoluteY(), getWidth(), getHeight(), getOptionColor("backgroundColor").getARGB());
			
			if (castOptionValueIntoBoolean("drawBorder")) {
				drawBorder(pos.getAbsoluteX(), pos.getAbsoluteY(), getWidth(), getHeight(), getOptionColor("borderColor").getARGB(), castOptionValueIntoFloat("borderThickness"));
			}
			
			drawTextCentered(text, pos.getAbsoluteX(), pos.getAbsoluteY(), getOptionColor("textColor").getARGB(), castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
		} else {
			drawTextAligned(text, pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, getOptionColor("textColor").getARGB(), castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
	    }
	}
	
	@Override
	public void renderDummy(ScreenPosition pos) {
		String text = Brackets.fromIndex(castOptionValueIntoInt("brackets")).wrap("Mem: 0%");
		
		if (castOptionValueIntoBoolean("drawBackground")) {
			drawRect(pos.getAbsoluteX(), pos.getAbsoluteY(), getWidth(), getHeight(), getOptionColor("backgroundColor").getARGB());
			
			if (castOptionValueIntoBoolean("drawBorder")) {
				drawBorder(pos.getAbsoluteX(), pos.getAbsoluteY(), getWidth(), getHeight(), getOptionColor("borderColor").getARGB(), castOptionValueIntoFloat("borderThickness"));
			}
	    	
			drawTextCentered(text, pos.getAbsoluteX(), pos.getAbsoluteY(), getOptionColor("textColor").getARGB(), castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
		} else {
			drawTextAligned(text, pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, getOptionColor("textColor").getARGB(), castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
		}
	}
}
