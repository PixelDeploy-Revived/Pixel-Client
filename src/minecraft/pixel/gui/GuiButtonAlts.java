package pixel.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonAlts extends GuiButton {
	private final ResourceLocation altsIconTexture = new ResourceLocation("pixel/gui/alts.png");
	
	public GuiButtonAlts(int buttonId, int x, int y) {
		super(buttonId, x, y, 20, 20, "");
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (visible) {
			super.drawButton(mc, mouseX, mouseY);
			
			int iconX = xPosition + (width - 14) / 2;
			int iconY = yPosition + (height - 14) / 2;
			
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			
			mc.getTextureManager().bindTexture(altsIconTexture);
			drawModalRectWithCustomSizedTexture(iconX, iconY, 0.0F, 0.0F, 14, 14, 14.0F, 14.0F);
		}
	}
}
