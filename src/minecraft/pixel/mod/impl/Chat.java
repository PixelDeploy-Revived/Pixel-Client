package pixel.mod.impl;

import pixel.mod.Mod;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;
import pixel.mod.option.type.ModOptionColor;
import pixel.util.ColorManager;

public class Chat extends Mod {
	public Chat() {
		super(true);
		
		loadOptions(
				new ModOption("chatHeightFix", true, new InGuiSettings("Chat Height Fix")),
				new ModOption("textShadow", true, new InGuiSettings("Text Shadow")),
				new ModOptionColor("backgroundColor", ColorManager.BLACK_7F.getARGB(), false, new ModOptionColor.InGuiSettings("Background Color", true, false))
				);
	}
}
