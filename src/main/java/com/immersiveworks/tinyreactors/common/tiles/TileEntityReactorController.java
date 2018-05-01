package com.immersiveworks.tinyreactors.common.tiles;

import com.immersiveworks.tinyreactors.common.storage.StorageReactor;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityReactorController extends TileEntityTiny implements IReactorTile {

	private StorageReactor structure;
	
	public TileEntityReactorController() {
		structure = new StorageReactor( this );
		structure.setValidationListener( () -> {
			syncClient();
		} );
	}
	
	@Override
	public void onLoad() {
		registerPulsar( 1, () -> {
			structure.tick( world );
		} );
		
		structure.validateStructure( world, pos, null );
	}
	
	@Override
	public void onStructureValidated( TileEntityReactorController controller ) {
	}
	
	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound ) {
		super.writeToNBT( compound );
		structure.writeToNBT( compound );
		return compound;
	}
	
	@Override
	public void readFromNBT( NBTTagCompound compound ) {
		super.readFromNBT( compound );
		structure.readFromNBT( compound );
	}
	
	public StorageReactor getStructure() {
		return structure;
	}
	
}
