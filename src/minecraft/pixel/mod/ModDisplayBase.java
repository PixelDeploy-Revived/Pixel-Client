package pixel.mod;

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

public abstract class ModDisplayBase extends ModDraggable {
	protected String dummyText;
	
	public ModDisplayBase(boolean enabled, String dummyText, ModOption... options) {
		super(enabled, 0, 0);
		
		this.dummyText = dummyText;
		
		ModOption[] defaultOptions = new ModOption[] {
				new ModOptionColor("textColor", ColorManager.WHITE.getARGB(), false, new ModOptionColor.InGuiSettings("Text Color", false, true)),
				new ModOption("textShadow", true, new InGuiSettings("Text Shadow")),
				new ModOptionEnum("brackets", Brackets.toEnumList(), Brackets.SQUARE.getIndex(), new InGuiSettings("Brackets")),
				new ModOption("drawBackground", false, new InGuiSettings("Draw Background")),
				new ModOptionColor(new ModOptionParent("drawBackground"), "backgroundColor", ColorManager.BLACK_66.getARGB(), false, new ModOptionColor.InGuiSettings("Background Color", true, false)),
				new ModOption(new ModOptionParent("drawBackground"), "drawBorder", false, new InGuiSettings("Draw Border")),
				new ModOptionFloat(new ModOptionParent("drawBorder"), "borderThickness", 1.0F, 0.5F, 2.0F, new ModOptionFloat.InGuiSettings("Border Thickness", 1)),
				new ModOptionColor(new ModOptionParent("drawBorder"), "borderColor", ColorManager.BLACK.getARGB(), false, new ModOptionColor.InGuiSettings("Border Color", true, false))
		};
		
		ModOption[] allOptions = new ModOption[defaultOptions.length + options.length];
		System.arraycopy(defaultOptions, 0, allOptions, 0, defaultOptions.length);
		System.arraycopy(options, 0, allOptions, defaultOptions.length, options.length);
		
		loadOptions(allOptions);
	}
	
	@Override
	public int getWidth() {
		return castOptionValueIntoBoolean("drawBackground") ? 54 : font.getStringWidth(Brackets.fromIndex(castOptionValueIntoInt("brackets")).wrap(dummyText));
	}
	
	@Override
	public int getHeight() {
		return castOptionValueIntoBoolean("drawBackground") ? 14 : font.FONT_HEIGHT;
	}
	
	@Override
	public void renderDummy(ScreenPosition pos) {
		draw(pos, dummyText);
	}
	
	public void draw(ScreenPosition pos, String text) {
		String formattedText = Brackets.fromIndex(castOptionValueIntoInt("brackets")).wrap(text);
		
		if (castOptionValueIntoBoolean("drawBackground")) {
			drawRect(pos.getAbsoluteX(), pos.getAbsoluteY(), getWidth(), getHeight(), getOptionColor("backgroundColor").getARGB());
			
			if (castOptionValueIntoBoolean("drawBorder")) {
				drawBorder(pos.getAbsoluteX(), pos.getAbsoluteY(), getWidth(), getHeight(), getOptionColor("borderColor").getARGB(), castOptionValueIntoFloat("borderThickness"));
			}
	    	
			drawTextCentered(formattedText, pos.getAbsoluteX(), pos.getAbsoluteY(), getOptionColor("textColor").getARGB(), castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
		} else {
			drawTextAligned(formattedText, pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 1, getOptionColor("textColor").getARGB(), castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
		}
	}
}
