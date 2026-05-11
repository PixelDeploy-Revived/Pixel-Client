package pixel.mod.impl;

import net.minecraft.client.network.NetworkPlayerInfo;
import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDisplayBase;

public class Ping extends ModDisplayBase {
	public Ping() {
		super(false, "-1 ms");
	}
	
	@Override
	public void render(ScreenPosition pos) {
		if (!mc.isSingleplayer()) {
			NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
			int ping = info != null ? info.getResponseTime() : 0;
			
			draw(pos, ping + " ms");
		}
	}
}
