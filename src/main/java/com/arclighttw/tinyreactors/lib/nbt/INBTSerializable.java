package com.arclighttw.tinyreactors.lib.nbt;

import net.minecraft.nbt.NBTTagCompound;

public interface INBTSerializable
{
	default void serialize(NBTTagCompound compound) {
	}
	
	default void deserialize(NBTTagCompound compound) {
	}
}
