package com.immersiveworks.tinyreactors.common.tiles;

import com.immersiveworks.tinyreactors.api.energy.EnergyStorageNBT;
import com.immersiveworks.tinyreactors.api.energy.IEnergyStorageNBT;

import net.minecraftforge.energy.CapabilityEnergy;

public class TileEntityEnergyConduit extends TileEntityTiny {

	private IEnergyStorageNBT energy;
	
	public TileEntityEnergyConduit() {
		energy = new EnergyStorageNBT( 0 );
		registerCapability( "energy", CapabilityEnergy.ENERGY, ( facing ) -> energy );
	}
	
}
