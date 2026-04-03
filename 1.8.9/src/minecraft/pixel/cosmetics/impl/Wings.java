package pixel.cosmetics.impl;

import java.awt.Color;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
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
import pixel.cosmetics.option.type.CosmeticOptionScale;
import pixel.util.ColorManager;

public class Wings extends Cosmetic {
	public Wings() {
		super(CosmeticWings.class);
		
		loadOptions(
				new CosmeticOptionColor("color", ColorManager.WHITE.getARGB(), false, 3000, new InGuiSettings("Color")),
				new CosmeticOptionScale("scale", 100, 60, 140)
				);
	}
	
	public static class CosmeticWings extends CosmeticBase {
		private final ModelWings modelWings;
		private final ResourceLocation wingsTexture = new ResourceLocation("pixel/cosmetics/wings.png");
		
		public CosmeticWings(RenderPlayer renderPlayer) {
			super(renderPlayer);
			
			modelWings = new ModelWings(renderPlayer);
		}
		
		public void render(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			GlStateManager.pushMatrix();
			
			if (player.isSneaking()) {
				GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.translate(0.0D, 0.20D, -0.05D);
			}
			
			modelWings.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			
			GlStateManager.popMatrix();
		}
		
		public class ModelWings extends CosmeticModelBase {
			private ModelRenderer wing;
			private ModelRenderer wingTip;
			
			public ModelWings(RenderPlayer renderPlayer) {
				super(renderPlayer);
				
				setTextureOffset("wing.bone", 0, 0);
				setTextureOffset("wing.skin", -10, 8);
				setTextureOffset("wingTip.bone", 0, 5);
				setTextureOffset("wingTip.skin", -10, 18);
				
				wing = new ModelRenderer(this, "wing");
				wing.setTextureSize(30, 30);
				wing.setRotationPoint(-2.0F, 0.0F, 0.0F);
				wing.addBox("bone", -10.0F, -1.0F, -1.0F, 10, 2, 2);
				wing.addBox("skin", -10.0F, 0.0F, 0.5F, 10, 0, 10);
				
				wingTip = new ModelRenderer(this, "wingTip");
				wingTip.setTextureSize(30, 30);
				wingTip.setRotationPoint(-10.0F, 0.0F, 0.0F);
				wingTip.addBox("bone", -10.0F, -0.5F, -0.5F, 10, 1, 1);
				wingTip.addBox("skin", -10.0F, 0.0F, 0.5F, 10, 0, 10);
				
				wing.addChild(wingTip);
			}
			
			public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale) {
				GlStateManager.pushMatrix();
				GlStateManager.disableLighting();
				
				Cosmetic cosmetic = CosmeticHandler.get(Wings.class);
				float wingsScale = cosmetic.castOptionValueIntoInt("scale") / 100.0F;
				
				GlStateManager.scale(wingsScale, wingsScale, wingsScale);
				GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.translate(0.0D, 0.0D, 0.09D);
				GlStateManager.translate(0.0D, 0.2D, 0.0D);
				
				renderPlayer.bindTexture(wingsTexture);
				
				for (int j = 0; j < 2; j++) {
					ColorManager color = new ColorManager(cosmetic.getOptionColor("color").getARGB());
					
					if (cosmetic.getOptionColor("color").isRainbowEnabled()) {
						int speed = cosmetic.getOptionColor("color").getRainbowSpeed();
			        	float hue = (System.currentTimeMillis() % speed) / (float) speed;

			        	color = new ColorManager(Color.getHSBColor(hue, 1.0F, 1.0F).getRGB());
					}
					
					GlStateManager.color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
					
					float angle = (float) (System.currentTimeMillis() % 1000L) / 1000.0F * 3.1415927F * 2.0F;
					
					wing.rotateAngleX = (float) Math.toRadians(-80.0D) - (float) Math.cos(angle) * 0.4F;
					wing.rotateAngleY = (float) Math.toRadians(30.0D) + (float) Math.sin(angle) * 0.2F;
					wing.rotateAngleZ = (float) Math.toRadians(20.0F);
					wingTip.rotateAngleZ = -((float) (Math.sin((angle + 2.0F)) + 0.9D)) * 0.75F;
					
					wing.render(0.0625F);
					GlStateManager.scale(-1.0F, 1.0F, 1.0F);
				}
				
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.enableLighting();
				GlStateManager.popMatrix();
			}
		}
	}
}
