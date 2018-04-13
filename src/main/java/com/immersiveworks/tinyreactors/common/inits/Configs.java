package com.immersiveworks.tinyreactors.common.inits;

import java.io.File;

import com.immersiveworks.tinyreactors.common.TinyReactors;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber( modid = TinyReactors.ID )
public class Configs {

	public static Configuration config;
	
	@SubscribeEvent
	public static void onConfigChanged( ConfigChangedEvent.OnConfigChangedEvent event ) {
		if( event.getModID().equals( TinyReactors.ID ) )
			syncConfiguration();
	}
	
	public static void initialize( File file ) {
		config = new Configuration( file );
		syncConfiguration();
	}
	
	public static void syncConfiguration() {
		String category;
		
		category = "General";
		config.addCustomCategoryComment( category, "General Settings for Tiny Reactors" );
		
		// VARIABLE = config.getBoolean( "Name in GUI", category, VARIABLE, VARIABLE_LABEL );
		
		if( config.hasChanged() )
			config.save();
	}
	
}
