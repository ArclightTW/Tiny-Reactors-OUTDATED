package com.arclighttw.tinyreactors.storage;

import java.util.function.Consumer;

import com.arclighttw.tinyreactors.helpers.NBTHelper;
import com.arclighttw.tinyreactors.lib.nbt.INBTSerializable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiblockStructure  implements INBTSerializable
{
	private BlockPos start, end;
	private boolean isValid;
	
	private Consumer<Boolean> listener;
	
	@SuppressWarnings("unchecked")
	public <T extends MultiblockStructure> T setValidityListener(Consumer<Boolean> listener)
	{
		this.listener = listener;
		return (T)this;
	}
	
	@Override
	public void serialize(NBTTagCompound compound)
	{
		NBTHelper.Serializer.blockPos(compound, "start", start);
		NBTHelper.Serializer.blockPos(compound, "end", end);
		
		compound.setBoolean("isValid", isValid);
	}
	
	@Override
	public void deserialize(NBTTagCompound compound)
	{
		start = NBTHelper.Deserializer.blockPos(compound, "start");
		end = NBTHelper.Deserializer.blockPos(compound, "end");
		
		isValid = compound.getBoolean("isValid");
	}
	
	public boolean validate(World world, BlockPos origin, BlockPos added, IBlockState addedBlock, BlockPos removed)
	{
		if(world == null)
			return false;
		
		if(addedBlock == null)
			addedBlock = Blocks.AIR.getDefaultState();
		
		BlockPos currentPos = origin;
		IBlockState currentState = null;
		
		int xStart, xEnd;
		xStart = xEnd = origin.getX();
		
		// yStart is Top (+)
		// yEnd is Bottom (-)
		int yStart, yEnd;
		yStart = yEnd = origin.getY();
		
		int zStart, zEnd;
		zStart = zEnd = origin.getZ();
		
		// Find the top Block of the structure
		while(true)
		{
			currentPos = currentPos.up();
			currentState = modifyState(world, currentPos, added, addedBlock, removed);
			
			if(!isValidWall(world, currentPos, currentState))
				break;
			
			yStart = currentPos.getY();
		}
		
		currentPos = origin;
		
		// Find the bottom Block of the structure
		while(true)
		{
			currentPos = currentPos.down();
			currentState = modifyState(world, currentPos, added, addedBlock, removed);
			
			if(!isValidWall(world, currentPos, currentState))
				break;
			
			yEnd = currentPos.getY();
		}
		
		// Minimum valid reactor height is 3
		if(yStart - yEnd < 2)
		{
			setValid(false);
			return false;
		}
		
		currentPos = origin;
		EnumFacing currentDirection = null;
		
		for(EnumFacing direction : EnumFacing.HORIZONTALS)
		{
			BlockPos offsetPos = currentPos.offset(direction);
			currentState = modifyState(world, offsetPos, added, addedBlock, removed);
			
			if(!isValidWall(world, offsetPos, currentState))
				continue;
			
			currentDirection = direction;
			break;
		}
		
		if(currentDirection == null)
		{
			setValid(false);
			return false;
		}
		
		int x1 = xStart, z1 = zStart;
		currentPos = new BlockPos(xStart, yStart, zStart);
		
		// Navigate one edge in one direction
		while(true)
		{
			currentPos = currentPos.offset(currentDirection);
			currentState = modifyState(world, currentPos, added, addedBlock, removed);
			
			if(!isValidWall(world, currentPos, currentState))
				break;
			
			x1 = currentPos.getX();
			z1 = currentPos.getZ();
		}
		
		int x2 = xStart, z2 = zStart;
		currentPos = new BlockPos(xStart, yStart, zStart);
		currentDirection = currentDirection.getOpposite();
		
		// Navigate same edge as above in opposite direction
		while(true)
		{
			currentPos = currentPos.offset(currentDirection);
			currentState = modifyState(world, currentPos, added, addedBlock, removed);
			
			if(!isValidWall(world, currentPos, currentState))
				break;
			
			x2 = currentPos.getX();
			z2 = currentPos.getZ();
		}
		
		boolean directionChanged = false;
		currentPos = new BlockPos(x1, yStart, z1);
		
		// Spin on spot to find other valid edge
		for(EnumFacing direction : EnumFacing.HORIZONTALS)
		{
			BlockPos offsetPos = currentPos.offset(direction);
			currentState = modifyState(world, offsetPos, added, addedBlock, removed);

			if(direction == currentDirection || direction == currentDirection.getOpposite())
				continue;
			
			if(!isValidWall(world, offsetPos, currentState))
				continue;
			
			currentDirection = direction;
			directionChanged = true;
			break;
		}
		
		// Only one wall has been built of the structure
		if(!directionChanged)
		{
			setValid(false);
			return false;
		}
		
		int x3 = xStart, z3 = zStart;
		
		// Navigate down other edge of structure
		while(true)
		{
			currentPos = currentPos.offset(currentDirection);
			currentState = modifyState(world, currentPos, added, addedBlock, removed);
			
			if(!isValidWall(world, currentPos, currentState))
				break;
			
			x3 = currentPos.getX();
			z3 = currentPos.getZ();
		}
		
		xStart = Math.min(Math.min(xStart, x1), Math.min(x2, x3));
		xEnd = Math.max(Math.max(xStart, x1), Math.max(x2, x3));
		
		zStart = Math.min(Math.min(zStart, z1), Math.min(z2, z3));
		zEnd = Math.max(Math.max(zStart, z1), Math.max(z2, z3));
		
		// Start > bottom-left corner
		start = new BlockPos(xStart, yEnd, zStart);
		// End > top-right corner
		end = new BlockPos(xEnd, yStart, zEnd);
		
		onPreCalculation(world);
		
		for(int x = xStart; x <= xEnd; x++)
		{
			for(int z = zStart; z <= zEnd; z++)
			{
				for(int y = yStart; y >= yEnd; y--)
				{
					currentPos = new BlockPos(x, y, z);
					currentState = modifyState(world, currentPos, added, addedBlock, removed);
					
					onBlockDetected(world, currentPos, currentState);
					
					// Checking the roof
					if(y == yStart)
					{
						// Air blocks are not permitted anywhere in the roof
						if(currentState.getBlock() == Blocks.AIR)
						{
							setValid(false);
							return false;
						}
						
						// Checking the roof corner blocks
						if((x == xStart && z == zStart) || (x == xStart && z == zEnd) || (x == xEnd && z == zStart) || (x == xEnd && z == zEnd))
						{
							if(!isValidRoofCorner(world, currentPos, currentState))
							{
								setValid(false);
								return false;
							}
						}
						// Checking the roof edge blocks
						else if(x == xStart || z == zStart || x == xEnd || z == zEnd)
						{
							if(!isValidRoofEdge(world, currentPos, currentState))
							{
								setValid(false);
								return false;
							}
						}
						// Checking the interior roof blocks
						else
						{
							if(!isValidRoofInterior(world, currentPos, currentState))
							{
								setValid(false);
								return false;
							}
						}
					}
					// Checking the base
					else if(y == yEnd)
					{
						// Air blocks are not permitted anywhere in the base
						if(currentState.getBlock() == Blocks.AIR)
						{
							setValid(false);
							return false;
						}
						
						// Checking the base corner blocks
						if((x == xStart && z == zStart) || (x == xStart && z == zEnd) || (x == xEnd && z == zStart) || (x == xEnd && z == zEnd))
						{
							if(!isValidBaseCorner(world, currentPos, currentState))
							{
								setValid(false);
								return false;
							}
						}
						// Checking the base edge blocks
						else if(x == xStart || z == zStart || x == xEnd || z == zEnd)
						{
							if(!isValidBaseEdge(world, currentPos, currentState))
							{
								setValid(false);
								return false;
							}
						}
						// Checking the interior base blocks
						else
						{
							if(!isValidBaseInterior(world, currentPos, currentState))
							{
								setValid(false);
								return false;
							}
						}
					}
					// Checking blocks in the middle layers
					else
					{
						// Checking the corner blocks
						if((x == xStart && z == zStart) || (x == xStart && z == zEnd) || (x == xEnd && z == zStart) || (x == xEnd && z == zEnd))
						{
							if(currentState.getBlock() == Blocks.AIR)
							{
								setValid(false);
								return false;
							}
							
							if(!isValidWallCorner(world, currentPos, currentState))
							{
								setValid(false);
								return false;
							}
						}
						// Checking blocks in the walls
						if(x == xStart || z == zStart || x == xEnd || z == zEnd)
						{
							if(currentState.getBlock() == Blocks.AIR)
							{
								setValid(false);
								return false;
							}
							
							if(!isValidWall(world, currentPos, currentState))
							{
								setValid(false);
								return false;
							}
						}
						// Checking blocks INSIDE the structure
						else
						{
							onInternalBlockDetected(world, currentPos, currentState);
							
							if(isAirPermitted() && currentState.getBlock() == Blocks.AIR)
								continue;
							
							if(!isValidInternalBlock(world, currentPos, currentState))
							{
								setValid(false);
								return false;
							}
						}
					}
				}
			}
		}

		onReactorValidated(world);
		return isValid;
	}
	
	/**
	 * Called to verify if a specific IBlockState is permitted in any of the four edge walls of the structure.
	 * @param world
	 * @param pos
	 * @param state
	 * @return True if the IBlockState is permitted and false if not.
	 */
	public boolean isValidWall(World world, BlockPos pos, IBlockState state)
	{
		return false;
	}
	
	/**
	 * Called to verify if a specific IBlockState is permitted in any of the four corners of the structure.
	 * @param world
	 * @param pos
	 * @param state
	 * @return True if the IBlockState is permitted and false if not.
	 */
	public boolean isValidWallCorner(World world, BlockPos pos, IBlockState state)
	{
		return false;
	}
	
	/**
	 * Called to verify if a specific IBlockState is permitted corners of the structure's roof.
	 * @param world
	 * @param pos
	 * @param state
	 * @return True if the IBlockState is permitted and false if not.
	 */
	public boolean isValidRoofCorner(World world, BlockPos pos, IBlockState state)
	{
		return false;
	}
	
	/**
	 * Called to verify if a specific IBlockState is permitted in the edge blocks of the structure's roof.
	 * @param world
 	 * @param pos
	 * @param state
	 * @return True if the IBlockState is permitted and false if not.
	 */
	public boolean isValidRoofEdge(World world, BlockPos pos, IBlockState state)
	{
		return false;
	}
	
	/**
	 * Called to verify if a specific IBlockState is permitted in the interior blocks of the structure's roof.
	 * @param world
	 * @param pos
	 * @param state
	 * @return True if the IBlockState is permitted and false if not.
	 */
	public boolean isValidRoofInterior(World world, BlockPos pos, IBlockState state)
	{
		return false;
	}
	
	/**
	 * Called to verify if a specific IBlockState is permitted corners of the structure's base.
	 * @param world
	 * @param pos
	 * @param state
	 * @return True if the IBlockState is permitted and false if not.
	 */
	public boolean isValidBaseCorner(World world, BlockPos pos, IBlockState state)
	{
		return false;
	}
	
	/**
	 * Called to verify if a specific IBlockState is permitted in the edge blocks of the structure's base.
	 * @param world
	 * @param pos
	 * @param state
	 * @return True if the IBlockState is permitted and false if not.
	 */
	public boolean isValidBaseEdge(World world, BlockPos pos, IBlockState state)
	{
		return false;
	}
	
	/**
	 * Called to verify if a specific IBlockState is permitted in the interior blocks of the structure's base.
	 * @param world
	 * @param pos
	 * @param state
	 * @return True if the IBlockState is permitted and false if not.
	 */
	public boolean isValidBaseInterior(World world, BlockPos pos, IBlockState state)
	{
		return false;
	}
	
	/**
	 * Called to verify if a specific IBlockState is permitted inside the structure's walls.
	 * @param world
	 * @param pos
	 * @param state
	 * @return True if the IBlockState is permitted and false if not.
	 */
	public boolean isValidInternalBlock(World world, BlockPos pos, IBlockState state)
	{
		return false;
	}

	/**
	 * Whether the structure can have air blocks inside or not.
	 * @return
	 */
	public boolean isAirPermitted()
	{
		return true;
	}
	
	/**
	 * Called when a block is identified within the structure (this is called for exterior AND interior blocks).
	 * @param world
	 * @param pos
	 * @param state
	 */
	public void onBlockDetected(World world, BlockPos pos, IBlockState state)
	{
	}
	
	/**
	 * Called when a block is identified within the structure's walls (this is called for interior blocks ONLY).
	 * @param world
	 * @param pos
	 * @param state
	 */
	public void onInternalBlockDetected(World world, BlockPos pos, IBlockState state)
	{
	}

	/**
	 * Called prior to the final calculation to determine if a structure is valid or not.
	 * @param world
	 */
	public void onPreCalculation(World world)
	{
	}
	
	/**
	 * Called after a reactor is identified as valid.
	 * @param world
	 */
	public void onReactorValidated(World world)
	{
		setValid(true);
	}
	
	public final void setValid(boolean valid)
	{
		isValid = valid;
		
		if(listener != null)
			listener.accept(valid);
	}
	
	public final BlockPos getStart()
	{
		return start;
	}
	
	public final BlockPos getEnd()
	{
		return end;
	}
	
	public final boolean isValid()
	{
		return isValid;
	}
	
	public final IBlockState modifyState(World world, BlockPos pos, BlockPos added, IBlockState addedBlock, BlockPos removed)
	{
		IBlockState state;
		
		if(areEqual(pos, removed))
			state = Blocks.AIR.getDefaultState();
		else if(areEqual(pos, added))
			state = addedBlock;
		else
			state = world.getBlockState(pos);
		
		return state;
	}
	
	public final boolean areEqual(BlockPos left, BlockPos right)
	{
		if(left == null && right == null)
			return true;
		
		if((left == null && right != null) || (left != null && right == null))
			return false;
		
		return left.getX() == right.getX() && left.getY() == right.getY() && left.getZ() == right.getZ();
	}
}
