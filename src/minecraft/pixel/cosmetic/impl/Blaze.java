package pixel.cosmetic.impl;

import java.awt.Color;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import pixel.cosmetic.Cosmetic;
import pixel.cosmetic.CosmeticBase;
import pixel.cosmetic.CosmeticHandler;
import pixel.cosmetic.CosmeticModelBase;
import pixel.cosmetic.option.CosmeticOption.InGuiSettings;
import pixel.cosmetic.option.type.CosmeticOptionColor;
import pixel.util.ColorManager;

public class Blaze extends Cosmetic {
	public Blaze() {
		super(CosmeticBlaze.class);
		
		loadOptions(
				new CosmeticOptionColor("color", ColorManager.YELLOW.getARGB(), false, 3000, new InGuiSettings("Color"))
				);
	}
	
	public static class CosmeticBlaze extends CosmeticBase {
		private final ModelBlaze modelBlaze;
		private final ResourceLocation textureBlaze = new ResourceLocation("pixel/cosmetics/blaze.png");
		
		public CosmeticBlaze(RenderPlayer renderPlayer) {
			super(renderPlayer);
			
			modelBlaze = new ModelBlaze(renderPlayer);
		}
		
		@Override
		public void render(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float headYaw, float headPitch, float scale) {
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			
			Cosmetic cosmetic = CosmeticHandler.get(Blaze.class);
			ColorManager color = new ColorManager(cosmetic.getOptionColor("color").getARGB());
			
			if (cosmetic.getOptionColor("color").isRainbowEnabled()) {
				int speed = cosmetic.getOptionColor("color").getRainbowSpeed();
				float hue = (System.currentTimeMillis() % speed) / (float) speed;
				
				color = new ColorManager(Color.getHSBColor(hue, 1.0F, 1.0F).getRGB());
			}
			
			GlStateManager.color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
			
			renderPlayer.bindTexture(textureBlaze);
			modelBlaze.setModelAttributes(renderPlayer.getMainModel());
			modelBlaze.render(player, limbSwing, limbSwingAmount, ageInTicks, headPitch, headPitch, scale);
			
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
		
		public class ModelBlaze extends CosmeticModelBase {
			private ModelRenderer[] blazeSticks = new ModelRenderer[12];
			
			public ModelBlaze(RenderPlayer player) {
				super(player);
				
				for (int i = 0; i < blazeSticks.length; i++) {
					blazeSticks[i] = new ModelRenderer(this, 0, 16);
					blazeSticks[i].addBox(0.0F, 0.0F, 0.0F, 2, 8, 2);
				}
			}
			
			@Override
			public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale) {
				setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale, entityIn);
				
				for (ModelRenderer modelRenderer : blazeSticks) {
					modelRenderer.render(scale);
				}
			}
			
			@Override
			public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale, Entity entityIn) {
				float angle = ageInTicks * (float) Math.PI * -0.1F;
				
				for (int i = 0; i < 4; i++) {
					blazeSticks[i].rotationPointY = -2.0F + MathHelper.cos(((float) (i * 2) + ageInTicks) * 0.25F);
					blazeSticks[i].rotationPointX = MathHelper.cos(angle) * 9.0F;
					blazeSticks[i].rotationPointZ = MathHelper.sin(angle) * 9.0F;
					
					angle++;
				}
				
				angle = ((float) Math.PI / 4F) + ageInTicks * (float) Math.PI * 0.03F;
				
				for (int i = 4; i < 8; i++) {
					blazeSticks[i].rotationPointY = 2.0F + MathHelper.cos(((float) (i * 2) + ageInTicks) * 0.25F);
					blazeSticks[i].rotationPointX = MathHelper.cos(angle) * 7.0F;
					blazeSticks[i].rotationPointZ = MathHelper.sin(angle) * 7.0F;
					
					angle++;
				}
				
				angle = 0.47123894F + ageInTicks * (float) Math.PI * -0.05F;
				
				for (int i = 8; i < 12; i++) {
					blazeSticks[i].rotationPointY = 11.0F + MathHelper.cos(((float) i * 1.5F + ageInTicks) * 0.5F);
					blazeSticks[i].rotationPointX = MathHelper.cos(angle) * 5.0F;
					blazeSticks[i].rotationPointZ = MathHelper.sin(angle) * 5.0F;
					
					angle++;
				}
			}
		}
	}
}
