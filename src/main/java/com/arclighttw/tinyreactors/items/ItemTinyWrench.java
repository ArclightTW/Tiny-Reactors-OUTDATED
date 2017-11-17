package com.arclighttw.tinyreactors.items;

import com.arclighttw.tinyreactors.blocks.BlockTiny;
import com.arclighttw.tinyreactors.config.TRConfig;
import com.arclighttw.tinyreactors.inits.TRSounds;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTinyWrench extends ItemTiny
{
	public ItemTinyWrench()
	{
		super("tiny_wrench");
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		if(player.isSneaking())
		{
			Block block = world.getBlockState(pos).getBlock();
			
			if(block instanceof BlockTiny)
			{
				ItemStack itemstack = new ItemStack(block);
				
				if(world.isRemote)
					world.playSound(player, pos, TRSounds.TINY_WRENCH, SoundCategory.BLOCKS, 1.0F * TRConfig.AUDIO_TINY_WRENCH, 1.0F);
				else
				{
					EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), itemstack);
					world.spawnEntity(item);
				
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
				}
				
				return EnumActionResult.SUCCESS;
			}
		}
		
		return EnumActionResult.FAIL;
	}
}
