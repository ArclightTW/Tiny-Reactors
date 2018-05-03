package com.immersiveworks.tinyreactors.common.tiles;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.immersiveworks.tinyreactors.api.energy.EnergyStorageNBT;
import com.immersiveworks.tinyreactors.api.energy.IEnergyStorageNBT;
import com.immersiveworks.tinyreactors.common.inits.Blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityTinyEnergy extends TileEntityTiny {
	
	protected IEnergyStorageNBT energy;
	protected int maxEnergyTransfer;
	
	public TileEntityTinyEnergy( int capacity, int energyTickDelay, int maxEnergyTransfer ) {
		this.maxEnergyTransfer = maxEnergyTransfer;
		
		energy = new EnergyStorageNBT( capacity );
		registerCapability( "energy", CapabilityEnergy.ENERGY, ( facing ) -> energy );
		
		registerPulsar( energyTickDelay, () -> handleEnergyTransfer() );
	}
	
	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound ) {
		super.writeToNBT( compound );
		energy.writeToNBT( compound );
		return compound;
	}
	
	@Override
	public void readFromNBT( NBTTagCompound compound ) {
		super.readFromNBT( compound );
		energy.readFromNBT( compound );
	}
	
	public IEnergyStorage getInternalEnergy() {
		return energy;
	}
	
	protected void handleEnergyTransfer() {
		if( energy.getEnergyStored() <= 0 || !energy.canExtract() )
			return;
		
		int requiredToFill = 0;
		Map<BlockPos, IEnergyStorageWrapper> energies = getConnectedSinks();
		modifySinks( energies );
		
		if( energies.size() == 0 )
			return;
		
		for( Map.Entry<BlockPos, IEnergyStorageWrapper> energy : energies.entrySet() )
			requiredToFill += energy.getValue().energy.getMaxEnergyStored() - energy.getValue().energy.getEnergyStored();
		
		if( requiredToFill <= energy.getEnergyStored() ) {
			for( Map.Entry<BlockPos, IEnergyStorageWrapper> energy : energies.entrySet() ) {
				int amnt = energy.getValue().energy.getMaxEnergyStored() - energy.getValue().energy.getEnergyStored();
				if( amnt > maxEnergyTransfer )
					amnt = maxEnergyTransfer;
				
				transferEnergy( energy.getKey(), energy.getValue(), amnt );
			}
			
			return;
		}
		
		int inNeed = 0;
		
		for( Map.Entry<BlockPos, IEnergyStorageWrapper> energy : energies.entrySet() )
			if( energy.getValue().energy.getEnergyStored() < energy.getValue().energy.getMaxEnergyStored() )
				inNeed++;
		
		if( inNeed == 0 )
			return;
		
		for( Map.Entry<BlockPos, IEnergyStorageWrapper> energy : energies.entrySet() ) {
			int amnt = this.energy.getEnergyStored() / inNeed;
			
			if( amnt > maxEnergyTransfer )
				amnt = maxEnergyTransfer;
			if( amnt > energy.getValue().energy.getMaxEnergyStored() - energy.getValue().energy.getEnergyStored() )
				amnt = energy.getValue().energy.getMaxEnergyStored() - energy.getValue().energy.getEnergyStored();
			if( amnt <= 0 )
				continue;
			
			transferEnergy( energy.getKey(), energy.getValue(), amnt );
		}
	}

	protected Map<BlockPos, IEnergyStorageWrapper> getConnectedSinks() {
		Map<BlockPos, IEnergyStorageWrapper> sinks = Maps.newHashMap();
		
		List<BlockPos> open = Lists.newLinkedList();
		List<BlockPos> closed = Lists.newLinkedList();
		
		BlockPos neighbor;
		IBlockState state;
		TileEntity tile;
		
		boolean firstBlock = true;
		
		open.add( pos );
		while( open.size() > 0 ) {
			BlockPos current = open.remove( 0 );
			closed.add( current );
			
			for( EnumFacing facing : EnumFacing.VALUES ) {
				if( firstBlock && !energy.canExtract( facing ) )
					continue;
				
				neighbor = current.offset( facing );
				
				if( open.contains( neighbor ) || closed.contains( neighbor ) )
					continue;
				
				state = world.getBlockState( neighbor );
				if( state.getBlock() == Blocks.ENERGY_CONDUIT ) {
					open.add( neighbor );
					continue;
				}
				
				tile = world.getTileEntity( neighbor );
				if( tile != null && tile.hasCapability( CapabilityEnergy.ENERGY, facing.getOpposite() ) ) {
					IEnergyStorage storage = tile.getCapability( CapabilityEnergy.ENERGY, facing.getOpposite() );
					if( ( storage instanceof IEnergyStorageNBT && !( ( IEnergyStorageNBT )storage ).canReceive( facing.getOpposite() ) ) || !storage.canReceive() )
						continue;
					
					sinks.put( neighbor, new IEnergyStorageWrapper( storage, false ) );
					closed.add( neighbor );
					continue;
				}
			}
			
			firstBlock = false;
		}
		
		return sinks;
	}
	
	protected void modifySinks( Map<BlockPos, IEnergyStorageWrapper> energies ) {
	}
	
	protected void transferEnergy( BlockPos pos, IEnergyStorageWrapper energy, int amount ) {
		energy.energy.receiveEnergy( amount, false );
		this.energy.extractEnergy( amount, false );
	}
	
	protected class IEnergyStorageWrapper {
		
		protected IEnergyStorage energy;
		protected boolean useEntity;
		
		protected IEnergyStorageWrapper( IEnergyStorage energy, boolean useEntity ) {
			this.energy = energy;
			this.useEntity = useEntity;
		}
		
	}
	
}
