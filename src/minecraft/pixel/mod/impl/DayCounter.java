package pixel.mod.impl;

import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDisplayBase;

public class DayCounter extends ModDisplayBase {
	public DayCounter() {
		super("365 days");
	}
	
	@Override
	public void render(ScreenPosition pos) {
		draw(pos, Long.valueOf(this.mc.theWorld.getWorldTime() / 24000L) + " days");
	}
}
