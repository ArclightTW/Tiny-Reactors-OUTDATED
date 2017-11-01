package com.arclighttw.tinyreactors.blocks;

import java.util.List;

import com.arclighttw.tinyreactors.managers.GuiManager;
import com.arclighttw.tinyreactors.properties.EnumEnergyPortTier;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorEnergyPort;

import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockReactorEnergyPort extends BlockReactorComponentDirectional
{
	private EnumEnergyPortTier tier;
	
	public BlockReactorEnergyPort(EnumEnergyPortTier tier)
	{
		super(Material.IRON, "reactor_energy_port_" + tier.getName());
		this.tier = tier;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityReactorEnergyPort(tier);
	}
	
	@Override
	public int getInterfaceId()
	{
		return GuiManager.REACTOR_ENERGY_PORT;
	}
	
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		tooltip.add(String.format("Tier: %s", tier.getName()));
	}
	
	public EnumEnergyPortTier getTier()
	{
		return tier;
	}
}
