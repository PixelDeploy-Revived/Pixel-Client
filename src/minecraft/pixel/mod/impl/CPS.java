package pixel.mod.impl;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDraggable;
import pixel.mod.option.Brackets;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOptionParent;
import pixel.mod.option.ModOption.InGuiSettings;
import pixel.mod.option.type.ModOptionColor;
import pixel.mod.option.type.ModOptionEnum;
import pixel.mod.option.type.ModOptionFloat;
import pixel.mod.option.type.ModOptionInt;
import pixel.util.ColorManager;

public class CPS extends ModDraggable {
	private List<Long> leftClicks = new ArrayList<>();
	private boolean wasLeftPressed;
	private long lastLeftPressed;
	
	private List<Long> rightClicks = new ArrayList<>();
	private boolean wasRightPressed;
	private long lastRightPressed;
	
	public CPS() {
		super(true, 0, 0);
		
		loadOptions(
				new ModOption("textShadow", true, new InGuiSettings("Text Shadow")),
				new ModOptionColor("textColor", ColorManager.WHITE.getARGB(), false, new ModOptionColor.InGuiSettings("Text Color", false, true)),
				new ModOptionEnum("brackets", Brackets.toEnumList(), Brackets.SQUARE.getIndex(), new InGuiSettings("Brackets")),
				new ModOption("drawBackground", false, new InGuiSettings("Draw Background")),
				new ModOptionColor(new ModOptionParent("drawBackground"), "backgroundColor", ColorManager.BLACK_66.getARGB(), false, new ModOptionColor.InGuiSettings("Background Color", true, false)),
				new ModOption(new ModOptionParent("drawBackground"), "drawBorder", false, new InGuiSettings("Draw Border")),
				new ModOptionFloat(new ModOptionParent("drawBorder"), "borderThickness", 1.0F, 0.5F, 2.0F, new ModOptionFloat.InGuiSettings("Border Thickness", 1)),
				new ModOptionColor(new ModOptionParent("drawBorder"), "borderColor", ColorManager.BLACK.getARGB(), false, new ModOptionColor.InGuiSettings("Border Color", true, false)),
				new ModOption("showRightCPS", false, new InGuiSettings("Show Right CPS"))
				);
	}
	
	@Override
	public int getWidth() {
		return castOptionValueIntoBoolean("drawBackground") ? 54 : font.getStringWidth(Brackets.fromIndex(castOptionValueIntoInt("brackets")).wrap("0" + (castOptionValueIntoBoolean("showRightCPS") ? " ⎟ 0" : "") + " CPS"));
	}

	@Override
	public int getHeight() {
		return castOptionValueIntoBoolean("drawBackground") ? 14 : font.FONT_HEIGHT;
	}

	@Override
	public void render(ScreenPosition pos) {
		boolean leftPressed = Mouse.isButtonDown(0);
		boolean rightPressed = Mouse.isButtonDown(1);
		
		if (leftPressed != wasLeftPressed) {
			lastLeftPressed = System.currentTimeMillis();
			wasLeftPressed = leftPressed;
			
			if (leftPressed) {
				leftClicks.add(lastLeftPressed);
			}
		}
		
		if (rightPressed != wasRightPressed) {
			lastRightPressed = System.currentTimeMillis();
			wasRightPressed = rightPressed;
			
			if (rightPressed) {
				rightClicks.add(lastRightPressed);
			}
		}
		
		String text = String.valueOf(getCPS(leftClicks));
		
		if (castOptionValueIntoBoolean("showRightCPS")) {
			text += " ⎟ " + getCPS(rightClicks);
		}
		
		text += " CPS";
		
		text = Brackets.fromIndex(castOptionValueIntoInt("brackets")).wrap(text);
		
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
		String text = "0";
		
		if (castOptionValueIntoBoolean("showRightCPS")) {
			text += " ⎟ 0";
		}
		
		text += " CPS";
		
		text = Brackets.fromIndex(castOptionValueIntoInt("brackets")).wrap(text);
		
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
	
	private int getCPS(List<Long> clicks) {
		long time = System.currentTimeMillis();
		
		clicks.removeIf((aLong) -> aLong + 1000 < time);
		
		return clicks.size();
	}
}