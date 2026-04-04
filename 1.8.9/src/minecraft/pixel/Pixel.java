package pixel;

import org.lwjgl.LWJGLException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import pixel.cosmetic.CosmeticHandler;
import pixel.event.EventManager;
import pixel.event.EventTarget;
import pixel.event.impl.TickEvent;
import pixel.gui.GuiModPositioning;
import pixel.gui.hud.HUD;
import pixel.mod.ModHandler;
import pixel.util.FileManager;

public class Pixel {
	private static final Pixel instance = new Pixel();
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	private static final String COMMIT = "";
	private static final String BRANCH = "main";
	
	private final Discord discord = new Discord();
	
	private HUD hud;
	
	private static FileManager modsFile;
	private static FileManager cosmeticsFile;
	
	public static final Pixel getInstance() {
		return instance;
	}
	
	public void drawSplashScreen(TextureManager textureManager) throws LWJGLException {
		ScaledResolution res = new ScaledResolution(mc);
		int factor = res.getScaleFactor();
		Framebuffer framebuffer = new Framebuffer(res.getScaledWidth() * factor, res.getScaledHeight() * factor, true);
		
		framebuffer.bindFramebuffer(false);
		GlStateManager.matrixMode(5889);
		GlStateManager.loadIdentity();
		GlStateManager.ortho(0.0D, res.getScaledWidth_double(), res.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
		GlStateManager.matrixMode(5888);
		GlStateManager.loadIdentity();
		GlStateManager.translate(0.0F, 0.0F, -2000.0F);
		GlStateManager.disableLighting();
		GlStateManager.disableFog();
		GlStateManager.disableDepth();
		GlStateManager.enableTexture2D();
		textureManager.bindTexture(new ResourceLocation("pixel/gui/splash.png"));
		
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		worldRenderer.pos(0.0D, (double) mc.displayHeight, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
		worldRenderer.pos((double) mc.displayWidth, (double) mc.displayHeight, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
		worldRenderer.pos((double) mc.displayWidth, 0.0D, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
		worldRenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
		tessellator.draw();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		mc.draw((res.getScaledWidth() - 256) / 2, (res.getScaledHeight() - 256) / 2, 0, 0, 256, 256, 255, 255, 255, 255);
		GlStateManager.disableLighting();
		GlStateManager.disableFog();
		framebuffer.unbindFramebuffer();
		framebuffer.framebufferRender(res.getScaledWidth() * factor, res.getScaledHeight() * factor);
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(516, 0.1F);
		mc.updateDisplay();
	}
	
	public static final String getDisplayName() {
		return "Pixel Client (1.8.9-" + COMMIT + "/" + BRANCH + ")";
	}
	
	public static final FileManager getModsFile() {
		return modsFile;
	}
	
	public static final FileManager getCosmeticsFile() {
		return cosmeticsFile;
	}
	
	public final Discord getDiscord() {
		return discord;
	}
	
	public void init() {		
		modsFile = FileManager.create("mods");
		cosmeticsFile = FileManager.create("cosmetics");
		
		discord.start();
		
		EventManager.register(this);
	}
	
	public void start() {
		hud = HUD.getInstance();
		
		ModHandler.init(hud);
	}
	
	public void renderCosmetics() {
		for (RenderPlayer renderPlayer : mc.getRenderManager().getSkinMap().values()) {
			CosmeticHandler.renderOn(renderPlayer);
		}
	}
	
	@EventTarget
	public void onTick(TickEvent event) {
		if (mc.gameSettings.keyBindModPositioning.isPressed()) {
			mc.displayGuiScreen(new GuiModPositioning(hud));
		}
	}
}
