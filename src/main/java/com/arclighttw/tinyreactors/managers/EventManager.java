package com.arclighttw.tinyreactors.managers;

import com.arclighttw.tinyreactors.capabilities.CapabilityHelper;
import com.arclighttw.tinyreactors.capabilities.IManualCapability;
import com.arclighttw.tinyreactors.capabilities.ManualCapability;
import com.arclighttw.tinyreactors.config.TRConfig;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class EventManager
{
	@SubscribeEvent
	public void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event)
	{
		if(!(event.getObject() instanceof EntityPlayer))
			return;
		
		event.addCapability(ManualCapability.KEY, new CapabilityHelper.Provider<IManualCapability>(ManualCapability.CAPABILITY));
	}
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event)
	{
		if(!TRConfig.MANUAL_ON_SPAWN)
			return;

		IManualCapability capability = event.player.getCapability(ManualCapability.CAPABILITY, null);
		
		if(capability == null)
			return;
		
		if(capability.bookGiven())
			return;
		
		capability.giveBook(event.player);
	}
	
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event)
	{
		IManualCapability oldManual = event.getOriginal().getCapability(ManualCapability.CAPABILITY, null);
		IManualCapability manual = event.getEntityPlayer().getCapability(ManualCapability.CAPABILITY, null);
		
		manual.set(oldManual);
	}
}
