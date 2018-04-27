package com.immersiveworks.tinyreactors.common.tiles;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.immersiveworks.tinyreactors.common.entities.EntityEnergyBeam;
import com.immersiveworks.tinyreactors.common.entities.EntityEnergyBurst;

import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileEntityEnergyRelay extends TileEntityTinyEnergy {

	private List<BlockPos> destinations;
	
	public TileEntityEnergyRelay() {
		super( 100000, 20, 200 );
		
		destinations = Lists.newLinkedList();
		
		registerPulsar( 10, () -> {
			for( BlockPos pos : destinations )
				createEntity( new EntityEnergyBeam( this, pos ) );
		} );
	}
	
	@Override
	protected void modifySinks( Map<BlockPos, IEnergyStorageWrapper> energies ) {
		for( int i = 0; i < destinations.size(); i++ ) {
			if( world.getTileEntity( destinations.get( i ) ) == null )
				continue;
			
			TileEntity tile = world.getTileEntity( destinations.get( i ) );
			
			for( EnumFacing facing : EnumFacing.VALUES ) {
				if( !tile.hasCapability( CapabilityEnergy.ENERGY, facing ) )
					continue;
				
				energies.put( destinations.get( i ), new IEnergyStorageWrapper( tile.getCapability( CapabilityEnergy.ENERGY, facing ), true ) );
				break;
			}
		}
	}
	
	@Override
	protected void transferEnergy( BlockPos pos, IEnergyStorageWrapper energy, int amount ) {
		if( !energy.useEntity ) {
			super.transferEnergy( pos, energy, amount );
			return;
		}
		
		createEntity( new EntityEnergyBurst( this, pos, amount, maxEnergyTransfer ) );	
		this.energy.extractEnergy( amount, false );
	}
	
	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound ) {
		super.writeToNBT( compound );
		
		NBTTagList destinationsList = new NBTTagList();
		for( int i = 0; i < destinations.size(); i++ )
			destinationsList.appendTag( NBTUtil.createPosTag( destinations.get( i ) ) );
		compound.setTag( "destinations", destinationsList );
		
		return compound;
	}
	
	@Override
	public void readFromNBT( NBTTagCompound compound ) {
		super.readFromNBT( compound );
		
		destinations = Lists.newLinkedList();
		NBTTagList destinationsList = compound.getTagList( "destinations", Constants.NBT.TAG_COMPOUND );
		for( int i = 0; i < destinationsList.tagCount(); i++ )
			addDestination( NBTUtil.getPosFromTag( destinationsList.getCompoundTagAt( i ) ) );
	}
	
	public int getNumberDestinations() {
		return destinations.size();
	}
	
	public void addDestination( BlockPos destination ) {
		destinations.add( destination );
		syncClient();
	}
	
	public void removeDestination( BlockPos destination ) {
		destinations.remove( destination );
		syncClient();
	}
	
	public void givePower() {
		energy.receiveEnergy( 150, false );
	}
	
	private <T extends EntityThrowable> void createEntity( T entity ) {
		if( world.isRemote )
			return;
		
		entity.setNoGravity( true );
		world.spawnEntity( entity );
	}
	
}
