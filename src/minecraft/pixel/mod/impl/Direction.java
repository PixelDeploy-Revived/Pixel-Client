package pixel.mod.impl;

import pixel.gui.Gui;
import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDraggable;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOptionParent;
import pixel.mod.option.ModOption.InGuiSettings;
import pixel.mod.option.type.ModOptionColor;
import pixel.mod.option.type.ModOptionFloat;
import pixel.mod.option.type.ModOptionInt;
import pixel.util.ColorManager;

public class Direction extends ModDraggable {
	public Direction() {
		super(false, 0, 0);
		
		loadOptions(
				new ModOption("textShadow", true, new InGuiSettings("Text Shadow")),
				new ModOptionColor("textColor", ColorManager.GRAY.getARGB(), false, new ModOptionColor.InGuiSettings("Text Color", false, true)),
				new ModOption("markedFacingTextShadow", true, new InGuiSettings("Marked Facing Text Shadow")),
				new ModOptionColor("markedFacingTextColor", ColorManager.WHITE.getARGB(), false, new ModOptionColor.InGuiSettings("Marked Facing Text Color", false, true)),
				new ModOptionColor("backgroundColor", ColorManager.BLACK_66.getARGB(), false, new ModOptionColor.InGuiSettings("Background Color", true, false)),
				new ModOption("drawBorder", false, new InGuiSettings("Draw Border")),
				new ModOptionColor(new ModOptionParent("drawBorder"), "borderColor", ColorManager.BLACK.getARGB(), false, new ModOptionColor.InGuiSettings("Border Color", true, false)),
				new ModOptionFloat(new ModOptionParent("drawBorder"), "borderThickness", 1.0F, 0.5F, 2.0F, new ModOptionFloat.InGuiSettings("Border Thickness", 1)),
				new ModOptionFloat("cardinalDirectionsScale", 1.5F, 0.5F, 1.5F, new ModOptionFloat.InGuiSettings("Cardinal Directions Scale", 1)),
				new ModOptionFloat("directionsScale", 1.0F, 0.5F, 1.5F, new ModOptionFloat.InGuiSettings("Directions Scale", 1)),
				new ModOptionColor("markerColor", ColorManager.WHITE.getARGB(), false, new ModOptionColor.InGuiSettings("Marker Color", true, false)),
				new ModOptionInt("spacing", 65, 30, 150, new ModOptionInt.InGuiSettings("Spacing"))
				);
	}

	@Override
	public int getWidth() {
		return 300;
	}

	@Override
	public int getHeight() {
		return 20;
	}

	@Override
	public void render(ScreenPosition pos) {
		Gui.drawRect(pos.getAbsoluteX(), pos.getAbsoluteY(), pos.getAbsoluteX() + getWidth(), pos.getAbsoluteY() + getHeight(), getOptionColor("backgroundColor").getARGB());
		
		if (castOptionValueIntoBoolean("drawBorder")) {
			drawBorder(pos.getAbsoluteX(), pos.getAbsoluteY(), getWidth(), getHeight(), getOptionColor("borderColor").getARGB(), castOptionValueIntoFloat("borderThickness"));
		}
		
		drawMarker(pos);
		
		for (int i = 0; i < 8; i++) {
			float offsetX = getDirectionOffsetX(i) * castOptionValueIntoInt("spacing");
			
			if (offsetX > -(getWidth() / 2 - 10) && offsetX < getWidth() / 2 - 10) {
				int color = getOptionColor("textColor").getARGB();
				boolean dropShadow = castOptionValueIntoBoolean("textShadow");
				boolean chroma = getOptionColor("textColor").isChromaEnabled();
					
				if (offsetX > -10 && offsetX < 10) {
					color = getOptionColor("markedFacingTextColor").getARGB();
					dropShadow = castOptionValueIntoBoolean("markedFacingTextShadow");
					chroma = getOptionColor("markedFacingTextColor").isChromaEnabled();
				}
				
				double scale = (double) ((i % 2 == 0) ? castOptionValueIntoFloat("cardinalDirectionsScale") : castOptionValueIntoFloat("directionsScale"));
				
		        drawScaledText(scale, getFacing(i), pos.getAbsoluteX() + (getWidth() - font.getStringWidth(getFacing(i)) * (float) scale) / 2.0F - offsetX, pos.getAbsoluteY() + (getHeight() - (font.FONT_HEIGHT - 1) * (float) scale) / 2.0F, color, dropShadow, chroma);
			}
		}
	}
	
	private void drawMarker(ScreenPosition pos) {
		int color = getOptionColor("markerColor").getARGB();
		
		for (int i = 0, j = 3; i < 3; i++, j--) {
			Gui.drawRect(pos.getAbsoluteX() + getWidth() / 2 - j, pos.getAbsoluteY(), pos.getAbsoluteX() + getWidth() / 2 - j + 1, pos.getAbsoluteY() + (i + 1), color);
			Gui.drawRect(pos.getAbsoluteX() + getWidth() / 2 + j, pos.getAbsoluteY(), pos.getAbsoluteX() + getWidth() / 2 + j + 1, pos.getAbsoluteY() + (i + 1), color);
		}
		
		Gui.drawRect(pos.getAbsoluteX() + getWidth() / 2, pos.getAbsoluteY(), pos.getAbsoluteX() + getWidth() / 2 + 1, pos.getAbsoluteY() + 4, color);
	}
	
	private float getDirectionOffsetX(int directionIndex) {
		float playerYaw = mc.thePlayer.rotationYaw % 360.0F;
		
		if (playerYaw < 0.0F) {
			playerYaw += 360.0F;
		}
		
		float directionYaw = directionIndex * 45.0F;
		
		directionYaw += 180.0F;
		directionYaw %= 360.0F;
		
		float diff = directionYaw - playerYaw;
		
		if (diff > 180.0F) {
			diff -= 360.0F;
		}
		
		if (diff < -180.0F) {
			diff += 360.0F;
		}
		
		return diff / 45.0F;
	}

	private String getFacing(int directionIndex) {
		switch (directionIndex) {
		case 0:
			return "N";
		case 1:
			return "NE";
		case 2:
			return "E";
		case 3:
			return "SE";
		case 4:
			return "S";
		case 5:
			return "SW";
		case 6:
			return "W";
		case 7:
			return "NW";
		default:
			return "";
		}
	}
}
