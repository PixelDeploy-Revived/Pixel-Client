package pixel.mod.impl;

import pixel.mod.Mod;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;
import pixel.mod.option.ModOptionParent;
import pixel.mod.option.type.ModOptionFloat;

public class HurtCam extends Mod {
	public HurtCam() {
		super(false);
		
		loadOptions(
				new ModOption("hurtShake", true, new InGuiSettings("Hurt Shake")),
				new ModOptionFloat(new ModOptionParent("hurtShake"), "hurtShakeIntensity", 14.0F, 5.0F, 35.0F, new ModOptionFloat.InGuiSettings("Hurt Shake Intensity", 1))
				);
	}
}
