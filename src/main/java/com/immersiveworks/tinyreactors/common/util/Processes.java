package com.immersiveworks.tinyreactors.common.util;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import com.immersiveworks.tinyreactors.api.IProcess;
import com.immersiveworks.tinyreactors.common.TinyReactors;

import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber( modid = TinyReactors.ID )
public class Processes {

	private static List<IProcess> processes = Lists.newLinkedList();
	private static List<IProcess> newProcesses = Lists.newLinkedList();
	
	public static void tick() {
		Iterator<IProcess> i = processes.iterator();
		while( i.hasNext() ) {
			IProcess process = i.next();
			if( process.isDead() )
				i.remove();
			else
				process.update();
		}
		
		if( !newProcesses.isEmpty() ) {
			processes.addAll( newProcesses );
			newProcesses.clear();
		}
	}
	
	public static void addProcess( IProcess process ) {
		newProcesses.add( process );
	}
	
	public static void clearHandler() {
		processes.clear();
		newProcesses.clear();
	}
	
}
