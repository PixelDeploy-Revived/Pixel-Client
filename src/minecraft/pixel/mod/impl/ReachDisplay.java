package pixel.mod.impl;

import pixel.event.EventTarget;
import pixel.event.impl.EntityDamageEvent;
import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDisplayBase;

public class ReachDisplay extends ModDisplayBase {
	public ReachDisplay() {
		super("0,00 blocks");
	}
	
	private float range = 0.0F;
	private long lastHit;
	
	@Override
	public void render(ScreenPosition pos) {
		if (System.currentTimeMillis() - lastHit >= 3000) {
			range = 0.0F;
		}
		
		draw(pos, String.format("%.2f", range) + " blocks");
	}
	
	@EventTarget
	public void onEntityDamage(EntityDamageEvent event) {
		range = mc.thePlayer.getDistanceToEntity(event.getEntity());
		lastHit = System.currentTimeMillis();
	}
}
