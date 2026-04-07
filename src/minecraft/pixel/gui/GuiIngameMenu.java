package pixel.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiIngameMenu extends net.minecraft.client.gui.GuiIngameMenu {
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		mc.getTextureManager().bindTexture(new ResourceLocation("pixel/icons/icon_256x256.png"));
		drawModalRectWithCustomSizedTexture((width - 32) / 2, height / 4 - 32, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		buttonList.add(new GuiButton(8, width / 2 - 100, height / 4 + 72 - 16, 200, 20, I18n.format("menu.multiplayer")));
		buttonList.add(new GuiButton(9, 5, 5, 80, 20, "Mod List"));
		buttonList.add(new GuiButton(10, 5, 5 + 20 + 4, 80, 20, "Cosmetics"));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		
		switch (button.id) {
		case 8:
			mc.displayGuiScreen(new GuiIngameMultiplayer(this));
			break;
		case 9:
			mc.displayGuiScreen(new GuiModList(this, 0));
			break;
		case 10:
			mc.displayGuiScreen(new GuiCosmetics(this, 0));
		}
	}
}
