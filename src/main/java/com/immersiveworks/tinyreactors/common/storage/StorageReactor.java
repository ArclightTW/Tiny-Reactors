package com.immersiveworks.tinyreactors.common.storage;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.immersiveworks.tinyreactors.api.energy.EnergyStorageNBT;
import com.immersiveworks.tinyreactors.api.energy.IEnergyStorageNBT;
import com.immersiveworks.tinyreactors.api.temperature.ITemperatureStorage;
import com.immersiveworks.tinyreactors.common.blocks.BlockReactorAirVent;
import com.immersiveworks.tinyreactors.common.inits.Blocks;
import com.immersiveworks.tinyreactors.common.inits.Configs;
import com.immersiveworks.tinyreactors.common.temperature.TemperatureStorageCritical;
import com.immersiveworks.tinyreactors.common.tiles.IReactorTile;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorAirVent;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorController;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorTransferPort;
import com.immersiveworks.tinyreactors.common.util.Reactants;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.IEnergyStorage;

public class StorageReactor extends StorageMultiblock {
	
	private TileEntityReactorController controller;
	
	private ITemperatureStorage temperature;
	private float temperatureGain;
	private float temperatureCooldown;
	
	private IEnergyStorageNBT energy;
	private int energyGain;
	
	private List<BlockPos> transferPorts;
	private List<BlockPos> airBlocks;
	
	private int countController;
	
	public StorageReactor( TileEntityReactorController controller ) {
		this.controller = controller;
		
		temperature = new TemperatureStorageCritical( 1000, false, true );
		energy = new EnergyStorageNBT( 1000000 );
		
		transferPorts = Lists.newLinkedList();
		airBlocks = Lists.newLinkedList();
	}
	
	@Override
	public void tick( World world ) {
		if( !isValid )
			return;
		
		performTemperature( world );
		performEnergy( world );
	}
	
	@Override
	public void writeToNBT( NBTTagCompound compound ) {
		super.writeToNBT( compound );
		energy.writeToNBT( compound );
		temperature.writeToNBT( compound );
		
		NBTTagCompound reactor = new NBTTagCompound();
		reactor.setFloat( "temperatureGain", temperatureGain );
		reactor.setFloat( "temperatureCooldown", temperatureCooldown );
		reactor.setInteger( "energyGain", energyGain );
		
		reactor.setInteger( "countController", countController );

		NBTTagList airBlocks = new NBTTagList();
		for( int i = 0; i < this.airBlocks.size(); i++ )
			airBlocks.appendTag( NBTUtil.createPosTag( this.airBlocks.get( i ) ) );
		reactor.setTag( "airBlocks", airBlocks );
		
		NBTTagList transferPorts = new NBTTagList();
		for( int i = 0; i < this.transferPorts.size(); i++ )
			transferPorts.appendTag( NBTUtil.createPosTag( this.transferPorts.get( i ) ) );
		reactor.setTag( "transferPorts", transferPorts );
		
		compound.setTag( "reactor", reactor );
	}
	
	@Override
	public void readFromNBT( NBTTagCompound compound ) {
		super.readFromNBT( compound );
		energy.readFromNBT( compound );
		temperature.readFromNBT( compound );
		
		NBTTagCompound reactor = compound.getCompoundTag( "reactor" );
		temperatureGain = reactor.getFloat( "temperatureGain" );
		temperatureCooldown = reactor.getFloat( "temperatureCooldown" );
		energyGain = reactor.getInteger( "energyGain" );
		
		countController = reactor.getInteger( "countController" );
		
		NBTTagList airBlocks = reactor.getTagList( "airBlocks", Constants.NBT.TAG_COMPOUND );
		for( int i = 0; i < airBlocks.tagCount(); i++ )
			this.airBlocks.add( NBTUtil.getPosFromTag( airBlocks.getCompoundTagAt( i ) ) );

		NBTTagList transferPorts = reactor.getTagList( "transferPorts", Constants.NBT.TAG_COMPOUND );
		for( int i = 0; i < transferPorts.tagCount(); i++ )
			this.transferPorts.add( NBTUtil.getPosFromTag( transferPorts.getCompoundTagAt( i ) ) );
	}
	
