package com.arclighttw.tinyreactors.client.render;

import java.util.Arrays;

import com.arclighttw.tinyreactors.blocks.BlockReactorComponentDirectional;
import com.arclighttw.tinyreactors.blocks.BlockTiny;
import com.arclighttw.tinyreactors.inits.TRBlocks;
import com.arclighttw.tinyreactors.tiles.TileEntityCapacitor;
import com.arclighttw.tinyreactors.util.Util;
import com.arclighttw.tinyreactors.util.Util.EnumTextAlignment;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTileEntityCapacitor extends TileEntitySpecialRenderer<TileEntityCapacitor>
{
	@Override
	public void render(TileEntityCapacitor tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(tile == null || !tile.hasWorld())
			return;
		
		IBlockState state = tile.getWorld().getBlockState(tile.getPos());
		EnumFacing facing = state.getValue(BlockReactorComponentDirectional.FACING);
		
		IBlockState neighbor = tile.getWorld().getBlockState(tile.getPos().offset(facing));
		
		if(neighbor.isOpaqueCube())
			return;
		
		if(neighbor.getBlock() instanceof BlockTiny && neighbor.getBlock() != TRBlocks.REACTOR_GLASS)
			return;
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		
		switch(facing)
		{
		case NORTH:
			break;
		case SOUTH:
			GlStateManager.rotate(180, 0, 1, 0);
			GlStateManager.translate(-1, 0, -1.001F);
			break;
		case EAST:
			GlStateManager.rotate(270, 0, 1, 0);
			GlStateManager.translate(0, 0, -1.001F);
			break;
		case WEST:
			GlStateManager.rotate(90, 0, 1, 0);
			GlStateManager.translate(-0.999F, 0, 0);
			break;
		default:
			break;
		}
		
		Util.drawTextInWorld(
				Arrays.asList(
						"Stored:",
						String.format("%,d RF", tile.getEnergyStored()),
						"Capacity:",
						String.format("%,d RF", tile.getMaxEnergyStored())
					),
				0.6F, 128, 22, EnumTextAlignment.CENTER
			);
		
		Util.drawTextInWorld(String.format("Rate Cap: %,d RF", tile.getEnergyStorage().getMaxExtract()), 0.3F, 248, 2, EnumTextAlignment.RIGHT);
		
		GlStateManager.popMatrix();
	}
}
