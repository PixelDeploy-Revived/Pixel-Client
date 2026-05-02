package pixel.event.impl;

import pixel.event.Event;
import net.minecraft.entity.Entity;

public class EntityDamageEvent extends Event {
	private Entity entity;
	
	public EntityDamageEvent(Entity entity) {
		this.entity = entity;
	}
	
	public Entity getEntity() {
		return entity;
	}
}