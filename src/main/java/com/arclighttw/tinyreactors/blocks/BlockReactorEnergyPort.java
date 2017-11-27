package com.arclighttw.tinyreactors.blocks;

import com.arclighttw.tinyreactors.lib.nbt.IStorableContents;
import com.arclighttw.tinyreactors.main.TinyReactors;
import com.arclighttw.tinyreactors.managers.GuiHandler;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorEnergyPort;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockReactorEnergyPort extends BlockReactorComponent implements IStorableContents
{
	public BlockReactorEnergyPort(String name)
	{
		super(name, Material.IRON, DirectionalType.FACING_TOWARD);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityReactorEnergyPort();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!world.isRemote)
			player.openGui(TinyReactors.instance, GuiHandler.REACTOR_ENERGY_PORT, world, pos.getX(), pos.getY(), pos.getZ());
		
		return true;
	}
	
	@Override
	public void onStructureValidityChanged(World world, BlockPos pos, BlockPos origin, boolean isValid)
	{
		TileEntity tile = world.getTileEntity(pos);
		
		if(tile == null || !(tile instanceof TileEntityReactorEnergyPort))
			return;
		
		TileEntityReactorEnergyPort energyPort = (TileEntityReactorEnergyPort)tile;
		energyPort.onStructureValidityChanged(origin, isValid);
	}
	
	@Override
	public NBTTagCompound saveContents(World world, BlockPos pos, IBlockState state)
	{
		TileEntity tile = world.getTileEntity(pos);
		
		if(tile == null || !(tile instanceof TileEntityReactorEnergyPort))
			return null;
		
		TileEntityReactorEnergyPort energyPort = (TileEntityReactorEnergyPort)tile;
		
		NBTTagCompound compound = new NBTTagCompound();
		energyPort.writeToNBT(compound);
		return compound;
	}
	
	@Override
	public void loadContents(ItemStack itemstack, World world, BlockPos pos, IBlockState state)
	{
		if(!itemstack.hasTagCompound())
			return;
		
		TileEntity tile = world.getTileEntity(pos);
		
		if(tile == null || !(tile instanceof TileEntityReactorEnergyPort))
			return;
		
		TileEntityReactorEnergyPort energyPort = (TileEntityReactorEnergyPort)tile;
		energyPort.readFromNBT(itemstack.getTagCompound());
	}
}
