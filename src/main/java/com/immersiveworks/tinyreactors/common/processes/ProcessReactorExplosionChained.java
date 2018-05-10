package com.immersiveworks.tinyreactors.common.processes;

import java.util.List;

import com.immersiveworks.tinyreactors.api.processes.IProcess;
import com.immersiveworks.tinyreactors.common.energy.EnergyNetwork;
import com.immersiveworks.tinyreactors.common.inits.Blocks;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ProcessReactorExplosionChained implements IProcess {

	private int counter;
	
	private World world;
	private List<BlockPos> structure;
	
	public ProcessReactorExplosionChained( World world, List<BlockPos> structure ) {
		this.world = world;
		this.structure = structure;
	}
	
	@Override
	public void update() {
		counter++;
		if( counter < 10 )
			return;
		counter = 0;
		
		BlockPos pos = structure.remove( 0 );
		
		Block block = world.getBlockState( pos ).getBlock();
		if( block == Blocks.REACTOR_CASING || block == Blocks.REACTOR_GLASS )
			return;
		
		world.createExplosion( null, pos.getX(), pos.getY(), pos.getZ(), 10, true );
	}
	
	@Override
	public void onDeath( boolean serverClosed ) {
		EnergyNetwork.get( world ).refreshAll( world, null );
	}

	@Override
	public boolean isDead() {
		return structure.size() == 0;
	}
	
	@Override
	public boolean shouldCompleteOnServerClose() {
		return true;
	}
	
}
