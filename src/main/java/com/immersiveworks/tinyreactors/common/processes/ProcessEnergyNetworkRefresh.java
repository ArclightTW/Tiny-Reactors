package com.immersiveworks.tinyreactors.common.processes;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.immersiveworks.tinyreactors.api.processes.IProcess;
import com.immersiveworks.tinyreactors.common.energy.IEnergyNetworkBlock;
import com.immersiveworks.tinyreactors.common.inits.Configs;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ProcessEnergyNetworkRefresh implements IProcess {

	private int counter;
	
	private World world;
	private BlockPos removed;
	
	private List<BlockPos> positions;
	private List<IEnergyNetworkBlock> components;
	
	public ProcessEnergyNetworkRefresh( World world, BlockPos removed, Collection<Map<BlockPos, IEnergyNetworkBlock>> components ) {
		this.world = world;
		this.removed = removed;
		
		this.positions = Lists.newLinkedList();
		this.components = Lists.newLinkedList();
		
		Iterator<Map<BlockPos, IEnergyNetworkBlock>> i = components.iterator();
		while( i.hasNext() ) {
			Map<BlockPos, IEnergyNetworkBlock> map = i.next();
			this.positions.addAll( map.keySet() );
			this.components.addAll( map.values() );
		}
		
		if( this.positions.size() != this.components.size() )
			this.components.clear();
	}
	
	@Override
	public void update() {
		if( Configs.NETWORK_REFRESH_BLOCK_COUNT == -1 ) {
			while( components.size() > 0 )
				components.remove( 0 ).onEnergyNetworkRefreshed( world, positions.remove( 0 ), removed );
			
			return;
		};
		
		while( counter < Configs.NETWORK_REFRESH_BLOCK_COUNT && components.size() > 0 ) {
			components.remove( 0 ).onEnergyNetworkRefreshed( world, positions.remove( 0 ), removed );
			counter++;
		}
		
		counter = 0;
	}

	@Override
	public boolean isDead() {
		return components.size() == 0;
	}
	
}
