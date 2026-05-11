package pixel.mod.impl;

import net.minecraft.client.gui.GuiScreen;
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
import pixel.util.ColorManager;

public class ServerAddress extends ModDraggable {
	public ServerAddress() {
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
	
	private int RECT_GAP = 8;
	
	@Override
	public int getWidth() {
		int width = font.getStringWidth(Brackets.fromIndex(castOptionValueIntoInt("brackets")).wrap("mc.example.org"));
		
		if (castOptionValueIntoBoolean("drawBackground")) {
			width += RECT_GAP;
		}
		
		return width;
	}
	
	@Override
	public int getHeight() {
		return castOptionValueIntoBoolean("drawBackground") ? 14 : font.FONT_HEIGHT;
	}
	
	@Override
	public void render(ScreenPosition pos) {
		if (!mc.isSingleplayer()) {			
			draw(pos, mc.getCurrentServerData().serverIP);
		}
	}
	
	@Override
	public void renderDummy(ScreenPosition pos) {
		draw(pos, "mc.example.org");
	}
	
	private void draw(ScreenPosition pos, String text) {
		String formattedText = Brackets.fromIndex(castOptionValueIntoInt("brackets")).wrap(text);
		
		if (castOptionValueIntoBoolean("drawBackground")) {
			drawRect(pos, pos.getAbsoluteX(), pos.getAbsoluteY(), getWidth(), getHeight(), getOptionColor("backgroundColor").getARGB(), formattedText, RECT_GAP);
			
			if (castOptionValueIntoBoolean("drawBorder")) {
				drawBorder(pos, pos.getAbsoluteX(), pos.getAbsoluteY(), getWidth(), getHeight(), getOptionColor("borderColor").getARGB(), castOptionValueIntoFloat("borderThickness"), formattedText, RECT_GAP);
			}
	    	
			drawTextCentered(formattedText, RECT_GAP, pos.getAbsoluteX(), pos.getAbsoluteY(), getOptionColor("textColor").getARGB(), castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
		} else {
    		drawTextAligned(formattedText, pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, getOptionColor("textColor").getARGB(), castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
		}
	}
}
