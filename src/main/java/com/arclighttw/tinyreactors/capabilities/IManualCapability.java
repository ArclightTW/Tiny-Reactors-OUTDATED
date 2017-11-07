package com.arclighttw.tinyreactors.capabilities;

import net.minecraft.entity.player.EntityPlayer;

public interface IManualCapability extends ICapabilityPersistable<IManualCapability>
{
	boolean hasReceivedBook();
	
	void setPageNumber(int page);
	int getPageNumber();
	
	void giveBook(EntityPlayer player);
}
