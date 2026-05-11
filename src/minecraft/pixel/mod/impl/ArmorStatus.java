package pixel.mod.impl;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDraggable;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;
import pixel.mod.option.ModOptionParent;
import pixel.mod.option.type.ModOptionColor;
import pixel.util.ColorManager;

public class ArmorStatus extends ModDraggable {
	public ArmorStatus() {
		super(true);
		
		loadOptions(
				new ModOption("textShadow", true, new InGuiSettings("Text Shadow")),
				new ModOptionColor("textColor", ColorManager.WHITE.getARGB(), false, new ModOptionColor.InGuiSettings("Text Color", false, true)),
				new ModOption("dynamicColors", false, new InGuiSettings("Dynamic Colors")),
				new ModOption("showPercentage", false, new InGuiSettings("Show Percentage")),
				new ModOption("showDamage", true, new InGuiSettings("Show Damage")),
				new ModOption(new ModOptionParent("showDamage"), "showMaxDamage", false, new InGuiSettings("Show Max Damage")),
				new ModOption("armor", true, new InGuiSettings("Armor")),
				new ModOption("equippedItem", false, new InGuiSettings("Equipped Item")),
				new ModOption("damageOverlays", true, new InGuiSettings("Damage Overlays")),
				new ModOption("reverse", false, new InGuiSettings(false))
				);
	}

	@Override
	public int getWidth() {
		int gap = 0;
		int width = 0;
		
		if (castOptionValueIntoBoolean("showPercentage")) {
			gap = 2;
			width = font.getStringWidth("100%");
		} else if (castOptionValueIntoBoolean("showDamage")) {
			gap = 2;
			
			int damage = castOptionValueIntoBoolean("equippedItem") ? Items.diamond_sword.getMaxDamage() : Items.diamond_chestplate.getMaxDamage();
			
			width = font.getStringWidth(String.valueOf(damage));
			
			if (castOptionValueIntoBoolean("showMaxDamage")) {
				width += font.getStringWidth("/" + damage);
			}
		}
		
		return 16 + gap + width;
	}

	@Override
	public int getHeight() {
		int slots = 0;
		
		if (castOptionValueIntoBoolean("armor")) {
			slots += 4;
		}
		
		if (castOptionValueIntoBoolean("equippedItem")) {
			slots += 1;
		}
		
		return 16 * slots;
	}

	@Override
	public void render(ScreenPosition pos) {
		if (pos.getRelativeX() < 1.0D / 3.0D && castOptionValueIntoBoolean("reverse")) {
			getOption("reverse").setValue(false);
			getOption("reverse").saveIn(this);
		} else if (pos.getRelativeX() > 2.0D / 3.0D && !castOptionValueIntoBoolean("reverse")) {
			getOption("reverse").setValue(true);
			getOption("reverse").saveIn(this);
		}
		
		int i = 0;
		
		if (castOptionValueIntoBoolean("equippedItem") && mc.thePlayer.inventory.getCurrentItem() != null) {
			drawItemStack(pos, i, mc.thePlayer.inventory.getCurrentItem());
			
			i++;
		}
		
		if (castOptionValueIntoBoolean("armor")) {
			for (ItemStack itemStack : mc.thePlayer.inventory.armorInventory) {
				if (itemStack != null) {
					drawItemStack(pos, i, itemStack);
					
					i++;
				}
			}
		}
	}
	
	@Override
	public void renderDummy(ScreenPosition pos) {
		if (pos.getRelativeX() < 1.0D / 3.0D && castOptionValueIntoBoolean("reverse")) {
			getOption("reverse").setValue(false);
			getOption("reverse").saveIn(this);
		} else if (pos.getRelativeX() > 2.0D / 3.0D && !castOptionValueIntoBoolean("reverse")) {
			getOption("reverse").setValue(true);
			getOption("reverse").saveIn(this);
		}
		
		int i = 0;
		
		if (castOptionValueIntoBoolean("equippedItem")) {
			drawItemStack(pos, i++, new ItemStack(Items.diamond_sword));
		}
		
		if (castOptionValueIntoBoolean("armor")) {
			drawItemStack(pos, i++, new ItemStack(Items.diamond_boots));
			drawItemStack(pos, i++, new ItemStack(Items.diamond_leggings));
			drawItemStack(pos, i++, new ItemStack(Items.diamond_chestplate));
			drawItemStack(pos, i, new ItemStack(Items.diamond_helmet));
		}
	}
	
	private void drawItemStack(ScreenPosition pos, int i, ItemStack itemStack) {
		GlStateManager.pushMatrix();
		
		RenderHelper.enableGUIStandardItemLighting();
		
		int itemX = castOptionValueIntoBoolean("reverse") ? pos.getAbsoluteX() + getWidth() - 16 : pos.getAbsoluteX();
		int offsetY = (-16 * i) + getHeight() - 16;
		
		mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, itemX, pos.getAbsoluteY() + offsetY);
		
		if (itemStack.isStackable()) {
			mc.getRenderItem().renderItemOverlays(font, itemStack, itemX, pos.getAbsoluteY() + offsetY);
		}
		
		if (itemStack.getItem().isDamageable() && (castOptionValueIntoBoolean("showDamage") || castOptionValueIntoBoolean("showPercentage"))) {
			if (castOptionValueIntoBoolean("damageOverlays")) {
				mc.getRenderItem().renderItemOverlays(font, itemStack, itemX, pos.getAbsoluteY() + offsetY);
			}
			
			String text = "";
			int damage = itemStack.getItemDamage();
			int maxDamage = itemStack.getMaxDamage();
			double damagePercentage = ((maxDamage - damage) / (double) maxDamage) * 100.0D;
			
			if (castOptionValueIntoBoolean("showPercentage")) {				
				text = String.valueOf(((maxDamage - damage) / maxDamage) * 100) + "%";
			} else if (castOptionValueIntoBoolean("showDamage")) {
				text = String.valueOf(maxDamage - damage);
				
				if (castOptionValueIntoBoolean("showMaxDamage")) {
					text += "/" + String.valueOf(maxDamage);
				}
			}
			
			int damageX = castOptionValueIntoBoolean("reverse") ? pos.getAbsoluteX() + getWidth() - font.getStringWidth(text) - 16 - 2 : pos.getAbsoluteX() + 16 + 2;
			int textColor = getOptionColor("textColor").getARGB();
			
			if (castOptionValueIntoBoolean("dynamicColors")) {				
				if (damagePercentage <= 10) {
			        textColor = ColorManager.DEFAULT_DARK_RED.getARGB();
			    } else if (damagePercentage <= 25) {
			    	textColor = ColorManager.DEFAULT_RED.getARGB();
			    } else if (damagePercentage <= 40) {
			    	textColor = ColorManager.DEFAULT_GOLD.getARGB();
			    } else if (damagePercentage <= 60) {
			    	textColor = ColorManager.DEFAULT_YELLOW.getARGB();
			    } else if (damagePercentage <= 80) {
			    	textColor = ColorManager.DEFAULT_GREEN.getARGB();
			    }
			}
			
			drawText(text, damageX, pos.getAbsoluteY() + offsetY + 5, textColor, castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
		}
		
		GlStateManager.popMatrix();
	}
}