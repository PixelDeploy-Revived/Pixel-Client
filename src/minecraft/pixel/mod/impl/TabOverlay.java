package pixel.mod.impl;

import java.util.List;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldSettings;
import pixel.mod.Mod;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;
import pixel.mod.option.ModOptionParent;
import pixel.mod.option.type.ModOptionColor;
import pixel.util.ColorManager;

public class TabOverlay extends Mod {
	public TabOverlay() {
		super(false);
		
		loadOptions(
				new ModOptionColor("backgroundColor", ColorManager.BLACK_80.getARGB(), false, new ModOptionColor.InGuiSettings("Background Color", true, false)),
				new ModOptionColor("playerBackgroundColor", ColorManager.WHITE_20.getARGB(), false, new ModOptionColor.InGuiSettings("Player Background Color", true, false)),
				new ModOption("textShadow", true, new InGuiSettings("Text Shadow")),
				new ModOption("showPlayerHeads", true, new InGuiSettings("Show Player Heads")),
				new ModOption("hidePing", false, new InGuiSettings("Hide Ping")),
				new ModOption(new ModOptionParent("hidePing", false), "pingAsText", false, new InGuiSettings("Ping As Text")),
				new ModOption(new ModOptionParent("pingAsText"), "dynamicColors", true, new InGuiSettings("Dynamic Colors")),
				new ModOptionColor(new ModOptionParent("dynamicColors", false), "pingColor", ColorManager.WHITE.getARGB(), false, new ModOptionColor.InGuiSettings("Ping Text Color", false, true)),
				new ModOption("showHeader", true, new InGuiSettings("Show Header")),
				new ModOptionColor(new ModOptionParent("showHeader"), "headerBackgroundColor", ColorManager.BLACK_80.getARGB(), false, new ModOptionColor.InGuiSettings("Header Background Color", true, false)),
				new ModOption("showFooter", true, new InGuiSettings("Show Footer")),
				new ModOptionColor(new ModOptionParent("showFooter"), "footerBackgroundColor", ColorManager.BLACK_80.getARGB(), false, new ModOptionColor.InGuiSettings("Footer Background Color", true, false))
				);
	}
	
