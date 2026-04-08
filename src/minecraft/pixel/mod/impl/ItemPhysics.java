package pixel.mod.impl;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import pixel.mod.Mod;
import pixel.mod.option.ModOption.InGuiSettings;
import pixel.mod.option.type.ModOptionInt;

public class ItemPhysics extends Mod {
	public ItemPhysics() {
		super(false);
		
		loadOptions(
				new ModOptionInt("rotationSpeed", 2, 1, 10, new InGuiSettings("Rotation Speed"))
				);
	}
	
	public int setupItemTransform(ItemStack itemStack, EntityItem itemIn, double x, double y, double z, float partialTicks, IBakedModel model) {
		boolean isModel3d = model.isGui3d();
		int itemRenderCount = getItemRenderCount(itemStack);
		float offset = -0.125F;
		
		if (!isModel3d) {
			offset = -0.175F;
		}
		
		float scaleY = model.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
		
		GlStateManager.translate((float) x, (float) y + offset + 0.25F * scaleY, (float) z);
		
		if (!isModel3d) {
			float offsetX = -0.0F * (float) (itemRenderCount - 1) * 0.5F;
			float offsetY = -0.0F * (float) (itemRenderCount - 1) * 0.5F;
			float offsetZ = -0.046875F * (float) (itemRenderCount - 1) * 0.5F;
			
			if (itemIn.onGround) {
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 1.0F);
			}
		}
		
		if (!itemIn.onGround) {
			float rotationAmount = ((itemIn.getAge() + partialTicks) * (castOptionValueIntoInt("rotationSpeed") * 10.0F)) % 360.0F;
			
			GlStateManager.rotate(rotationAmount, 1.0F, 0.0F, 1.0F);
		}
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		return itemRenderCount;
	}
	
	private int getItemRenderCount(ItemStack stack) {
		int i = 1;
		
		if (stack.stackSize > 48) {
			i = 5;
		} else if (stack.stackSize > 32) {
			i = 4;
		} else if (stack.stackSize > 16) {
			i = 3;
		} else if (stack.stackSize > 1) {
			i = 2;
		}
		
		return i;
	}
}
