package pixel.mod.impl;

import pixel.event.EventTarget;
import pixel.event.impl.EntityAttackEvent;
import pixel.event.impl.EntityDamageEvent;
import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDisplayBase;

public class ComboCounter extends ModDisplayBase {
	public ComboCounter() {
		super(false, "0 combos");
	}
	
	private int targetId;
	private int combo = 0;
	private long lastCombo;
	
	@Override
	public void render(ScreenPosition pos) {
		if (mc.thePlayer.hurtTime > 3 || System.currentTimeMillis() - lastCombo >= 5000) {
			combo = 0;
		}
				
		draw(pos, combo + " combos");
	}
	
	@EventTarget
	public void onEntityAttack(EntityAttackEvent event) {
		targetId = event.getEntity().getEntityId();
	}
	
	@EventTarget
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity().getEntityId() == targetId) {
			targetId = -1;
			combo++;
			lastCombo = System.currentTimeMillis();
		} else if (event.getEntity() == mc.thePlayer) {
			combo = 0;
		}
	}
}
