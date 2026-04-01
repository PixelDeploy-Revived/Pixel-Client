package pixel.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.util.IChatComponent;

public class GuiDisconnected extends net.minecraft.client.gui.GuiDisconnected {
	public static String lastIP;
	public static int lastPort;
	
	public GuiDisconnected(String reasonLocalizationKey, IChatComponent chatComp) {
		super(new GuiMultiplayer(new GuiMainMenu()), reasonLocalizationKey, chatComp);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		buttonList.add(new GuiButton(1, width / 2 - 100, height / 2 + super.field_175353_i / 2 + fontRendererObj.FONT_HEIGHT + 20 + 5, "Reconnect"));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		
		switch (button.id) {
		case 1:
			mc.displayGuiScreen(new GuiConnecting(super.parentScreen, mc, GuiDisconnected.lastIP, GuiDisconnected.lastPort));
		}
	}
}
