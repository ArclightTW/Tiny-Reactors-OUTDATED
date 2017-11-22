package com.arclighttw.tinyreactors.items;

import com.arclighttw.tinyreactors.blocks.BlockTiny;
import com.arclighttw.tinyreactors.lib.nbt.IStorableContents;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTinyWrench extends ItemTiny
{
	public ItemTinyWrench(String name)
	{
		super(name);
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		if(!world.isRemote && player.isSneaking())
		{
			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			
			if(block instanceof BlockTiny)
			{
				ItemStack itemstack = new ItemStack(block);
				
				if(block instanceof IStorableContents)
				{
					NBTTagCompound compound = ((IStorableContents)block).saveContents(world, pos, state);
					itemstack.setTagCompound(compound);
				}
				
				InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemstack);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
		}
		
		return EnumActionResult.SUCCESS;
	}
}
