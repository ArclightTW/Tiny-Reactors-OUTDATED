package com.arclighttw.tinyreactors.helpers;

import com.arclighttw.tinyreactors.lib.nbt.INBTSerializable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class NBTHelper
{
	public static void writeToNBT(NBTTagCompound compound, String key, INBTSerializable serializable)
	{
		NBTTagCompound localCompound = new NBTTagCompound();
		serializable.serialize(localCompound);
		
		compound.setTag(key, localCompound);
	}
	
	public static void readFromNBT(NBTTagCompound compound, String key, INBTSerializable serializable)
	{
		NBTTagCompound localCompound = compound.getCompoundTag(key);
		serializable.deserialize(localCompound);
	}
	
	public static class Serializer
	{
		public static void blockPos(NBTTagCompound compound, String key, BlockPos pos)
		{
			if(pos == null)
				compound.setString(key + "Missing", "");
			else
			{
				compound.setInteger(key + "X", pos.getX());
				compound.setInteger(key + "Y", pos.getY());
				compound.setInteger(key + "Z", pos.getZ());
			}
		}		
	}
	
	public static class Deserializer
	{
		public static BlockPos blockPos(NBTTagCompound compound, String key)
		{
			if(compound.hasKey(key + "Missing"))
				return null;
			
			return new BlockPos(compound.getInteger(key + "X"), compound.getInteger(key + "Y"), compound.getInteger(key + "Z"));
		}
	}
}
