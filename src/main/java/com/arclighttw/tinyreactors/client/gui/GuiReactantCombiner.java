package com.arclighttw.tinyreactors.client.gui;

import java.util.Arrays;

import com.arclighttw.tinyreactors.container.ContainerReactantCombiner;
import com.arclighttw.tinyreactors.main.Reference;
import com.arclighttw.tinyreactors.tiles.TileEntityReactantCombiner;
import com.arclighttw.tinyreactors.util.Util;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiReactantCombiner extends GuiContainerMulti
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.ID, "textures/gui/reactant_combiner.png");
	private final TileEntityReactantCombiner combiner;
	
	public GuiReactantCombiner(TileEntityReactantCombiner combiner, InventoryPlayer player)
	{
		super(new ContainerReactantCombiner(combiner, player));
		this.combiner = combiner;
		
		xSize = 177;
		ySize = 163;
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		drawDefaultBackground();
		
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		int progress = (int)((combiner.getProcessTick() / (float)combiner.getProcessLength()) * 18);
		drawTexturedModalRect(guiLeft + 79, guiTop + 38, 177, 0, progress, 4);
		
		progress = Util.getEnergyFilledScaled(combiner, 56);
		drawTexturedModalRect(guiLeft + 12, guiTop + 70 - progress, 177, 60 - progress, 8, progress);
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		if(mouseX >= guiLeft + 8 && mouseX <= guiLeft + 12 + 12 && mouseY >= guiTop + 8 && mouseY <= guiTop + 12 + 60)
			drawHoveringText(Arrays.asList(
					String.format("Energy: %,d RF", combiner.getEnergyStored()),
					String.format("Capacity: %,d RF", combiner.getMaxEnergyStored())
				), mouseX - guiLeft, mouseY - guiTop);
		
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
}
