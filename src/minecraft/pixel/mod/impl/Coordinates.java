package pixel.mod.impl;

import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDraggable;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;
import pixel.mod.option.ModOptionParent;
import pixel.mod.option.type.ModOptionColor;
import pixel.mod.option.type.ModOptionFloat;
import pixel.util.ColorManager;

public class Coordinates extends ModDraggable {
	private int RECT_GAP = 6;
	
	public Coordinates() {
		super(false, 0, 0);
				
		loadOptions(
				new ModOptionColor("textColor", ColorManager.WHITE.getARGB(), false, new ModOptionColor.InGuiSettings("Text Color", false, true)),
				new ModOption("textShadow", true, new InGuiSettings("Text Shadow")),
				new ModOptionColor("backgroundColor", ColorManager.BLACK_66.getARGB(), false, new ModOptionColor.InGuiSettings("Background Color", true, false)),
				new ModOption("drawBorder", false, new InGuiSettings("Draw Border")),
				new ModOptionColor(new ModOptionParent("drawBorder"), "borderColor", ColorManager.BLACK.getARGB(), false, new ModOptionColor.InGuiSettings("Border Color", true, false)),
				new ModOptionFloat(new ModOptionParent("drawBorder"), "borderThickness", 1.0F, 0.5F, 2.0F, new ModOptionFloat.InGuiSettings("Border Thickness", 1)),
				new ModOption("showBiome", true, new InGuiSettings("Show Biome")),
				new ModOption("showFacing", true, new InGuiSettings("Show Facing")),
				new ModOption("showFacingTowards", false, new InGuiSettings("Show Facing Towards"))
				);
	}
	
	@Override
	public int getWidth() {
		int width = RECT_GAP * 2;

		if (castOptionValueIntoBoolean("showBiome")) {
			int biomeWidth = font.getStringWidth(getBiomeText());
			int coordsWidth = font.getStringWidth(getLongestCoordinateText());

			if (biomeWidth > coordsWidth) {
				width += biomeWidth;
			} else {
				width += coordsWidth;
			}
		} else {
			width += font.getStringWidth(getLongestCoordinateText());
		}
		
		if (castOptionValueIntoBoolean("showFacing") || castOptionValueIntoBoolean("showFacingTowards")) {
			width += 10 + 6;
		}

		return width;
	}


	@Override
	public int getHeight() {
		int height = RECT_GAP * 2;
		
		int lines = 3;
		
		if (castOptionValueIntoBoolean("showBiome")) {
			lines++;
		}
		
		height += 10 * lines;
		
		return height;
	}

	@Override
	public void render(ScreenPosition pos) {
		drawRect(pos.getAbsoluteX(), pos.getAbsoluteY(), getWidth(), getHeight(), getOptionColor("backgroundColor").getARGB());
		
		if (castOptionValueIntoBoolean("drawBorder")) {
			drawBorder(pos.getAbsoluteX(), pos.getAbsoluteY(), getWidth(), getHeight(), getOptionColor("borderColor").getARGB(), castOptionValueIntoFloat("borderThickness"));
		}
		
		int i = 11;
		
		drawText("X: " + (int) mc.getRenderViewEntity().posX, pos.getAbsoluteX() + RECT_GAP, pos.getAbsoluteY() + i * 1 - RECT_GAP + 1, getOptionColor("textColor").getARGB(), castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
		drawText("Y: " + (int) mc.getRenderViewEntity().getEntityBoundingBox().minY, pos.getAbsoluteX() + RECT_GAP, pos.getAbsoluteY() + i * 2 - RECT_GAP + 1, getOptionColor("textColor").getARGB(), castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
		drawText("Z: " + (int) mc.getRenderViewEntity().posZ, pos.getAbsoluteX() + RECT_GAP, pos.getAbsoluteY() + i * 3 - RECT_GAP + 1, getOptionColor("textColor").getARGB(), castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
		
		if (castOptionValueIntoBoolean("showFacing")) {
			drawText(getFacing(), pos.getAbsoluteX() + getWidth() - font.getStringWidth(getFacing()) - RECT_GAP + 1, pos.getAbsoluteY() + i * 2 - RECT_GAP + 1, getOptionColor("textColor").getARGB(), castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
		}
		
		if (castOptionValueIntoBoolean("showFacingTowards")) {
			drawText(getFacingTowardsX(), pos.getAbsoluteX() + getWidth() - font.getStringWidth(getFacingTowardsX()) - RECT_GAP + 1, pos.getAbsoluteY() + i * 1 - RECT_GAP + 1, getOptionColor("textColor").getARGB(), castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
			drawText(getFacingTowardsZ(), pos.getAbsoluteX() + getWidth() - font.getStringWidth(getFacingTowardsZ()) - RECT_GAP + 1, pos.getAbsoluteY() + i * 3 - RECT_GAP + 1, getOptionColor("textColor").getARGB(), castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
		}
		
		if (castOptionValueIntoBoolean("showBiome")) {
			drawText(getBiomeText(), pos.getAbsoluteX() + RECT_GAP, pos.getAbsoluteY() + i * 4 - RECT_GAP + 1, getOptionColor("textColor").getARGB(), castOptionValueIntoBoolean("textShadow"), getOptionColor("textColor").isChromaEnabled());
		}
	}
	
	private String getLongestCoordinateText() {
		String longestText = "";
		
		String textX = "X: " + (int) mc.getRenderViewEntity().posX;
		String textY = "Y: " + (int) mc.getRenderViewEntity().getEntityBoundingBox().minY;
		String textZ = "Z: " + (int) mc.getRenderViewEntity().posZ;
		
		if (font.getStringWidth(textX) > font.getStringWidth(longestText)) {
			longestText = textX;
		}
		
		if (font.getStringWidth(textY) > font.getStringWidth(longestText)) {
			longestText = textY;
		}
		
		if (font.getStringWidth(textZ) > font.getStringWidth(longestText)) {
			longestText = textZ;
		}
		
		return longestText;
	}
	
	private String getBiomeText() {
		BlockPos playerPos = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getEntityBoundingBox().minY, mc.getRenderViewEntity().posZ);
		Chunk chunk = mc.theWorld.getChunkFromBlockCoords(playerPos);
		
		return "Biome: " + chunk.getBiome(playerPos, mc.theWorld.getWorldChunkManager()).biomeName;
	}
	
	private int getDirectionFacing() {
		int yaw = (int) mc.getRenderViewEntity().rotationYaw;
		
		yaw += 360;
		yaw += 22;
		yaw %= 360;
		
		return yaw / 45;
	}
	
	private String getFacingTowardsX() {
		switch (getDirectionFacing()) {
		case 1:
			return "-";
		case 2:
			return "-";
		case 3:
			return "-";
		case 5:
			return "+";
		case 6:
			return "+";
		case 7:
			return "+";
		default:
			return "";
		}
	}

	private String getFacing() {
		switch (getDirectionFacing()) {
		case 0:
			return "S";
		case 1:
			return "SW";
		case 2:
			return "W";
		case 3:
			return "NW";
		case 4:
			return "N";
		case 5:
			return "NE";
		case 6:
			return "E";
		case 7:
			return "SE";
		default:
			return "";
		}
	}
	
	private String getFacingTowardsZ() {
		switch (getDirectionFacing()) {
		case 0:
			return "+";
		case 1:
			return "+";
		case 3:
			return "-";
		case 4:
			return "-";
		case 5:
			return "-";
		case 7:
			return "+";
		default:
			return "";
		}
	}
}
