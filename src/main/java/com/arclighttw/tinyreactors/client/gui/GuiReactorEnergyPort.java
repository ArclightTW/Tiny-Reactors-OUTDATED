package com.arclighttw.tinyreactors.client.gui;

import java.io.IOException;
import java.util.Arrays;

import com.arclighttw.tinyreactors.client.gui.components.GuiButtonToggleable;
import com.arclighttw.tinyreactors.client.gui.components.TextureMapping;
import com.arclighttw.tinyreactors.container.ContainerReactorEnergyPort;
import com.arclighttw.tinyreactors.helpers.UIHelper;
import com.arclighttw.tinyreactors.main.Reference;
import com.arclighttw.tinyreactors.main.TinyReactors;
import com.arclighttw.tinyreactors.network.SMessageReactorEnergyPort;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorEnergyPort;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiReactorEnergyPort extends GuiContainerBase
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.ID, "textures/gui/reactor_energy_port.png");
	
	private final TileEntityReactorEnergyPort energyPort;
	
	private GuiButtonToggleable buttonInputDecrease, buttonInputIncrease;
	private GuiButtonToggleable buttonOutputDecrease, buttonOutputIncrease;
	
	public GuiReactorEnergyPort(EntityPlayer player, TileEntityReactorEnergyPort energyPort)
	{
		super(new ContainerReactorEnergyPort(player, energyPort));
		this.energyPort = energyPort;
		
		xSize = 177;
		ySize = 163;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		addButton(buttonInputDecrease = new GuiButtonToggleable(this, 0, guiLeft + 26, guiTop + 52, 12, 12, new TextureMapping[] {
			new TextureMapping(TEXTURE, 177, 56, 177, 78, 177, 67).setEnabledTooltip("button.tinyreactors.energyport.decrease").setActionListener((message) -> {
				// TODO: Pressing Shift .v. not?
				((SMessageReactorEnergyPort)message).setInput(-1);
			})
		}));
		
		addButton(buttonInputIncrease = new GuiButtonToggleable(this, 1, guiLeft + 46, guiTop + 52, 12, 12, new TextureMapping[] {
			new TextureMapping(TEXTURE, 189, 56, 189, 78, 189, 67).setEnabledTooltip("button.tinyreactors.energyport.increase").setActionListener((message) -> {
				// TODO: Pressing Shift .v. not?
				((SMessageReactorEnergyPort)message).setInput(1);
			})
		}));
		
		addButton(buttonOutputDecrease = new GuiButtonToggleable(this, 2, guiLeft + 118, guiTop + 52, 12, 12, new TextureMapping[] {
			new TextureMapping(TEXTURE, 177, 56, 177, 78, 177, 67).setEnabledTooltip("button.tinyreactors.energyport.decrease").setActionListener((message) -> {
				// TODO: Pressing Shift .v. not?
				((SMessageReactorEnergyPort)message).setOutput(-1);
			})
		}));
		
		addButton(buttonOutputIncrease = new GuiButtonToggleable(this, 3, guiLeft + 138, guiTop + 52, 12, 12, new TextureMapping[] {
			new TextureMapping(TEXTURE, 189, 56, 189, 78, 189, 67).setEnabledTooltip("button.tinyreactors.energyport.increase").setActionListener((message) -> {
				// TODO: Pressing Shift .v. not?
				((SMessageReactorEnergyPort)message).setOutput(1);
			})
		}));
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException
	{
		SMessageReactorEnergyPort message = new SMessageReactorEnergyPort(energyPort);
		((GuiButtonToggleable)button).actionPerformed(message);
		TinyReactors.instance.network.sendToServer(message);
	}
	
	@Override
	public void updateScreen()
	{
		buttonInputDecrease.setButton(energyPort.getEnergy().getCurrentReceive() > 0, 0);
		buttonInputIncrease.setButton(energyPort.getEnergy().getCurrentReceive() < energyPort.getEnergy().getMaxReceive(), 0);
		
		buttonOutputDecrease.setButton(energyPort.getEnergy().getCurrentExtract() > 0, 0);
		buttonOutputIncrease.setButton(energyPort.getEnergy().getCurrentExtract() < energyPort.getEnergy().getMaxExtract(), 0);
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
		drawCenteredString(UIHelper.smallFontRenderer, "Max Reactor Drain:", 42, 26, 0xFFFFFF);
		drawCenteredString(UIHelper.smallFontRenderer, energyPort.getEnergy().getCurrentReceive() + " RF/t", 42, 34, 0xFFFFFF);
		
		drawCenteredString(UIHelper.smallFontRenderer, "Max Output:", 134, 26, 0xFFFFFF);
		drawCenteredString(UIHelper.smallFontRenderer, energyPort.getEnergy().getCurrentExtract() + " RF/t", 134, 34, 0xFFFFFF);
		
		for(GuiButton button : buttonList)
		{
			if(button.isMouseOver())
				button.drawButtonForegroundLayer(mouseX - guiLeft, mouseY - guiTop);
		}
		
		if(mouseX >= guiLeft + 80 && mouseX <= guiLeft + 84 + 12 && mouseY >= guiTop + 8 && mouseY <= guiTop + 12 + 60)
			drawHoveringText(Arrays.asList(
					String.format("Energy: %,d RF", energyPort.getEnergy().getEnergyStored()),
					String.format("Capacity: %,d RF", energyPort.getEnergy().getMaxEnergyStored())
				), mouseX - guiLeft, mouseY - guiTop);
	}
}
