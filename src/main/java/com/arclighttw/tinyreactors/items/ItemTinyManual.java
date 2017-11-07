package com.arclighttw.tinyreactors.items;

import com.arclighttw.tinyreactors.client.gui.GuiTinyManual;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemTinyManual extends ItemTiny
{
	public ItemTinyManual()
	{
		super("tiny_manual");
		setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if(world.isRemote)
			Minecraft.getMinecraft().displayGuiScreen(new GuiTinyManual());
		
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}
