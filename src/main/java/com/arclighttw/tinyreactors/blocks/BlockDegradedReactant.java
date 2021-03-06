package com.arclighttw.tinyreactors.blocks;

import java.util.List;
import java.util.Random;

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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDegradedReactant extends BlockTiny implements IItemProvider, IModelProvider
{
	public static final PropertyUnlistedIBlockState REPRESENTATIVE = new PropertyUnlistedIBlockState();
	
	public BlockDegradedReactant()
	{
		super(Material.ROCK, "degraded_reactant");
		setCreativeTab(null);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return false;
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
	@SideOnly(Side.CLIENT)
	public IRuntimeModel createModel()
	{
		return new ModelBlockDegradedReactant();
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if(!world.isRemote)
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
			
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		if(!world.isRemote && !player.capabilities.isCreativeMode)
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
			world.removeTileEntity(pos);
		}
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return null;
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
		if(stack.hasTagCompound())
		{
			String registryName = stack.getTagCompound().getString("registryName");
			Block block = Block.REGISTRY.getObject(new ResourceLocation(registryName));
			
			if(block == null)
			{
				tooltip.add("Unrecognised Reactant; this has likely been saved wrong!");
				return;
			}
			
			float quality = stack.getTagCompound().getFloat("quality");
			
			tooltip.add("§6Reactant: §b" + block.getLocalizedName());
			tooltip.add(String.format("§6Quality: §b%.2f ", quality) + "%");
		}
		
		tooltip.add("§5Non-placeable");
	}
}
