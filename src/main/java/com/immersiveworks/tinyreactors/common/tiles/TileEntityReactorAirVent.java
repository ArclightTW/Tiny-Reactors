package com.immersiveworks.tinyreactors.common.tiles;

import com.immersiveworks.tinyreactors.common.properties.EnumAirVent;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityReactorAirVent extends TileEntityTiny implements IReactorTile {

	private State state;
	private EnumAirVent type;
	private int tier;
	
	private boolean obstructed;
	private boolean operational;
	
	private BlockPos controllerPos;
	private TileEntityReactorController controller;
	
	private int burnTimer = -1;
	
	public TileEntityReactorAirVent() {
		state = State.START;
		type = EnumAirVent.EMPTY;
		tier = 0;
		
		obstructed = true;
		operational = false;
		
		registerPulsar( () -> {
			boolean obstructed = world.getBlockState( pos.up() ).getBlock() != Blocks.AIR && world.getBlockState( pos.up() ).getBlock() != Blocks.FIRE;
			if( obstructed != this.obstructed ) {
				this.obstructed = obstructed;
				syncClient();
				
				if( this.obstructed )
					setState( State.OBSTRUCTED );
				else
					setState( State.UNOBSTRUCTED );
				
				if( controller != null )
					controller.getStructure().changeTemperatureCooldown( this );
			}
			
			if( controller == null ) {
				burnTimer = -1;
				
				if( world.getBlockState( pos.up() ).getBlock() == Blocks.FIRE )
					world.setBlockToAir( pos.up() );
				
				return;
			}
			
			if( isOperational() && burnTimer == -1 && type != EnumAirVent.EMPTY && controller.getStructure().getTemperature().getCurrentTemperature() >= type.getMeltingPoint( controller.getStructure().getTemperature() ) )
				ignite();
			
			if( type != EnumAirVent.EMPTY && controller.getStructure().getTemperature().getCurrentTemperature() < type.getMeltingPoint( controller.getStructure().getTemperature() ) ) {
				burnTimer = -1;
				
				if( world.getBlockState( pos.up() ).getBlock() == Blocks.FIRE )
					world.setBlockToAir( pos.up() );
			}
			
			if( burnTimer == -1 )
				return;
			
			setState( State.OVERHEATING );
			
			burnTimer--;
			if( burnTimer > 0 )
				return;
			
			setState( State.OVERHEATED );
			setVentType( EnumAirVent.EMPTY );
			if( world.getBlockState( pos.up() ).getBlock() == Blocks.FIRE )
				world.setBlockToAir( pos.up() );
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
		airVent.setInteger( "state", state.ordinal() );
		airVent.setInteger( "type", type.ordinal() );
		airVent.setInteger( "tier", tier );
		airVent.setBoolean( "obstructed", obstructed );
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
		
		state = State.values()[ airVent.getInteger( "state" ) ];
		type = EnumAirVent.values()[ airVent.getInteger( "type" ) ];
		tier = airVent.getInteger( "tier" );
		obstructed = airVent.getBoolean( "obstructed" );
		operational = airVent.getBoolean( "operational" );
		burnTimer = airVent.getInteger( "burnTimer" );
		controllerPos = airVent.hasKey( "controller" ) ? NBTUtil.getPosFromTag( airVent.getCompoundTag( "controller" ) ) : null;
		
		if( world != null )
			onLoad();
	}
	
	public void ignite() {
		burnTimer = 400;
		
		setState( State.OVERHEATING );
		
		if( world.getBlockState( pos.up() ).getBlock() == Blocks.AIR )
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
		return operational && !obstructed;
	}
	
	public boolean isObstructed() {
		return obstructed;
	}
	
	public int getBurnTimer() {
		return burnTimer;
	}
	
	public TileEntityReactorController getController() {
		return controller;
	}
	
	private void setState( State state ) {
		if( this.state == state )
			return;
		
		this.state = state;
		if( controller != null )
			controller.logMessage( this.state.getDisplay() );
	}
	
	private enum State {
		
		START( "" ),
		UNOBSTRUCTED( "Air Vent Obstruction Cleared" ),
		OBSTRUCTED( "Air Vent Obstructed" ),
		OVERHEATING( "Air Vent Overheating" ),
		OVERHEATED( "Air Vent Overheated" );
		
		private String display;
		
		private State( String display ) {
			this.display = display;
		}
		
		public String getDisplay() {
			return display;
		}
		
	}
	
}
