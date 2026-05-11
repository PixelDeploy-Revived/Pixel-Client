package pixel.mod.impl;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDisplayBase;

public class Clock extends ModDisplayBase {
	public Clock() {
		super(false, "12:00");
	}
	
	@Override
	public void render(ScreenPosition pos) {
		draw(pos, LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
	}
}
