package pixel.util;

public class ColorManager {
	public static final ColorManager BLACK_5A = new ColorManager(90, 0, 0, 0);
	public static final ColorManager WHITE = new ColorManager(255, 255, 255);
	public static final ColorManager BLACK = new ColorManager(0, 0, 0);
	public static final ColorManager BLACK_66 = new ColorManager(102, 0, 0, 0);
	public static final ColorManager WHITE_66 = new ColorManager(102, 255, 255, 255);
	
	private int alpha;
	private int red;
	private int green;
	private int blue;
	
	public ColorManager(int alpha, int red, int green, int blue) {
		this.alpha = alpha;
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public ColorManager(int red, int green, int blue) {
		this(255, red, green, blue);
	}
	
	public ColorManager(int argb) {
		this((argb >> 24) & 0xFF, (argb >> 16) & 0xFF, (argb >> 8) & 0xFF, (argb >> 0) & 0xFF);
	}
	
	public ColorManager setAlpha(int value) {
		alpha = value;
		
		return this;
	}
	
	public int getAlpha() {
		return alpha;
	}
	
	public ColorManager setRed(int value) {
		red = value;
		
		return this;
	}
	
	public int getRed() {
		return red;
	}
	
	public ColorManager setGreen(int value) {
		green = value;
		
		return this;
	}
	
	public int getGreen() {
		return green;
	}
	
	public ColorManager setBlue(int value) {
		blue = value;
		
		return this;
	}
	
	public int getBlue() {
		return blue;
	}
	
	public ColorManager setARGB(int argb) {
		alpha = (argb >> 24) & 0xFF;
		red = (argb >> 16) & 0xFF;
		green = (argb >> 8) & 0xFF;
		blue = (argb >> 0) & 0xFF;
		
		return this;
	}
	
	public int getARGB() {
		return (alpha << 24) | (red << 16) | (green << 8) | (blue << 0);
	}
}
