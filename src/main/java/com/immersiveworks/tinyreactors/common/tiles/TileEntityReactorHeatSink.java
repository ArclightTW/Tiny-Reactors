package com.immersiveworks.tinyreactors.common.tiles;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityReactorHeatSink extends TileEntityTiny implements IReactorTile {

	public TileEntityReactorHeatSink() {
	}
	
	@Override
	public void onStructureValidated( TileEntityReactorController controller ) {
	}
	
	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound ) {
		super.writeToNBT( compound );
		return compound;
	}
	
	@Override
	public void readFromNBT( NBTTagCompound compound ) {
		super.readFromNBT( compound );
	}
	
}
