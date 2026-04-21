package pixel.mod.impl;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.boss.BossStatus;
import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDraggable;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;
import pixel.mod.option.ModOptionParent;
import pixel.mod.option.type.ModOptionColor;
import pixel.util.ColorManager;

public class Bossbar extends ModDraggable {
	public Bossbar() {
		super(false, 0, 0);
		
		loadOptions(
				new ModOption("hide", false, new InGuiSettings("Hide")),
				new ModOption(new ModOptionParent("hide", false), "showName", true, new InGuiSettings("Show Name")),
				new ModOptionColor(new ModOptionParent("showName"), "textColor", ColorManager.WHITE.getARGB(), false, new ModOptionColor.InGuiSettings("Text Color", false, true)),
				new ModOption(new ModOptionParent("showName"), "textShadow", true, new InGuiSettings("Text Shadow")),
				new ModOption(new ModOptionParent("hide", false), "showHealth", true, new InGuiSettings("Show Health"))
				);
	}
	
	@Override
	public int getWidth() {
		int width = 0;
		
		if (!castOptionValueIntoBoolean("hide")) {
			if (castOptionValueIntoBoolean("showName")) {
				width = font.getStringWidth(BossStatus.bossName != null && BossStatus.statusBarTime > 0 ? BossStatus.bossName : "Ender Dragon");
			}
			
			if (castOptionValueIntoBoolean("showHealth")) {
				width = 182;
			}
		}
		
		return width;
	}
	
	@Override
	public int getHeight() {
		int height = 0;
		
		if (!castOptionValueIntoBoolean("hide")) {
			if (castOptionValueIntoBoolean("showName")) {
				height += font.FONT_HEIGHT;
			}
			
			if (castOptionValueIntoBoolean("showHealth")) {
				if (castOptionValueIntoBoolean("showName")) {
					height++;
				}
				
				height += 5;
			}
		}
		
		return height;
	}
	
	@Override
	public void render(ScreenPosition pos) {
		if (!castOptionValueIntoBoolean("hide") && BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
			BossStatus.statusBarTime--;
			
			this.renderBossbar(pos, BossStatus.healthScale, BossStatus.bossName);
		}
	}
	
	@Override
	public void renderDummy(ScreenPosition pos) {
		if (!castOptionValueIntoBoolean("hide")) {
			float bossHealthScale = 1.0F;
			String bossName = "Ender Dragon";
			
			if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
				bossHealthScale = BossStatus.healthScale;
				bossName = BossStatus.bossName;
			}
			
			this.renderBossbar(pos, bossHealthScale, bossName);
		}
	}
	
	private void renderBossbar(ScreenPosition pos, float bossHealthScale, String bossName) {
		if (castOptionValueIntoBoolean("showHealth")) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			
			mc.getTextureManager().bindTexture(Gui.icons);
			
			int healthY = pos.getAbsoluteY() + (castOptionValueIntoBoolean("showName") ? font.FONT_HEIGHT + 1 : 0);

            drawTexturedModalRect(pos.getAbsoluteX(), healthY, 0, 74, 182, 5);
            
            int healthWidth = (int) (bossHealthScale * (182.0F + 1.0F));
            
            if (healthWidth > 0) {
            	drawTexturedModalRect(pos.getAbsoluteX(), healthY, 0, 79, healthWidth, 5);
            }
		}
		
		if (castOptionValueIntoBoolean("showName")) {
			drawText(bossName.replace("\u00A7r", ""), pos.getAbsoluteX() + getWidth() / 2 - font.getStringWidth(bossName) / 2, pos.getAbsoluteY(), getOptionColor("textColor").getARGB(), castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
		}
	}
}
