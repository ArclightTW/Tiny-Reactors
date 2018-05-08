package com.immersiveworks.tinyreactors.common.temperature;

import java.util.Random;

import com.immersiveworks.tinyreactors.api.temperature.TemperatureStorage;
import com.immersiveworks.tinyreactors.common.inits.Configs;

public class TemperatureStorageCritical extends TemperatureStorage {

	private Random random;
	private Runnable runnable;
	
	public TemperatureStorageCritical( float maximumTemperature ) { this( maximumTemperature, false ); }
	public TemperatureStorageCritical( float maximumTemperature, boolean exceedsCap ) { this( maximumTemperature, exceedsCap, exceedsCap ); }
	
	public TemperatureStorageCritical( float maximumTemperature, boolean exceedsMin, boolean exceedsMax ) {
		super( maximumTemperature, exceedsMin, exceedsMax );
		random = new Random();
	}
	
	public void setListener( Runnable runnable ) {
		this.runnable = runnable;
	}
	
	@Override
	public float extractHeat( float maxExtract, boolean simulate ) {
		float value = super.extractHeat( maxExtract, simulate );
		if( runnable != null )
			runnable.run();
		return value;
	}
	
	@Override
	public float receiveHeat( float maxReceive, boolean simulate ) {
		float value = super.receiveHeat( maxReceive, simulate );
		if( runnable != null )
			runnable.run();
		return value;
	}
	
	@Override
	public float getCriticalTemperature() {
		return getMaximumTemperature() * ( Configs.REACTOR_TEMPERATURE_CRITICAL_PERCENTAGE / 100F );
	}
	
	@Override
	public boolean isCritical() {
		if( getCurrentTemperature() < getCriticalTemperature() )
			return false;
		
		int rand = ( int )( getMaximumTemperature() - getCriticalTemperature() );
		if( rand <= 0 )
			return false;
		
		return random.nextInt( rand ) >= ( int )( getMaximumTemperature() - getCriticalTemperature() ) - ( int )( getCurrentTemperature() - getCriticalTemperature() );
	}
	
	@Override
	public float getPeakEfficiencyTemperature() {
		return getMaximumTemperature() * ( Configs.REACTOR_ENERGY_EFFICIENCY_PERCENTAGE / 100F );
	}
	
}
