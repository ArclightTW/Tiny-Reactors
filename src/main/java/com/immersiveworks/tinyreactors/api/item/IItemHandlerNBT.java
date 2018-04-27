package com.immersiveworks.tinyreactors.api.item;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;

public interface IItemHandlerNBT extends IItemHandler {

	void writeToNBT( NBTTagCompound compound );
	void readFromNBT( NBTTagCompound compound );
	
}