	@Override
	public boolean onPreCalculation( World world ) {
		temperature.setMaximumTemperature( Configs.REACTOR_TEMPERATURE ? Configs.REACTOR_TEMPERATURE_THRESHOLD : -1 );
		temperatureGain = 0;
		temperatureCooldown = 0;

		energyGain = 0;
		
		countController = 0;
		
		transferPorts.clear();
		airBlocks.clear();
		
		return true;
	}
	
	@Override
	public boolean onPostCalculation( World world ) {
		return countController == 1 && transferPorts.size() > 0;
	}
	
	@Override
	public void onValidationFinalized( World world ) {
		for( int i = 0; i < structure.size(); i++ ) {
			try {
				TileEntity tile = world.getTileEntity( structure.get( i ) );
				if(  tile instanceof IReactorTile )
					( ( IReactorTile )tile ).onStructureValidated( controller );
			}
			catch( Exception e ) {
			}
		}
		
		if( isValid ) {
			return;
		}
		
		energyGain = 0;
		temperature.setMaximumTemperature( Configs.REACTOR_TEMPERATURE ? 0 : -1 );
	}
	
	@Override
	public void onInternalBlockDetected( World world, BlockPos pos, IBlockState state ) {
		if( state.getBlock() == net.minecraft.init.Blocks.AIR ) {
			temperatureGain += Configs.REACTOR_TEMPERATURE ? Configs.REACTOR_AIR_TEMPERATURE_GAIN : 0;
			airBlocks.add( pos );
			return;
		}
		
		temperatureGain += Configs.REACTOR_TEMPERATURE ? Configs.REACTOR_REACTANT_TEMPERATURE_GAIN : 0;
		energyGain += Reactants.getReactantRate( state );
	}
	
	@Override
	public void onExternalBlockDetected( World world, BlockPos pos, IBlockState state ) {
		if( state.getBlock() == Blocks.REACTOR_CONTROLLER )
			countController += 1;
		
		if( state.getBlock() == Blocks.REACTOR_TRANSFER_PORT )
			transferPorts.add( pos );
		
		if( state.getBlock() == Blocks.REACTOR_HEAT_SINK )
			temperature.setMaximumTemperature( temperature.getMaximumTemperature() + ( Configs.REACTOR_TEMPERATURE && Configs.REACTOR_HEAT_SINK ? Configs.REACTOR_HEAT_SINK_AMOUNT : 0 ) );
		
		if( state.getBlock() == Blocks.REACTOR_AIR_VENT ) {
			TileEntityReactorAirVent vent = ( ( BlockReactorAirVent )state.getBlock() ).getTileEntity( world, pos );
			if( vent.isOperational() )
				temperatureCooldown += Configs.REACTOR_TEMPERATURE && Configs.REACTOR_AIR_VENT ? ( Configs.REACTOR_AIR_VENT_AMOUNT * vent.getVentType().ordinal() * ( vent.getTier() / 10F ) ) : 0;
		}
	}
	
	@Override
	public boolean isValidRoofCorner( IBlockState state ) {
		return state.getBlock() == Blocks.REACTOR_CASING;
	}
	
	@Override
	public boolean isValidRoofWall( IBlockState state ) {
		return state.getBlock() == Blocks.REACTOR_CASING;
	}
	
	@Override
	public boolean isValidRoofInterior( IBlockState state ) {
		return state.getBlock() == Blocks.REACTOR_CASING || state.getBlock() == Blocks.REACTOR_AIR_VENT;
	}
	
	@Override
	public boolean isValidBaseCorner( IBlockState state ) {
		return state.getBlock() == Blocks.REACTOR_CASING;
	}
	
	@Override
	public boolean isValidBaseWall( IBlockState state ) {
		return state.getBlock() == Blocks.REACTOR_CASING;
	}
	
