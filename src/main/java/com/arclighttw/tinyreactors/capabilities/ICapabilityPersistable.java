package com.arclighttw.tinyreactors.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICapabilityPersistable<T> extends INBTSerializable<NBTTagCompound>
{
	void set(T oldVersion);
}
