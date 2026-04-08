package pixel.mod.impl;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import pixel.event.EventTarget;
import pixel.event.impl.KeyEvent;
import pixel.mod.Mod;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;

public class Freelook extends Mod {
	public Freelook() {
		super(true);
				
		loadOptions(
				new ModOption("hold", true, new InGuiSettings("Hold")),
				new ModOption("invertYaw", false, new InGuiSettings("Invert Yaw")),
				new ModOption("invertPitch", false, new InGuiSettings("Invert Pitch"))
				);
	}
	
	private boolean perspectiveToggled = false;
	private float cameraYaw = 0.0F;
	private float cameraPitch = 0.0F;
	private int previousPerspective = 0;
	
	@EventTarget
	public void onKey(KeyEvent e) {
		if (Keyboard.getEventKey() == mc.gameSettings.keyBindFreelook.getKeyCode()) {
			if (Keyboard.getEventKeyState()) {
				perspectiveToggled = !perspectiveToggled;
				
				cameraYaw = mc.thePlayer.rotationYaw;
				cameraPitch = mc.thePlayer.rotationPitch;
				
				if (perspectiveToggled) {
					previousPerspective = mc.gameSettings.thirdPersonView;
					mc.gameSettings.thirdPersonView = 1;
				} else {
					mc.gameSettings.thirdPersonView = previousPerspective;
				}
			} else if (castOptionValueIntoBoolean("hold")) {
				perspectiveToggled = false;
				mc.gameSettings.thirdPersonView = previousPerspective;
			}
		}
		
		if (Keyboard.getEventKey() == mc.gameSettings.keyBindTogglePerspective.getKeyCode()) {
			perspectiveToggled = false;
		}
	}
	
	public float getCameraYaw() {
		return perspectiveToggled ? cameraYaw : mc.thePlayer.rotationYaw;
	}
	
	public float getCameraPitch() {
		return perspectiveToggled ? cameraPitch : mc.thePlayer.rotationPitch;
	}
	
	public boolean overrideMouse() {
		if (mc.inGameHasFocus && Display.isActive()) {
			if (!perspectiveToggled) {
				return true;
			}
			
			mc.mouseHelper.mouseXYChange();
			
			float mouseSensitivity = mc.gameSettings.mouseSensitivity * 0.1F;
			float sensitivityMultiplier = mouseSensitivity * 3.0F * 8.0F;
			float yawDelta = mc.mouseHelper.deltaX * sensitivityMultiplier;
			float pitchDelta = mc.mouseHelper.deltaY * sensitivityMultiplier;
			
			cameraYaw = castOptionValueIntoBoolean("invertYaw") ? cameraYaw - (yawDelta * 0.15F) : cameraYaw + (yawDelta * 0.15F);
			cameraPitch = castOptionValueIntoBoolean("invertPitch") ? cameraPitch + (pitchDelta * 0.15F) : cameraPitch - (pitchDelta * 0.15F);
			
			if (cameraPitch > 90.0F) {
				cameraPitch = 90.0F;
			}
			
			if (cameraPitch < -90.0F) {
				cameraPitch = -90.0F;
			}
		}
		
		return false;
	}
}