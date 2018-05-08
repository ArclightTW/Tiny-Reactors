package com.immersiveworks.tinyreactors.common.processes;

import com.immersiveworks.tinyreactors.api.IProcess;
import com.immersiveworks.tinyreactors.common.storage.StorageReactor;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorPreigniter;

import net.minecraft.tileentity.TileEntityFurnace;

// TODO: Process isn't saved, new one created on world load
public class ProcessReactorPreigniter implements IProcess {

	private StorageReactor structure;
	private TileEntityReactorPreigniter preigniter;
	
	private int burnTime;
	
	public ProcessReactorPreigniter( TileEntityReactorPreigniter preigniter, StorageReactor structure ) {
		this.preigniter = preigniter;
		this.structure = structure;
	}
	
	@Override
	public void update() {
		if( burnTime == 0 ) {
			for( int i = 0; i < preigniter.getInternalItem().getSlots(); i++ )
				if( preigniter.getInternalItem().getStackInSlot( i ).isEmpty() ) {
					burnTime = TileEntityFurnace.getItemBurnTime( preigniter.getInternalItem().getStackInSlot( i ) ) / 40;
					break;
				}
		}
		
		burnTime--;
		if( burnTime == 0 )
			return;
		
		structure.getTemperature().receiveHeat( 10, false );
	}
	
	@Override
	public boolean isDead() {
		if( structure.getTemperature().getCurrentTemperature() >= structure.getTemperature().getPeakEfficiencyTemperature() / 2F )
			return true;
		
		for( int i = 0; i < preigniter.getInternalItem().getSlots(); i++ )
			if( !preigniter.getInternalItem().getStackInSlot( i ).isEmpty() )
				return false;
		
		return true;
	}
	
}
