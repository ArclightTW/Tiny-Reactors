package com.immersiveworks.tinyreactors.common.entities;

import com.immersiveworks.tinyreactors.client.fx.FXTypes;
import com.immersiveworks.tinyreactors.common.TinyReactors;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityEnergyRelay;
import com.immersiveworks.tinyreactors.common.util.PlayerHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityEnergyBeam extends EntityEnergyOrb {

	public EntityEnergyBeam() {
		this( null );
	}
	
	public EntityEnergyBeam( World world ) {
		super( world );
		setSize( 0F, 0F );
	}
	
	public EntityEnergyBeam( TileEntityEnergyRelay relay, BlockPos pos ) {
		super( relay, pos );
	}
	
	@Override
	@SideOnly( Side.CLIENT )
	public void particles() {
		if( !PlayerHelper.hasWrenchVisibility( Minecraft.getMinecraft().player ) )
			return;
		
		TinyReactors.proxy.displayFX( FXTypes.ENERGY_ORB, posX, posY, posZ, 0F, 1F, 0F, 0.1F, 0F, 0F, 0F );
	}
	
	@Override
	protected void onDestinationInterrupted( EnumFacing side ) {
		onDestinationExceeded();
	}
	
	@Override
	public void onDestinationReached( EnumFacing side ) {
		setDead();
	}
	
	@Override
	protected void onDestinationExceeded() {		
		TileEntity tile = world.getTileEntity( dataManager.get( COORDS_SOURCE ) );
		if( tile != null && tile instanceof TileEntityEnergyRelay )
			( ( TileEntityEnergyRelay )tile ).removeDestination( dataManager.get( COORDS_DESTINATION ) );
		
		setDead();
	}
	
}
