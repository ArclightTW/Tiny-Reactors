package com.immersiveworks.tinyreactors.api.temperature;

import net.minecraft.nbt.NBTTagCompound;

public class TemperatureStorage implements ITemperatureStorage {

	private float currentTemperature;
	private float maximumTemperature;
	
	private boolean exceedsMin;
	private boolean exceedsMax;
	
	public TemperatureStorage( float maximumTemperature ) { this( maximumTemperature, false ); }
	public TemperatureStorage( float maximumTemperature, boolean exceedsCap ) { this( maximumTemperature, exceedsCap, exceedsCap ); }
	
	public TemperatureStorage( float maximumTemperature, boolean exceedsMin, boolean exceedsMax ) {
		this.maximumTemperature = maximumTemperature;
		this.exceedsMin = exceedsMin;
		this.exceedsMax = exceedsMax;
	}
	
	@Override
	public float receiveHeat( float maxReceive, boolean simulate ) {
		if( maxReceive < 0 )
			return extractHeat( -maxReceive, simulate );
		
		if( !canReceive() )
			return 0;
		
		float received = exceedsMax ? maxReceive : Math.min( maximumTemperature - currentTemperature, maxReceive );
		if( !simulate )
			currentTemperature += received;
		
		return received;
	}

	@Override
	public float extractHeat( float maxExtract, boolean simulate ) {
		if( maxExtract < 0 )
			return receiveHeat( -maxExtract, simulate );
		
		if( !canExtract() )
			return 0;
		
		float extracted = exceedsMin ? maxExtract : Math.min( currentTemperature, maxExtract );
		if( !simulate )
			currentTemperature -= extracted;
		
		return extracted;
	}
	
	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound ) {
		NBTTagCompound temperature = new NBTTagCompound();
		temperature.setFloat( "current", currentTemperature );
		temperature.setFloat( "maximum", maximumTemperature );
		temperature.setBoolean( "exceedsMin", exceedsMin );
		temperature.setBoolean( "exceedsMax", exceedsMax );
		
		compound.setTag( "temperature", temperature );
		return compound;
	}
	
	@Override
	public void readFromNBT( NBTTagCompound compound ) {
		NBTTagCompound temperature = compound.getCompoundTag( "temperature" );
		currentTemperature = temperature.getFloat( "current" );
		maximumTemperature = temperature.getFloat( "maximum" );
		exceedsMin = temperature.getBoolean( "exceedsMin" );
		exceedsMax = temperature.getBoolean( "exceedsMax" );
	}
	
	@Override
	public float getCurrentTemperature() {
		return currentTemperature;
	}

	@Override
	public void setCurrentTemperature( float temperature ) {
		currentTemperature = temperature;
		
		if( !exceedsMax && currentTemperature >= maximumTemperature )
			currentTemperature = maximumTemperature;
		
		if( !exceedsMin && currentTemperature <= 0 )
			currentTemperature = 0;
	}

	@Override
	public float getMaximumTemperature() {
		return maximumTemperature;
	}

	@Override
	public void setMaximumTemperature( float temperature ) {
		maximumTemperature = temperature;
		
		if( !exceedsMax && currentTemperature >= maximumTemperature )
			currentTemperature = maximumTemperature;
	}
	
}
