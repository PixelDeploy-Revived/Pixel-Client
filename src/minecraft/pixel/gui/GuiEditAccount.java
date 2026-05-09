package pixel.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import pixel.Pixel;
import pixel.util.ColorManager;
import pixel.util.SessionChanger;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

public class GuiEditAccount extends GuiScreen {
	private final SessionChanger sessionChanger = SessionChanger.getInstance();
	private final GuiScreen prevGuiScreen;
	private final int selectedIndex;
	
	private GuiButton buttonEdit;
	private GuiTextField textFieldUsername;
	
	public GuiEditAccount(GuiScreen prevGuiScreen, int selectedIndex) {
		this.prevGuiScreen = prevGuiScreen;
		this.selectedIndex = selectedIndex;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		drawCenteredString(fontRendererObj, "Edit the SP (offline) account", width / 2, 17, ColorManager.WHITE.getARGB());
		drawString(fontRendererObj, "Account Username", width / 2 - 100, 100, 10526880);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		textFieldUsername.drawTextBox();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		textFieldUsername.textboxKeyTyped(typedChar, keyCode);
		
		if (keyCode == 15) {
			textFieldUsername.setFocused(!textFieldUsername.isFocused());
		}
		
		if (keyCode == 28 || keyCode == 156) {
			actionPerformed(buttonEdit);
		}
		
		buttonEdit.enabled = !textFieldUsername.getText().isEmpty();
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
			if (mc.session.getUsername().equals(Pixel.getInstance().accountUsernames.get(selectedIndex))) {
				sessionChanger.setUserOffline(textFieldUsername.getText());
			}
			
			Pixel.getInstance().accountUsernames.set(selectedIndex, textFieldUsername.getText());
			Pixel.getAltsFile().put("usernames", Pixel.getInstance().accountUsernames);
			
			mc.displayGuiScreen(prevGuiScreen);
		}
	}
	
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		
		buttonList.clear();
		
		int i = -16;
		
		buttonList.add(buttonEdit = new GuiButton(1, width / 2 - 100, height / 4 + 96 + 12, I18n.format("gui.done", new Object[0])));
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
		
		textFieldUsername = new GuiTextField(2, fontRendererObj, width / 2 - 100, 116, 200, 20);
		textFieldUsername.setText(Pixel.getInstance().accountUsernames.get(selectedIndex));
		textFieldUsername.setMaxStringLength(16);
		textFieldUsername.setFocused(true);
		
		buttonEdit.enabled = !textFieldUsername.getText().isEmpty();
	}
	
	@Override
	public void updateScreen() {
		textFieldUsername.updateCursorCounter();
	}
	
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}
}
