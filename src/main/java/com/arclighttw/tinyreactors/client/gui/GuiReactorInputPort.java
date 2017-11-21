package com.arclighttw.tinyreactors.client.gui;

import com.arclighttw.tinyreactors.container.ContainerReactorInputPort;
import com.arclighttw.tinyreactors.main.Reference;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorInputPort;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiReactorInputPort extends GuiContainerMulti
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.ID, "textures/gui/reactor_input_port.png");
	
	public GuiReactorInputPort(TileEntityReactorInputPort input, InventoryPlayer player)
	{
		super(new ContainerReactorInputPort(input, player));
		
		xSize = 177;
		ySize = 163;
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		drawDefaultBackground();
		
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
}
