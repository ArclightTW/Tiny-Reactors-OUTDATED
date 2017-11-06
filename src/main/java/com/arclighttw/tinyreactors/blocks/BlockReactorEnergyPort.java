package com.arclighttw.tinyreactors.blocks;

import java.util.List;

import com.arclighttw.tinyreactors.inits.Registry.ICraftedTrigger;
import com.arclighttw.tinyreactors.inits.TRBlocks;
import com.arclighttw.tinyreactors.main.TinyReactors;
import com.arclighttw.tinyreactors.managers.GuiManager;
import com.arclighttw.tinyreactors.network.SMessageReactorEnergyPort.Mode;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorEnergyPort;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockReactorEnergyPort extends BlockReactorComponentDirectional implements ICraftedTrigger
{
	public BlockReactorEnergyPort()
	{
		super(Material.ROCK, "reactor_energy_port");
		setCreativeTab(TinyReactors.tab);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityReactorEnergyPort();
	}
	
	@Override
	public int getInterfaceId()
	{
		return GuiManager.REACTOR_ENERGY_PORT;
	
	}
	@Override
	public void onCrafted(ItemStack result, IInventory craftMatrix)
	{
		if(Block.getBlockFromItem(result.getItem()) != TRBlocks.REACTOR_ENERGY_PORT)
			return;
		
		List<ItemStack> ingredients = Lists.newArrayList();
		
		for(int i = 0; i < craftMatrix.getSizeInventory(); i++)
		{
			ItemStack ingredient = craftMatrix.getStackInSlot(i);
			
			if(ingredient.isEmpty())
				continue;
			
			ingredients.add(ingredient);
		}
		
		NBTTagCompound compound = result.getTagCompound();
		
		if(compound == null)
			compound = new NBTTagCompound();
		
		compound.setInteger("limit", 256);
		compound.setInteger("capacity", 250000);
		
		if(ingredients.size() == 2)
		{
			int limit = 0;
			int capacity = 0;
			
			for(ItemStack ingredient : ingredients)
			{
				if(!ingredient.hasTagCompound())
					continue;
				
				limit += ingredient.getTagCompound().getInteger("limit");
				capacity += ingredient.getTagCompound().getInteger("capacity");
			}
			
			compound.setInteger("limit", limit);
			compound.setInteger("capacity", capacity);
		}
		
		result.setTagCompound(compound);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		TileEntity tile = world.getTileEntity(pos);
		
		if(tile == null || !(tile instanceof TileEntityReactorEnergyPort) || !stack.hasTagCompound())
			return;
		
		TileEntityReactorEnergyPort energy = (TileEntityReactorEnergyPort)tile;
		NBTTagCompound compound = stack.getTagCompound();
		
		int limit = compound.getInteger("limit");
		int capacity = compound.getInteger("capacity");
		
		energy.setLimit(limit);
		energy.setCapacity(capacity);

		energy.getEnergyStorage().modify(Mode.IncreaseInput, limit);
		energy.getEnergyStorage().modify(Mode.IncreaseOutput, limit);
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		if(!world.isRemote && !player.capabilities.isCreativeMode)
		{
			TileEntity tile = world.getTileEntity(pos);
			
			if(tile == null || !(tile instanceof TileEntityReactorEnergyPort))
				return;
			
			TileEntityReactorEnergyPort energy = (TileEntityReactorEnergyPort)tile;
			
			NBTTagCompound compound = new NBTTagCompound();
			compound.setInteger("limit", energy.getLimit());
			compound.setInteger("capacity", energy.getMaxEnergyStored());
			
			ItemStack itemstack = new ItemStack(this);
			itemstack.setTagCompound(compound);
			
			EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), itemstack);
			world.spawnEntity(item);
			world.removeTileEntity(pos);
		}
	}
	
	@Override
	public void getSubBlocks(CreativeTabs item, NonNullList<ItemStack> items)
	{
		ItemStack creative = new ItemStack(this);

		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("limit", 1024);
		compound.setInteger("capacity", 1000000);
		
		creative.setTagCompound(compound);
		items.add(creative);
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
		
		int limit = stack.getTagCompound().getInteger("limit");
		int capacity = stack.getTagCompound().getInteger("capacity");
		
		tooltip.add(String.format("Limit: %,d RF/t", limit));
		tooltip.add(String.format("Capacity: %,d RF", capacity));
	}
}
