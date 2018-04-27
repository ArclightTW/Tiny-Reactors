package com.immersiveworks.tinyreactors.common.tiles;

import com.immersiveworks.tinyreactors.common.storage.StorageReactor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityReactorAirVent extends TileEntityTiny implements IReactorTile {

	private boolean operational;
	
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
		
		NBTTagCompound airVent = new NBTTagCompound();
		airVent.setBoolean( "operational", operational );
		if( structurePos != null )
			airVent.setTag( "structure", NBTUtil.createPosTag( structurePos ) );
		
		compound.setTag( "airVent", airVent );		
		return compound;
	}
	
	@Override
	public void readFromNBT( NBTTagCompound compound ) {
		super.readFromNBT( compound );
		NBTTagCompound airVent = compound.getCompoundTag( "airVent" );
		
		operational = airVent.getBoolean( "operational" );
		structurePos = airVent.hasKey( "structure" ) ? NBTUtil.getPosFromTag( airVent.getCompoundTag( "structure" ) ) : null;
		
		if( world != null )
			onLoad();
	}
	
	public void toggleOperational() {
		operational = !operational;
		syncClient();
		
		if( structure != null )
			structure.changeTemperatureCooldown( this );
	}
	
	public boolean isOperational() {
		return operational;
	}
	
	public StorageReactor getStructure() {
		return structure;
	}
	
}
