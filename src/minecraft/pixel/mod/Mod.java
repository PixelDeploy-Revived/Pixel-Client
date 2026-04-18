package pixel.mod;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import pixel.Pixel;
import pixel.event.EventManager;
import pixel.gui.Gui;
import pixel.gui.GuiModOptions;
import pixel.mod.option.ModOption;
import pixel.mod.option.type.ModOptionColor;
import pixel.mod.option.type.ModOptionEnum;
import pixel.mod.option.type.ModOptionFloat;
import pixel.mod.option.type.ModOptionInt;

public abstract class Mod {
	protected boolean enabled;
	protected ModOption[] options;
	
	protected final Pixel pixelClient;
	protected final Minecraft mc;
	protected final FontRenderer font;
	protected final ModFile file;
	
	public Mod(boolean enabled) {
		pixelClient = Pixel.getInstance();
		mc = Minecraft.getMinecraft();
		font = mc.fontRendererObj;
		file = new ModFile(pixelClient, this);
		
		enable((boolean) file.safeGet("enabled", enabled));
	}
	
	public void enable(boolean enabled) {
		this.enabled = enabled;
		
		if (enabled) {
			EventManager.register(this);
		} else {
			EventManager.unregister(this);
		}
		
		file.set("enabled", enabled);
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public ModFile getFile() {
		return file;
	}
	
	public String getName() {
		return StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(getClass().getSimpleName().replaceAll("\\d+", "")), " ");
	}
	
	public GuiScreen getGuiOptions(GuiScreen prevGuiScreen) {
		return new GuiModOptions(prevGuiScreen, this, 0);
	}
	
	public ModOption[] getOptions() {
		return options;
	}
	
	public ModOption getOption(String key) {
		for (ModOption option : options) {			
			if (key.equals(option.getKey())) {
				return option;
			}
		}
		
		return null;
	}
	
	public void loadOptions(ModOption... options) {
		this.options = options;
		
		for (ModOption option : options) {
			if (option.getValue() instanceof Boolean) {
				option.setValue((boolean) file.safeGet(option.getKey(), (boolean) option.getValue()));
				option.saveIn(this);
			} else if (option instanceof ModOptionColor) {
				ModOptionColor optionColor = (ModOptionColor) option;

				int argb = (int) ((long) file.safeGet(optionColor.getKeyARGB(), optionColor.getARGB()));
				boolean chroma = (boolean) file.safeGet(optionColor.getKeyChroma(), optionColor.isChromaEnabled());
				
				optionColor.setARGB(argb);
				optionColor.enableChroma(chroma);
				optionColor.saveIn(this);
			} else if (option instanceof ModOptionFloat) {
				option.setValue((float) ((double) file.safeGet(option.getKey(), (float) option.getValue())));
				option.saveIn(this);
			} else if (option instanceof ModOptionInt || option instanceof ModOptionEnum) {
				option.setValue((int) ((long) file.safeGet(option.getKey(), (int) option.getValue())));
				option.saveIn(this);
			}
		}
	}
	
	public boolean castOptionValueIntoBoolean(String key) {
		return (boolean) getOption(key).getValue();
	}
	
	public int castOptionValueIntoInt(String key) {
		return (int) getOption(key).getValue();
	}
	
	public float castOptionValueIntoFloat(String key) {
		return (float) getOption(key).getValue();
	}
	
	public ModOptionColor getOptionColor(String key) {
		return (ModOptionColor) getOption(key);
	}
	
	public void drawText(String text, float x, float y, int color, boolean dropShadow, boolean chroma) {
		Gui.drawText(font, text, x, y, color, dropShadow, chroma);
	}
	
	public void drawTexturedModalRect(float x, float y, float textureX, float textureY, int width, int height) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
        
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldRenderer.pos((double) (x), (double) (y + height), 0.0D).tex((double) (textureX * f), (double) ((textureY + height) * f1)).endVertex();
		worldRenderer.pos((double) (x + width), (double) (y + height), 0.0D).tex((double) ((textureX + width) * f), (double) ((textureY + height) * f1)).endVertex();
		worldRenderer.pos((double) (x + width), (double) (y), 0.0D).tex((double) ((textureX + width) * f), (double) (textureY * f1)).endVertex();
		worldRenderer.pos((double) (x), (double) (y), 0.0D).tex((double) (textureX * f), (double) (textureY * f1)).endVertex();
		
		tessellator.draw();
	}
	
	public void drawRect(float left, float top, float right, float bottom, int color) {
		Gui.drawRect(left, top, right, bottom, color);
	}
	
	public void drawBorder(float x, float y, float width, float height, int color, float thickness) {
		Gui.drawHollowRect(x, y, width - thickness, height - thickness, thickness, color);
	}
	
	public void drawScaledText(double scale, String text, float x, float y, int color, boolean textShadow, boolean chroma) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0.0F);
		GlStateManager.scale(scale, scale, 1.0D);
		GlStateManager.translate(-x, -y, 0.0F);
		
		drawText(text, x, y, color, textShadow, chroma);
		
		GlStateManager.popMatrix();
	}
}
