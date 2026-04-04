package pixel.mod;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import pixel.gui.Gui;
import pixel.gui.hud.IRenderer;
import pixel.gui.hud.ScreenPosition;

public abstract class ModDraggable extends Mod implements IRenderer {
	protected ScreenPosition pos;
	
	public ModDraggable(boolean enabled, double x, double y) {
		super(enabled);
		
		setPosition(ScreenPosition.fromRelativePosition((double) file.safeGet("x", x), (double) file.safeGet("y", y)));
	}
	
	public void setPosition(ScreenPosition pos) {
		this.pos = pos;
		
		file.set("x", pos.getRelativeX());
		file.set("y", pos.getRelativeY());
	}
	
	public ScreenPosition getPosition() {
		return pos;
	}
	
	public void drawText(String text, float x, float y, int color, boolean dropShadow, boolean chroma) {
		Gui.drawText(font, text, x, y, color, dropShadow, chroma);
	}
	
	public void drawTextAligned(String text, float x, float y, int color, boolean dropShadow, boolean chroma) {
		float alignedX;
		
		if (pos.getRelativeX() < 1.0D / 3.0D) {
			alignedX = x;
		} else if (pos.getRelativeX() > 2.0D / 3.0D) {
			alignedX = x + getWidth() - font.getStringWidth(text);
		} else {
			alignedX = x + (getWidth() - font.getStringWidth(text)) / 2.0F;
		}
		
		drawText(text, alignedX, y, color, dropShadow, chroma);
	}
	
	public void drawTextCentered(String text, float x, float y, int color, boolean dropShadow, boolean chroma) {
		drawText(text, x + (getWidth() - font.getStringWidth(text) + 1) / 2.0F, y + (getHeight() - font.FONT_HEIGHT + 1) / 2.0F, color, dropShadow, chroma);
	}
	
	public void drawTexturedModalRect(float x, float y, float textureX, float textureY, int width, int height) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
        
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldRenderer.pos((double) (x), (double) (y + height), 0.0D).tex((double) (textureX * f), (double) ((textureY + height) * f1)).endVertex();
		worldRenderer.pos((double) (x + width), (double) (y + height), 0.0D).tex((double) ((textureX + width) * f), (double) ((textureY + height) * f1)).endVertex();
		worldRenderer.pos((double) (x + width), (double) (y), 0.0D).tex((double) ((textureX + width) * f), (double) (textureY * f1)).endVertex();
		worldRenderer.pos((double) (x), (double) (y), 0.0D).tex((double) (textureX * f), (double) (textureY * f1)).endVertex();
		
		tessellator.draw();
	}
	
	public void drawTextCentered(String text, int gap, float x, float y, int color, boolean dropShadow, boolean chroma) {
		float textX;
		
		if (pos.getRelativeX() < 1.0D / 3.0D) {
			textX = x + gap / 2.0F;
		} else if (pos.getRelativeX() > 2.0D / 3.0D) {
			textX = x + getWidth() - font.getStringWidth(text) - gap / 2.0F;
		} else {
			textX = x + (getWidth() - font.getStringWidth(text)) / 2.0F;
		}
		
		drawText(text, textX, y + (getHeight() - font.FONT_HEIGHT + 1) / 2.0F, color, dropShadow, chroma);
	}
	
	public void drawRect(float x, float y, float width, float height, int color) {
		Gui.drawRect(x, y, x + width, y + height, color);
	}
	
	public void drawRect(ScreenPosition pos, float x, float y, float width, float height, int color, String text, int gap) {
		float rectLeft;
		float rectRight;
		
		if (pos.getRelativeX() < 1.0D / 3.0D) {
			rectLeft = x;
			rectRight = x + font.getStringWidth(text) + gap;
		} else if (pos.getRelativeX() > 2.0D / 3.0D) {
			rectLeft = x - font.getStringWidth(text) + width - gap;
			rectRight = x + width;
		} else {
			rectLeft = x - (font.getStringWidth(text) + gap) / 2.0F + width / 2.0F;
			rectRight = x + (font.getStringWidth(text) + gap) / 2.0F + width / 2.0F;
		}
		
		Gui.drawRect(rectLeft, y, rectRight, y + height, color);
	}
	
	public void drawBorder(float x, float y, float width, float height, int color, float thickness) {
		Gui.drawHollowRect(x, y, width - thickness, height - thickness, thickness, color);
	}
	
	public void drawBorder(ScreenPosition pos, float x, float y, float width, float height, int color, float thickness, String text, int gap) {
		float rectLeft;
		float rectRight;
		
		if (pos.getRelativeX() < 1.0D / 3.0D) {
			rectLeft = x;
			rectRight = rectLeft + font.getStringWidth(text) + gap;
		} else if (pos.getRelativeX() > 2.0D / 3.0D) {
			rectRight = x + width;
			rectLeft = rectRight - font.getStringWidth(text) - gap;
		} else {
			rectLeft = x - (font.getStringWidth(text) + gap) / 2.0F + width / 2.0F;
			rectRight = x + (font.getStringWidth(text) + gap) / 2.0F + width / 2.0F;
		}
		
		Gui.drawRect(rectLeft + thickness, y + thickness, rectRight - thickness, y, color);
		Gui.drawRect(rectLeft + thickness, y + height, rectRight - thickness, y + height - thickness, color);
		Gui.drawRect(rectLeft + thickness, y, rectLeft, y + height, color);
		Gui.drawRect(rectRight, y, rectRight - thickness, y + height, color);
	}
	
	public void drawScaledText(double scale, String text, float x, float y, int color, boolean textShadow, boolean chroma) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0.0F);
		GlStateManager.scale(scale, scale, 1.0D);
		GlStateManager.translate(-x, -y, 0.0F);
		
		drawText(text, x, y, color, textShadow, chroma);
		
		GlStateManager.popMatrix();
	}
}
