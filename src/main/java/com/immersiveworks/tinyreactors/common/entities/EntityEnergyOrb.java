package com.immersiveworks.tinyreactors.common.entities;

import com.immersiveworks.tinyreactors.common.tiles.TileEntityEnergyRelay;

import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class EntityEnergyOrb extends EntityThrowable {

	public static final DataParameter<BlockPos> COORDS_SOURCE = EntityDataManager.createKey( EntityEnergyOrb.class, DataSerializers.BLOCK_POS );
	public static final DataParameter<BlockPos> COORDS_DESTINATION = EntityDataManager.createKey( EntityEnergyOrb.class, DataSerializers.BLOCK_POS );
	
	public EntityEnergyOrb() {
		this( null );
	}
	
	public EntityEnergyOrb( World world ) {
		super( world );
		setSize( 0F, 0F );
	}
	
	public EntityEnergyOrb( TileEntityEnergyRelay relay, BlockPos pos ) {
		this( relay.getWorld() );
		
		Vec3d diff = new Vec3d( pos.getX(), pos.getY(), pos.getZ() ).subtract( new Vec3d( relay.getPos().getX(), relay.getPos().getY(), relay.getPos().getZ() ) ).normalize();
		setMotion( diff.x / 5F, diff.y / 5F, diff.z / 5F );
		
		posX = relay.getPos().getX() + 0.55F;
		posY = relay.getPos().getY() + 0.5F;
		posZ = relay.getPos().getZ() + 0.55F;
		
		dataManager.set( COORDS_SOURCE, relay.getPos() );
		dataManager.set( COORDS_DESTINATION, pos );
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		
		dataManager.register( COORDS_SOURCE, BlockPos.ORIGIN );
		dataManager.register( COORDS_DESTINATION, BlockPos.ORIGIN );
	}
	
	@Override
	public void onUpdate() {
		update();
		
		if( !isDead && world.isRemote )
			particles();

		setMotion( motionX, motionY, motionZ );
	}

	@Override
	protected void onImpact( RayTraceResult result ) {
		if( result.typeOfHit != RayTraceResult.Type.BLOCK || result.getBlockPos().equals( dataManager.get( COORDS_SOURCE ) ) )
			return;
		
		if( !result.getBlockPos().equals( dataManager.get( COORDS_DESTINATION ) ) ) {
			onDestinationInterrupted( result.sideHit );
			return;
		}
		
		onDestinationReached( result.sideHit );
	}
	
	@Override
	public boolean handleWaterMovement() {
		return false;
	}
	
	public void setMotion( double motionX, double motionY, double motionZ ) {
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
	}
	
	private void update() {
		lastTickPosX = posX;
		lastTickPosY = posY;
		lastTickPosZ = posZ;
		
		RayTraceResult result = world.rayTraceBlocks( new Vec3d( posX, posY, posZ ), new Vec3d( posX + motionX, posY + motionY, posZ + motionZ ) );
		if( result != null )
			onImpact( result );
		
		if( motionX <= 0 && posX <= dataManager.get( COORDS_DESTINATION ).getX() || motionX >= 0 && posX >= dataManager.get( COORDS_DESTINATION ).getX() + 1 )
			if( motionY <= 0 && posY <= dataManager.get( COORDS_DESTINATION ).getY() || motionY >= 0 && posY >= dataManager.get( COORDS_DESTINATION ).getY() + 1 )
				if( motionZ <= 0 && posZ <= dataManager.get( COORDS_DESTINATION ).getZ() || motionZ >= 0 && posZ >= dataManager.get( COORDS_DESTINATION ).getZ() + 1 )
					onDestinationExceeded();
		
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
	}
	
	protected abstract void particles();
	
	protected abstract void onDestinationInterrupted( EnumFacing side );
	protected abstract void onDestinationReached( EnumFacing side );
	protected abstract void onDestinationExceeded();
	
}
