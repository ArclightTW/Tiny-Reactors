package com.immersiveworks.tinyreactors.common.tiles;

import com.immersiveworks.tinyreactors.common.storage.StorageReactor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityReactorGlass extends TileEntityTiny implements IReactorTile {

	private BlockPos structurePos;
	private StorageReactor structure;
	
	@Override
	public void onStructureValidated( StorageReactor reactor ) {
		structure = reactor == null ? null : reactor.isValid() ? reactor : null;
		structurePos = structure != null ? structure.origin : null;
		syncClient();
	}
	
	@Override
	public void onLoad() {
		structure = null;
		if( structurePos != null ) {
			TileEntity tile = world.getTileEntity( structurePos );
			if( tile != null && tile instanceof TileEntityReactorController )
				structure = ( ( TileEntityReactorController )tile ).getStructure();
		}
		
		syncClient();
	}
	
	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound ) {
		super.writeToNBT( compound );
		
		NBTTagCompound glass = new NBTTagCompound();
		if( structurePos != null ) glass.setTag( "structure", NBTUtil.createPosTag( structurePos ) );
		
		compound.setTag( "glass", glass );		
		return compound;
	}
	
	@Override
	public void readFromNBT( NBTTagCompound compound ) {
		super.readFromNBT( compound );
		NBTTagCompound glass = compound.getCompoundTag( "glass" );
		
		structurePos = glass.hasKey( "structure" ) ? NBTUtil.getPosFromTag( glass.getCompoundTag( "structure" ) ) : null;
		if( world != null )
			onLoad();
	}
	
	public StorageReactor getStructure() {
		return structure;
	}
	
}
