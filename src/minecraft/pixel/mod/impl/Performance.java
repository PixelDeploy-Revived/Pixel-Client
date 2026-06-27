package pixel.mod.impl;

import pixel.mod.Mod;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;

public class Performance extends Mod {
	public Performance() {
		super(true);

		loadOptions(
				new ModOption("disableSystemGC", true, new InGuiSettings("Disable System GC")),
				new ModOption("fastWorldLoadingScreen", true, new InGuiSettings("Fast World Loading Screen"))
				);
	}
}
