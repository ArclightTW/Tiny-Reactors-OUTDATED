package com.arclighttw.tinyreactors.storage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class MultiBlockStorage
{
	BlockPos start, end;
	boolean isValid;
	
	Runnable listener;
	
	public MultiBlockStorage()
	{
		start = end = new BlockPos(0, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends MultiBlockStorage> T setValidityListener(Runnable listener)
	{
		this.listener = listener;
		return (T)this;
	}
	
	public BlockPos getStart()
	{
		return start;
	}
	
	public BlockPos getEnd()
	{
		return end;
	}
	
	public BlockPos getCenter()
	{
		return new BlockPos(
				start.getX() + (end.getX() - start.getX()),
				start.getY() + (end.getY() - start.getY()),
				start.getZ() + (end.getZ() - start.getZ())
		);
	}
	
	public BlockPos getDimensions()
	{
		return isValid ? new BlockPos(end.getX() - start.getX() + 1, end.getY() - start.getY() + 1, end.getZ() - start.getZ() + 1) : new BlockPos(-1, -1, -1);
	}
	
	public boolean isValid()
	{
		return isValid;
	}
	
	public void setValid(boolean valid)
	{
		isValid = valid;
		
		if(listener != null)
			listener.run();
	}
	
	public void invalidate()
	{
		setValid(false);
	}
	
	public void writeToNBT(NBTTagCompound compound)
	{
		compound.setBoolean("isValid", isValid);
		
		compound.setInteger("xStart", start.getX());
		compound.setInteger("yStart", start.getY());
		compound.setInteger("zStart", start.getZ());
		
		compound.setInteger("xEnd", end.getX());
		compound.setInteger("yEnd", end.getY());
		compound.setInteger("zEnd", end.getZ());
	}
	
	public void readFromNBT(NBTTagCompound compound)
	{
		boolean valid = compound.getBoolean("isValid");
		setValid(valid);
		
		int x = compound.getInteger("xStart");
		int y = compound.getInteger("yStart");
		int z = compound.getInteger("zStart");
		start = new BlockPos(x, y, z);
		
		x = compound.getInteger("xEnd");
		y = compound.getInteger("yEnd");
		z = compound.getInteger("zEnd");
		end = new BlockPos(x, y, z);
	}
	
	public void checkValidity(World world, BlockPos origin)
	{
		if(world == null || !(world instanceof WorldServer))
			return;
		
		((WorldServer)world).addScheduledTask(() -> {
			BlockPos pos = origin;
			IBlockState b = null;
			
			int xStart, yStart, zStart;
			int xEnd, yEnd, zEnd;
			
			xStart = xEnd = origin.getX();
			yStart = yEnd = origin.getY();
			zStart = zEnd = origin.getZ();

			while(true)
			{
				pos = pos.up();
				b = world.getBlockState(pos);
				
				if(!isValidCasing(b))
					break;
				
				yStart = pos.getY();
			}
			
			pos = origin;
			
			while(true)
			{
				pos = pos.down();
				b = world.getBlockState(pos);
				
				if(!isValidCasing(b))
					break;
				
				yEnd = pos.getY();
			}
			
			if(yStart - yEnd < 2)
			{
				invalidate();
				return;
			}
			
			pos = origin;
			xStart = xEnd = pos.getX();
			zStart = zEnd = pos.getZ();
			
			EnumFacing f = null;
			
			for(EnumFacing facing : EnumFacing.HORIZONTALS)
			{
				b = world.getBlockState(pos.offset(facing));
				
				if(!isValidCasing(b))
					continue;
				
				f = facing;
				break;
			}
			
			if(f == null)
			{
				invalidate();
				return;
			}
			
			int x1 = xStart, z1 = zStart;
			pos = new BlockPos(xStart, yStart, zStart);
			
			while(true)
			{
				pos = pos.offset(f);
				b = world.getBlockState(pos);
				
				if(!isValidCasing(b))
					break;
				
				x1 = pos.getX();
				z1 = pos.getZ();
			}
			
			int x2 = xStart, z2 = zStart;
			f = f.getOpposite();
			
			while(true)
			{
				pos = pos.offset(f);
				b = world.getBlockState(pos);
				
				if(!isValidCasing(b))
					break;
				
				x2 = pos.getX();
				z2 = pos.getZ();
			}
			
			boolean dirChanged = false;
			pos = new BlockPos(x1, yStart, z1);
			
			for(EnumFacing facing : EnumFacing.HORIZONTALS)
			{
				b = world.getBlockState(pos.offset(facing));
				
				if(!isValidCasing(b))
					continue;
				
				if(facing == f || facing == f.getOpposite())
					continue;
				
				f = facing;
				dirChanged = true;
				break;
			}
			
			if(!dirChanged)
			{
				invalidate();
				return;
			}
			
			int x3 = xStart, z3 = zStart;
			
			while(true)
			{
				pos = pos.offset(f);
				b = world.getBlockState(pos);
				
				if(!isValidCasing(b))
					break;
				
				x3 = pos.getX();
				z3 = pos.getZ();
			}
			
			xStart = Math.min(Math.min(xStart, x1), Math.min(x2, x3));
			xEnd = Math.max(Math.max(xStart, x1), Math.max(x2, x3));
			
			zStart = Math.min(Math.min(zStart, z1), Math.min(z2, z3));
			zEnd = Math.max(Math.max(zStart, z1), Math.max(z2, z3));
			
			start = new BlockPos(xStart, yEnd, zStart);
			end = new BlockPos(xEnd, yStart, zEnd);
			
			onPreCalculation();
			
			for(int x = xStart; x <= xEnd; x++)
			{
				for(int z = zStart; z <= zEnd; z++)
				{
					for(int y = yStart; y >= yEnd; y--)
					{
						pos = new BlockPos(x, y, z);
						b = world.getBlockState(pos);
						
						onBlockDetected(world, pos, b);
						
						if(y == yStart || y == yEnd || (x == xStart && z == zStart) || (x == xEnd && z == zEnd) || (x == xStart && z == zEnd) || (x == xEnd && z == zStart))
						{
							if(b.getBlock() == Blocks.AIR || !isValidStructure(b))
							{
								invalidate();
								return;
							}
						}
						else if(x == xStart || x == xEnd || z == zStart || z == zEnd)
						{
							if(b.getBlock() == Blocks.AIR || !isValidCasing(b))
							{
								invalidate();
								return;
							}
						}
						else
						{
							onInternalBlockDetected(world, pos, b);
							
							if(b.getBlock() == Blocks.AIR)
								continue;
							
							if(!isValidInternalBlock(b))
							{
								invalidate();
								return;
							}
						}
					}
				}
			}
			
			onPostCalculation();
		});
	}
	
	public void onBlockDetected(World world, BlockPos pos, IBlockState state)
	{
	}
	
	public void onInternalBlockDetected(World world, BlockPos pos, IBlockState state)
	{
	}
	
	public void onPreCalculation()
	{
	}
	
	public void onPostCalculation()
	{
	}
	
	public abstract boolean isValidCasing(IBlockState state);
	public abstract boolean isValidStructure(IBlockState state);
	public abstract boolean isValidInternalBlock(IBlockState state);
}
