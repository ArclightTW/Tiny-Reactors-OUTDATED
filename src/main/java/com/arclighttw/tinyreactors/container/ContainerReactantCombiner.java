package com.arclighttw.tinyreactors.container;

import com.arclighttw.tinyreactors.inits.TRBlocks;
import com.arclighttw.tinyreactors.inits.TRItems;
import com.arclighttw.tinyreactors.tiles.TileEntityReactantCombiner;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerReactantCombiner extends ContainerBase
{
	private final TileEntityReactantCombiner combiner;
	
	public ContainerReactantCombiner(TileEntityReactantCombiner combiner, InventoryPlayer player)
	{
		super(combiner.getInventory(), player, 8, 81);
		this.combiner = combiner;
		
		addSlotToContainer(new Slot(combiner, 0, 44, 12) {
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return Block.getBlockFromItem(stack.getItem()) == TRBlocks.DEGRADED_REACTANT;
			}
		});
		addSlotToContainer(new Slot(combiner, 1, 116, 12) {
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return stack.getItem() == TRItems.RADIOACTIVE_INGOT;
			}
		});
		addSlotToContainer(new Slot(combiner, 2, 80, 50) {
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return false;
			}
		});
	}
	
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		if(slotId == 38)
		{
			if(!getSlot(slotId).getHasStack())
				return ItemStack.EMPTY;
			
			if(combiner.isProcessing())
				return ItemStack.EMPTY;
			
			if(!combiner.hasProcessed())
			{
				combiner.startProcessing();
				return ItemStack.EMPTY;
			}
			
			combiner.resetProcess();
		}
		else if(slotId >= 36)
		{
			if(combiner.isProcessing())
				return ItemStack.EMPTY;
		}
		
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return player.getDistanceSq(combiner.getPos()) <= 64.0;
	}
}
