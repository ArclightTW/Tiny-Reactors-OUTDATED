package com.arclighttw.tinyreactors.client.render;

import com.arclighttw.tinyreactors.tiles.TileEntityDegradedReactant;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTileEntityDegradedReactant extends TileEntitySpecialRenderer<TileEntityDegradedReactant>
{
	@Override
	public void render(TileEntityDegradedReactant tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(tile == null || !tile.hasWorld())
			return;
	}
}