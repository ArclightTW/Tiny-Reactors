package com.immersiveworks.tinyreactors.api.temperature;

import net.minecraft.nbt.NBTTagCompound;

public interface ITemperatureStorage {

	/**
	 * 
	 * @return
	 */
	float getCurrentTemperature();
	
	/**
	 * 
	 * @param temperature
	 */
	void setCurrentTemperature( float temperature );
	
	/**
	 * 
	 * @return
	 */
	float getMaximumTemperature();
	
	/**
	 * 
	 * @param temperature
	 */
	void setMaximumTemperature( float temperature );
	
	/**
	 * 
	 * @param heat
	 * @param simulate
	 */
	float receiveHeat( float heat, boolean simulate );
	
	/**
	 * 
	 * @param heat
	 * @param simulate
	 */
	float extractHeat( float heat, boolean simulate );
	
	/**
	 * 
	 * @return
	 */
	default boolean canReceive() {
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	default boolean canExtract() {
		return true;
	}

	/**
	 * 
	 * @param compound
	 * @return
	 */
	default NBTTagCompound writeToNBT( NBTTagCompound compound ) {
		return compound;
	}
	
	/**
	 * 
	 * @param compound
	 */
	default void readFromNBT( NBTTagCompound compound ) {
	}
	
	default float getCriticalTemperature() {
		return getMaximumTemperature();
	}
	
	/**
	 * 
	 * @return
	 */
	default boolean isCritical() {
		return getCurrentTemperature() == getCriticalTemperature();
	}
	
	default float getPeakEfficiencyTemperature() {
		return getMaximumTemperature() / 2;
	}
	
}
