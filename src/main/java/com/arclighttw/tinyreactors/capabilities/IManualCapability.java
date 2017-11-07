package com.arclighttw.tinyreactors.capabilities;

import net.minecraft.entity.player.EntityPlayer;

public interface IManualCapability extends ICapabilityPersistable<IManualCapability>
{
	boolean bookGiven();
	void giveBook(EntityPlayer player);
}
