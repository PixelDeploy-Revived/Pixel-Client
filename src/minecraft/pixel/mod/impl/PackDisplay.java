package pixel.mod.impl;

import java.util.ArrayList;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.ResourceLocation;
import pixel.gui.Gui;
import pixel.gui.hud.ScreenPosition;
import pixel.mod.ModDraggable;
import pixel.mod.option.ModOption;
import pixel.mod.option.ModOption.InGuiSettings;
import pixel.mod.option.ModOptionParent;
import pixel.mod.option.type.ModOptionColor;
import pixel.util.ColorManager;

public class PackDisplay extends ModDraggable {
	private ArrayList<Pack> selectedPacks = new ArrayList<Pack>();
	private Pack defaultPack = new Pack(new ResourceLocation("pixel/pack.png"), "Default", "The default look of Minecraft");
	
	public PackDisplay() {
		super(false);
				
		loadOptions(
				new ModOptionColor("nameTextColor", ColorManager.WHITE.getARGB(), false, new ModOptionColor.InGuiSettings("Name Text Color", false, true)),
				new ModOption("nameTextShadow", true, new InGuiSettings("Name Text Shadow")),
				new ModOption("showDescription", false, new InGuiSettings("Show Description")),
				new ModOptionColor(new ModOptionParent("showDescription"), "descriptionTextColor", ColorManager.GRAY.getARGB(), false, new ModOptionColor.InGuiSettings("Description Text Color", false, true)),
				new ModOption(new ModOptionParent("showDescription"), "descriptionTextShadow", true, new InGuiSettings("Description Text Shadow")),
				new ModOptionColor("backgroundColor", ColorManager.BLACK_66.getARGB(), false, new ModOptionColor.InGuiSettings("Background Color", true, false)),
				new ModOption("showIcon", true, new InGuiSettings("Show Icon")),
				new ModOption("showAllSelectedPacks", false, new InGuiSettings("Show All Selected Packs"))
				);
	}
	
	@Override
	public int getWidth() {
		int width = 8;
		
		if (castOptionValueIntoBoolean("showIcon")) {
			width += 28;
		}
		
		width += font.getStringWidth((castOptionValueIntoBoolean("showDescription") ? defaultPack.getDescription() : defaultPack.getName()));
		
		return width;
	}

	@Override
	public int getHeight() {
		return 28;
	}

	@Override
	public void render(ScreenPosition pos) {
		selectedPacks.clear();
		
		if (castOptionValueIntoBoolean("showAllSelectedPacks")) {
			for (ResourcePackRepository.Entry selectedPack : mc.getResourcePackRepository().getRepositoryEntries()) {
				selectedPacks.add(new Pack(selectedPack.getResourceLocation(mc.getTextureManager()), selectedPack.getResourcePackName(), selectedPack.getTexturePackDescription()));
			}
		} else {
			ResourcePackRepository.Entry selectedPack = mc.getResourcePackRepository().getRepositoryEntries().get(mc.getResourcePackRepository().getRepositoryEntries().size() - 1);
			
			selectedPacks.add(new Pack(selectedPack.getResourceLocation(mc.getTextureManager()), selectedPack.getResourcePackName(), selectedPack.getTexturePackDescription()));
		}
		
		if (!selectedPacks.isEmpty()) {
			int offsetY = 0;
			
			for (int i = 0; i < selectedPacks.size(); i++) {				
				drawPack(selectedPacks.get(i), offsetY);
				
				offsetY += 28;
			}
		}
	}
	
	@Override
	public void renderDummy(ScreenPosition pos) {
		selectedPacks.clear();
		selectedPacks.add(defaultPack);
		
		drawPack(selectedPacks.get(0), 0);
	}
	
