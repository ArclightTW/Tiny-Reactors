package com.immersiveworks.tinyreactors.common.entities;

import com.immersiveworks.tinyreactors.client.fx.FXTypes;
import com.immersiveworks.tinyreactors.common.TinyReactors;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityEnergyRelay;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityEnergyBurst extends EntityEnergyOrb {

	public static final DataParameter<Integer> ENERGY_START = EntityDataManager.createKey( EntityEnergyBurst.class, DataSerializers.VARINT );
	public static final DataParameter<Integer> ENERGY_MAX = EntityDataManager.createKey( EntityEnergyBurst.class, DataSerializers.VARINT );
	
	public EntityEnergyBurst() {
		this( null );
	}
	
	public EntityEnergyBurst( World world ) {
		super( world );
		setSize( 0F, 0F );
	}
	
	public EntityEnergyBurst( TileEntityEnergyRelay relay, BlockPos pos, int energy, int maxEnergy ) {
		super( relay, pos );
		
		dataManager.set( ENERGY_START, energy );
		dataManager.set( ENERGY_MAX, maxEnergy );
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		
		dataManager.register( ENERGY_START, 0 );
		dataManager.register( ENERGY_MAX, 1 );
	}
	
	@Override
	@SideOnly( Side.CLIENT )
	public void particles() {
		float percent = ( float )dataManager.get( ENERGY_START ) / ( float )dataManager.get( ENERGY_MAX );
		
		TinyReactors.proxy.displayFX( FXTypes.ENERGY_ORB, posX, posY, posZ, 0F, 0F, 1F, 0.4F * percent, ( float )-motionX * 0.01F, ( float )-motionY * 0.01F, ( float )-motionZ * 0.01F );
		TinyReactors.proxy.displayFX( FXTypes.ENERGY_ORB, posX, posY, posZ, 0F, 0F, 1F, 0.2F * percent,( float )( Math.random() - 0.5F ) * 0.06F, ( float )( Math.random() - 0.5F ) * 0.06F, ( float )( Math.random() - 0.5F ) * 0.06F );
	}
	
	@Override
	protected void onDestinationInterrupted( EnumFacing side ) {
	}
	
	@Override
	public void onDestinationReached( EnumFacing side ) {
		TileEntity tile = world.getTileEntity( dataManager.get( COORDS_DESTINATION ) );
		if( tile == null )
			return;
		
		IEnergyStorage energy = tile.getCapability( CapabilityEnergy.ENERGY, side );
		if( energy == null )
			return;
		
		energy.receiveEnergy( dataManager.get( ENERGY_START ), false );
		onDestinationExceeded();
	}
	
	@Override
	protected void onDestinationExceeded() {
		setDead();
	}
	
}