	@Override
	public boolean isValidBaseInterior( IBlockState state ) {
		return state.getBlock() == Blocks.REACTOR_CASING;
	}
	
	@Override
	public boolean isValidCentralCorner( IBlockState state ) {
		return state.getBlock() == Blocks.REACTOR_CASING || state.getBlock() == Blocks.REACTOR_CONTROLLER || state.getBlock() == Blocks.REACTOR_TRANSFER_PORT;
	}
	
	@Override
	public boolean isValidCentralWall( IBlockState state ) {
		return state.getBlock() == Blocks.REACTOR_CASING || state.getBlock() == Blocks.REACTOR_GLASS || state.getBlock() == Blocks.REACTOR_CONTROLLER || state.getBlock() == Blocks.REACTOR_TRANSFER_PORT || state.getBlock() == Blocks.REACTOR_HEAT_SINK;
	}
	
	@Override
	public boolean isValidCentralInterior( IBlockState state ) {
		return Reactants.isValidReactant( state );
	}
	
	public boolean hasAvailableSpace() {
		return airBlocks.size() > 0;
	}
	
	@SuppressWarnings( "deprecation" )
	public void insertBlock( World world, ItemStack itemstack ) {
		if( !hasAvailableSpace() )
			return;
		
		Block block = Block.getBlockFromItem( itemstack.getItem() );
		if( block == null )
			return;

		BlockPos pos = airBlocks.remove( 0 );
		world.setBlockState( pos, block.getStateFromMeta( itemstack.getItemDamage() ) );
		validateStructure( world, origin, null );
	}
	
	public ITemperatureStorage getTemperature() {
		return temperature;
	}
	
	public float getTemperatureGain() {
		return temperatureGain;
	}
	
	public void changeTemperatureCooldown( TileEntityReactorAirVent airVent ) {
		if( !Configs.REACTOR_TEMPERATURE || !Configs.REACTOR_AIR_VENT )
			return;
		
		if( airVent.isOperational() )
			temperatureCooldown += Configs.REACTOR_AIR_VENT_AMOUNT * airVent.getVentType().ordinal() * ( airVent.getTier() / 10F );
		else
			temperatureCooldown -= Configs.REACTOR_AIR_VENT_AMOUNT * airVent.getVentType().ordinal() * ( airVent.getTier() / 10F );

		if( runnable != null )
			runnable.run();
	}
	
	public float getTemperatureCooldown() {
		return temperatureCooldown;
	}
	
	public int getEnergyGain() {
		return ( int )( energyGain * getEnergyMultiplier() );
	}
	
	private void performTemperature( World world ) {
		if( Configs.REACTOR_TEMPERATURE ) {
			temperature.receiveHeat( temperatureGain - temperatureCooldown, false );
			
			// TODO: Test meltdown modes
			if( Configs.REACTOR_MELTDOWN && temperature.isCritical() ) {
				switch( Configs.REACTOR_MELTDOWN_MODE ) {
				case 0:	// Explosive
					world.createExplosion( null, origin.getX(), origin.getY(), origin.getZ(), 25, true );
					return;
				case 1:	// Implosive
					world.createExplosion( null, start.getX() + ( end.getX() - start.getX() ) / 2, start.getY() + ( end.getY() - start.getY() ) / 2, start.getZ() + ( end.getZ() - start.getZ() ), 15, true );
					return;
				case 2:	// Chained
					for( int i = 0; i < structure.size(); i++ ) {
						BlockPos pos = structure.get( i );
						
						Block block = world.getBlockState( pos ).getBlock();
						if( block == Blocks.REACTOR_CASING || block == Blocks.REACTOR_GLASS )
							continue;
						
						world.createExplosion( null, pos.getX(), pos.getY(), pos.getZ(), 10, true );
					}
					
					return;
				case 3:	// Consumption
					for( int x = start.getX() + 1; x <= end.getX() - 1; x++ )
						for( int z = start.getZ() + 1; z <= end.getZ() - 1; z++ )
							for( int y = start.getY() + 1; y <= end.getY() - 1; y ++ )
								world.setBlockToAir( new BlockPos( x, y, z ) );
					
					return;
				case 4:	// Drainage
					List<IEnergyStorage> energies = Lists.newLinkedList();
					for( BlockPos pos : transferPorts ) {
						TileEntity tile = world.getTileEntity( pos );
						if( tile == null || !( tile instanceof TileEntityReactorTransferPort ) )
							continue;
						energies.add( ( ( TileEntityReactorTransferPort )tile ).getInternalEnergy() );
					}
					
					if( energies.size() == 0 )
						return;
					
					for( IEnergyStorage energy : energies )
						energy.extractEnergy( energy.getMaxEnergyStored(), false );
					
					return;
				case 5:	// No Effect
					break;
				}
			}
		}
	}
	
