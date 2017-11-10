package com.arclighttw.tinyreactors.tiles;

import com.arclighttw.tinyreactors.config.TRConfig;
import com.arclighttw.tinyreactors.managers.ReactorManager;
import com.arclighttw.tinyreactors.properties.EnumFormatting;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityDegradedReactant extends TileEntitySync
{
	private Block representedBlock;
	private float quality;
	
	private int ingotCount;
	
	public TileEntityDegradedReactant()
	{
		quality = 100F;
	}
	
	public void setRepresentedBlock(Block block)
	{
		representedBlock = block;
		ingotCount = -1;
		
		int[] ids = OreDictionary.getOreIDs(new ItemStack(block));
		
		for(int i = 0; i < ids.length; i++)
		{
			String name = OreDictionary.getOreName(ids[i]);
			
			if(name.contains("ore"))
			{
				ingotCount = 4;
				break;
			}
			
			if(name.contains("block"))
			{
				ingotCount = 8;
				break;
			}
		}
		
		if(ingotCount == -1)
			ingotCount = 2;
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
	
	public String getVariant()
	{
		EnumFormatting ore = ingotCount == 4 ? EnumFormatting.BOLD : EnumFormatting.RESET;
		EnumFormatting block = ingotCount == 8 ? EnumFormatting.BOLD : EnumFormatting.RESET;
		EnumFormatting unrecognised = ingotCount == 2 ? EnumFormatting.BOLD : EnumFormatting.RESET;
		
		return String.format("%sOre§r/%sBlock§r/%sDefault", ore, block, unrecognised);
	}
	
	public boolean degrade(TileEntityReactorController controller, float qualityDegradation)
	{
		quality -= qualityDegradation;
		
		int roundedDivider = (int)(100 / (float)ingotCount);
		
		if((int)quality % roundedDivider == 0)
			controller.getMultiblock().produceIngot();
		
		if(quality <= 0)
		{
			quality = 0;
			
			if(TRConfig.REACTANT_REMOVAL_ON_FULL_DEGRADATION)
			{
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				ReactorManager.validateAllReactors(world);
			}
		}
		
		sync();
		return quality == 0;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		
		compound.setString("representedBlock", getRepresentedBlock().getRegistryName().toString());
		compound.setFloat("quality", quality);
		compound.setInteger("ingotCount", ingotCount);
		
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		representedBlock = Block.REGISTRY.getObject(new ResourceLocation(compound.getString("representedBlock")));
		quality = compound.getFloat("quality");
		ingotCount = compound.getInteger("ingotCount");
	}
}
