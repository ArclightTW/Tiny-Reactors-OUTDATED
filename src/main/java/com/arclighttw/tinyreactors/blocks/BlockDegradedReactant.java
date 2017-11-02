package com.arclighttw.tinyreactors.blocks;

import java.util.List;

import com.arclighttw.tinyreactors.tiles.TileEntityDegradedReactant;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
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
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		TileEntity tile = world.getTileEntity(pos);
		
		if(tile == null || !(tile instanceof TileEntityDegradedReactant))
			return;
		
		TileEntityDegradedReactant reactant = (TileEntityDegradedReactant)tile;
		
		NBTTagCompound compound = stack.getTagCompound();
		
		if(compound == null)
			return;
		
		String registryName = compound.getString("registryName");
		Block block = Block.REGISTRY.getObject(new ResourceLocation(registryName));
		reactant.setRepresentedBlock(block);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		if(!world.isRemote)
		{
			TileEntity tile = world.getTileEntity(pos);
			
			if(tile == null || !(tile instanceof TileEntityDegradedReactant))
				return;
			
			TileEntityDegradedReactant reactant = (TileEntityDegradedReactant)tile;
			
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("registryName", reactant.getRepresentedBlock().getRegistryName().toString());
			
			ItemStack itemstack = new ItemStack(this);
			itemstack.setTagCompound(compound);
			
			EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), itemstack);
			world.spawnEntity(item);
		}
		
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		drops.clear();
	}
	
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		if(!stack.hasTagCompound())
			return;
		
		String registryName = stack.getTagCompound().getString("registryName");
		Block block = Block.REGISTRY.getObject(new ResourceLocation(registryName));
		
		if(block == null)
		{
			tooltip.add("Unrecognised Reactant; this has likely been saved wrong!");
			return;
		}
		
		tooltip.add("Reactant: " + block.getLocalizedName());
		tooltip.add("Degradation: 100 %");
	}
}
