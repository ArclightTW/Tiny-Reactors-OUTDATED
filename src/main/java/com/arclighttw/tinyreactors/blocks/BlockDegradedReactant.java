package com.arclighttw.tinyreactors.blocks;

import com.arclighttw.tinyreactors.tiles.TileEntityDegradedReactant;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDegradedReactant extends BlockTiny
{
	public BlockDegradedReactant()
	{
		super(Material.ROCK, "degraded_reactant");
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityDegradedReactant();
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		drops.clear();
		
		TileEntity tile = world.getTileEntity(pos);
		
		if(tile == null || !(tile instanceof TileEntityDegradedReactant))
			return;
		
		TileEntityDegradedReactant reactant = (TileEntityDegradedReactant)tile;
		
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("registryName", reactant.getRepresentedBlock().getRegistryName().toString());
		
		ItemStack itemstack = new ItemStack(this);
		itemstack.setTagCompound(compound);
		
		drops.add(itemstack);
	}
}