	private void performEnergy( World world ) {
		if( !Configs.REACTOR_ENERGY )
			return;
		
		if( energy.getEnergyStored() > 0 ) {
			int requiredToFill = 0;
			int amount = 0;
			
			Map<IEnergyStorage, Integer> energies = Maps.newHashMap();
			for( BlockPos pos : transferPorts ) {
				TileEntity tile = world.getTileEntity( pos );
				if( tile == null || !( tile instanceof TileEntityReactorTransferPort ) )
					continue;
				TileEntityReactorTransferPort transferPort = ( TileEntityReactorTransferPort )tile;
				if( transferPort.powerDemand == 0 )
					continue;
				energies.put( transferPort.getInternalEnergy(), transferPort.powerDemand );
			}
			
			if( energies.size() > 0 ) {
				for( Map.Entry<IEnergyStorage, Integer> energy : energies.entrySet() )
					requiredToFill += energy.getValue() == -1 ? energy.getKey().getMaxEnergyStored() - energy.getKey().getEnergyStored() : energy.getValue();
				
				if( requiredToFill <= energy.getEnergyStored() ) {
					for( Map.Entry<IEnergyStorage, Integer> energy : energies.entrySet() ) {
						int amnt = energy.getValue() == -1 ? energy.getKey().getMaxEnergyStored() - energy.getKey().getEnergyStored() : energy.getValue();
						amount += amnt;
						
						energy.getKey().receiveEnergy( amnt, false );
					}
					
					energy.extractEnergy( amount, false );
				}
				else {
					int inNeed = 0;
					
					for( Map.Entry<IEnergyStorage, Integer> energy : energies.entrySet() )
						if( energy.getValue() > 0 || energy.getKey().getEnergyStored() < energy.getKey().getMaxEnergyStored() )
							inNeed++;
					
					if( inNeed > 0 ) {
						for( Map.Entry<IEnergyStorage, Integer> energy : energies.entrySet() ) {
							int amnt = this.energy.getEnergyStored() / inNeed;
							if( energy.getValue() > 0 && amnt > energy.getValue() )
								amnt = energy.getValue();
							if( amnt > energy.getKey().getMaxEnergyStored() - energy.getKey().getEnergyStored() )
								amnt = energy.getKey().getMaxEnergyStored() - energy.getKey().getEnergyStored();
							if( amnt <= 0 )
								continue;
							amount += amnt;
							
							energy.getKey().receiveEnergy( amnt, false );
						}
						
						energy.extractEnergy( amount, false );
					}
				}
			}
		}
		
		energy.receiveEnergy( getEnergyGain(), false );
	}
	
	public float getEnergyMultiplier() {
		float peak = temperature.getPeakEfficiencyTemperature();
		float diff = temperature.getCurrentTemperature() > peak ? temperature.getCurrentTemperature() - peak : peak - temperature.getCurrentTemperature();
		return 1 + ( Configs.REACTOR_ENERGY_GENERATION_MULTIPLIER - 1 ) * ( ( peak - diff ) / peak );
	}
	
}
