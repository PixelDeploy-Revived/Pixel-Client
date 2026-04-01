package pixel.mod.impl;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;
import pixel.mod.Mod;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;

public class OldVisuals extends Mod {
	private long sneak = 0L;
	private boolean isSneaking = false;
	private int value = 0;
	
	public OldVisuals() {
		super(true);
		
		loadOptions(
				new ModOption("fishingRod", true, new InGuiSettings("Fishing Rod")),
				new ModOption("bow", true, new InGuiSettings("Bow")),
				new ModOption("blockHitting", true, new InGuiSettings("Block Hitting")),
				new ModOption("sneaking", true, new InGuiSettings("Sneaking")),
				new ModOption("armorHitAnimation", true, new InGuiSettings("Armor Hit Animation"))
				);
	}
	
	public float getCustomEyeHeight(Entity entity) {
		if (!castOptionValueIntoBoolean("sneaking")) {
			return entity.getEyeHeight();
		}
		
		if (isSneaking != entity.isSneaking() || sneak <= 0L) {
			sneak = System.currentTimeMillis();
		}
		
		isSneaking = entity.isSneaking();
		float f = 1.62F;
		
		if (entity.isSneaking()) {
			int i = (int) (sneak + 8L - System.currentTimeMillis());
			
			if (i > -50) {
				f = (float) (f + i * 0.0017D);
				
				if (f < 0.0F || f > 10.0F) {
					f = 1.54F;
				}
			} else {
				f = (float) (f - 0.08D);
			}
		} else {
			int j = (int) (sneak + 8L - System.currentTimeMillis());
			
			if (j > -50) {
				f = (float) (f - j * 0.0017D);
				f = (float) (f - 0.08D);
				
				if (f < 0.0F) {
					f = 1.62F;
				}
			} else {
				f = f - 0.0F;
			}
		}
		
		return f;
	}
    
    public void attemptSwing() {
    	EntityPlayerSP player = mc.thePlayer;
    	
		if (player.getItemInUseCount() > 0) {
			boolean isMouseDown = mc.gameSettings.keyBindAttack.isKeyDown() && mc.gameSettings.keyBindUseItem.isKeyDown();
			
			if (isMouseDown && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				int swingAnimationEnd = player.isPotionActive(Potion.digSpeed) ? (6 - (1 + player.getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1) : (player.isPotionActive(Potion.digSlowdown) ? (6 + (1 + player.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2) : 6);
				
				if (!player.isSwingInProgress || player.swingProgressInt >= swingAnimationEnd / 2 || player.swingProgressInt < 0) {
					player.swingProgressInt = -1;
					player.isSwingInProgress = true;
				}
			}
		}
	}
}
