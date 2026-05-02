package pixel.mod;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pixel.gui.hud.HUD;
import pixel.mod.impl.ArmorStatus;
import pixel.mod.impl.BlockOverlay;
import pixel.mod.impl.Bossbar;
import pixel.mod.impl.CPS;
import pixel.mod.impl.Chat;
import pixel.mod.impl.Clock;
import pixel.mod.impl.ComboCounter;
import pixel.mod.impl.Coordinates;
import pixel.mod.impl.DayCounter;
import pixel.mod.impl.Direction;
import pixel.mod.impl.Extra;
import pixel.mod.impl.FPS;
import pixel.mod.impl.Freelook;
import pixel.mod.impl.HealthIndicator;
import pixel.mod.impl.HitColor;
import pixel.mod.impl.HurtCam;
import pixel.mod.impl.ItemPhysics;
import pixel.mod.impl.KeyStrokes;
import pixel.mod.impl.MemoryUsage;
import pixel.mod.impl.Menu;
import pixel.mod.impl.Nametags;
import pixel.mod.impl.OldVisuals;
import pixel.mod.impl.PackDisplay;
import pixel.mod.impl.Particles;
import pixel.mod.impl.Ping;
import pixel.mod.impl.PotionEffects;
import pixel.mod.impl.Scoreboard;
import pixel.mod.impl.TabOverlay;
import pixel.mod.impl.TimeChanger;
import pixel.mod.impl.Zoom;
import pixel.mod.impl.togglesprintsneak.ToggleSprintSneak;

public class ModHandler {
	private static final List<Mod> MODS_LIST = Arrays.asList(
			new FPS(),
			new CPS(),
			new ArmorStatus(),
			new PotionEffects(),
			new KeyStrokes(),
			new ToggleSprintSneak(),
			new OldVisuals(),
			new Chat(),
			new Nametags(),
			new Scoreboard(),
			new Coordinates(),
			new PackDisplay(),
			new Direction(),
			new BlockOverlay(),
			new Freelook(),
			new HurtCam(),
			new ItemPhysics(),
			new Ping(),
			new Extra(),
			new Menu(),
			new Particles(),
			new TabOverlay(),
			new Zoom(),
			new HealthIndicator(),
			new Bossbar(),
			new HitColor(),
			new TimeChanger(),
			new Clock(),
			new ComboCounter(),
			new DayCounter(),
			new MemoryUsage()
			);
	private static final Map<Class<? extends Mod>, Mod> MODS_MAP = new HashMap<>();
	
	public static void init(HUD hud) {
		for (Mod mod : MODS_LIST) {
			if (mod instanceof ModDraggable) {
				register(hud, (ModDraggable) mod);
				
				continue;
			}
			
			register(mod);
		}
	}
	
	public static void register(HUD hud, ModDraggable mod) {
		hud.register((ModDraggable) mod);
		
		register(mod);
	}
	
	public static void register(Mod mod) {
		MODS_MAP.put(mod.getClass(), mod);
	}
	
	public static <T extends Mod> T get(Class<T> clazz) {
		return clazz.cast(MODS_MAP.get(clazz));
	}
	
	public static List<Mod> getModsList() {
		return MODS_LIST;
	}
}
