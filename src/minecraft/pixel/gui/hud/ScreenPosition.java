package pixel.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ScreenPosition {
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	private double x;
	private double y;
	
	private ScreenPosition(double x, double y) {
		setRelative(x, y);
	}
	
	public static ScreenPosition fromRelativePosition(double x, double y) {
		return new ScreenPosition(x, y);
	}
	
	public double getRelativeX() {
		return x;
	}
	
	public double getRelativeY() {
		return y;
	}
	
	public void setRelative(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	private ScreenPosition(int x, int y) {
		setAbsolute(x, y);
	}
	
	public static ScreenPosition fromAbsolutePosition(int x, int y) {
		return new ScreenPosition(x, y);
	}
	
	public int getAbsoluteX() {
		ScaledResolution res = new ScaledResolution(mc);
		
		return (int) (x * res.getScaledWidth());
	}
	
	public int getAbsoluteY() {
		ScaledResolution res = new ScaledResolution(mc);
		
		return (int) (y * res.getScaledHeight());
	}
	
	public void setAbsolute(int x, int y) {
		ScaledResolution res = new ScaledResolution(mc);
		
		this.x = x / res.getScaledWidth_double();
		this.y = y / res.getScaledHeight_double();
	}
}
