package pixel.cosmetics;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public abstract class CosmeticBase implements LayerRenderer<AbstractClientPlayer> {
	protected final RenderPlayer renderPlayer;
	
	public CosmeticBase(RenderPlayer renderPlayer) {
		this.renderPlayer = renderPlayer;
	}
	
	@Override
	public void doRenderLayer(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float headYaw_, float headPitch, float scale) {
		if (player.getUniqueID().equals(Minecraft.getMinecraft().thePlayer.getUniqueID()) && !player.isInvisible()) {
			render(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, headYaw_, headPitch, scale);
		}
	}
	
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
	
	public abstract void render(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float headYaw_, float headPitch, float scale);
}
