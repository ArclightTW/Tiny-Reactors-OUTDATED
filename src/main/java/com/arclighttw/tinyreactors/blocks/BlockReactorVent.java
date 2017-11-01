package com.arclighttw.tinyreactors.blocks;

import com.arclighttw.tinyreactors.inits.Registry.IItemProvider;
import com.arclighttw.tinyreactors.inits.Registry.IModelProvider;
import com.arclighttw.tinyreactors.items.ItemReactorVent;
import com.arclighttw.tinyreactors.main.Reference;
import com.arclighttw.tinyreactors.properties.EnumVentTier;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorVent;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockReactorVent extends BlockReactorComponent implements IItemProvider, IModelProvider
{
	public BlockReactorVent()
	{
		super(Material.IRON, "reactor_vent");
		setDefaultState(blockState.getBaseState().withProperty(EnumVentTier.PROPERTY, EnumVentTier.IRON));
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityReactorVent();
	}
	
	@Override
	public ItemBlock getItemBlock()
	{
		return new ItemReactorVent(this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels()
	{
		for(int i = 0; i < EnumVentTier.values().length; i++)
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, new ModelResourceLocation(new ResourceLocation(Reference.ID, "reactor_vent_" + i), "inventory"));
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return getMetaFromState(state);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(EnumVentTier.PROPERTY, EnumVentTier.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(EnumVentTier.PROPERTY).ordinal();
	}

	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { EnumVentTier.PROPERTY });
    	}
	
	@Override
	public void getSubBlocks(CreativeTabs item, NonNullList<ItemStack> items)
	{
		for(int i = 0; i < EnumVentTier.values().length; i++)
			items.add(new ItemStack(this, 1, i));
	}
}
