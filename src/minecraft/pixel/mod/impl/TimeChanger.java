package pixel.mod.impl;

import java.time.LocalTime;

import pixel.mod.Mod;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;
import pixel.mod.option.ModOptionParent;
import pixel.mod.option.type.ModOptionFloat;

public class TimeChanger extends Mod {
	public TimeChanger() {
		super(false);
		
		loadOptions(
				new ModOptionFloat(new ModOptionParent("useRealCurrentTime", false), "time", 0.0F, 0.0F, 1.0F, new ModOptionFloat.InGuiSettings("Time", 2)),
				new ModOption("useRealCurrentTime", false, new InGuiSettings("Use Real Current Time"))
				);
	}
	
	public float getTime() {
		float time = castOptionValueIntoFloat("time");
		
		if (castOptionValueIntoBoolean("useRealCurrentTime")) {
			LocalTime now = LocalTime.now();
		    float totalHours = now.getHour() + (now.getMinute() / 60.0F);
		    
		    time = ((totalHours - 12.0F + 24.0F) % 24.0F) / 24.0F;
		}
		
	    return time;
	}
}
