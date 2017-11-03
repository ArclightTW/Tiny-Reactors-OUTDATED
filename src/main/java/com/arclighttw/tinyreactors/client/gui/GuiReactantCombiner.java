package com.arclighttw.tinyreactors.client.gui;

import com.arclighttw.tinyreactors.container.ContainerReactantCombiner;
import com.arclighttw.tinyreactors.main.Reference;
import com.arclighttw.tinyreactors.tiles.TileEntityReactantCombiner;

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
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
}