	private void drawPack(Pack pack, int offsetY) {
		int maxWidth = font.getStringWidth(getLongestPackText()) + (castOptionValueIntoBoolean("showIcon") ? 28 : 0) + 8;
		int absoluteX;
		
		if (pos.getRelativeX() < 1.0D / 3.0D) {
			absoluteX = pos.getAbsoluteX();
		} else if (pos.getRelativeX() > 2.0D / 3.0D) {
			absoluteX = pos.getAbsoluteX() + getWidth() - maxWidth;
		} else {
			absoluteX = pos.getAbsoluteX() + getWidth() / 2 - maxWidth / 2;
		}
		
		int absoluteY = offsetY;
		int totalHeight = selectedPacks.size() * 28;
		
		if (pos.getRelativeY() < 1.0D / 3.0D) {
			absoluteY += pos.getAbsoluteY();
		} else if (pos.getRelativeY() > 2.0D / 3.0D) {
			absoluteY += pos.getAbsoluteY() + getHeight() - totalHeight;
		} else {
			absoluteY += pos.getAbsoluteY() + getHeight() / 2 - totalHeight / 2;
		}
		
		Gui.drawRect(absoluteX + (castOptionValueIntoBoolean("showIcon") ? 28 : 0), absoluteY, absoluteX + maxWidth, absoluteY + 28, getOptionColor("backgroundColor").getARGB());
		
		if (castOptionValueIntoBoolean("showIcon")) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			
			mc.getTextureManager().bindTexture(pack.getResourceLocation());
			
			net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(absoluteX, absoluteY, 0.0F, 0.0F, 28, 28, 28, 28);
		}

		int packX = absoluteX + 4 + (castOptionValueIntoBoolean("showIcon") ? 28 : 0);
		int packNameY = absoluteY + (castOptionValueIntoBoolean("showDescription") ? 4 : 28 / 2 - 4);
		
		String packName = pack.getName().replace("\u00A7r", "");
		
		drawText(packName, packX, packNameY, getOptionColor("nameTextColor").getARGB(), castOptionValueIntoBoolean("nameTextShadow"), !packName.contains("\u00A7") && getOptionColor("nameTextColor").isChromaEnabled());
		
		if (castOptionValueIntoBoolean("showDescription")) {
			String packDescription = pack.getDescription().replace("\u00A7r", "");
			
			drawText(packDescription, packX, absoluteY + 28 - font.FONT_HEIGHT - 4, getOptionColor("descriptionTextColor").getARGB(), castOptionValueIntoBoolean("descriptionTextShadow"), !packDescription.contains("\u00A7") && getOptionColor("descriptionTextColor").isChromaEnabled());
		}
	}
	
	private String getLongestPackText() {
		String longestName = getLongestSelectedPackName();
		String longestDescription = getLongestSelectedPackDescription();
		
		String longest = longestName;
		
		if (castOptionValueIntoBoolean("showDescription")) {
			if (font.getStringWidth(longestDescription) > font.getStringWidth(longest)) {
				longest = longestDescription;
			}
		}
		
		return longest;
	}
	
	private String getLongestSelectedPackName() {		
		String longest = "";
		
		for (Pack selectedPack : selectedPacks) {
			String name = selectedPack.getName();
			
			if (font.getStringWidth(name) > font.getStringWidth(longest)) {
				longest = name;
			}
		}
		
		return longest;
	}
	
	private String getLongestSelectedPackDescription() {		
		String longest = "";
		
		for (Pack selectedPack : selectedPacks) {
			String name = selectedPack.getDescription();
			
			if (font.getStringWidth(name) > font.getStringWidth(longest)) {
				longest = name;
			}
		}
		
		return longest;
	}
	
	private static class Pack {
		private final ResourceLocation resourceLocation;
		private final String name;
		private final String description;
		
		public Pack(ResourceLocation resourceLocation, String name, String description) {
			this.resourceLocation = resourceLocation;
			this.name = name;
			this.description = description;
		}
		
		public ResourceLocation getResourceLocation() {
			return resourceLocation;
		}
		
		public String getName() {
			return name;
		}
		
		public String getDescription() {
			return description;
		}
	}
}