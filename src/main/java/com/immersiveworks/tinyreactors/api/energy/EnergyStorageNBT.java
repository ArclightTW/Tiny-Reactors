package com.immersiveworks.tinyreactors.api.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyStorageNBT extends EnergyStorage implements IEnergyStorageNBT {

	private boolean[] canReceive;
	private boolean[] canExtract;
	
	public EnergyStorageNBT( int capacity ) { this( capacity, capacity ); }
	public EnergyStorageNBT( int capacity, int maxTransfer ) { this( capacity, maxTransfer, maxTransfer ); }
	public EnergyStorageNBT( int capacity, int maxReceive, int maxExtract ) { this( capacity, maxReceive, maxExtract, 0 ); }
	public EnergyStorageNBT( int capacity, int maxReceive, int maxExtract, int energy ) {
		super( capacity, maxReceive, maxExtract, energy );
		
		canReceive = new boolean[ EnumFacing.VALUES.length ];
		canExtract = new boolean[ EnumFacing.VALUES.length ];
		for( int i = 0; i < canExtract.length; i++ ) {
			canReceive[ i ] = maxReceive > 0;
			canExtract[ i ] = maxExtract > 0;
		}
	}
	
	@Override
	public void setCanReceive( EnumFacing facing, boolean canReceive ) {
		if( facing.ordinal() < 0 || facing.ordinal() >= this.canReceive.length )
			return;
		
		this.canReceive[ facing.ordinal() ] = canReceive;
	}
	
	@Override
	public boolean canReceive( EnumFacing facing ) {
		return super.canReceive() && canReceive[ facing.ordinal() ];
	}
	
	@Override
	public void setCanExtract( EnumFacing facing, boolean canExtract ) {
		if( facing.ordinal() < 0 || facing.ordinal() >= this.canExtract.length )
			return;
		
		this.canExtract[ facing.ordinal() ] = canExtract;
	}
	
	@Override
	public boolean canExtract( EnumFacing facing ) {
		return super.canExtract() && canExtract[ facing.ordinal() ];
	}
	
	@Override
	public void writeToNBT( NBTTagCompound compound ) {
		NBTTagCompound energy = new NBTTagCompound();
		energy.setInteger( "energy", this.energy );
		energy.setInteger( "capacity", this.capacity );
		energy.setInteger( "maxReceive", this.maxReceive );
		energy.setInteger( "maxExtract", this.maxExtract );

		NBTTagList canReceive = new NBTTagList();
		for( int i = 0; i < this.canReceive.length; i++ )
			canReceive.appendTag( new NBTTagFloat( this.canReceive[ i ] ? 1 : 0 ) );
		energy.setTag( "canReceive", canReceive );
		
		NBTTagList canExtract = new NBTTagList();
		for( int i = 0; i < this.canExtract.length; i++ )
			canExtract.appendTag( new NBTTagFloat( this.canExtract[ i ] ? 1 : 0 ) );
		energy.setTag( "canExtract", canExtract );
		
		compound.setTag( "energy", energy );
	}
	
	@Override
	public void readFromNBT( NBTTagCompound compound ) {
		NBTTagCompound energy = compound.getCompoundTag( "energy" );
		this.energy = energy.getInteger( "energy" );
		this.capacity = energy.getInteger( "capacity" );
		this.maxReceive = energy.getInteger( "maxReceive" );
		this.maxExtract = energy.getInteger( "maxExtract" );

		NBTTagList canReceive = energy.getTagList( "canReceive", Constants.NBT.TAG_FLOAT );
		for( int i = 0; i < canReceive.tagCount(); i++ )
			this.canReceive[ i ] = canReceive.getFloatAt( i ) == 1;

		NBTTagList canExtract = energy.getTagList( "canExtract", Constants.NBT.TAG_FLOAT );
		for( int i = 0; i < canExtract.tagCount(); i++ )
			this.canExtract[ i ] = canExtract.getFloatAt( i ) == 1;
	}
	
}
