package pixel.gui;

import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;

public class GuiIngameMultiplayer extends GuiMultiplayer {
	public GuiIngameMultiplayer(GuiScreen prevGuiScreen) {
		super(prevGuiScreen);
	}
	
	@Override
	public void connectToSelected() {
		disconnect();
		
		super.connectToSelected();
	}
	
	public void disconnect() {
		if (mc.theWorld != null) {
			mc.theWorld.sendQuittingDisconnectingPacket();
			mc.loadWorld(null);
		}
	}
}
