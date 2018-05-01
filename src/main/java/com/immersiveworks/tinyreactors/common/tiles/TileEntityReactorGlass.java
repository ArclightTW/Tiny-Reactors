package com.immersiveworks.tinyreactors.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityReactorGlass extends TileEntityTiny implements IReactorTile {

	private BlockPos controllerPos;
	private TileEntityReactorController controller;
	
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
		
		NBTTagCompound glass = new NBTTagCompound();
		if( controllerPos != null ) glass.setTag( "controller", NBTUtil.createPosTag( controllerPos ) );
		
		compound.setTag( "glass", glass );		
		return compound;
	}
	
	@Override
	public void readFromNBT( NBTTagCompound compound ) {
		super.readFromNBT( compound );
		NBTTagCompound glass = compound.getCompoundTag( "glass" );
		
		controllerPos = glass.hasKey( "controller" ) ? NBTUtil.getPosFromTag( glass.getCompoundTag( "controller" ) ) : null;
		if( world != null )
			onLoad();
	}
	
	public TileEntityReactorController getController() {
		return controller;
	}
	
}
