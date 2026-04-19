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
				new ModOption("hotbarScrolling", true, new InGuiSettings("Hotbar Scrolling"))
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
