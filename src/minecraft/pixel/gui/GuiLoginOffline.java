package pixel.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import pixel.util.ColorManager;
import pixel.util.SessionChanger;

import java.io.IOException;

public class GuiLoginOffline extends GuiScreen {
	private final SessionChanger sessionChanger = SessionChanger.getInstance();
	private final GuiScreen prevGuiScreen;
	
	private GuiButton buttonLogin;
	private GuiTextField textFieldUsername;
	
	public GuiLoginOffline(GuiScreen prevGuiScreen) {
		this.prevGuiScreen = prevGuiScreen;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		drawCenteredString(fontRendererObj, "Login with an SP (offline) account", width / 2, 40, ColorManager.WHITE.getARGB());
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		textFieldUsername.drawTextBox();
	}
	
	@Override
	protected void keyTyped(char typedChar, int key) throws IOException {
		if (typedChar == '\t' && !textFieldUsername.isFocused()) {
			textFieldUsername.setFocused(true);
		}
		
		if (typedChar == '\r') {
			actionPerformed(buttonList.get(0));
		}
		
		textFieldUsername.textboxKeyTyped(typedChar, key);
		
		buttonLogin.enabled = !textFieldUsername.getText().isEmpty();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		textFieldUsername.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0:
			mc.displayGuiScreen(prevGuiScreen);
			break;
		case 1:
			sessionChanger.setUserOffline(textFieldUsername.getText());
			mc.displayGuiScreen(new GuiMainMenu());
		}
	}
	
	@Override
	public void initGui() {
		buttonList.clear();
		
		int i = -16;
		
		buttonList.add(buttonLogin = new GuiButton(1, (width - 120) / 2, height / 6 + 96 + i, 120, 20, "Login"));
		buttonList.add(new GuiButton(0, (width - 120) / 2, height / 6 + 120 + i, 120, 20, I18n.format("gui.cancel", new Object[0])));
		
		textFieldUsername = new GuiTextField(2, fontRendererObj, (width - 120) / 2, height / 6 + 72 + i - 2, 120, 20);
		textFieldUsername.setText(mc.session.getUsername());
		textFieldUsername.setMaxStringLength(16);
		textFieldUsername.setFocused(true);
	}
	
	@Override
	public void updateScreen() {
		textFieldUsername.updateCursorCounter();
	}
}
