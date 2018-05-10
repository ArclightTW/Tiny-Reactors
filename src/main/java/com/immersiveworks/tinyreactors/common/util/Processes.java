package com.immersiveworks.tinyreactors.common.util;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import com.immersiveworks.tinyreactors.api.processes.IProcess;
import com.immersiveworks.tinyreactors.common.TinyReactors;

import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber( modid = TinyReactors.ID )
public class Processes {

	private static List<IProcess> processes = Lists.newLinkedList();
	private static List<IProcess> newProcesses = Lists.newLinkedList();
	
	public static void addProcess( IProcess process ) {
		newProcesses.add( process );
	}
	
	public static void tick() {
		runInternal( false );
	}
	
	public static void clearHandler() {
		runInternal( true );
		
		processes.clear();
		newProcesses.clear();
	}
	
	private static void runInternal( boolean serverClosed ) {
		Iterator<IProcess> i = processes.iterator();
		while( i.hasNext() ) {
			IProcess process = i.next();
			if( process.isDead() ) {
				process.onDeath( false );
				i.remove();
			}
			else {
				if( !serverClosed ) {
					process.update();
					continue;
				}
				
				if( !process.shouldCompleteOnServerClose() )
					continue;
				
				while( !process.isDead() )
					process.update();
				
				process.onDeath( true );
			}
		}
		
		if( !newProcesses.isEmpty() ) {
			processes.addAll( newProcesses );
			newProcesses.clear();
		}
	}
	
}
