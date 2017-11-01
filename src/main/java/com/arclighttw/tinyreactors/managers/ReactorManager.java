package com.arclighttw.tinyreactors.managers;

import java.util.List;
import java.util.Map;

import com.arclighttw.tinyreactors.blocks.BlockReactorController;
import com.arclighttw.tinyreactors.blocks.BlockReactorEnergyPort;
import com.arclighttw.tinyreactors.config.TRConfig;
import com.arclighttw.tinyreactors.container.ContainerReactorController;
import com.arclighttw.tinyreactors.inits.TRBlocks;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorController;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class ReactorManager
{
	static List<BlockPos> REACTORS =  Lists.newArrayList();
	static Map<ResourceLocation, Reactant> REACTANTS = Maps.newHashMap();
	
	static int MAXIMUM_YIELD = -1;
	
	public static void populate()
	{
		REACTANTS = Maps.newHashMap();
		
		for(String reactant : TRConfig.REACTANT_REGISTRY)
		{
			String[] parts = reactant.split(":");
			
			if(parts.length < 3 || parts.length > 4)
			{
				System.out.println("Invalid Custom Reactor Rate: [" + reactant + "]");
				continue;
			}
			
			if(!Loader.isModLoaded(parts[0]))
				continue;
			
			ResourceLocation registryName = new ResourceLocation(parts[0], parts[1]);
			Block block = Block.REGISTRY.getObject(registryName);
			
			if(block == null)
			{
				System.out.println("Invalid Block in Custom Reactor Rate: [" + registryName.toString() + "]");
				continue;
			}
			
			int metadata = -1;
			int rate = 0;
			
			if(parts.length == 3)
				rate = Integer.parseInt(parts[2]);
			else if(parts.length == 4)
			{
				metadata = Integer.parseInt(parts[2]);
				rate = Integer.parseInt(parts[3]);
			}
			
			REACTANTS.put(block.getRegistryName(), new Reactant(metadata, rate));
			
			if(rate > MAXIMUM_YIELD)
				MAXIMUM_YIELD = rate;
		}
	}
	
	public static boolean isReactant(IBlockState state)
	{
		return getReactantRate(state) > 0;
	}
	
	public static boolean isValidRoof(Block block)
	{
		return block == TRBlocks.REACTOR_CASING ||
				block == TRBlocks.REACTOR_VENT;
	}
	
	public static boolean isValidCasing(Block block)
	{
		return block instanceof BlockReactorController ||
				block instanceof BlockReactorEnergyPort ||
				block == TRBlocks.REACTOR_CASING ||
				block == TRBlocks.REACTOR_GLASS ||
				block == TRBlocks.REACTOR_HEAT_SINK;
	}
	
	public static boolean isValidStructure(Block block)
	{
		return block instanceof BlockReactorController ||
				block instanceof BlockReactorEnergyPort ||
				block == TRBlocks.REACTOR_CASING ||
				block == TRBlocks.REACTOR_HEAT_SINK;
	}
	
	public static int getReactantRate(IBlockState state)
	{
		Block block = state.getBlock();
		
		if(!REACTANTS.containsKey(block.getRegistryName()))
			return 0;
		
		Reactant reactant = REACTANTS.get(block.getRegistryName());
		return reactant.metadata == -1 || block.getMetaFromState(state) == reactant.metadata ? reactant.rate : 0;
	}
	
	public static int getMaximumReactantRate()
	{
		return MAXIMUM_YIELD;
	}
	
	public static void addReactor(BlockPos pos)
	{
		REACTORS.add(pos);
	}
	
	public static void removeReactor(World world, BlockPos pos)
	{
		REACTORS.remove(pos);
		
		if(!world.isRemote)
		{
			for(EntityPlayer player : world.playerEntities)
			{
				if(player.openContainer != null && player.openContainer instanceof ContainerReactorController)
				{
					ContainerReactorController controller = (ContainerReactorController)player.openContainer;
					
					if(controller.getController().getPos().equals(pos))
						player.closeScreen();
				}
			}
		}
	}
	
	public static void validateAllReactors(World world)
	{
		List<BlockPos> invalid = Lists.newArrayList();
		
		for(BlockPos pos : REACTORS)
		{
			TileEntity tile = world.getTileEntity(pos);
			
			if(tile == null || !(tile instanceof TileEntityReactorController))
			{
				invalid.add(pos);
				continue;
			}
			
			((TileEntityReactorController)tile).getMultiblock().checkValidity(world, pos);
		}
		
		for(BlockPos pos : invalid)
			removeReactor(world, pos);
	}
	
	public static class Reactant
	{
		public int metadata;
		public int rate;
		
		public Reactant(int rate)
		{
			this(-1, rate);
		}
		
		public Reactant(int metadata, int rate)
		{
			this.metadata = metadata;
			this.rate = rate;
		}
	}
}
