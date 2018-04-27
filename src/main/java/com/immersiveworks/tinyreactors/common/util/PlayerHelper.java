package com.immersiveworks.tinyreactors.common.util;

import com.immersiveworks.tinyreactors.common.inits.Items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PlayerHelper {

	public static boolean hasWrenchVisibility( EntityPlayer player ) {
		if( player.getHeldItemMainhand().getItem() == Items.TINY_WRENCH || player.getHeldItemOffhand().getItem() == Items.TINY_WRENCH )
			return true;
		
		for( ItemStack itemstack : player.getArmorInventoryList() )
			if( itemstack.hasTagCompound() && itemstack.getTagCompound().getBoolean( "hasInbuiltWrench" ) )
				return true;
			
		return false;
	}
	
}
