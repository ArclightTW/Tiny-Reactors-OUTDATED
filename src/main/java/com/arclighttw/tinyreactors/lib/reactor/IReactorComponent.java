package com.arclighttw.tinyreactors.lib.reactor;

import com.arclighttw.tinyreactors.managers.ReactorManager;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;

public interface IReactorComponent
{
	default void onReactorComponentPlaced(World world, BlockSnapshot snapshot) {
		ReactorManager.validateAll(world, snapshot.getPos(), snapshot.getCurrentBlock(), null);
	}
	
	default void onReactorComponentRemoved(World world, BlockPos pos) {
		ReactorManager.validateAll(world, null, null, pos);
	}
}
