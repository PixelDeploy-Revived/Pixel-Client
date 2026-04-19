package pixel.cosmetic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.entity.RenderPlayer;
import pixel.cosmetic.impl.Blaze;
import pixel.cosmetic.impl.Halo;
import pixel.cosmetic.impl.TopHat;
import pixel.cosmetic.impl.Wings;

public class CosmeticHandler {
	private static final List<Cosmetic> COSMETICS = Arrays.asList(
			new Wings(),
			new TopHat(),
			new Blaze(),
			new Halo()
			);
	private static final Map<Class<? extends Cosmetic>, Cosmetic> COSMETICS_MAP = new HashMap<>();
	private static final Map<RenderPlayer, Map<Class<? extends CosmeticBase>, CosmeticBase>> ACTIVE_COSMETICS = new HashMap<>();
	
	public static void renderOn(RenderPlayer renderPlayer) {
		for (Cosmetic cosmetic : COSMETICS) {
			COSMETICS_MAP.put(cosmetic.getClass(), cosmetic);
			
			if (cosmetic.isEnabled()) {
				activeCosmetic(renderPlayer, cosmetic.getClazz());
			} else {
				disactiveCosmetic(renderPlayer, cosmetic.getClazz());
			}
		}
	}
	
	public static final List<Cosmetic> getCosmetics() {
		return COSMETICS;
	}
	
	public static <T extends Cosmetic> T get(Class<T> clazz) {
		return clazz.cast(COSMETICS_MAP.get(clazz));
	}
	
	private static void activeCosmetic(RenderPlayer renderPlayer, Class<? extends CosmeticBase> clazz) {
		ACTIVE_COSMETICS.putIfAbsent(renderPlayer, new HashMap<>());
		Map<Class<? extends CosmeticBase>, CosmeticBase> playerCosmetics = ACTIVE_COSMETICS.get(renderPlayer);
		
		if (!playerCosmetics.containsKey(clazz)) {
			try {
				CosmeticBase cosmeticBase = clazz.getConstructor(RenderPlayer.class).newInstance(renderPlayer);
				
				renderPlayer.addLayer(cosmeticBase);
				playerCosmetics.put(clazz, cosmeticBase);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void disactiveCosmetic(RenderPlayer renderPlayer, Class<? extends CosmeticBase> clazz) {
		if (ACTIVE_COSMETICS.containsKey(renderPlayer)) {
			Map<Class<? extends CosmeticBase>, CosmeticBase> playerCosmetics = ACTIVE_COSMETICS.get(renderPlayer);
			
			if (playerCosmetics.containsKey(clazz)) {
				CosmeticBase cosmeticBase = playerCosmetics.get(clazz);
				
				renderPlayer.removeLayer(cosmeticBase);
				playerCosmetics.remove(clazz);
			}	
		}
	}
}
