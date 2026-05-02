package pixel.event.impl;

import pixel.event.Event;
import net.minecraft.entity.Entity;

public class EntityAttackEvent extends Event {
	private Entity entity;
	
	public EntityAttackEvent(Entity entity) {
		this.entity = entity;
	}
	
	public Entity getEntity() {
		return entity;
	}
}