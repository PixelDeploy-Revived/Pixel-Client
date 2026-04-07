package pixel.mod.impl.togglesprintsneak;

import java.text.DecimalFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.Potion;
import pixel.mod.ModHandler;

public class MovementInput extends net.minecraft.util.MovementInput {
	private boolean sprint = false;
	private GameSettings gameSettings;
	private int sneakWasPressed = 0;
	private int sprintWasPressed = 0;
	private EntityPlayerSP player;
	private float originalFlySpeed = -1.0F;
	private float boostedFlySpeed = 0;
	private Minecraft mc;
	
	private static final DecimalFormat df = new DecimalFormat("#.0");
	
	private ToggleSprintSneak toggleSprintSneakMod = ModHandler.get(ToggleSprintSneak.class);
	
	public MovementInput(GameSettings gameSettings) {
		this.gameSettings = gameSettings;
		mc = Minecraft.getMinecraft();
		sprint = toggleSprintSneakMod.sprinting;
		sneak = toggleSprintSneakMod.sneaking;
	}
	
	@Override
	public void updatePlayerMoveState() {
		player = mc.thePlayer;
		moveStrafe = 0.0F;
		moveForward = 0.0F;
		
		if (gameSettings.keyBindForward.isKeyDown()) {
			moveForward++;
		}
		
		if (gameSettings.keyBindBack.isKeyDown()) {
			moveForward--;
		}
		
		if (gameSettings.keyBindLeft.isKeyDown()) {
			moveStrafe++;
		}
		
		if (gameSettings.keyBindRight.isKeyDown()) {
			moveStrafe--;
		}
		
		jump = gameSettings.keyBindJump.isKeyDown();
		
		if (toggleSprintSneakMod.isEnabled() && toggleSprintSneakMod.castOptionValueIntoBoolean("toggleSneak")) {
			if (gameSettings.keyBindSneak.isKeyDown()) {
				if (sneakWasPressed == 0) {
					if (sneak) {
						sneakWasPressed = -1;
					} else if (player.isRiding() || player.capabilities.isFlying) {
						sneakWasPressed = toggleSprintSneakMod.keyHoldTicks + 1;
					} else {
						sneakWasPressed = 1;
					}
					
					sneak = !sneak;
				} else if (sneakWasPressed > 0) {
					sneakWasPressed++;
				}
			} else {
				if ((toggleSprintSneakMod.keyHoldTicks > 0) && (sneakWasPressed > toggleSprintSneakMod.keyHoldTicks)) {
					sneak = false;
				}
				
				sneakWasPressed = 0;
			}
		} else {
			sneak = gameSettings.keyBindSneak.isKeyDown();
		}
		
		if (sneak) {
			moveStrafe *= 0.3F;
			moveForward *= 0.3F;
		}
		
		toggleSprintSneakMod.sneaking = sneak;
		
		if (toggleSprintSneakMod.isEnabled() && toggleSprintSneakMod.castOptionValueIntoBoolean("toggleSprint")) {
			if (gameSettings.keyBindSprint.isKeyDown() && !player.capabilities.isFlying) {
				if (sprintWasPressed == 0) {
					if (sprint) {
						sprintWasPressed = -1;
					} else if (player.capabilities.isFlying) {
						sprintWasPressed = toggleSprintSneakMod.keyHoldTicks + 1;
					} else {
						sprintWasPressed = 1;
					}
					
					sprint = !sprint;
				} else if (sprintWasPressed > 0) {
					sprintWasPressed++;
				}
			} else {
				if ((toggleSprintSneakMod.keyHoldTicks > 0) && (sprintWasPressed > toggleSprintSneakMod.keyHoldTicks)) {
					sprint = false;
				}
				
				sprintWasPressed = 0;
			}
		} else {
			sprint = false;
		}
		
		if (sprint && moveForward == 1.0F && player.onGround && !player.isUsingItem() && !player.isPotionActive(Potion.blindness)) {
			player.setSprinting(true);
		}
		
		toggleSprintSneakMod.sprinting = sprint;
		
		if (toggleSprintSneakMod.castOptionValueIntoBoolean("flyBoost") && player.capabilities.isCreativeMode && player.capabilities.isFlying && (mc.getRenderViewEntity() == player) && mc.gameSettings.keyBindSprint.isKeyDown()) {
			if (originalFlySpeed < 0.0F || this.player.capabilities.getFlySpeed() != boostedFlySpeed) {
				originalFlySpeed = this.player.capabilities.getFlySpeed();
			}
			
			float boostFactor = toggleSprintSneakMod.castOptionValueIntoFloat("flyBoostFactor");
			
			boostedFlySpeed = originalFlySpeed * boostFactor;
			
			player.capabilities.setFlySpeed(boostedFlySpeed);
			
			if (sneak) {
				player.motionY -= 0.15D * (double) (boostFactor - 1.0F);
			}
			
			if (jump) {
				player.motionY += 0.15D * (double) (boostFactor - 1.0F);
			}
		} else {
			if (player.capabilities.getFlySpeed() == boostedFlySpeed) {
				this.player.capabilities.setFlySpeed(originalFlySpeed);
			}
			
			originalFlySpeed = -1.0F;
		}
	}
	
	public String getDisplayText() {
		String displayText = "";
		
		boolean isFlying = mc.thePlayer.capabilities.isFlying;
		boolean isRiding = mc.thePlayer.isRiding();
		boolean isHoldingSneak = gameSettings.keyBindSneak.isKeyDown();
		boolean isHoldingSprint = gameSettings.keyBindSprint.isKeyDown();
		
		String spacing = " ";
		
		if (isFlying) {
			displayText = "Flying" + spacing;
			
			if (originalFlySpeed > 0.0F) {
				displayText += "(" + df.format(boostedFlySpeed / originalFlySpeed) + "x Boost)" + spacing;
			}
			
			if (sneak) {
				displayText += "(Descending)" + spacing;
			}
		}
		
		if (isRiding) {
			displayText = "Riding" + spacing;
			
			if (sneak) {
				displayText += "(Dismounting)" + spacing;
			}
		}
		
		if (sneak && !isFlying && !isRiding) {
			displayText = "Sneaking" + spacing;
			
			if (isHoldingSneak) {
				displayText += "(Key Held)" + spacing;
			} else {
				displayText += "(Toggled)" + spacing;
			}
		} else if (sprint && !isFlying && !isRiding) {
			displayText = "Sprinting" + spacing;
			
			if (isHoldingSprint) {
				displayText += "(Key Held)" + spacing;
			} else {
				displayText += "(Toggled)" + spacing;
			}
		}
		
		return displayText.trim();
	}
}