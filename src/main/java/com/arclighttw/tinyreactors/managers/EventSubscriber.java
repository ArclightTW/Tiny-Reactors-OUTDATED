package com.arclighttw.tinyreactors.managers;

import com.arclighttw.tinyreactors.lib.reactor.IReactorComponent;
import com.arclighttw.tinyreactors.main.Reference;

import net.minecraft.block.Block;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Reference.ID)
public class EventSubscriber
{
	@SubscribeEvent
	public static void onBlockPlaceEvent(BlockEvent.PlaceEvent event)
	{
		Block block = event.getBlockSnapshot().getCurrentBlock().getBlock();
		
		if(block instanceof IReactorComponent)
			((IReactorComponent)block).onReactorComponentPlaced(event.getWorld(), event.getBlockSnapshot());
	}
	
	@SubscribeEvent
	public static void onBlockBreakEvent(BlockEvent.BreakEvent event)
	{
		Block block = event.getState().getBlock();
		
		if(block instanceof IReactorComponent)
			((IReactorComponent)block).onReactorComponentRemoved(event.getWorld(), event.getPos());
	}
}
