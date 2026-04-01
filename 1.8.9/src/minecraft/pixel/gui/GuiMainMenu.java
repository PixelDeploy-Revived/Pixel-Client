package pixel.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import pixel.Pixel;
import pixel.util.ColorManager;

public class GuiMainMenu extends GuiScreen {
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		mc.getTextureManager().bindTexture(new ResourceLocation("pixel/gui/background.png"));
		drawModalRectWithCustomSizedTexture(0, 0, 0.0F, 0.0F, width, height, (float) width, (float) height);
		
		drawRect(width - 120, 0, width, height, ColorManager.BLACK_5A.getARGB());
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableTexture2D();
		GlStateManager.enableBlend();
		mc.getTextureManager().bindTexture(new ResourceLocation("pixel/icons/icon_256x256.png"));
		drawModalRectWithCustomSizedTexture(width - 120 - 64 + 120 / 2 + 64 / 2, 20, 0.0F, 0.0F, 64, 64, 64.0F, 64.0F);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		Gui.drawText(fontRendererObj, Pixel.getDisplayName(), 2, height - fontRendererObj.FONT_HEIGHT - 2, ColorManager.WHITE.getARGB(), true, false);
		Gui.drawText(fontRendererObj, "Born Again!", 2, height - fontRendererObj.FONT_HEIGHT - 2 - fontRendererObj.FONT_HEIGHT - 2, ColorManager.WHITE.getARGB(), true, false);
	}
	
	@Override
	public void initGui() {
		buttonList.add(new GuiButton(0, width - 120 - 100 + 120 / 2 + 100 / 2, height / 2 - 4 / 2 - 20 - 4, 100, 20, I18n.format("menu.singleplayer")));
		buttonList.add(new GuiButton(1, width - 120 - 100 + 120 / 2 + 100 / 2, height / 2 - 4 / 2, 100, 20, I18n.format("menu.multiplayer")));
		buttonList.add(new GuiButton(2, width - 120 - 100 + 120 / 2 + 100 / 2, height / 2 + 4 / 2 + 20, 100, 20, I18n.format("menu.options")));
		buttonList.add(new GuiButton(3, width - 120 - 100 + 120 / 2 + 100 / 2, height / 2 + 4 / 2 + 20 + 4 + 20, 100, 20, I18n.format("menu.quit")));
		
		Pixel.getInstance().getDiscord().update("In Main Menu", "Idle");
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case 0:
			mc.displayGuiScreen(new GuiSelectWorld(this));
			break;
		case 1:
			mc.displayGuiScreen(new GuiMultiplayer(this));
			break;
		case 2:
			mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
			break;
		case 3:
			mc.shutdown();
		}
	}
}
