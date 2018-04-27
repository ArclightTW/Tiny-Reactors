package com.immersiveworks.tinyreactors.common.energy;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.immersiveworks.tinyreactors.common.TinyReactors;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class EnergyNetwork extends WorldSavedData {

	private boolean loaded;
	private List<BlockPos> loadedComponents;
	private Map<Priority, Map<BlockPos, IEnergyNetworkBlock>> components;
	
	public EnergyNetwork() {
		this( TinyReactors.ID + ":EnergyNetwork" );
	}
	
	public EnergyNetwork( String id ) {
		super( id );

		loadedComponents = Lists.newLinkedList();
		components = Maps.newHashMap();
		
		for( Priority priority : Priority.VALUES )
			components.put( priority, Maps.newHashMap() );
	}
	
	public static EnergyNetwork get( World world ) {
		MapStorage storage = world.getMapStorage();
		EnergyNetwork instance = ( EnergyNetwork )storage.getOrLoadData( EnergyNetwork.class, TinyReactors.ID + ":EnergyNetwork" );
		
		if( instance == null ) {
			instance = new EnergyNetwork();
			storage.setData( TinyReactors.ID + ":EnergyNetwork", instance );
		}
		
		return instance;
	}
	
	@Override
	public NBTTagCompound writeToNBT( NBTTagCompound compound ) {
		NBTTagCompound data = new NBTTagCompound();
		
		NBTTagList list = new NBTTagList();
		for( Map.Entry<Priority, Map<BlockPos, IEnergyNetworkBlock>> component : components.entrySet() )
			for( BlockPos pos : component.getValue().keySet() )
				list.appendTag( NBTUtil.createPosTag( pos ) );
		
		data.setTag( "list", list );
		compound.setTag( "data", data );
		return compound;
	}
	
	@Override
	public void readFromNBT( NBTTagCompound compound ) {
		NBTTagCompound data = compound.getCompoundTag( "data" );
		
		NBTTagList list = data.getTagList( "list", Constants.NBT.TAG_COMPOUND );
		for( int i = 0; i < list.tagCount(); i++ )
			loadedComponents.add( NBTUtil.getPosFromTag( list.getCompoundTagAt( i ) ) );
	}
	
	public void addComponent( World world, BlockPos pos, IEnergyNetworkBlock component ) { addComponent( world, pos, component, true ); }
	
	public void addComponent( World world, BlockPos pos, IEnergyNetworkBlock component, boolean refresh ) {
		Map<BlockPos, IEnergyNetworkBlock> existing;
		if( components.containsKey( component.getEnergyNetworkPriority() ) )
			existing = components.get( component.getEnergyNetworkPriority() );
		else
			existing = Maps.newHashMap();
		
		existing.put( pos, component );
		components.put( component.getEnergyNetworkPriority(), existing );
		
		if( refresh )
			refreshAll( world, null );
	}
	
	public void removeComponent( World world, BlockPos pos ) {
		refreshAll( world, pos );
		
		for( Priority priority : Priority.VALUES ) {
			if( !components.containsKey( priority ) )
				continue;
			
			if( !components.get( priority ).containsKey( pos ) )
				continue;
			
			components.get( priority ).remove( pos );
			markDirty();
		}
	}
	
	public void refreshAll( World world, BlockPos removed ) {
		if( !loaded && loadedComponents.size() > 0 ) {
			for( BlockPos loaded : loadedComponents ) {
				Block block = world.getBlockState( loaded ).getBlock();
				
				if( block instanceof IEnergyNetworkBlock )
					addComponent( world, loaded,  ( IEnergyNetworkBlock )block, false );
			}
			
			loaded = true;
		}
		
		for( Map.Entry<Priority, Map<BlockPos, IEnergyNetworkBlock>> priority : components.entrySet() )
			for( Map.Entry<BlockPos, IEnergyNetworkBlock> component : priority.getValue().entrySet() )
				component.getValue().onEnergyNetworkRefreshed( world, component.getKey(), removed );
		
		markDirty();
	}
	
	public enum Priority {
		
		HIGH,
		NORMAL,
		LOW;
		
		public static Priority[] VALUES = { HIGH, NORMAL, LOW };

	}
	
}
