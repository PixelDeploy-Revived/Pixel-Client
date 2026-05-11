package pixel.mod.impl;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import pixel.gui.Gui;
import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDraggable;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;
import pixel.mod.option.ModOptionParent;
import pixel.mod.option.type.ModOptionColor;
import pixel.mod.option.type.ModOptionFloat;
import pixel.util.ColorManager;

public class KeyStrokes extends ModDraggable {
	public KeyStrokes() {
		super(true);
		
		loadOptions(
				new ModOption("textShadow", true, new InGuiSettings("Text Shadow")),
				new ModOption("textShadow.pressed", true, new InGuiSettings("Text Shadow (Pressed)")),
				new ModOptionColor("textColor", ColorManager.WHITE.getARGB(), false, new ModOptionColor.InGuiSettings("Text Color", false, true)),
				new ModOptionColor("textColor.pressed", ColorManager.WHITE.getARGB(), false, new ModOptionColor.InGuiSettings("Text Color (Pressed)", false, true)),
				new ModOptionColor("backgroundColor", ColorManager.BLACK_66.getARGB(), false, new ModOptionColor.InGuiSettings("Background Color", true, false)),
				new ModOptionColor("backgroundColor.pressed", ColorManager.WHITE_66.getARGB(), false, new ModOptionColor.InGuiSettings("Background Color (Pressed)", true, false)),
				new ModOption("drawBorder", false, new InGuiSettings("Border")),
				new ModOptionFloat(new ModOptionParent("drawBorder"), "borderThickness", 1.0F, 0.5F, 2.0F, new ModOptionFloat.InGuiSettings("Border Thickness", 1)),
				new ModOptionFloat(new ModOptionParent("drawBorder"), "borderThickness.pressed", 1.0F, 0.5F, 2.0F, new ModOptionFloat.InGuiSettings("Border Thickness (Pressed)", 1)),
				new ModOptionColor(new ModOptionParent("drawBorder"), "borderColor", ColorManager.BLACK.getARGB(), false, new ModOptionColor.InGuiSettings("Border Color", true, false)),
				new ModOptionColor(new ModOptionParent("drawBorder"), "borderColor.pressed", ColorManager.BLACK.getARGB(), false, new ModOptionColor.InGuiSettings("Border Color (Pressed)", true, false)),
				new ModOption("showMovementKeys", true, new InGuiSettings("Show Movement Keys")),
				new ModOption("showMouse", true, new InGuiSettings("Show Mouse")),
				new ModOption("showSpacebar", true, new InGuiSettings("Show Spacebar")),
				new ModOption(new ModOptionParent("showMovementKeys"), "useArrows", false, new InGuiSettings("Use Arrows"))
				);
	}
	
	@Override
	public int getWidth() {
		return 53;
	}
	
	@Override
	public int getHeight() {
		int height = 0;
		boolean flag = false;
		
		if (castOptionValueIntoBoolean("showMovementKeys")) {
			height += 17 + 1 + 17;
			
			flag = true;
		}
		
		if (castOptionValueIntoBoolean("showMouse")) {
			if (flag) {
				height += 1;
			}
			
			height += 17;
			
			flag = true;
		}
		
		if (castOptionValueIntoBoolean("showSpacebar")) {
			if (flag) {
				height += 1;
			}
			
			height += 8;
		}
		
		return height;
	}
	
	@Override
	public void render(ScreenPosition pos) {
		int y = pos.getAbsoluteY();
		
		if (castOptionValueIntoBoolean("showMovementKeys")) {
			boolean drawArrows = castOptionValueIntoBoolean("useArrows");
			int i = 17;
			
			drawKey(drawArrows ? "▲" : Keyboard.getKeyName(mc.gameSettings.keyBindForward.getKeyCode()), pos.getAbsoluteX() + 1 + i, y, i, i, mc.gameSettings.keyBindForward.isKeyDown());
			
			y += 1 + i;
			
			drawKey(drawArrows ? "◄" : Keyboard.getKeyName(mc.gameSettings.keyBindLeft.getKeyCode()), pos.getAbsoluteX(), y, i, i, mc.gameSettings.keyBindLeft.isKeyDown());
			drawKey(drawArrows ? "▼" : Keyboard.getKeyName(mc.gameSettings.keyBindBack.getKeyCode()), pos.getAbsoluteX() + 1 + i, y, i, i, mc.gameSettings.keyBindBack.isKeyDown());
			drawKey(drawArrows ? "►" : Keyboard.getKeyName(mc.gameSettings.keyBindRight.getKeyCode()), pos.getAbsoluteX() + (1 + i) * 2, y, i, i, mc.gameSettings.keyBindRight.isKeyDown());
			
			y += 1 + i;
		}
		
		if (castOptionValueIntoBoolean("showMouse")) {
			int i = 26;
			int j = 17;
			
			drawKey("LMB", pos.getAbsoluteX(), y, i, j, mc.gameSettings.keyBindAttack.isKeyDown());
			drawKey("RMB", pos.getAbsoluteX() + 1 + i, y, i, j, mc.gameSettings.keyBindUseItem.isKeyDown());
			
			y += 1 + j;
		}
		
		if (castOptionValueIntoBoolean("showSpacebar")) {
			drawKey(EnumChatFormatting.STRIKETHROUGH + "---", pos.getAbsoluteX(), y, getWidth(), 8, mc.gameSettings.keyBindJump.isKeyDown());
		}
	}
	
	private void drawKey(String text, int x, int y, int width, int height, boolean isKeyDown) {
		String suffix = isKeyDown ? ".pressed" : "";
		
		Gui.drawRect(x, y, x + width, y + height, getOptionColor("backgroundColor" + suffix).getARGB());
		
		if (castOptionValueIntoBoolean("drawBorder")) {
			Gui.drawHollowRect(x, y, width - castOptionValueIntoFloat("borderThickness" + suffix), height - castOptionValueIntoFloat("borderThickness" + suffix), castOptionValueIntoFloat("borderThickness" + suffix), getOptionColor("borderColor" + suffix).getARGB());
		}
		
		drawText(text, x + (width - font.getStringWidth(text) + 1) / 2.0F, y + (height - font.FONT_HEIGHT + 1) / 2.0F, getOptionColor("textColor" + suffix).getARGB(), castOptionValueIntoBoolean("textShadow" + suffix), getOptionColor("textColor" + suffix).isChromaEnabled());
	}
}
