package pixel.mod.impl;

import org.lwjgl.input.Mouse;

import pixel.mod.Mod;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;
import pixel.mod.option.ModOptionParent;
import pixel.mod.option.type.ModOptionInt;

public class Zoom extends Mod {
	private int scrollTotal = 4;
	
	public Zoom() {
		super(true);
		
		loadOptions(
				new ModOption("scrollToZoom", true, new InGuiSettings("Scroll To Zoom")),
				new ModOption("smoothCamera", true, new InGuiSettings("Cinematic Camera")),
				new ModOptionInt("zoomLevel", 4, 2, 64, new InGuiSettings("Zoom Level")),
				new ModOptionInt(new ModOptionParent("scrollToZoom"), "zoomLevelMin", 4, 2, 64, new InGuiSettings("Zoom Level Min")),
				new ModOptionInt(new ModOptionParent("scrollToZoom"), "zoomLevelMax", 16, 2, 64, new InGuiSettings("Zoom Level Max"))
				);
	}
	
	public void setScrollTotal(int scrollTotal) {
		this.scrollTotal = scrollTotal;
	}
	
	public int getScrollAmount() {
		if (enabled && castOptionValueIntoBoolean("scrollToZoom")) {
			int dWheel = Mouse.getDWheel();
			
    		if (dWheel != 0) {
    			if (dWheel > 1) {
    				scrollTotal++;
    			}

    			if (dWheel < -1) {
    				scrollTotal--;
    			}
    			
    			if (scrollTotal > castOptionValueIntoInt("zoomLevelMax")) {
    				scrollTotal = castOptionValueIntoInt("zoomLevelMax");
    			}
    			
    			if (scrollTotal < castOptionValueIntoInt("zoomLevelMin")) {
    				scrollTotal = castOptionValueIntoInt("zoomLevelMin");
    			}
    		}
    	}
    	
    	return scrollTotal;
    }
}
