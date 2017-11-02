package com.arclighttw.tinyreactors.tiles;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class TileEntityDegradedReactant extends TileEntitySync
{
	private Block representedBlock;
	
	public void setRepresentedBlock(Block block)
	{
		representedBlock = block;
	}
	
	public Block getRepresentedBlock()
	{
		return representedBlock == null ? Blocks.AIR : representedBlock;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		
		compound.setString("representedBlock", representedBlock.getRegistryName().toString());
		
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		representedBlock = Block.REGISTRY.getObject(new ResourceLocation(compound.getString("representedBlock")));
	}
}
