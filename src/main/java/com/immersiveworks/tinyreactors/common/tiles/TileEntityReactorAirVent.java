package com.immersiveworks.tinyreactors.common.tiles;

import com.immersiveworks.tinyreactors.common.properties.EnumAirVent;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityReactorAirVent extends TileEntityTiny implements IReactorTile {

	private EnumAirVent type;
	private int tier;
	private boolean operational;
	
	private BlockPos controllerPos;
	private TileEntityReactorController controller;
	
	private int burnTimer = -1;
	
	public TileEntityReactorAirVent() {
		type = EnumAirVent.EMPTY;
		tier = 0;
		operational = false;
		
		registerPulsar( () -> {
			if( controller == null )
				return;
			
			if( burnTimer == -1 && type != EnumAirVent.EMPTY && controller.getStructure().getTemperature().getCurrentTemperature() >= type.getMeltingPoint() )
				burnTimer = 400;
			
			if( burnTimer == -1 )
				return;
			
			burnTimer--;
			if( burnTimer > 0 )
				return;
			
			// TODO: burn out Vent
		} );
	}
	
	@Override
	public void onStructureValidated( TileEntityReactorController controller ) {
		this.controller = controller;
		this.controllerPos = this.controller != null ? this.controller.getPos() : null;
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
		
		NBTTagCompound airVent = new NBTTagCompound();
		airVent.setInteger( "type", type.ordinal() );
		airVent.setInteger( "tier", tier );
		airVent.setBoolean( "operational", operational );
		airVent.setInteger( "burnTimer", burnTimer );
		if( controllerPos != null )
			airVent.setTag( "controller", NBTUtil.createPosTag( controllerPos ) );
		
		compound.setTag( "airVent", airVent );		
		return compound;
	}
	
	@Override
	public void readFromNBT( NBTTagCompound compound ) {
		super.readFromNBT( compound );
		NBTTagCompound airVent = compound.getCompoundTag( "airVent" );
		
		type = EnumAirVent.values()[ airVent.getInteger( "type" ) ];
		tier = airVent.getInteger( "tier" );
		operational = airVent.getBoolean( "operational" );
		burnTimer = airVent.getInteger( "burnTimer" );
		controllerPos = airVent.hasKey( "controller" ) ? NBTUtil.getPosFromTag( airVent.getCompoundTag( "controller" ) ) : null;
		
		if( world != null )
			onLoad();
	}
	
	public void ignite() {
		burnTimer = 400;
		world.setBlockState( pos.up(), Blocks.FIRE.getDefaultState() );
	}
	
	public void setVentType( EnumAirVent type ) {
		boolean op = operational;
		if( controller != null && operational ) {
			operational = false;
			controller.getStructure().changeTemperatureCooldown( this );
		}
		operational = op;
		
		this.type = type;
		tier = 10;
		
		if( this.type == EnumAirVent.EMPTY && operational )
			toggleOperational();
		else if( controller != null && operational )
			controller.getStructure().changeTemperatureCooldown( this );
		
		syncClient();
	}
	
	public EnumAirVent getVentType() {
		return type;
	}
	
	public void incrementTier() {
		boolean op = operational;
		if( controller != null && operational ) {
			operational = false;
			controller.getStructure().changeTemperatureCooldown( this );
		}
		operational = op;
		
		tier -= 1;
		if( tier <= 0 )
			tier = 10;
		
		if( controller != null && operational )
			controller.getStructure().changeTemperatureCooldown( this );
		
		syncClient();
	}
	
	public int getTier() {
		return tier;
	}

	public void toggleOperational() {
		if( type == EnumAirVent.EMPTY )
			return;
		
		operational = type == EnumAirVent.EMPTY ? false : !operational;
		
		if( controller != null )
			controller.getStructure().changeTemperatureCooldown( this );
		
		syncClient();
	}
	
	public boolean isOperational() {
		return operational;
	}
	
	public TileEntityReactorController getController() {
		return controller;
	}
	
}
