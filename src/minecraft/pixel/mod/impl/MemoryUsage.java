package pixel.mod.impl;

import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDisplayBase;

public class MemoryUsage extends ModDisplayBase {
	public MemoryUsage() {
		super(false, "Mem: 0%");
	}
	
	@Override
	public void render(ScreenPosition pos) {
		draw(pos, String.format("Mem: %1d%%", Long.valueOf((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) * 100L / Runtime.getRuntime().maxMemory())));
	}
}
