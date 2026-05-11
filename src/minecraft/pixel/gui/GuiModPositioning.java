package pixel.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import net.minecraft.client.gui.GuiScreen;
import pixel.gui.hud.HUD;
import pixel.gui.hud.IRenderer;
import pixel.gui.hud.ScreenPosition;
import pixel.util.ColorManager;

public class GuiModPositioning extends GuiScreen {
	private final HashMap<IRenderer, ScreenPosition> renderers = new HashMap<IRenderer, ScreenPosition>();
	private final ArrayList<IRenderer> clickedRenderers = new ArrayList<IRenderer>();
	private Optional<IRenderer> selectedRenderer = Optional.empty();
	
	private boolean isRightButtonDown = false;
	
	public GuiModPositioning(HUD hud) {
		Collection<IRenderer> registeredRenderers = hud.getRegisteredRenderers();
		
		for (IRenderer renderer : registeredRenderers) {
			if (renderer.isEnabled()) {
				ScreenPosition pos = renderer.getPosition();
				
				if (pos == null) {
					pos = ScreenPosition.fromRelativePosition(0, 0);
				}
				
				renderers.put(renderer, pos);
			}
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		Gui.drawHorizontalLine(0, width, height / 2.0F, 0.5F, ColorManager.WHITE.getARGB());
		Gui.drawVerticalLine(width / 2.0F, 0, height, 0.5F, ColorManager.WHITE.getARGB());
		Gui.drawVerticalLine(width / 3.0F, 0, height, 0.5F, ColorManager.WHITE.getARGB());
		Gui.drawVerticalLine(width - (width / 3.0F), 0, height, 0.5F, ColorManager.WHITE.getARGB());
		Gui.drawHorizontalLine(0, width, height / 3.0F, 0.5F, ColorManager.WHITE.getARGB());
		Gui.drawHorizontalLine(0, width, height - (height / 3.0F), 0.5F, ColorManager.WHITE.getARGB());
		
		for (IRenderer renderer: renderers.keySet()) {
			ScreenPosition pos = renderers.get(renderer);
			
			renderer.renderDummy(pos);
		}
		
		if (selectedRenderer.isPresent()) {
			IRenderer renderer = selectedRenderer.get();
			
			if (isRightButtonDown) {
				if (!clickedRenderers.contains(renderer)) {
					clickedRenderers.add(renderer);
				}
				
				renderers.get(renderer).setAbsolute(mouseX - renderer.getWidth() / 2, mouseY - renderer.getHeight() / 2);
			}
			
			drawRendererPointer(renderer, ColorManager.BLACK.getARGB(), 5, 5);
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		drawCenteredString(fontRendererObj, "Mod Positioning", width / 2, 15, ColorManager.WHITE.getARGB());
		drawCenteredString(fontRendererObj, "Use Right Click To Move", width / 2, 27, ColorManager.WHITE.getARGB());
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		switch (mouseButton) {
		case 0:
			selectedRenderer = Optional.empty();
			break;
		case 1:
			isRightButtonDown = true;
			
			if (!selectedRenderer.isPresent()) {
				List<IRenderer> list = new ArrayList<>(renderers.keySet());
				
				for (int i = list.size() - 1; i >= 0; i--) {
					IRenderer renderer = list.get(i);
					
					if (isRendererHovered(renderer, mouseX, mouseY)) {
						selectedRenderer = Optional.of(renderer);
						break;
					}
				}
			}
		}
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		System.out.println(state);
		switch (state) {
		case 1:
			isRightButtonDown = false;
		}
	}
	
	@Override
	public void onGuiClosed() {
		for (IRenderer renderer : clickedRenderers) {
			renderer.setPosition(renderers.get(renderer));
		}
	}
	
	private void drawRendererPointer(IRenderer renderer, int color, int length, int gap) {
		ScreenPosition pos = renderers.get(renderer);
		
		Gui.drawHorizontalLine(pos.getAbsoluteX() - gap, pos.getAbsoluteX() - gap + length, pos.getAbsoluteY() - gap, 1, color);
		Gui.drawVerticalLine(pos.getAbsoluteX() - gap, pos.getAbsoluteY() - gap, pos.getAbsoluteY() - gap + length + 1, 1, color);
		Gui.drawHorizontalLine(pos.getAbsoluteX() + renderer.getWidth() + gap - 1, pos.getAbsoluteX() + renderer.getWidth() + gap - length - 1, pos.getAbsoluteY() - gap, 1, color);
		Gui.drawVerticalLine(pos.getAbsoluteX() + renderer.getWidth() + gap - 1, pos.getAbsoluteY() - gap, pos.getAbsoluteY() - gap + length + 1, 1, color);
		Gui.drawHorizontalLine(pos.getAbsoluteX() - gap, pos.getAbsoluteX() - gap + length, pos.getAbsoluteY() + renderer.getHeight() + gap - 1, 1, color);
		Gui.drawVerticalLine(pos.getAbsoluteX() - gap, pos.getAbsoluteY() + renderer.getHeight() + gap - 1, pos.getAbsoluteY() + renderer.getHeight() + gap - length - 1 - 1, 1, color);
		Gui.drawHorizontalLine(pos.getAbsoluteX() + renderer.getWidth() + gap - 1, pos.getAbsoluteX() + renderer.getWidth() + gap - length - 1, pos.getAbsoluteY() + renderer.getHeight() + gap - 1, 1, color);
		Gui.drawVerticalLine(pos.getAbsoluteX() + renderer.getWidth() + gap - 1, pos.getAbsoluteY() + renderer.getHeight() + gap, pos.getAbsoluteY() + renderer.getHeight() + gap - length - 1 - 1, 1, color);
	}
	
	private boolean isRendererHovered(IRenderer renderer, int mouseX, int mouseY) {
		ScreenPosition pos = renderers.get(renderer);
		
		int absoluteX = pos.getAbsoluteX();
		int absoluteY = pos.getAbsoluteY();
		
		return (mouseX >= absoluteX && mouseX <= absoluteX + renderer.getWidth()) && (mouseY >= absoluteY && mouseY <= absoluteY + renderer.getHeight());
	}
}
