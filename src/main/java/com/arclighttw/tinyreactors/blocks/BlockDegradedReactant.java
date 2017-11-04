package com.arclighttw.tinyreactors.blocks;

import java.util.List;

import com.arclighttw.tinyreactors.client.model.ModelBlockDegradedReactant;
import com.arclighttw.tinyreactors.inits.Registry.IItemProvider;
import com.arclighttw.tinyreactors.inits.Registry.IModelProvider;
import com.arclighttw.tinyreactors.inits.Registry.IRuntimeModel;
import com.arclighttw.tinyreactors.items.ItemDegradedReactant;
import com.arclighttw.tinyreactors.properties.PropertyUnlistedIBlockState;
import com.arclighttw.tinyreactors.tiles.TileEntityDegradedReactant;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockDegradedReactant extends BlockTiny implements IItemProvider, IModelProvider
{
	public static final PropertyUnlistedIBlockState REPRESENTATIVE = new PropertyUnlistedIBlockState();
	
	public BlockDegradedReactant()
	{
		super(Material.ROCK, "degraded_reactant");
		setCreativeTab(null);
	}
	
	@Override
	public ItemBlock getItemBlock()
	{
		return new ItemDegradedReactant(this);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityDegradedReactant();
	}
	
	@Override
	public IRuntimeModel createModel()
	{
		return new ModelBlockDegradedReactant();
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		TileEntity tile = world.getTileEntity(pos);
		
		if(tile == null || !(tile instanceof TileEntityDegradedReactant) || !stack.hasTagCompound())
			return;
		
		TileEntityDegradedReactant reactant = (TileEntityDegradedReactant)tile;
		NBTTagCompound compound = stack.getTagCompound();
		
		String registryName = compound.getString("registryName");
		Block block = Block.REGISTRY.getObject(new ResourceLocation(registryName));
		float quality = compound.getFloat("quality");
		
		reactant.setRepresentedBlock(block);
		reactant.setQuality(quality);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		if(!world.isRemote)
		{
			TileEntity tile = world.getTileEntity(pos);
			
			if(tile == null || !(tile instanceof TileEntityDegradedReactant))
				return;
			
			TileEntityDegradedReactant reactant = (TileEntityDegradedReactant)tile;
			
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("registryName", reactant.getRepresentedBlock().getRegistryName().toString());
			compound.setFloat("quality", reactant.getQuality());
			
			ItemStack itemstack = new ItemStack(this);
			itemstack.setTagCompound(compound);
			
			EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), itemstack);
			world.spawnEntity(item);
		}
		
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		drops.clear();
	}
	
	@Override
	public BlockStateContainer createBlockState()
	{
		return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[] { REPRESENTATIVE });
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		if(state instanceof IExtendedBlockState)
		{
			IExtendedBlockState extended = (IExtendedBlockState)state;
			
			TileEntity tile = world.getTileEntity(pos);
			
			if(tile != null && tile instanceof TileEntityDegradedReactant)
				return extended.withProperty(REPRESENTATIVE, ((TileEntityDegradedReactant)tile).getRepresentedBlock().getDefaultState());
		}
		
		return state;
	}
	
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		if(!stack.hasTagCompound())
			return;
		
		String registryName = stack.getTagCompound().getString("registryName");
		Block block = Block.REGISTRY.getObject(new ResourceLocation(registryName));
		
		if(block == null)
		{
			tooltip.add("Unrecognised Reactant; this has likely been saved wrong!");
			return;
		}
		
		float quality = stack.getTagCompound().getFloat("quality");
		
		tooltip.add("Reactant: " + block.getLocalizedName());
		tooltip.add(String.format("Quality: %.2f ", quality) + "%");
	}
}
