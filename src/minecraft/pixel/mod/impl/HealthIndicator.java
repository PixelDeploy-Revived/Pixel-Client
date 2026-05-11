package pixel.mod.impl;

import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDisplayBase;

public class HealthIndicator extends ModDisplayBase {
	public HealthIndicator() {
		super("10 hearts");
	}

	@Override
	public void render(ScreenPosition pos) {
		if (!mc.thePlayer.capabilities.isCreativeMode && !mc.thePlayer.isSpectator()) {
			draw(pos, mc.thePlayer.getHealth() / 2 + " hearts");
		}
	}
}
