package pixel.mod.impl;

import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDisplayBase;

public class HealthIndicator extends ModDisplayBase {
	public HealthIndicator() {
		super(false, "10.0 hearts");
	}

	@Override
	public void render(ScreenPosition pos) {
		if (!mc.thePlayer.capabilities.isCreativeMode && !mc.thePlayer.isSpectator()) {
			draw(pos, mc.ingameGUI.playerHealth / 2.0F + " hearts");
		}
	}
}
