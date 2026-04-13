package pixel.mod.impl;

import pixel.mod.Mod;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;
import pixel.mod.option.ModOptionParent;
import pixel.mod.option.type.ModOptionColor;
import pixel.util.ColorManager;

public class Menu extends Mod {
	public Menu() {
		super(false);
		
		loadOptions(
				new ModOptionColor("primaryColor", ColorManager.BLACK_C0.getARGB(), false, new ModOptionColor.InGuiSettings("Primary Color", true, false)),
				new ModOption("gradient", true, new InGuiSettings("Gradient")),
				new ModOptionColor(new ModOptionParent("gradient"), "secondaryColor", ColorManager.BLACK_D0.getARGB(), false, new ModOptionColor.InGuiSettings("Secondary Color", true, false))
				);
	}
}
