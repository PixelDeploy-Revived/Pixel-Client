package pixel.gui.hud;

public interface IRenderer {
	public void setPosition(ScreenPosition pos);
	
	public ScreenPosition getPosition();
	
	public int getWidth();
	
	public int getHeight();
	
	public void render(ScreenPosition pos);
	
	public default void renderDummy(ScreenPosition pos) {
		render(pos);
	}
	
	public default boolean isEnabled() {
		return false;
	};
}
