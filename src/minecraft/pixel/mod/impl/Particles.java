package pixel.mod.impl;

import pixel.mod.Mod;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;
import pixel.mod.option.ModOptionParent;
import pixel.mod.option.type.ModOptionInt;

public class Particles extends Mod {
	public Particles() {
		super(false);
		
		loadOptions(
				new ModOption("affectCriticals", true, new InGuiSettings("Affect Criticals")),
				new ModOption("affectSharpness", true, new InGuiSettings("Affect Sharpness")),
				new ModOption(new ModOptionParent("affectCriticals"), "alwaysCriticals", false, new InGuiSettings("Always Criticals")),
				new ModOption(new ModOptionParent("affectSharpness"), "alwaysSharpness", false, new InGuiSettings("Always Sharpness")),
				new ModOptionInt("multiplierFactor", 1, 1, 10, new InGuiSettings("Multiplier Factor"))
				);
	}
}
