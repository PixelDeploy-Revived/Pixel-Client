package pixel.mod.impl;

import pixel.mod.Mod;
import pixel.mod.option.type.ModOptionColor;
import pixel.util.ColorManager;

public class HitColor extends Mod {
	public HitColor() {
		super(false);
				
		loadOptions(
				new ModOptionColor("hitColor", ColorManager.RED_4C.getARGB(), false, new ModOptionColor.InGuiSettings("Hit Color", true, true))
				);
	}
}