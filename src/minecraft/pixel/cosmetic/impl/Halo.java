package pixel.cosmetic.impl;

import java.awt.Color;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import pixel.cosmetic.Cosmetic;
import pixel.cosmetic.CosmeticBase;
import pixel.cosmetic.CosmeticHandler;
import pixel.cosmetic.CosmeticModelBase;
import pixel.cosmetic.option.CosmeticOption.InGuiSettings;
import pixel.cosmetic.option.type.CosmeticOptionColor;
import pixel.util.ColorManager;

public class Halo extends Cosmetic {
	public Halo() {
		super(CosmeticHalo.class);
		
		loadOptions(
				new CosmeticOptionColor("color", ColorManager.WHITE.getARGB(), false, 3000, new InGuiSettings("Color"))
				);
	}

	public static class CosmeticHalo extends CosmeticBase {
		private final ModelHalo modelHalo;
		private final ResourceLocation haloTexture = new ResourceLocation("pixel/cosmetics/halo.png");
		
		public CosmeticHalo(RenderPlayer renderPlayer) {
			super(renderPlayer);
			
			modelHalo = new ModelHalo(renderPlayer);
		}
		
		@Override
		public void render(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float headYaw, float headPitch, float scale) {
			GlStateManager.pushMatrix();
			
			renderPlayer.bindTexture(haloTexture);
			
			if (player.isSneaking()) {
				GlStateManager.translate(0.0D, 0.225D, 0.0D);
			}
			
			Cosmetic cosmetic = CosmeticHandler.get(Halo.class);
			ColorManager color = new ColorManager(cosmetic.getOptionColor("color").getARGB());
			
			if (cosmetic.getOptionColor("color").isRainbowEnabled()) {
				int speed = cosmetic.getOptionColor("color").getRainbowSpeed();
				float hue = (System.currentTimeMillis() % speed) / (float) speed;
				
				color = new ColorManager(Color.getHSBColor(hue, 1.0F, 1.0F).getRGB());
			}
			
			GlStateManager.color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
			
			modelHalo.render(player, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale);
			
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.popMatrix();
		}
		
		private class ModelHalo extends CosmeticModelBase {
			private ModelRenderer front, back, left, right;
			
			public ModelHalo(RenderPlayer renderPlayer) {
				super(renderPlayer);
				
				front = new ModelRenderer(playerModel, 0, 0);
				front.addBox(-4.5F, -11.0F, -4.5F, 9, 1, 1);
				
				back = new ModelRenderer(playerModel, 0, 0);
				back.addBox(-4.5F, -11.0F, 3.5F, 9, 1, 1);
				
				left = new ModelRenderer(playerModel, 0, 0);
				left.addBox(-4.5F, -11.0F, -4.5F, 1, 1, 9);
				
				right = new ModelRenderer(playerModel, 0, 0);
				right.addBox(3.5F, -11.0F, -4.5F, 1, 1, 9);
			}
			
			@Override
			public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale) {
				front.rotateAngleX = playerModel.bipedHead.rotateAngleX;
				front.rotateAngleY = playerModel.bipedHead.rotateAngleY;
				front.rotationPointX = 0.0F;
				front.rotationPointY = 0.0F;
				front.render(scale);
				
				back.rotateAngleX = playerModel.bipedHead.rotateAngleX;
				back.rotateAngleY = playerModel.bipedHead.rotateAngleY;
				back.rotationPointX = 0.0F;
				back.rotationPointY = 0.0F;
				back.render(scale);
				
				left.rotateAngleX = playerModel.bipedHead.rotateAngleX;
				left.rotateAngleY = playerModel.bipedHead.rotateAngleY;
				left.rotationPointX = 0.0F;
				left.rotationPointY = 0.0F;
				left.render(scale);
				
				right.rotateAngleX = playerModel.bipedHead.rotateAngleX;
				right.rotateAngleY = playerModel.bipedHead.rotateAngleY;
				right.rotationPointX = 0.0F;
				right.rotationPointY = 0.0F;
				right.render(scale);
			}
		}
	}
}
