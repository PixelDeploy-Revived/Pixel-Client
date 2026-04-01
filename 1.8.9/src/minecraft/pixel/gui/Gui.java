package pixel.gui;

import java.awt.Color;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import pixel.util.ColorManager;

public class Gui {
	public static void drawRect(float left, float top, float right, float bottom, int color) {
		if (left < right) {
			float tmp = left;
			
			left = right;
			right = tmp;
		}
		
		if (top < bottom) {
			float tmp = top;
			
			top = bottom;
			bottom = tmp;
		}
		
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		ColorManager c = new ColorManager(color);
		
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(c.getRed() / 255.0F, c.getGreen() / 255.0F, c.getBlue() / 255.0F, c.getAlpha() / 255.0F);
		
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos((double) left, (double) bottom, 0.0D).endVertex();
		worldRenderer.pos((double) right, (double) bottom, 0.0D).endVertex();
		worldRenderer.pos((double) right, (double) top, 0.0D).endVertex();
		worldRenderer.pos((double) left, (double) top, 0.0D).endVertex();
		
		tessellator.draw();
		
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
	
	public static void drawHorizontalLine(float startX, float endX, float y, float thickness, int color) {
		if (endX < startX) {
			float tmp = startX;
			
			startX = endX;
			endX = tmp;
		}
		
		drawRect(startX, y, endX + thickness, y + thickness, color);
	}
	
	public static void drawVerticalLine(float x, float startY, float endY, float thickness, int color) {
		if (endY < startY) {
			float tmp = startY;
			
			startY = endY;
			endY = tmp;
		}
		
		drawRect(x, startY + thickness, x + thickness, endY, color);
	}
	
	public static void drawHollowRect(float x, float y, float width, float height, float thickness, int color) {
		drawHorizontalLine(x, x + width, y, thickness, color);
		drawHorizontalLine(x, x + width, y + height, thickness, color);
		drawVerticalLine(x, y + height, y, thickness, color);
		drawVerticalLine(x + width, y + height, y, thickness, color);
	}
	
	public static void drawText(FontRenderer font, String text, float x, float y, int color, boolean dropShadow, boolean chroma) {
		if (chroma) {
			float textCharX = x;
			
			for (char textChar : text.toCharArray()) {
				long t = System.currentTimeMillis() - (long) (textCharX * 10 - y * 10);
				int c = Color.HSBtoRGB(t % 2000 / 2000.0F, 0.8F, 0.8F);
				
				if (text.startsWith("§")) {
					drawText(font, text, x, y, c, dropShadow, false);
				} else {
					drawText(font, String.valueOf(textChar), textCharX, y, c, dropShadow, false);
				}
				
				textCharX += font.getCharWidth(textChar);
			}
		} else {
			font.drawString(text, x, y, color, dropShadow);
		}
	}
	
	public static void drawText(FontRenderer font, double scale, String text, float x, float y, int color, boolean dropShadow, boolean chroma) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0);
		GlStateManager.scale(scale, scale, 1);
		GlStateManager.translate(-x, -y, 0);
		
		drawText(font, text, x, y, color, dropShadow, chroma);
		
		GlStateManager.popMatrix();
	}
}
