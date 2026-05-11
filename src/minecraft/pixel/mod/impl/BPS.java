package pixel.mod.impl;

import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDisplayBase;

public class BPS extends ModDisplayBase {
	public BPS() {
		super(false, "0,00 m/s");
	}
	
	private float blocks;
	
	@Override
	public void render(ScreenPosition pos) {
		float ticks = mc.timer.ticksPerSecond * mc.timer.timerSpeed;
		
        blocks = (float) (mc.thePlayer.getDistance(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ) * ticks);
        		
		draw(pos, String.format("%.2f", blocks) + " m/s");
	}
}
