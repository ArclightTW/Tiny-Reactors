package com.immersiveworks.tinyreactors.api.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.IEnergyStorage;

public interface IEnergyStorageNBT extends IEnergyStorage {
	
	void writeToNBT( NBTTagCompound compound );
	void readFromNBT( NBTTagCompound compound );
	
	void setCanReceive( EnumFacing facing, boolean canReceive );
	void setCanExtract( EnumFacing facing, boolean canExtract );

	boolean canReceive( EnumFacing facing );
	boolean canExtract( EnumFacing facing );
	
}
