package com.arclighttw.tinyreactors.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockTiny extends Block
{
	public BlockTiny(String name, Material material)
	{
		super(material);
		setUnlocalizedName(name);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}
}
