package com.immersiveworks.tinyreactors.common.processes;

import com.immersiveworks.tinyreactors.api.processes.IProcess;
import com.immersiveworks.tinyreactors.common.storage.StorageReactor;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorPreigniter;

import net.minecraft.tileentity.TileEntityFurnace;

public class ProcessReactorPreigniter implements IProcess {

	private StorageReactor structure;
	private TileEntityReactorPreigniter preigniter;
	
	private int burnTime;
	
	public ProcessReactorPreigniter() { }
	public ProcessReactorPreigniter( TileEntityReactorPreigniter preigniter, StorageReactor structure ) {
		this.preigniter = preigniter;
		this.burnTime = preigniter.getBurnTime();
		
		this.structure = structure;
	}
	
	@Override
	public void update() {
		if( burnTime == 0 ) {
			for( int i = 0; i < preigniter.getInternalItem().getSlots(); i++ )
				if( !preigniter.getInternalItem().getStackInSlot( i ).isEmpty() ) {
					burnTime = TileEntityFurnace.getItemBurnTime( preigniter.getInternalItem().getStackInSlot( i ) ) / 40;
					if( burnTime > 0 ) {
						preigniter.getInternalItem().extractItem( i, 1, false );
						preigniter.syncClient();
					}
					
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
	
	@Override
	public void onDeath( boolean serverClosed ) {
		if( serverClosed )
			preigniter.setBurnTime( burnTime );
		else
			preigniter.setBurnTime( 0 );
	}
	
}
