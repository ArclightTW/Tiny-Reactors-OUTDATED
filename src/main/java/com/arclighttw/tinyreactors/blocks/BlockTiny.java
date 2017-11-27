package com.arclighttw.tinyreactors.blocks;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.arclighttw.tinyreactors.helpers.TranslationHelper;
import com.arclighttw.tinyreactors.lib.EnumFormatting;
import com.arclighttw.tinyreactors.lib.nbt.IStorableContents;
import com.google.common.collect.Lists;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTiny extends BlockDirectional implements ITileEntityProvider
{
	public enum DirectionalType { NONE, FACING_TOWARD, FACING_AWAY }
	
	protected List<String> standardTooltip;
	protected List<String> additionalTooltip;
	
	private final DirectionalType directional;
	
	public BlockTiny(String name, Material material, DirectionalType directional)
	{
		super(material);
		setUnlocalizedName(name);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		
		if(directional != DirectionalType.NONE)
			setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		
		this.directional = directional;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BlockTiny> T setStandardTooltip(String... tooltip)
	{
		standardTooltip = Arrays.asList(tooltip);
		return (T)this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BlockTiny> T setAdditionalTooltip(String... tooltip)
	{
		additionalTooltip = Arrays.asList(tooltip);
		return (T)this;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		if(standardTooltip != null)
			for(String line : standardTooltip)
				tooltip.add(TranslationHelper.translate(line));
		else
		{
			String additional = TranslationHelper.translate(getUnlocalizedName() + ".desc");
			if(!(getUnlocalizedName() + ".desc").equals(additional))
				tooltip.add(additional);
		}
		
		if(additionalTooltip != null)
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
				for(String line : additionalTooltip)
					tooltip.add(TranslationHelper.translate(line));
			else
			{
				String additional = TranslationHelper.translate("tooltip.tinyreactors.additional");
				additional = String.format(additional.replace("Shift", "%s<Shift>%s"), EnumFormatting.AQUA, EnumFormatting.GRAY);
				tooltip.add(additional);
			}
		}
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		if(directional == DirectionalType.NONE)
			return;
		
		if(!world.isRemote)
		{
			IBlockState north = world.getBlockState(pos.north());
			IBlockState south = world.getBlockState(pos.south());
			
			IBlockState east = world.getBlockState(pos.east());
			IBlockState west = world.getBlockState(pos.west());
			
			EnumFacing facing = (EnumFacing)state.getValue(FACING);
			
			if(facing == EnumFacing.NORTH && north.isFullBlock() && !south.isFullBlock())
				facing = directional == DirectionalType.FACING_TOWARD ? EnumFacing.SOUTH : EnumFacing.NORTH;
			if(facing == EnumFacing.SOUTH && south.isFullBlock() && !north.isFullBlock())
				facing = directional == DirectionalType.FACING_TOWARD ? EnumFacing.NORTH : EnumFacing.SOUTH;
			
			if(facing == EnumFacing.EAST && east.isFullBlock() && !west.isFullBlock())
				facing = directional == DirectionalType.FACING_TOWARD ? EnumFacing.WEST : EnumFacing.EAST;
			if(facing == EnumFacing.WEST && west.isFullBlock() && !east.isFullBlock())
				facing = directional == DirectionalType.FACING_TOWARD ? EnumFacing.EAST : EnumFacing.WEST;
			
			world.setBlockState(pos, state.withProperty(FACING, facing), 2);
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if(directional != DirectionalType.NONE)
		{
			EnumFacing direction = placer.getHorizontalFacing();
			world.setBlockState(pos, state.withProperty(FACING, directional == DirectionalType.FACING_TOWARD ? direction.getOpposite() : direction), 2);
		}
		
		if(state.getBlock() instanceof IStorableContents)
			((IStorableContents)state.getBlock()).loadContents(stack, world, pos, state);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		if(directional == DirectionalType.NONE)
			return getDefaultState();
		
		EnumFacing direction = placer.getHorizontalFacing();
		return getDefaultState().withProperty(FACING, directional == DirectionalType.FACING_TOWARD ? direction.getOpposite() : direction);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		if(directional == DirectionalType.NONE)
			return getDefaultState();
		
		EnumFacing facing = EnumFacing.getFront(meta - 1);
		
		if(facing.getAxis() == EnumFacing.Axis.Y)
			facing = EnumFacing.NORTH;
		
		return getDefaultState().withProperty(FACING, facing);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return directional == DirectionalType.NONE ? 0 : ((EnumFacing)state.getValue(FACING)).getIndex() + 1;
	}
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return directional == DirectionalType.NONE ? state : state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
	}
	
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirror)
	{
		return directional == DirectionalType.NONE ? state : state.withRotation(mirror.toRotation((EnumFacing)state.getValue(FACING)));
	}
	
	@Override
	public BlockStateContainer createBlockState()
	{
		List<IProperty<?>> properties = Lists.newArrayList();
		
		if(directional != DirectionalType.NONE)
			properties.add(FACING);
		
		return new BlockStateContainer(this, properties.toArray(new IProperty[properties.size()]));
	}
}
