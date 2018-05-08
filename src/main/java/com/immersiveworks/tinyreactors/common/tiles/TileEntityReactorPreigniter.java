package com.immersiveworks.tinyreactors.common.tiles;

import com.immersiveworks.tinyreactors.api.item.IItemHandlerNBT;
import com.immersiveworks.tinyreactors.api.item.ItemHandlerNBT;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntityReactorPreigniter extends TileEntityTiny {
	
	private IItemHandlerNBT item;
	
	public TileEntityReactorPreigniter() {
		item = new ItemHandlerNBT( 9 );
		registerCapability( "item", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, ( facing ) -> item );
	}
	
	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound ) {
		super.writeToNBT( compound );
		item.writeToNBT( compound );
		
		return compound;
	}
	
	@Override
	public void readFromNBT( NBTTagCompound compound ) {
		super.readFromNBT( compound );
		item.readFromNBT( compound );
	}
	
	public ItemStack insertCombustible( ItemStack itemstack ) {
		if( !TileEntityFurnace.isItemFuel( itemstack ) )
			return itemstack;
		
		int emptySlot = -1;
		
		for( int i = 0; i < item.getSlots(); i++ ) {
			ItemStack current = item.getStackInSlot( i );
			if( current.isEmpty() ) {
				if( emptySlot == -1 )
					emptySlot = i;
				
				continue;
			}
			
			if( !current.isItemEqual( itemstack ) )
				continue;
			
			itemstack = item.insertItem( i, itemstack, false );
			if( itemstack.isEmpty() )
				return ItemStack.EMPTY;
		}
		
		if( emptySlot == -1 )
			return itemstack;
		
		ItemStack remaining = item.insertItem( emptySlot, itemstack, false );
		syncClient();
		return remaining;
	}
	
	public ItemStack retreiveCombustible() {
		for( int i = item.getSlots() - 1; i >= 0; i-- ) {
			if( item.getStackInSlot( i ).isEmpty() )
				continue;
			
			ItemStack taken = item.extractItem( i, Integer.MAX_VALUE, false );
			syncClient();
			return taken;
		}
		
		return ItemStack.EMPTY;
	}
	
	public IItemHandlerNBT getInternalItem() {
		return item;
	}
	
}
