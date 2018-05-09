package com.immersiveworks.tinyreactors.api.item;

import net.minecraftforge.items.IItemHandler;

public interface IInternalInventory {

	public boolean isAccessible();
	public IItemHandler getInternalItem();
	
}
