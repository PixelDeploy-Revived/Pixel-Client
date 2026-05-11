package pixel.mod.impl.togglesprintsneak;

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

public class ToggleSprintSneak extends ModDraggable {
	public int keyHoldTicks = 7;
	public boolean sprinting = false;
	public boolean sneaking = false;
	
	public ToggleSprintSneak() {
		super(true);
				
		loadOptions(
				new ModOption("toggleSprint", true, new InGuiSettings("Toggle Sprint")),
				new ModOption("toggleSneak", false, new InGuiSettings("Toggle Sneak")),
				new ModOption("flyBoost", true, new InGuiSettings("Fly Boost")),
				new ModOptionFloat(new ModOptionParent("flyBoost"), "flyBoostFactor", 4.0F, 2.0F, 8.0F, new ModOptionFloat.InGuiSettings("Fly Boost Factor", 1)),
				new ModOption("showText", true, new InGuiSettings("Show Text")),
				new ModOptionColor(new ModOptionParent("showText"), "textColor", ColorManager.WHITE.getARGB(), false, new ModOptionColor.InGuiSettings("Text Color", false, true)),
				new ModOption(new ModOptionParent("showText"), "textShadow", true, new InGuiSettings("Text Shadow")),
				new ModOptionEnum(new ModOptionParent("showText"), "brackets", Brackets.toEnumList(), Brackets.SQUARE.getIndex(), new InGuiSettings("Brackets")),
				new ModOption(new ModOptionParent("showText"), "drawBackground", false, new InGuiSettings("Draw Background")),
				new ModOptionColor(new ModOptionParent("drawBackground"), "backgroundColor", ColorManager.BLACK_66.getARGB(), false, new ModOptionColor.InGuiSettings("Background Color", true, false)),
				new ModOptionInt(new ModOptionParent("drawBackground"), "backgroundGap", 10, 5, 20, new ModOptionInt.InGuiSettings("Background Width")),
				new ModOptionInt(new ModOptionParent("drawBackground"), "backgroundHeight", 17, 10, 20, new ModOptionInt.InGuiSettings("Background Height")),
				new ModOption(new ModOptionParent("drawBackground"), "drawBorder", false, new InGuiSettings("Draw Border")),
				new ModOptionColor(new ModOptionParent("drawBorder"), "borderColor", ColorManager.BLACK.getARGB(), false, new ModOptionColor.InGuiSettings("Border Color", true, false)),
				new ModOptionFloat(new ModOptionParent("drawBorder"), "borderThickness", 1.0F, 0.5F, 2.0F, new ModOptionFloat.InGuiSettings("Border Thickness", 1))
				);
	}
	
	@Override
	public String getName() {
		return "Toggle Sprint/Sneak";
	}

	@Override
	public int getWidth() {
		int width = font.getStringWidth(Brackets.fromIndex(castOptionValueIntoInt("brackets")).wrap("Sprinting (Toggled)"));
		
		if (castOptionValueIntoBoolean("drawBackground")) {
			width += castOptionValueIntoInt("backgroundGap");
		}
		
		return width;
	}

	@Override
	public int getHeight() {
		return castOptionValueIntoBoolean("drawBackground") ? castOptionValueIntoInt("backgroundHeight") : font.FONT_HEIGHT;
	}

	@Override
	public void render(ScreenPosition pos) {
	    if (castOptionValueIntoBoolean("showText") && mc.thePlayer.movementInput.getDisplayText() != "") {
	    	draw(pos, mc.thePlayer.movementInput.getDisplayText());
	    }
	}
	
	@Override
	public void renderDummy(ScreenPosition pos) {
		if (castOptionValueIntoBoolean("showText")) {
			draw(pos, "Sprinting (Toggled)");
	    }
	}
	
	private void draw(ScreenPosition pos, String text) {
		String formattedText = Brackets.fromIndex(castOptionValueIntoInt("brackets")).wrap(text);
		
		if (castOptionValueIntoBoolean("drawBackground")) {	    		
			drawRect(pos, pos.getAbsoluteX(), pos.getAbsoluteY(), getWidth(), getHeight(), getOptionColor("backgroundColor").getARGB(), formattedText, castOptionValueIntoInt("backgroundGap"));
	    	
	    	if (castOptionValueIntoBoolean("drawBorder")) {
	    		drawBorder(pos, pos.getAbsoluteX(), pos.getAbsoluteY(), getWidth(), getHeight(), getOptionColor("borderColor").getARGB(), castOptionValueIntoFloat("borderThickness"), formattedText, castOptionValueIntoInt("backgroundGap"));
	    	}
	    	
	    	drawTextCentered(formattedText, castOptionValueIntoInt("backgroundGap"), pos.getAbsoluteX(), pos.getAbsoluteY(), getOptionColor("textColor").getARGB(), castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
    	} else {
		    drawTextAligned(formattedText, pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, getOptionColor("textColor").getARGB(), castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
    	}
	}
}