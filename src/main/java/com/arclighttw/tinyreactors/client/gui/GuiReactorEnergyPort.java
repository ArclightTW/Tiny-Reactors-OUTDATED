package com.arclighttw.tinyreactors.client.gui;

import java.util.Arrays;

import com.arclighttw.tinyreactors.container.ContainerReactorEnergyPort;
import com.arclighttw.tinyreactors.main.Reference;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorEnergyPort;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiReactorEnergyPort extends GuiContainerBase
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.ID, "textures/gui/reactor_energy_port.png");
	
	private final TileEntityReactorEnergyPort energyPort;
	
	public GuiReactorEnergyPort(EntityPlayer player, TileEntityReactorEnergyPort energyPort)
	{
		super(new ContainerReactorEnergyPort(player, energyPort));
		this.energyPort = energyPort;
		
		xSize = 177;
		ySize = 163;
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		drawDefaultBackground();
		
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		int energy = (int)((energyPort.getEnergy().getEnergyStored() / (float)energyPort.getEnergy().getMaxEnergyStored()) * 56);
		drawTexturedModalRect(guiLeft + 84, guiTop + 68 - energy, 177, 56 - energy, 8, energy);
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		if(mouseX >= guiLeft + 80 && mouseX <= guiLeft + 84 + 12 && mouseY >= guiTop + 8 && mouseY <= guiTop + 12 + 60)
			drawHoveringText(Arrays.asList(
					String.format("Energy: %,d RF", energyPort.getEnergy().getEnergyStored()),
					String.format("Capacity: %,d RF", energyPort.getEnergy().getMaxEnergyStored())
				), mouseX - guiLeft, mouseY - guiTop);
	}
}
