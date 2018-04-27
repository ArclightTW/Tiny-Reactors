package com.immersiveworks.tinyreactors.api.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

public class ItemHandlerNBT extends ItemStackHandler implements IItemHandlerNBT {

	public ItemHandlerNBT() {
		this( 1 );
	}
	
	public ItemHandlerNBT( int size ) {
		this( NonNullList.withSize( size, ItemStack.EMPTY ) );
	}
	
	public ItemHandlerNBT( NonNullList<ItemStack> stacks ) {
		this.stacks = stacks;
	}
	
	@Override
	public void writeToNBT( NBTTagCompound compound ) {
		NBTTagCompound item = new NBTTagCompound();
		
		NBTTagList items = new NBTTagList();
		for( int i = 0; i < stacks.size(); i++ )
			items.appendTag( stacks.get( i ).writeToNBT( new NBTTagCompound() ) );
		item.setTag( "items", items );
		
		compound.setTag( "item", item );
	}
	
	@Override
	public void readFromNBT( NBTTagCompound compound ) {
		NBTTagCompound item = compound.getCompoundTag( "item" );
		
		NBTTagList items = item.getTagList( "items", Constants.NBT.TAG_COMPOUND );
		for( int i = 0; i < items.tagCount(); i++ )
			stacks.set( i, new ItemStack( items.getCompoundTagAt( i ) ) );
	}
	
}
