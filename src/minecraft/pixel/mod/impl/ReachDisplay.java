package pixel.mod.impl;

import pixel.event.EventTarget;
import pixel.event.impl.EntityAttackEvent;
import pixel.event.impl.EntityDamageEvent;
import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDisplayBase;

public class ReachDisplay extends ModDisplayBase {
	public ReachDisplay() {
		super(false, "0,00 blocks");
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
	public void onEntityAttack(EntityAttackEvent event) {
		range = mc.thePlayer.getDistanceToEntity(event.getEntity());
		lastHit = System.currentTimeMillis();
	}
}
