package com.arclighttw.tinyreactors.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;

public class CapabilityHelper
{
	public static <T extends INBTSerializable<NBTTagCompound>> void register(Class<T> type, final Class<? extends T> implementation)
	{
		CapabilityManager.INSTANCE.register(type, new CapabilityHelper.Storage<T>(), implementation);
	}
	
	public static class Storage<T extends INBTSerializable<NBTTagCompound>> implements Capability.IStorage<T>
	{
		@Override
		public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt)
		{
			instance.deserializeNBT((NBTTagCompound)nbt);
		}
	}
	
	public static class Provider<T> implements ICapabilitySerializable<NBTBase>
	{
		private Capability<T> capability;
		private T instance;
		
		public Provider(Capability<T> capability)
		{
			this.capability = capability;
			this.instance = capability.getDefaultInstance();
		}
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing)
		{
			return capability == this.capability;
		}
		
		@Override
		public <C> C getCapability(Capability<C> capability, EnumFacing facing)
		{
			return capability == this.capability ? this.capability.cast(instance) : null;
		}
		
		@Override
		public NBTBase serializeNBT()
		{
			return this.capability.getStorage().writeNBT(this.capability, instance, null);
		}
		
		@Override
		public void deserializeNBT(NBTBase nbt)
		{
			this.capability.getStorage().readNBT(this.capability, instance, null, nbt);
		}
	}
}
