package com.arclighttw.tinyreactors.client.gui;

import java.io.IOException;
import java.util.Arrays;

import com.arclighttw.tinyreactors.client.gui.components.GuiButtonDrawable;
import com.arclighttw.tinyreactors.container.ContainerReactorEnergyPort;
import com.arclighttw.tinyreactors.main.Reference;
import com.arclighttw.tinyreactors.main.TinyReactors;
import com.arclighttw.tinyreactors.network.SMessageReactorEnergyPort;
import com.arclighttw.tinyreactors.network.SMessageReactorEnergyPort.Mode;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorEnergyPort;
import com.arclighttw.tinyreactors.util.Util;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class GuiReactorEnergyPort extends GuiContainerMulti
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.ID, "textures/gui/reactor_energy_port.png");
	
	private final TileEntityReactorEnergyPort energyPort;
	
	public GuiReactorEnergyPort(TileEntityReactorEnergyPort energyPort, InventoryPlayer player)
	{
		super(new ContainerReactorEnergyPort(energyPort, player));
		this.energyPort = energyPort;
		
		xSize = 177;
		ySize = 163;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		addButton(new GuiButtonDrawable(0, guiLeft + 26, guiTop + 52, TEXTURE, 177, 56, 12, 12).setHoverTextureLocation(177, 67).setDisabledTextureLocation(177, 78).setLabel("Decrease Input by 100/1").setShiftLabel("Decrease Input by 1000/50"));
		addButton(new GuiButtonDrawable(1, guiLeft + 46, guiTop + 52, TEXTURE, 189, 56, 12, 12).setHoverTextureLocation(189, 67).setDisabledTextureLocation(189, 78).setLabel("Increase Input by 100/1").setShiftLabel("Increase Input by 1000/50"));
		
		addButton(new GuiButtonDrawable(2, guiLeft + 118, guiTop + 52, TEXTURE, 177, 56, 12, 12).setHoverTextureLocation(177, 67).setDisabledTextureLocation(177, 78).setLabel("Decrease Output by 100/1").setShiftLabel("Decrease Output by 1000/50"));
		addButton(new GuiButtonDrawable(3, guiLeft + 138, guiTop + 52, TEXTURE, 189, 56, 12, 12).setHoverTextureLocation(189, 67).setDisabledTextureLocation(189, 78).setLabel("Increase Output by 100/1").setShiftLabel("Increase Output by 1000/50"));
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException
	{
		if(!(button instanceof GuiButtonDrawable))
			return;
		
		GuiButtonDrawable guiButton = (GuiButtonDrawable)button;
		Mode mode = Mode.DecreaseInput;
		
		switch(guiButton.id)
		{
		case 1:
			mode = Mode.IncreaseInput;
			break;
		case 2:
			mode = Mode.DecreaseOutput;
			break;
		case 3:
			mode = Mode.IncreaseOutput;
			break;
		}
		
		TinyReactors.network.sendToServer(new SMessageReactorEnergyPort(mode, guiButton.mouseButton == 0 ? (isShiftKeyDown() ? 1000 : 100) : (isShiftKeyDown() ? 50 : 1), energyPort.getPos()));
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		drawDefaultBackground();
		
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		int progress = Util.getEnergyFilledScaled(energyPort, 56);
		drawTexturedModalRect(guiLeft + 84, guiTop + 68 - progress, 177, 56 - progress, 8, progress);
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{	
		drawCenteredString(fontRenderer, "Max Input:", 42, 26, 0xB8B8B8);
		drawCenteredString(fontRenderer, String.format("%,d RF/t", energyPort.getEnergyStorage().getCurrentReceive()), 42, 38, 0xB8B8B8);
		
		drawCenteredString(fontRenderer, "Max Output:", 134, 26, 0xB8B8B8);
		drawCenteredString(fontRenderer, String.format("%,d RF/t", energyPort.getEnergyStorage().getCurrentExtract()), 134, 38, 0xB8B8B8);
		
		if(mouseX >= guiLeft + 80 && mouseX <= guiLeft + 84 + 12 && mouseY >= guiTop + 8 && mouseY <= guiTop + 12 + 60)
			drawHoveringText(Arrays.asList(
					String.format("Energy: %,d RF", energyPort.getEnergyStored(EnumFacing.NORTH)),
					String.format("Capacity: %,d RF", energyPort.getMaxEnergyStored(EnumFacing.NORTH))
				), mouseX - guiLeft, mouseY - guiTop);
		
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
}
