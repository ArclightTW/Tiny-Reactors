package com.immersiveworks.tinyreactors.common.events;

import com.immersiveworks.tinyreactors.common.TinyReactors;
import com.immersiveworks.tinyreactors.common.energy.EnergyNetwork;
import com.immersiveworks.tinyreactors.common.energy.IEnergyNetworkBlock;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber( modid = TinyReactors.ID )
public class BlockEvents {

	@SubscribeEvent
	public static void onPlace( BlockEvent.PlaceEvent event ) {
		if( event.getPlacedBlock().getBlock() instanceof IEnergyNetworkBlock )
			EnergyNetwork.get( event.getWorld() ).addComponent( event.getWorld(), event.getPos(), ( IEnergyNetworkBlock )event.getPlacedBlock().getBlock() );
		else
			EnergyNetwork.get( event.getWorld() ).refreshAll( event.getWorld(), null );
	}
	
	@SubscribeEvent
	public static void onBreak( BlockEvent.BreakEvent event ) {
		EnergyNetwork.get( event.getWorld() ).removeComponent( event.getWorld(), event.getPos() );
	}
	
}
