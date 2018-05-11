package com.immersiveworks.tinyreactors.api.helpers;

import com.immersiveworks.tinyreactors.api.TinyReactorsAPI;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PlayerHelper {

	public static boolean hasWrenchVisibility( EntityPlayer player ) {
		if( player.getHeldItemMainhand().getItem() == TinyReactorsAPI.getItem( "tiny_wrench" ) || player.getHeldItemOffhand().getItem() == TinyReactorsAPI.getItem( "tiny_wrench" ) )
			return true;
		
		for( ItemStack itemstack : player.getArmorInventoryList() )
			if( itemstack.hasTagCompound() && itemstack.getTagCompound().getBoolean( "hasInbuiltWrench" ) )
				return true;
			
		return false;
	}
	
}
