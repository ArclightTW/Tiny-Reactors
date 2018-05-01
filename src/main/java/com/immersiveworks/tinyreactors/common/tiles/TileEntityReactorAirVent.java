package com.immersiveworks.tinyreactors.common.tiles;

import com.immersiveworks.tinyreactors.common.properties.EnumAirVent;
import com.immersiveworks.tinyreactors.common.storage.StorageReactor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityReactorAirVent extends TileEntityTiny implements IReactorTile {

	private EnumAirVent type;
	private int oldTier;
	private int tier;
	private boolean operational;
	
	private BlockPos structurePos;
	private StorageReactor structure;
	
	public TileEntityReactorAirVent() {
		type = EnumAirVent.EMPTY;
		oldTier = 0;
		tier = 0;
		operational = false;
	}
	
	@Override
	public void onStructureValidated( StorageReactor reactor ) {
		structure = reactor == null ? null : reactor.isValid() ? reactor : null;
		structurePos = structure != null ? structure.origin : null;
		syncClient();
	}
	
	@Override
	public void onLoad() {
		structure = null;
		if( structurePos != null ) {
			TileEntity tile = world.getTileEntity( structurePos );
			if( tile != null && tile instanceof TileEntityReactorController )
				structure = ( ( TileEntityReactorController )tile ).getStructure();
		}
		
		syncClient();
	}
	
	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound ) {
		super.writeToNBT( compound );
		
		NBTTagCompound airVent = new NBTTagCompound();
		airVent.setInteger( "type", type.ordinal() );
		airVent.setInteger( "oldTier", oldTier );
		airVent.setInteger( "tier", tier );
		airVent.setBoolean( "operational", operational );
		if( structurePos != null )
			airVent.setTag( "structure", NBTUtil.createPosTag( structurePos ) );
		
		compound.setTag( "airVent", airVent );		
		return compound;
	}
	
	@Override
	public void readFromNBT( NBTTagCompound compound ) {
		super.readFromNBT( compound );
		NBTTagCompound airVent = compound.getCompoundTag( "airVent" );
		
		type = EnumAirVent.values()[ airVent.getInteger( "type" ) ];
		tier = airVent.getInteger( "tier" );
		oldTier = airVent.getInteger( "oldTier" );
		operational = airVent.getBoolean( "operational" );
		structurePos = airVent.hasKey( "structure" ) ? NBTUtil.getPosFromTag( airVent.getCompoundTag( "structure" ) ) : null;
		
		if( world != null )
			onLoad();
	}
	
	// TODO: Vent Type changed -- inform structure
	public void setVentType( EnumAirVent type ) {
		this.type = type;
		syncClient();
		
		if( this.type == EnumAirVent.EMPTY && operational )
			toggleOperational();
		else if( structure != null )
			structure.changeTemperatureCooldown( this );
	}
	
	public EnumAirVent getVentType() {
		return type;
	}
	
	public void incrementTier() {
		oldTier = tier;
		
		tier += 1;
		if( tier >= type.ordinal() )
			tier = 0;
		
		syncClient();
		
		if( structure != null )
			structure.changeTemperatureCooldown( this );
	}
	
	public int getTier() {
		return tier;
	}

	public int getOldTier() {
		return oldTier;
	}
	
	public void toggleOperational() {
		if( type == EnumAirVent.EMPTY )
			return;
		
		operational = !operational;
		syncClient();
		
		if( structure != null )
			structure.changeTemperatureCooldown( this );
	}
	
	public boolean isOperational() {
		return operational;
	}
	
	public StorageReactor getStructure() {
		return structure;
	}
	
}
