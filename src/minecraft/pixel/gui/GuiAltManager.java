package pixel.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.resources.I18n;
import pixel.Pixel;
import pixel.util.ColorManager;
import pixel.util.SessionChanger;

public class GuiAltManager extends GuiScreen {
	private final SessionChanger sessionChanger = SessionChanger.getInstance();
	private final GuiScreen prevGuiScreen;    
	
	private int selectedIndex = -1;
	
	private GuiSlot accountList;
	private GuiButton buttonLogin;
	private GuiButton buttonEdit;
	private GuiButton buttonDelete;
	
	public GuiAltManager(GuiScreen prevGuiScreen) {
		this.prevGuiScreen = prevGuiScreen;
	}
	
	@Override
	public void initGui() {
		buttonList.clear();
		
		buttonList.add(buttonLogin = new GuiButton(1, width / 2 - 154, height - 52, 152, 20, "Login (Offline)"));
		buttonList.add(new GuiButton(2, width / 2 + 2, height - 52, 152, 20, "Add Account"));
		buttonList.add(buttonEdit = new GuiButton(3,  width / 2 - 154, height - 28, 100, 20, "Edit"));
		buttonList.add(buttonDelete = new GuiButton(4,   width / 2 - 50, height - 28, 100, 20, "Delete"));
		buttonList.add(new GuiButton(0,  width / 2 + 4+  50, height - 28, 100, 20, I18n.format("gui.cancel", new Object[0])));
		
		buttonLogin.enabled = selectedIndex >= 0 && !mc.session.getUsername().equals(Pixel.getInstance().accountUsernames.get(selectedIndex));
		buttonEdit.enabled = buttonDelete.enabled = selectedIndex >= 0;
		
		accountList = new List(mc, width, height);
		accountList.registerScrollButtons(7, 8);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case 1:
			if (selectedIndex >= 0) {
				sessionChanger.setUserOffline(Pixel.getInstance().accountUsernames.get(selectedIndex));
				
				initGui();
			}
			break;
		case 2:
			mc.displayGuiScreen(new GuiAddAccount(this));
			break;
		case 3:
			if (selectedIndex >= 0) {
				mc.displayGuiScreen(new GuiEditAccount(this, selectedIndex));
			}
			break;
		case 4:
			if (selectedIndex >= 0) {
				Pixel.getInstance().accountUsernames.remove(selectedIndex);
				Pixel.getAltsFile().put("usernames", Pixel.getInstance().accountUsernames);
				
				if (Pixel.getInstance().accountUsernames.size() <= 0) {
					selectedIndex = -1;
				}
				
				initGui();
			}
			break;
		case 0:
			mc.displayGuiScreen(prevGuiScreen);
		}
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		
		if (accountList != null) {
			accountList.handleMouseInput();
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		accountList.drawScreen(mouseX, mouseY, partialTicks);
		
		drawCenteredString(fontRendererObj, "Alt Manager", width / 2, 10, ColorManager.WHITE.getARGB());
		drawCenteredString(fontRendererObj, "Currently logged in as " + mc.session.getUsername(), width / 2, 20, ColorManager.DEFAULT_GRAY.getARGB());
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private class List extends GuiSlot {
		public List(Minecraft mc, int width, int height) {
			super(mc, width, height, 32, height - 64, 14);
		}
		
		@Override
		protected int getSize() {
			return Pixel.getInstance().accountUsernames.size();
		}
		
		@Override
		protected void elementClicked(int index, boolean doubleClick, int mouseX, int mouseY) {
			selectedIndex = index;
			
			initGui();
		}
		
		@Override
		protected boolean isSelected(int index) {
			return index == selectedIndex;
		}
		
		@Override
		protected int getContentHeight() {
			return getSize() * 14;
		}
		
		@Override
		protected void drawBackground() {
			drawDefaultBackground();
		}
		
		@Override
		protected void drawSlot(int entryID, int x, int y, int height, int mouseX, int mouseY) {
			String username = Pixel.getInstance().accountUsernames.get(entryID);
			
			fontRendererObj.drawString(username, (width - 220) / 2 + 4, y + (14 - fontRendererObj.FONT_HEIGHT) / 2 - 1, mc.session.getUsername().equals(username) ? ColorManager.DEFAULT_GREEN.getARGB() : ColorManager.WHITE.getARGB());
		}
	}
}
