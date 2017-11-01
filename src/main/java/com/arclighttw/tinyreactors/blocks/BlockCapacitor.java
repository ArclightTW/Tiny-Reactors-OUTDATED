package com.arclighttw.tinyreactors.blocks;

import java.util.List;

import com.arclighttw.tinyreactors.properties.EnumCapacitorTier;
import com.arclighttw.tinyreactors.tiles.TileEntityCapacitor;

import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCapacitor extends BlockReactorComponentDirectional
{
	private EnumCapacitorTier tier;
	
	public BlockCapacitor(EnumCapacitorTier tier)
	{
		super(Material.IRON, "capacitor_" + tier.getName());
		this.tier = tier;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityCapacitor(tier);
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		TileEntity tile = world.getTileEntity(pos);
		
		if(tile != null && tile instanceof TileEntityCapacitor)
			((TileEntityCapacitor)tile).calculateNeighbors();
	}
	
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		tooltip.add(String.format("Tier: %s", tier.getName()));
	}
	
	public EnumCapacitorTier getTier()
	{
		return tier;
	}
}