	public void renderPlayerlist(net.minecraft.scoreboard.Scoreboard scoreboardIn, ScoreObjective scoreObjectiveIn) {
		ScaledResolution res = new ScaledResolution(mc);
		int width = res.getScaledWidth();
		NetHandlerPlayClient netHandlerPlayClient = mc.thePlayer.sendQueue;
		List<NetworkPlayerInfo> networkPlayerInfoList = GuiPlayerTabOverlay.getNetworkPlayerInfos().<NetworkPlayerInfo>sortedCopy(netHandlerPlayClient.getPlayerInfoMap());
		GuiPlayerTabOverlay tabList = mc.ingameGUI.getTabList();
		int i = 0;
		int j = 0;
		
		for (NetworkPlayerInfo networkplayerinfo : networkPlayerInfoList) {
			int k = font.getStringWidth(tabList.getPlayerName(networkplayerinfo));
			i = Math.max(i, k);
			
			if (scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
				k = font.getStringWidth(" " + scoreboardIn.getValueFromObjective(networkplayerinfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
				j = Math.max(j, k);
			}
		}
		
		networkPlayerInfoList = networkPlayerInfoList.subList(0, Math.min(networkPlayerInfoList.size(), 80));
		int l3 = networkPlayerInfoList.size();
		int i4 = l3;
		int j4;
		
		for (j4 = 1; i4 > 20; i4 = (l3 + j4 - 1) / j4) {
			++j4;
		}
		
		int l;
		
		if (scoreObjectiveIn != null) {
			if (scoreObjectiveIn.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
				l = 90;
			} else {
				l = j;
			}
		} else {
			l = 0;
		}
		
		int i1 = Math.min(j4 * ((castOptionValueIntoBoolean("showPlayerHeads") ? 9 : 0) + i + l + (castOptionValueIntoBoolean("hidePing") ? 0 : castOptionValueIntoBoolean("pingAsText") ? mc.fontRendererObj.getStringWidth("000") + 3 : 13)), width - 50) / j4;
		int j1 = width / 2 - (i1 * j4 + (j4 - 1) * 5) / 2;
		int k1 = 10;
		int l1 = i1 * j4 + (j4 - 1) * 5;
		List<String> header = null;
		List<String> footer = null;
		
		if (tabList.getHeader() != null) {
			header = font.listFormattedStringToWidth(tabList.getHeader().getFormattedText(), width - 50);
			
			for (String s : header) {
				l1 = Math.max(l1, font.getStringWidth(s));
			}
		}
		
		if (tabList.getFooter() != null) {
			footer = font.listFormattedStringToWidth(tabList.getFooter().getFormattedText(), width - 50);
			
			for (String s2 : footer) {
				l1 = Math.max(l1, font.getStringWidth(s2));
			}
		}
		
		if (castOptionValueIntoBoolean("showHeader") && header != null) {
			drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + header.size() * font.FONT_HEIGHT, getOptionColor("headerBackgroundColor").getARGB());
			
			for (String s3 : header) {
				int i2 = font.getStringWidth(s3);
				
                drawText(s3, (float) (width / 2 - i2 / 2), (float) k1, ColorManager.WHITE.getARGB(), castOptionValueIntoBoolean("textShadow"), false);
                
                k1 += font.FONT_HEIGHT;
			}
			
			k1++;
		}
		
		drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + i4 * 9, getOptionColor("backgroundColor").getARGB());
		
		for (int k4 = 0; k4 < l3; k4++) {
			int l4 = k4 / i4;
			int i5 = k4 % i4;
			int j2 = j1 + l4 * i1 + l4 * 5;
			int k2 = k1 + i5 * 9;
			
			drawRect(j2, k2, j2 + i1, k2 + 8, getOptionColor("playerBackgroundColor").getARGB());
			
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			
			if (k4 < networkPlayerInfoList.size()) {
				NetworkPlayerInfo networkPlayerInfo = (NetworkPlayerInfo) networkPlayerInfoList.get(k4);
				String playerName = tabList.getPlayerName(networkPlayerInfo);
				GameProfile gameProfile = networkPlayerInfo.getGameProfile();
				
				if (castOptionValueIntoBoolean("showPlayerHeads")) {
					EntityPlayer entityPlayer = mc.theWorld.getPlayerEntityByUUID(gameProfile.getId());
					boolean isDinnerboneOrGrumm = entityPlayer != null && entityPlayer.isWearing(EnumPlayerModelParts.CAPE) && (gameProfile.getName().equals("Dinnerbone") || gameProfile.getName().equals("Grumm"));
					
					mc.getTextureManager().bindTexture(networkPlayerInfo.getLocationSkin());
					
					int l2 = 8 + (isDinnerboneOrGrumm ? 8 : 0);
					int i3 = 8 * (isDinnerboneOrGrumm ? -1 : 1);
					
					net.minecraft.client.gui.Gui.drawScaledCustomSizeModalRect(j2, k2, 8.0F, (float) l2, 8, i3, 8, 8, 64.0F, 64.0F);
					
					if (entityPlayer != null && entityPlayer.isWearing(EnumPlayerModelParts.HAT)) {
						int j3 = 8 + (isDinnerboneOrGrumm ? 8 : 0);
						int k3 = 8 * (isDinnerboneOrGrumm ? -1 : 1);
						
						Gui.drawScaledCustomSizeModalRect(j2, k2, 40.0F, (float) j3, 8, k3, 8, 8, 64.0F, 64.0F);
					}
					
					j2 += 9;
				}
				
				if (networkPlayerInfo.getGameType() == WorldSettings.GameType.SPECTATOR) {
					playerName = EnumChatFormatting.ITALIC + playerName;
					
					drawText(playerName, (float)j2, (float)k2, ColorManager.WHITE_90.getARGB(), castOptionValueIntoBoolean("textShadow"), false);
				} else {
					drawText(playerName, (float)j2, (float)k2, -1, castOptionValueIntoBoolean("textShadow"), false);
				}
				
				if (scoreObjectiveIn != null && networkPlayerInfo.getGameType() != WorldSettings.GameType.SPECTATOR) {
					int k5 = j2 + i + 1;
					int l5 = k5 + l;
					
					if (l5 - k5 > 5) {
						tabList.drawScoreboardValues(scoreObjectiveIn, k2, gameProfile.getName(), k5, l5, networkPlayerInfo);
					}
				}
				
				if (!castOptionValueIntoBoolean("hidePing")) {
					if (castOptionValueIntoBoolean("pingAsText")) {
						int playerHeadsWidth = castOptionValueIntoBoolean("showPlayerHeads") ? 9 : 0;
						
						writePing(i1, j2 - playerHeadsWidth, k2, networkPlayerInfo);
					} else {
						tabList.drawPing(i1, j2 - (castOptionValueIntoBoolean("showPlayerHeads") ? 9 : 0), k2, networkPlayerInfo);
					}
				}
			}
		}
		
		if (castOptionValueIntoBoolean("showFooter") && footer != null) {
			k1 = k1 + i4 * 9 + 1;
			
			drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + footer.size() * font.FONT_HEIGHT, getOptionColor("footerBackgroundColor").getARGB());
			
			for (String s4 : footer) {
				int j5 = font.getStringWidth(s4);
				
				drawText(s4, (float) (width / 2 - j5 / 2), (float)k1, -1,castOptionValueIntoBoolean("textShadow"), false);
				
				k1 += font.FONT_HEIGHT;
			}
		}
	}
	
	public void writePing(int p_175245_1_, int p_175245_2_, int p_175245_3_, NetworkPlayerInfo networkPlayerInfoIn) {
		int ping = networkPlayerInfoIn.getResponseTime();
		String pingText = String.valueOf(ping);
		int pingColor = getOptionColor("pingColor").getARGB();
		
		if (castOptionValueIntoBoolean("dynamicColors")) {
			if (ping > 300) {
				pingColor = ColorManager.DEFAULT_DARK_RED.getARGB();
			} else if (ping > 200) {
				pingColor = ColorManager.DEFAULT_RED.getARGB();
			} else if (ping > 150) {
				pingColor = ColorManager.DEFAULT_GOLD.getARGB();
			} else if (ping > 100) {
				pingColor = ColorManager.DEFAULT_YELLOW.getARGB();
			} else if (ping > 50) {
				pingColor = ColorManager.DEFAULT_DARK_GREEN.getARGB();
			} else {
				pingColor = ColorManager.DEFAULT_GREEN.getARGB();
			}
		}
		
		drawText(pingText, p_175245_2_ + p_175245_1_ - font.getStringWidth(pingText), p_175245_3_, pingColor, castOptionValueIntoBoolean("textShadow"), getOptionColor("pingColor").isChromaEnabled());
	}
}
