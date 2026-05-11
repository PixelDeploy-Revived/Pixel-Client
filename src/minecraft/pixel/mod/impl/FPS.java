package pixel.mod.impl;

import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDisplayBase;

public class FPS extends ModDisplayBase {
	public FPS() {
		super("60 FPS");
	}

	@Override
	public void render(ScreenPosition pos) {
		draw(pos, mc.getDebugFPS() + " FPS");
	}
}
