package pixel.mod.impl;

import net.minecraft.item.ItemStack;
import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDisplayBase;

public class PotsCounter extends ModDisplayBase {
	public PotsCounter() {
		super("0 pots");
	}
	
	@Override
	public void render(ScreenPosition pos) {
		int potsCount = 0;
		
		for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
			ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
			
			if (itemStack != null && itemStack.getItem().getIdFromItem(itemStack.getItem()) == 373) {
				potsCount++;
			}
		}
				
		draw(pos, potsCount + " pots");
	}
}
