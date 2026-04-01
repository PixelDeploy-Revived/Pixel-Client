package pixel.mod.impl;

import pixel.mod.Mod;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;
import pixel.mod.option.type.ModOptionColor;
import pixel.util.ColorManager;

public class Nametags extends Mod {
	public Nametags() {
		super(false);
		
		loadOptions(
				new ModOption("showInThirdPerson", true, new InGuiSettings("Show In Third Person")),
				new ModOption("textShadow", false, new InGuiSettings("Text Shadow")),
				new ModOptionColor("backgroundColor", ColorManager.BLACK_40.getARGB(), false, new ModOptionColor.InGuiSettings("Background Color", true, false))
				);
	}
}