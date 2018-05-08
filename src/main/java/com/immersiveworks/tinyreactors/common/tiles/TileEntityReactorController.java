package com.immersiveworks.tinyreactors.common.tiles;

import java.util.List;

import com.google.common.collect.Lists;
import com.immersiveworks.tinyreactors.common.storage.StorageReactor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

public class TileEntityReactorController extends TileEntityTiny implements IReactorTile {

	private boolean isActive;
	private boolean isManuallyActive;
	
	private List<String> consoleDisplay;
	
	private StorageReactor structure;
	
	public TileEntityReactorController() {
		consoleDisplay = Lists.newLinkedList();
		
		structure = new StorageReactor( this );
		structure.setValidationListener( () -> {
			syncClient();
		} );
	}
	
	@Override
	public void onLoad() {
		registerPulsar( 1, () -> {
			structure.tick( world );
		} );
		
		structure.validateStructure( world, pos, null );
	}
	
	@Override
	public void onStructureValidated( TileEntityReactorController controller ) {
	}
	
	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound ) {
		super.writeToNBT( compound );
		structure.writeToNBT( compound );
		
		compound.setBoolean( "isActive", isActive );
		compound.setBoolean( "isManuallyActive", isManuallyActive );
		
		NBTTagList list = new NBTTagList();
		for( int i = 0; i < consoleDisplay.size(); i++ )
			list.appendTag( new NBTTagString( consoleDisplay.get( i ) ) );
		compound.setTag( "consoleDisplay", list );
		
		return compound;
	}
	
	@Override
	public void readFromNBT( NBTTagCompound compound ) {
		super.readFromNBT( compound );
		structure.readFromNBT( compound );
		
		isActive = compound.getBoolean( "isActive" );
		isManuallyActive = compound.getBoolean( "isManuallyActive" );
		
		NBTTagList list = compound.getTagList( "consoleDisplay", Constants.NBT.TAG_STRING );
		for( int i = 0; i < list.tagCount(); i++ )
			consoleDisplay.add( list.getStringTagAt( i ) );
	}
	
	public void logMessage( String display, Object... args ) {
		if( consoleDisplay.size() > 18 ) {
			consoleDisplay.remove( 0 );
			
			for( int i = 1; i < 18; i++ )
				consoleDisplay.set( i - 1, consoleDisplay.get( i ) );
		}
		
		consoleDisplay.add( String.format( display, args ) );
		syncClient();
	}
	
	public boolean isActive() {
		return isActive && isManuallyActive;
	}
	
	public boolean isManuallyActive() {
		return isManuallyActive;
	}
	
	public void setActive( boolean active, boolean isManual ) {
		this.isActive = active;
		
		if( isManual )
			this.isManuallyActive = active;
		
		if( isActive() )
			structure.startPreigniters( world );
		
		syncClient();
	}
	
	public List<String> getConsoleDisplay() {
		return consoleDisplay;
	}
	
	public StorageReactor getStructure() {
		return structure;
	}
	
}
