package com.immersiveworks.tinyreactors.common.tiles;

import com.immersiveworks.tinyreactors.api.energy.EnergyStorageNBT;
import com.immersiveworks.tinyreactors.api.energy.IEnergyStorageNBT;
import com.immersiveworks.tinyreactors.common.inits.Configs;
import com.immersiveworks.tinyreactors.common.storage.StorageReactor;

import net.minecraft.block.BlockDirectional;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileEntityReactorSurgeProtector extends TileEntityTiny implements IReactorTile {

	private IEnergyStorageNBT energy;
	
	private boolean isActive;
	private int thresholdMinimum;
	private int thresholdMaximum;
	
	private BlockPos controllerPos;
	private TileEntityReactorController controller;
	
	public TileEntityReactorSurgeProtector() {
		thresholdMinimum = ( int )( StorageReactor.BASE_TEMPERATURE * ( ( Configs.REACTOR_ENERGY_EFFICIENCY_PERCENTAGE - 5 ) / 100F ) );
		if( thresholdMinimum < 0 )
			thresholdMinimum = 0;
		thresholdMaximum = ( int )( StorageReactor.BASE_TEMPERATURE * ( ( Configs.REACTOR_TEMPERATURE_CRITICAL_PERCENTAGE - 5 ) / 100F ) );
		if( thresholdMaximum < thresholdMinimum ) {
			int temp = thresholdMaximum;
			thresholdMaximum = thresholdMinimum;
			thresholdMinimum = temp;
		}
		
		energy = new EnergyStorageNBT( 250000, 250000, 0 );
		registerCapability( "energy", CapabilityEnergy.ENERGY, ( facing ) -> {
			return world.getBlockState( pos ).getValue( BlockDirectional.FACING ) == facing ? energy : null;
		} );
		
		registerPulsar( () -> { 
			if( controller == null )
				return;
			
			if( controller.getStructure().getTemperature().getCurrentTemperature() >= thresholdMaximum ) {
				if( controller.isActive() )
					controller.setActive( false, false );
				
				isActive = true;
				syncClient();
			}
			
			if( controller.getStructure().getTemperature().getCurrentTemperature() <= thresholdMinimum ) {
				if( !controller.isActive() )
					controller.setActive( true, false );
				
				isActive = false;
				syncClient();
			}
		} );
	}

	@Override
	public void onStructureValidated( TileEntityReactorController controller ) {
		this.controller = controller;
		this.controllerPos = this.controller != null ? this.controller.getPos() : null;
		
		setMinimumThreshold( thresholdMinimum );
		setMaximumThreshold( thresholdMaximum );
		
		syncClient();
	}
	
	@Override
	public void onLoad() {
		controller = null;
		if( controllerPos != null ) {
			TileEntity tile = world.getTileEntity( controllerPos );
			if( tile != null && tile instanceof TileEntityReactorController )
				controller = ( TileEntityReactorController )tile;
		}
		
		syncClient();
	}
	
	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound ) {
		super.writeToNBT( compound );
		energy.writeToNBT( compound );
		
		NBTTagCompound surge = new NBTTagCompound();
		surge.setBoolean( "isActive", isActive );
		surge.setInteger( "thresholdMinimum", thresholdMinimum );
		surge.setInteger( "thresholdMaximum", thresholdMaximum );
		if( controllerPos != null )
			surge.setTag( "controller", NBTUtil.createPosTag( controllerPos ) );
		
		compound.setTag( "surge", surge );
		return compound;
	}
	
	@Override
	public void readFromNBT( NBTTagCompound compound ) {
		super.readFromNBT( compound );
		energy.readFromNBT( compound );
		
		NBTTagCompound surge = compound.getCompoundTag( "surge" );
		isActive = surge.getBoolean( "isActive" );
		thresholdMinimum = surge.getInteger( "thresholdMinimum" );
		thresholdMaximum = surge.getInteger( "thresholdMaximum" );
		controllerPos = surge.hasKey( "controller" ) ? NBTUtil.getPosFromTag( surge.getCompoundTag( "controller" ) ) : null;
		
		if( world != null )
			onLoad();
	}
	
	public void setMinimumThreshold( int minimum ) {
		if( minimum > thresholdMaximum - 50 )
			minimum = thresholdMaximum - 50;
		if( minimum < 0 )
			minimum = 0;
		if( controller != null && minimum > controller.getStructure().getTemperature().getMaximumTemperature() )
			minimum = ( int )controller.getStructure().getTemperature().getMaximumTemperature();
		
		thresholdMinimum = minimum;
		syncClient();
	}
	
	public int getMinimumThreshold() {
		return thresholdMinimum;
	}
	
	public void setMaximumThreshold( int maximum ) {
		if( maximum < 0 )
			maximum = 0;
		if( maximum < thresholdMinimum + 50 )
			maximum = thresholdMinimum + 50;
		if( controller != null && maximum > controller.getStructure().getTemperature().getMaximumTemperature() )
			maximum = ( int )controller.getStructure().getTemperature().getMaximumTemperature();
		
		thresholdMaximum = maximum;
		syncClient();
	}
	
	public int getMaximumThreshold() {
		return thresholdMaximum;
	}
	
	public TileEntityReactorController getController() {
		return controller;
	}

}
