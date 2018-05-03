package com.immersiveworks.tinyreactors.common.items;

import java.util.List;

import com.immersiveworks.tinyreactors.common.inits.Items;
import com.immersiveworks.tinyreactors.common.properties.EnumAirVent;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorAirVent;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemVentilationFan extends ItemTiny {

	public ItemVentilationFan() {
		setHasSubtypes( true );
	}
	
	@Override
	public void addInformation( ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag ) {
		tooltip.add( String.format( "Base Melting Point: %,.0f C", EnumAirVent.FANS[ stack.getItemDamage() ].getMeltingPoint( null ) ) );
	}

	@Override
	public EnumActionResult onItemUseFirst( EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand ) {
		ItemStack itemstack = player.getHeldItem( hand );
		if( itemstack.isEmpty() || itemstack.getItem() != Items.VENTILATION_FAN )
			return EnumActionResult.PASS;
		
		TileEntity tile = world.getTileEntity( pos );
		if( tile == null || !( tile instanceof TileEntityReactorAirVent ) )
			return EnumActionResult.PASS;
		
		TileEntityReactorAirVent airVent = ( TileEntityReactorAirVent )tile;
		if( airVent.getVentType() != EnumAirVent.EMPTY && !player.addItemStackToInventory( new ItemStack( Items.VENTILATION_FAN, 1, airVent.getVentType().ordinal() - 1 ) ) )
			InventoryHelper.spawnItemStack( world, player.posX, player.posY, player.posZ, new ItemStack( Items.VENTILATION_FAN, 1, airVent.getVentType().ordinal() - 1 ) );
		
		airVent.setVentType( EnumAirVent.FANS[ itemstack.getItemDamage() ] );
		itemstack.setCount( itemstack.getCount() - 1 );
		return EnumActionResult.SUCCESS;
	}
	
	@Override
	public String getUnlocalizedName( ItemStack itemstack ) {
		return String.format( "%s_%s", getUnlocalizedName(), EnumAirVent.FANS[ itemstack.getItemDamage() ].getName() );
	}
	
	@Override
	public void getSubItems( CreativeTabs tab, NonNullList<ItemStack> items ) {
		for( EnumAirVent vent : EnumAirVent.FANS )
			items.add( new ItemStack( this, 1, vent.ordinal() - 1 ) );
	}
	
}
