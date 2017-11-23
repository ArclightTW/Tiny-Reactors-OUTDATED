package com.arclighttw.tinyreactors.blocks;

import com.arclighttw.tinyreactors.lib.nbt.IStorableContents;
import com.arclighttw.tinyreactors.main.TinyReactors;
import com.arclighttw.tinyreactors.managers.GuiHandler;
import com.arclighttw.tinyreactors.managers.ReactorManager;
import com.arclighttw.tinyreactors.tiles.TileEntityReactorController;

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
import net.minecraftforge.common.util.BlockSnapshot;

public class BlockReactorController extends BlockReactorComponent implements IStorableContents
{
	public BlockReactorController(String name)
	{
		super(name, Material.IRON);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityReactorController();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!world.isRemote)
			player.openGui(TinyReactors.instance, GuiHandler.REACTOR_CONTROLLER, world, pos.getX(), pos.getY(), pos.getZ());
		
		return true;
	}
	
	@Override
	public NBTTagCompound saveContents(World world, BlockPos pos, IBlockState state)
	{
		TileEntity tile = world.getTileEntity(pos);
		
		if(tile == null || !(tile instanceof TileEntityReactorController))
			return null;
		
		TileEntityReactorController controller = (TileEntityReactorController)tile;
		
		NBTTagCompound compound = new NBTTagCompound();
		controller.writeToNBT(compound);
		return compound;
	}
	
	@Override
	public void loadContents(ItemStack itemstack, World world, BlockPos pos, IBlockState state)
	{
		if(!itemstack.hasTagCompound())
			return;
		
		TileEntity tile = world.getTileEntity(pos);
		
		if(tile == null || !(tile instanceof TileEntityReactorController))
			return;
		
		TileEntityReactorController controller = (TileEntityReactorController)tile;
		controller.readFromNBT(itemstack.getTagCompound());
	}
	
	@Override
	public void onReactorComponentPlaced(World world, BlockSnapshot snapshot)
	{
		ReactorManager.addReactor(snapshot);
		super.onReactorComponentPlaced(world, snapshot);
	}
}
