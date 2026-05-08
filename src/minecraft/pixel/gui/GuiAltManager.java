package pixel.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import pixel.util.ColorManager;

public class GuiAltManager extends GuiScreen {
	private final GuiScreen prevGuiScreen;
	
	private GuiButton buttonMicrosoft;
	private GuiButton buttonMojang;
	
	public GuiAltManager(GuiScreen prevGuiScreen) {
		this.prevGuiScreen = prevGuiScreen;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		drawCenteredString(fontRendererObj, "Currently logged in as " + mc.session.getUsername(), width / 2, 40, ColorManager.WHITE.getARGB());
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0:
			mc.displayGuiScreen(prevGuiScreen);
			break;
		case 1:
			mc.displayGuiScreen(new GuiLoginOffline(this));
		}
	}
	
	@Override
	public void initGui() {
		buttonList.clear();
		
		int i = -16;
		
		buttonList.add(new GuiButton(1, (width - 150) / 2, height / 4 + 24 + i, 150, 20, "Use SP (offline)"));
		buttonList.add(buttonMicrosoft = new GuiButton(2, (width - 150) / 2, height / 4 + 48 + i, 150, 20, "Use Microsoft"));
		buttonList.add(buttonMojang = new GuiButton(3, (width - 150) / 2, height / 4 + 72 + i, 150, 20, "Use Mojang"));
		buttonList.add(new GuiButton(0, (width - 200) / 2, height / 6 + 168, I18n.format("gui.done", new Object[0])));
		
		buttonMicrosoft.enabled = buttonMojang.enabled = false;
	}
}
