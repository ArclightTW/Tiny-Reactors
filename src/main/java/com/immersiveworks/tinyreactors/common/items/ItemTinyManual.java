package com.immersiveworks.tinyreactors.common.items;

import com.immersiveworks.tinyreactors.api.manual.IManualEntry;
import com.immersiveworks.tinyreactors.client.gui.GuiTinyManual;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTinyManual extends ItemTiny {

	public ItemTinyManual() {
		setMaxStackSize( 1 );
		setContainerItem( this );
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick( World world, EntityPlayer player, EnumHand hand ) {
		GuiTinyManual.instance.openCurrent();
		return ActionResult.newResult( EnumActionResult.SUCCESS, player.getHeldItem( hand ) );
	}
	
	@Override
	public EnumActionResult onItemUseFirst( EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand ) {
		Block block = world.getBlockState( pos ).getBlock();
		if( block instanceof IManualEntry ) {
			GuiTinyManual.instance.openPage( ( IManualEntry )block );
			return EnumActionResult.SUCCESS;
		}
		
		return EnumActionResult.PASS;
	}
	
}
