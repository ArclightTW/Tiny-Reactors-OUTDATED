package com.arclighttw.tinyreactors.tiles;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class TileEntityDegradedReactant extends TileEntitySync
{
	private Block representedBlock;
	private float quality;
	
	public TileEntityDegradedReactant()
	{
		quality = 100F;
	}
	
	public void setRepresentedBlock(Block block)
	{
		representedBlock = block;
	}
	
	public Block getRepresentedBlock()
	{
		return representedBlock == null ? Blocks.AIR : representedBlock;
	}
	
	public void setQuality(float quality)
	{
		this.quality = quality;
	}
	
	public float getQuality()
	{
		return quality;
	}
	
	public void degrade(float qualityDegradation)
	{
		quality -= qualityDegradation;
		sync();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		
		compound.setString("representedBlock", getRepresentedBlock().getRegistryName().toString());
		compound.setFloat("quality", quality);
		
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		representedBlock = Block.REGISTRY.getObject(new ResourceLocation(compound.getString("representedBlock")));
		quality = compound.getFloat("quality");
	}
}
