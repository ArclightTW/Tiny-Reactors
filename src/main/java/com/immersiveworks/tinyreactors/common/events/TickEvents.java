package com.immersiveworks.tinyreactors.common.events;

import com.immersiveworks.tinyreactors.common.TinyReactors;
import com.immersiveworks.tinyreactors.common.energy.EnergyNetwork;
import com.immersiveworks.tinyreactors.common.inits.Configs;
import com.immersiveworks.tinyreactors.common.util.Processes;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber( modid = TinyReactors.ID )
public class TickEvents {

	@SubscribeEvent
	public static void onWorldTickEvent( TickEvent.WorldTickEvent event ) {
		if( Configs.changed ) {
			Configs.changed = false;
			EnergyNetwork.get( event.world ).refreshAll( event.world, null );
		}
	}
	
	@SubscribeEvent
	public static void onServerTick( TickEvent.ServerTickEvent event ) {
		if( event.phase != TickEvent.Phase.START )
			return;
		
		Processes.tick();
	}
	
}
