package pixel.mod.impl;

import pixel.event.EventTarget;
import pixel.event.impl.KeyEvent;
import pixel.mod.Mod;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;

public class Extra extends Mod {
	public Extra() {
		super(true);
		
		loadOptions(
				new ModOption("fullbright", false, new InGuiSettings("Fullbright")),
				new ModOption("minimalViewBobbing", false, new InGuiSettings("Minimal View Bobbing")),
				new ModOption("hotbarScrolling", true, new InGuiSettings("Hotbar Scrolling")),
				new ModOption("showCrosshairInF3", true, new InGuiSettings("Show Crosshair In F3")),
				new ModOption("achievementNotifications", true, new InGuiSettings("Achievement Notifications")),
				new ModOption("leftHand", false, new InGuiSettings("Left Hand")),
				new ModOption("fireLayer", true, new InGuiSettings("Fire Layer"))
				);
	}
	
	public float getGamma() {
		return castOptionValueIntoBoolean("fullbright") ? 10.0F : mc.gameSettings.gammaSetting;
	}
	
	@EventTarget
	public void onKey(KeyEvent e) {
		if (mc.gameSettings.keyBindFullbright.isPressed()) {
			getOption("fullbright").setValue(!castOptionValueIntoBoolean("fullbright"));
			getOption("fullbright").saveIn(this);
		}
	}
}
