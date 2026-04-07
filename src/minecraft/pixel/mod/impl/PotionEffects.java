package pixel.mod.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDraggable;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;
import pixel.mod.option.ModOptionParent;
import pixel.mod.option.type.ModOptionColor;
import pixel.mod.option.type.ModOptionFloat;
import pixel.mod.option.type.ModOptionInt;
import pixel.util.ColorManager;

public class PotionEffects extends ModDraggable {
	private List<PotionEffect> dummyPotionEffects = Arrays.asList(new PotionEffect(Potion.moveSpeed.getId(), 20 * 60, 3), new PotionEffect(Potion.damageBoost.getId(), 20, 3));
	
	public PotionEffects() {
		super(true, 0, 0);
		
		loadOptions(
				new ModOption("durationTextShadow", true, new InGuiSettings("Duration Text Shadow")),
				new ModOptionColor("durationTextColor", ColorManager.WHITE.getARGB(), false, new ModOptionColor.InGuiSettings("Duration Text Color", false, true)),
				new ModOption("showName", true, new InGuiSettings("Show Name")),
				new ModOption(new ModOptionParent("showName"), "nameTextShadow", true, new InGuiSettings("Name Text Shadow")),
				new ModOptionColor(new ModOptionParent("showName"), "nameTextColor", ColorManager.WHITE.getARGB(), false, new ModOptionColor.InGuiSettings("Name Text Color", false, true)),
				new ModOption("showIcon", true, new InGuiSettings("Show Icon")),
				new ModOption("blink", true, new InGuiSettings("Blink")),
				new ModOptionInt(new ModOptionParent("blink"), "blinkStart", 10, 5, 30, new InGuiSettings("Start blinking")),
				new ModOptionFloat(new ModOptionParent("blink"), "blinkDuration", 0.5F, 0.5F, 2.0F, new ModOptionFloat.InGuiSettings("Blink duration", 1)),
				new ModOption("reverse", false, new InGuiSettings(false))
				);
	}
		
	@Override
	public int getWidth() {
		int gap = 2;
		int width = 0;
		
		if (castOptionValueIntoBoolean("showIcon")) {
			width += 20;
		}
		
		PotionEffect moveSpeed = dummyPotionEffects.get(0);
		PotionEffect damageBoost = dummyPotionEffects.get(1);
		
		String longestPotionEffectName = PotionEffects.getPotionName(damageBoost);
		
		if (PotionEffects.getPotionName(moveSpeed).length() > longestPotionEffectName.length()) {
			longestPotionEffectName = PotionEffects.getPotionName(moveSpeed);
		}
		
		if (castOptionValueIntoBoolean("showName")) {
			gap += 2;
			width += font.getStringWidth(longestPotionEffectName);
		} else {
			gap += 2;
			width += font.getStringWidth(Potion.getDurationString(moveSpeed));
		}
		
		return width + gap;
	}

	@Override
	public int getHeight() {
		return 20 * dummyPotionEffects.size();
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
		
		List<PotionEffect> activePotionEffects = new ArrayList<>(mc.thePlayer.getActivePotionEffects());
		
		for (int i = 0; i < activePotionEffects.size(); i++) {
			drawPotionEffect(pos, i, activePotionEffects.get(i));
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
		
		for (int i = 0; i < dummyPotionEffects.size(); i++) {
			drawPotionEffect(pos, i, dummyPotionEffects.get(i));
		}
	}
	
	private static String getPotionName(PotionEffect potionEffect) {
		String potionName = I18n.format(potionEffect.getEffectName());
		
		if (potionEffect.getAmplifier() < 4) {
			potionName = potionName + " " + I18n.format("enchantment.level." + String.valueOf(potionEffect.getAmplifier() + 1));
		}
		
		return potionName;
	}
	
	private void drawPotionEffect(ScreenPosition pos, int i, PotionEffect potionEffect) {
		if (potionEffect != null) {
			int offsetY = i * 20;
			
			if (castOptionValueIntoBoolean("showIcon")) {
				Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
				int iconX = castOptionValueIntoBoolean("reverse") ? pos.getAbsoluteX() + getWidth() - 20 : pos.getAbsoluteX();
				
				if (potion.hasStatusIcon()) {
					 GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					 
					 mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
					 
					 int iconIndex = potion.getStatusIconIndex();
					 
					 drawTexturedModalRect(iconX, pos.getAbsoluteY() + offsetY + 2, iconIndex % 8 * 18, 198 + iconIndex / 8 * 18, 18, 18);
				}
			}
			
			int j = castOptionValueIntoBoolean("showIcon") ? 20 : 0;
			
			if (castOptionValueIntoBoolean("showName")) {
				 String potionName = getPotionName(potionEffect);
				 
				 int nameX = castOptionValueIntoBoolean("reverse") ? pos.getAbsoluteX() + getWidth() - font.getStringWidth(potionName) - j - 2 : pos.getAbsoluteX() + j + 2;
				 
				 drawText(potionName, nameX, pos.getAbsoluteY() + offsetY + 2, getOptionColor("nameTextColor").getARGB(), castOptionValueIntoBoolean("nameTextShadow"), getOptionColor("nameTextColor").isChromaEnabled());
			}
			
			String durationString = Potion.getDurationString(potionEffect);
			int durationX = castOptionValueIntoBoolean("reverse") ? pos.getAbsoluteX() + getWidth() - font.getStringWidth(durationString) - j - 2: pos.getAbsoluteX() + j + 2;
			int durationY = pos.getAbsoluteY() + offsetY + font.FONT_HEIGHT + (castOptionValueIntoBoolean("showName") ? 2 : -2);
			
			if (castOptionValueIntoBoolean("blink")) {
				if (potionEffect.getDuration() / 20 < castOptionValueIntoInt("blinkStart")) {
					if ((System.currentTimeMillis() / (int) (castOptionValueIntoFloat("blinkDuration") * 1000)) % 2 == 0) {
						drawText(durationString, durationX, durationY, getOptionColor("durationTextColor").getARGB(), castOptionValueIntoBoolean("durationTextShadow"), getOptionColor("durationTextColor").isChromaEnabled());
					}
				} else {
					drawText(durationString, durationX, durationY, getOptionColor("durationTextColor").getARGB(), castOptionValueIntoBoolean("durationTextShadow"), getOptionColor("durationTextColor").isChromaEnabled());
				}
			} else {
				drawText(durationString, durationX, durationY, getOptionColor("durationTextColor").getARGB(), castOptionValueIntoBoolean("durationTextShadow"), getOptionColor("durationTextColor").isChromaEnabled());
			}
		}
	}
}