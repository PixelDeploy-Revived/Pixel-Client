package pixel.mod.impl;

import net.minecraft.client.network.NetworkPlayerInfo;
import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDraggable;
import pixel.mod.option.Brackets;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;
import pixel.mod.option.ModOptionParent;
import pixel.mod.option.type.ModOptionColor;
import pixel.mod.option.type.ModOptionEnum;
import pixel.mod.option.type.ModOptionFloat;
import pixel.mod.option.type.ModOptionInt;
import pixel.util.ColorManager;

public class Ping extends ModDraggable {
	public Ping() {
		super(false);
		
		loadOptions(
				new ModOptionColor("textColor", ColorManager.WHITE.getARGB(), false, new ModOptionColor.InGuiSettings("Text Color", false, true)),
				new ModOption("dynamicColors", false, new InGuiSettings("Dynamic Colors")),
				new ModOption("textShadow", true, new InGuiSettings("Text Shadow")),
				new ModOptionEnum("brackets", Brackets.toEnumList(), Brackets.SQUARE.getIndex(), new InGuiSettings("Brackets")),
				new ModOption("drawBackground", false, new InGuiSettings("Draw Background")),
				new ModOptionColor(new ModOptionParent("drawBackground"), "backgroundColor", ColorManager.BLACK_66.getARGB(), false, new ModOptionColor.InGuiSettings("Background Color", true, false)),
				new ModOptionInt(new ModOptionParent("drawBackground"), "backgroundWidth", 54, 50, 60, new ModOptionInt.InGuiSettings("Background Width")),
				new ModOptionInt(new ModOptionParent("drawBackground"), "backgroundHeight", 14, 10, 20, new ModOptionInt.InGuiSettings("Background Height")),
				new ModOption(new ModOptionParent("drawBackground"), "drawBorder", false, new InGuiSettings("Draw Border")),
				new ModOptionFloat(new ModOptionParent("drawBorder"), "borderThickness", 1.0F, 0.5F, 2.0F, new ModOptionFloat.InGuiSettings("Border Thickness", 1)),
				new ModOptionColor(new ModOptionParent("drawBorder"), "borderColor", ColorManager.BLACK.getARGB(), false, new ModOptionColor.InGuiSettings("Border Color", true, false))
				);
	}
	
	@Override
	public int getWidth() {
		return castOptionValueIntoBoolean("drawBackground") ? castOptionValueIntoInt("backgroundWidth") : font.getStringWidth(Brackets.fromIndex(castOptionValueIntoInt("brackets")).wrap("-1 ms"));
	}
	
	@Override
	public int getHeight() {
		return castOptionValueIntoBoolean("drawBackground") ? castOptionValueIntoInt("backgroundHeight") : font.FONT_HEIGHT;
	}
	
	@Override
	public void render(ScreenPosition pos) {
		if (!mc.isSingleplayer()) {
			NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
			int ping = info != null ? info.getResponseTime() : 0;
			
			draw(pos, ping);
		}
	}
	
	@Override
	public void renderDummy(ScreenPosition pos) {
		draw(pos, -1);
	}
	
	public void draw(ScreenPosition pos, int ping) {
		String text = Brackets.fromIndex(castOptionValueIntoInt("brackets")).wrap(ping + " ms");
		
		int textColor = getOptionColor("textColor").getARGB();
		
		if (castOptionValueIntoBoolean("dynamicColors")) {
			if (ping > 300) {
				textColor = ColorManager.DEFAULT_DARK_RED.getARGB();
			} else if (ping > 200) {
				textColor = ColorManager.DEFAULT_RED.getARGB();
			} else if (ping > 150) {
				textColor = ColorManager.DEFAULT_GOLD.getARGB();
			} else if (ping > 100) {
				textColor = ColorManager.DEFAULT_YELLOW.getARGB();
			} else if (ping > 50) {
				textColor = ColorManager.DEFAULT_GREEN.getARGB();
			}
		}
		
		if (castOptionValueIntoBoolean("drawBackground")) {
			drawRect(pos.getAbsoluteX(), pos.getAbsoluteY(), getWidth(), getHeight(), getOptionColor("backgroundColor").getARGB());
			
			if (castOptionValueIntoBoolean("drawBorder")) {
				drawBorder(pos.getAbsoluteX(), pos.getAbsoluteY(), getWidth(), getHeight(), getOptionColor("borderColor").getARGB(), castOptionValueIntoFloat("borderThickness"));
			}
	    	
			drawTextCentered(text, pos.getAbsoluteX(), pos.getAbsoluteY(), textColor, castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
		} else {
    		drawTextAligned(text, pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, textColor, castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
		}
	}
}