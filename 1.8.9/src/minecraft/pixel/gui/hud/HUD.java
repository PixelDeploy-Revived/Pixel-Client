package pixel.gui.hud;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import pixel.event.EventManager;
import pixel.event.EventTarget;
import pixel.event.impl.RenderEvent;
import pixel.gui.GuiModPositioning;

public class HUD {
	private static HUD instance = null;
	
	public static HUD getInstance() {
		if (instance == null) {
			instance = new HUD();
		}
		
		EventManager.register(instance);
		
		return instance;
	}
	
	private Set<IRenderer> registeredRenderers = Sets.newHashSet();
	private Minecraft mc = Minecraft.getMinecraft();
	
	public void register(IRenderer... renderers) {
		for (IRenderer renderer : renderers) {
			registeredRenderers.add(renderer);
		}
	}
	
	public void unregister(IRenderer... renderers) {
		for (IRenderer renderer : renderers) {
			registeredRenderers.remove(renderer);
		}
	}
	
	public Collection<IRenderer> getRegisteredRenderers() {
		return Sets.newHashSet(registeredRenderers);
	}
	
	@EventTarget
	public void onRender(RenderEvent e) {
		if (mc.gameSettings.showDebugInfo) return;
		
		if (mc.currentScreen == null || mc.currentScreen instanceof GuiScreen && !(mc.currentScreen instanceof GuiModPositioning)) {
			for (IRenderer renderer : registeredRenderers) {
				if (renderer.isEnabled()) {
					render(renderer);
				}
			}
		}
	}

	private void render(IRenderer renderer) {
		ScreenPosition pos = renderer.getPosition();
		
		if (pos == null) {
			pos = ScreenPosition.fromRelativePosition(0.5, 0.5);
		}
		
		renderer.render(pos);
	}
}
