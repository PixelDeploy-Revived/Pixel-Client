package pixel.mod.impl;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDisplayBase;
import pixel.mod.option.Brackets;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;

public class CPS extends ModDisplayBase {
	public CPS() {
		super(true, "0 CPS", new ModOption("showRightCPS", false, new InGuiSettings("Show Right CPS")));
	}
	
	private List<Long> leftClicks = new ArrayList<>();
	private boolean wasLeftPressed;
	private long lastLeftPressed;
	
	private List<Long> rightClicks = new ArrayList<>();
	private boolean wasRightPressed;
	private long lastRightPressed;
	
	@Override
	public int getWidth() {
		if (castOptionValueIntoBoolean("showRightCPS")) {
			dummyText = "0 ⎟ 0 CPS";
		} else if (!dummyText.equals("0 CPS")) {
			dummyText = "0 CPS";
		}
		
		return castOptionValueIntoBoolean("drawBackground") ? 54 : font.getStringWidth(Brackets.fromIndex(castOptionValueIntoInt("brackets")).wrap(dummyText));
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
		
		draw(pos, text);
	}
	
	@Override
	public void renderDummy(ScreenPosition pos) {
		draw(pos, dummyText);
	}
	
	private int getCPS(List<Long> clicks) {
		long time = System.currentTimeMillis();
		
		clicks.removeIf((aLong) -> aLong + 1000 < time);
		
		return clicks.size();
	}
}