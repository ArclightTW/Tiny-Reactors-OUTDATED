package com.arclighttw.tinyreactors.items;

import com.arclighttw.tinyreactors.blocks.BlockTiny;
import com.arclighttw.tinyreactors.capabilities.IManualCapability;
import com.arclighttw.tinyreactors.capabilities.ManualCapability;
import com.arclighttw.tinyreactors.config.TRConfig;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemTinyBlock extends ItemBlock
{
	public ItemTinyBlock(BlockTiny block)
	{
		super(block);
	}
	
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player)
	{
		if(!TRConfig.MANUAL_ON_CRAFT)
			return;
		
		IManualCapability capability = player.getCapability(ManualCapability.CAPABILITY, null);
		
		if(capability == null)
			return;
		
		if(capability.hasReceivedBook())
			return;
		
		capability.giveBook(player);
	}
}
