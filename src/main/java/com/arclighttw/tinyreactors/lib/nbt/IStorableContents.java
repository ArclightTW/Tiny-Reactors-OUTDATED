package com.arclighttw.tinyreactors.lib.nbt;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IStorableContents
{
	NBTTagCompound saveContents(World world, BlockPos pos, IBlockState state);
	void loadContents(World world, BlockPos pos, IBlockState state);
}
