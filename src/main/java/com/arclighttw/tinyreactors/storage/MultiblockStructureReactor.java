package com.arclighttw.tinyreactors.storage;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiblockStructureReactor extends MultiblockStructure
{
	private List<Block> baseCorners;
	private List<Block> baseEdges;
	private List<Block> baseInteriors;
	
	private List<Block> roofCorners;
	private List<Block> roofEdges;
	private List<Block> roofInteriors;
	
	private List<Block> wallCorners;
	private List<Block> walls;
	
	private List<Block> internalBlocks;
	
	public MultiblockStructureReactor()
	{
		baseCorners = Lists.newArrayList();
		baseEdges = Lists.newArrayList();
		baseInteriors = Lists.newArrayList();
		
		roofCorners = Lists.newArrayList();
		roofEdges = Lists.newArrayList();
		roofInteriors = Lists.newArrayList();
		
		wallCorners = Lists.newArrayList();
		walls = Lists.newArrayList();
		
		internalBlocks = Lists.newArrayList();
	}
	
	@Override
	public void onPreCalculation(World world)
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onReactorValidated(World world)
	{
		setValid(true);
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onBlockDetected(World world, BlockPos pos, IBlockState state)
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onInternalBlockDetected(World world, BlockPos pos, IBlockState state)
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean isAirPermitted()
	{
		return true;
	}
	
	@Override
	public boolean isValidInternalBlock(World world, BlockPos pos, IBlockState state)
	{
		return internalBlocks.contains(state.getBlock());
	}
	
	@Override
	public boolean isValidBaseCorner(World world, BlockPos pos, IBlockState state)
	{
		return baseCorners.contains(state.getBlock());
	}
	
	@Override
	public boolean isValidBaseEdge(World world, BlockPos pos, IBlockState state)
	{
		return baseEdges.contains(state.getBlock());
	}
	
	@Override
	public boolean isValidBaseInterior(World world, BlockPos pos, IBlockState state)
	{
		return baseInteriors.contains(state.getBlock());
	}
	
	@Override
	public boolean isValidRoofCorner(World world, BlockPos pos, IBlockState state)
	{
		return roofCorners.contains(state.getBlock());
	}
	
	@Override
	public boolean isValidRoofEdge(World world, BlockPos pos, IBlockState state)
	{
		return roofEdges.contains(state.getBlock());
	}
	
	@Override
	public boolean isValidRoofInterior(World world, BlockPos pos, IBlockState state)
	{
		return roofInteriors.contains(state.getBlock());
	}
	
	@Override
	public boolean isValidWallCorner(World world, BlockPos pos, IBlockState state)
	{
		return wallCorners.contains(state.getBlock());
	}
	
	@Override
	public boolean isValidWall(World world, BlockPos pos, IBlockState state)
	{
		return walls.contains(state.getBlock());
	}
	
	public void addBaseCornerBlock(Block block)
	{
		baseCorners.add(block);
	}
	
	public void addBaseEdgeBlock(Block block)
	{
		baseEdges.add(block);
	}
	
	public void addBaseInteriorBlock(Block block)
	{
		baseInteriors.add(block);
	}
	
	public void addBaseBlock(Block block)
	{
		addBaseCornerBlock(block);
		addBaseEdgeBlock(block);
		addBaseInteriorBlock(block);
	}
	
	public void addRoofCornerBlock(Block block)
	{
		roofCorners.add(block);
	}
	
	public void addRoofEdgeBlock(Block block)
	{
		roofEdges.add(block);
	}
	
	public void addRoofInteriorBlock(Block block)
	{
		roofInteriors.add(block);
	}
	
	public void addRoofBlock(Block block)
	{
		addRoofCornerBlock(block);
		addRoofEdgeBlock(block);
		addRoofInteriorBlock(block);
	}
	
	public void addWallCornerBlock(Block block)
	{
		wallCorners.add(block);
	}
	
	public void addWallBlock(Block block)
	{
		walls.add(block);
	}
	
	public void addInternalBlock(Block block)
	{
		internalBlocks.add(block);
	}
	
	public void addAccessibleStructureBlock(Block block)
	{
		addRoofCornerBlock(block);
		addRoofEdgeBlock(block);
		
		addBaseCornerBlock(block);
		addBaseEdgeBlock(block);
		
		addWallCornerBlock(block);
		addWallBlock(block);
	}
}
