package com.immersiveworks.tinyreactors.common.tiles;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityTiny extends TileEntity implements ITickable {

	protected Map<String, CapabilityWrapper<?>> capabilities;
	protected List<Pulsar> pulsars;
	
	public TileEntityTiny() {
		pulsars = Lists.newLinkedList();
		capabilities = Maps.newHashMap();
	}
	
	@Override
	public void update() {
		for( int i = 0; i < pulsars.size(); i++ )
			pulsars.get( i ).tick();
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity( pos, 3, getUpdateTag() );
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT( new NBTTagCompound() );
	}
	
	@Override
	public void onDataPacket( NetworkManager net, SPacketUpdateTileEntity pkt ) {
		super.onDataPacket( net, pkt );
		handleUpdateTag( pkt.getNbtCompound() );
	}
	
	@Override
	public final boolean hasCapability( Capability<?> capability, EnumFacing facing ) {
		for( Map.Entry<String, CapabilityWrapper<?>> cap : capabilities.entrySet() ) {
			if( cap.getValue().capability == capability )
				return cap.getValue().retriever.apply( facing ) != null;
		}
		
		return super.hasCapability( capability, facing );
	}
	
	@Override
	@SuppressWarnings( "unchecked" )
	public final <T> T getCapability( Capability<T> capability, EnumFacing facing ) {
		if( !hasCapability( capability, facing ) )
			return null;
		
		for( Map.Entry<String, CapabilityWrapper<?>> cap : capabilities.entrySet() ) {
			if( cap.getValue().capability == capability )
				return ( T )cap.getValue().retriever.apply( facing );
		}
		
		return null;
	}
	
	public final void syncClient() {
		if( world != null ) {
			world.markBlockRangeForRenderUpdate( pos, pos );
			world.notifyBlockUpdate( pos, world.getBlockState( pos ), world.getBlockState( pos ), 3 );
			world.scheduleBlockUpdate( pos, getBlockType(), 0, 0 );
		}
		
		markDirty();
	}
	
	protected final void registerPulsar( Runnable action ) {
		pulsars.add( new Pulsar( 1, action ) );
	}
	
	protected final void registerPulsar( int tickDelay, Runnable action ) {
		pulsars.add( new Pulsar( tickDelay, action ) );
	}
	
	protected final <T> void registerCapability( String name, Capability<T> capability, Function<EnumFacing, T> retriever ) {
		capabilities.put( name, new CapabilityWrapper<T>( capability, retriever ) );
	}
	
	protected final boolean hasCapability( String name ) {
		return capabilities.containsKey( name );
	}
	
	protected class Pulsar {
		
		protected int counter;
		
		protected int tickDelay;
		protected Runnable action;
		
		protected Pulsar( int tickDelay, Runnable action ) {
			this.tickDelay = this.counter = tickDelay;
			this.action = action;
		}
		
		protected void tick() {
			counter++;
			if( counter < tickDelay )
				return;
			run();
		}
		
		protected void run() {
			counter = 0;
			action.run();
		}
		
	}
	
	protected class CapabilityWrapper<T> {
		
		protected Capability<T> capability;
		protected Function<EnumFacing, T> retriever;
		
		protected CapabilityWrapper( Capability<T> capability, Function<EnumFacing, T> retriever ) {
			this.capability = capability;
			this.retriever = retriever;
		}
		
	}
	
}
