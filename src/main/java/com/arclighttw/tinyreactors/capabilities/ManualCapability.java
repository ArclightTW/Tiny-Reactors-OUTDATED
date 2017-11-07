package com.arclighttw.tinyreactors.capabilities;

import com.arclighttw.tinyreactors.inits.TRItems;
import com.arclighttw.tinyreactors.main.Reference;
import com.arclighttw.tinyreactors.managers.InventoryManager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class ManualCapability implements IManualCapability
{
	public static final ResourceLocation KEY = new ResourceLocation(Reference.ID, "manual_capability");
	
	@CapabilityInject(IManualCapability.class)
	public static final Capability<IManualCapability> CAPABILITY = null;
	
	private boolean given;
	
	@Override
	public boolean bookGiven()
	{
		return given;
	}
	
	@Override
	public void giveBook(EntityPlayer player)
	{
		ItemStack itemstack = new ItemStack(TRItems.TINY_MANUAL);
		
		if(!InventoryManager.addItemStackToInventory(player.inventory, itemstack))
			InventoryHelper.spawnItemStack(player.world, (int)player.posX, (int)player.posY, (int)player.posZ, itemstack);
		
		given = true;
	}
	
	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setBoolean("given", given);
		return compound;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound compound)
	{
		given = compound.getBoolean("given");
	}

	@Override
	public void set(IManualCapability oldVersion)
	{
		given = oldVersion.bookGiven();
	}
}
