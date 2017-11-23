package com.arclighttw.tinyreactors.blocks;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.arclighttw.tinyreactors.helpers.TranslationHelper;
import com.arclighttw.tinyreactors.lib.EnumFormatting;
import com.arclighttw.tinyreactors.lib.nbt.IStorableContents;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTiny extends Block implements ITileEntityProvider
{
	protected List<String> standardTooltip;
	protected List<String> additionalTooltip;
	
	public BlockTiny(String name, Material material)
	{
		super(material);
		setUnlocalizedName(name);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
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
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if(state.getBlock() instanceof IStorableContents)
			((IStorableContents)state.getBlock()).loadContents(stack, world, pos, state);
	}
}
