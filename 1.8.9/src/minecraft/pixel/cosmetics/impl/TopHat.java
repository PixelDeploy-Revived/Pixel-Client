package pixel.cosmetics.impl;

import java.awt.Color;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import pixel.cosmetics.Cosmetic;
import pixel.cosmetics.CosmeticBase;
import pixel.cosmetics.CosmeticHandler;
import pixel.cosmetics.CosmeticModelBase;
import pixel.cosmetics.option.CosmeticOption.InGuiSettings;
import pixel.cosmetics.option.type.CosmeticOptionColor;
import pixel.util.ColorManager;

public class TopHat extends Cosmetic {
	public TopHat() {
		super(CosmeticTopHat.class);
		
		loadOptions(
				new CosmeticOptionColor("color", ColorManager.WHITE.getARGB(), false, 3000, new InGuiSettings("Color"))
				);
	}

	public static class CosmeticTopHat extends CosmeticBase {
		private final ModelTopHat modelTopHat;
		private final ResourceLocation topHatTexture = new ResourceLocation("pixel/cosmetics/tophat.png");
		
		public CosmeticTopHat(RenderPlayer renderPlayer) {
			super(renderPlayer);
			
			modelTopHat = new ModelTopHat(renderPlayer);
		}
		
		@Override
		public void render(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float headYaw_, float headPitch, float scale) {
			GlStateManager.pushMatrix();
			
			renderPlayer.bindTexture(topHatTexture);
			
			if (player.isSneaking()) {
				GlStateManager.translate(0.0D, 0.225D, 0.0D);
			}
			
			Cosmetic cosmetic = CosmeticHandler.get(TopHat.class);
			ColorManager color = new ColorManager(cosmetic.getOptionColor("color").getARGB());
			
			if (cosmetic.getOptionColor("color").isRainbowEnabled()) {
				int speed = cosmetic.getOptionColor("color").getRainbowSpeed();
	        	float hue = (System.currentTimeMillis() % speed) / (float) speed;

	        	color = new ColorManager(Color.getHSBColor(hue, 1.0F, 1.0F).getRGB());
			}
			
			GlStateManager.color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
			
			modelTopHat.render(player, limbSwing, limbSwingAmount, ageInTicks, headYaw_, headPitch, scale);
			
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.popMatrix();
		}
		
		private class ModelTopHat extends CosmeticModelBase {
			private ModelRenderer rim;
			private ModelRenderer pointy;
			
			public ModelTopHat(RenderPlayer renderPlayer) {
				super(renderPlayer);
				
				rim = new ModelRenderer(playerModel, 0, 0);
				rim.addBox(-5.5F, -9.0F, -5.5F, 11, 2, 11);
				
				pointy = new ModelRenderer(playerModel, 0, 13);
				pointy.addBox(-3.5F, -17.0F, -3.5F, 7, 8, 7);
			}
			
			@Override
			public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw_, float headPitch, float scale) {
				rim.rotateAngleX = playerModel.bipedHead.rotateAngleX;
				rim.rotateAngleY = playerModel.bipedHead.rotateAngleY;
				rim.rotationPointX = 0.0F;
				rim.rotationPointY = 0.0F;
				rim.render(scale);
				
				pointy.rotateAngleX = playerModel.bipedHead.rotateAngleX;
				pointy.rotateAngleY = playerModel.bipedHead.rotateAngleY;
				pointy.rotationPointX = 0.0F;
				pointy.rotationPointY = 0.0F;
				pointy.render(scale);
			}
		}
	}
}
