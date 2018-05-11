package com.immersiveworks.tinyreactors.common.events;

import com.immersiveworks.tinyreactors.api.events.ManualRegistrationEvent;
import com.immersiveworks.tinyreactors.api.manual.IManualEntry;
import com.immersiveworks.tinyreactors.api.util.Registries;
import com.immersiveworks.tinyreactors.client.gui.GuiTinyManual;
import com.immersiveworks.tinyreactors.common.TinyReactors;
import com.immersiveworks.tinyreactors.common.inits.Blocks;
import com.immersiveworks.tinyreactors.common.inits.Items;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber( modid = TinyReactors.ID )
public class ManualEvents {
	
	@SubscribeEvent( priority = EventPriority.HIGHEST )
	public static void onManualRegistrationHighest( ManualRegistrationEvent event ) {
		Registries.BLOCKS.iterateAll( Blocks.class, ( block, name ) -> {
			if( block instanceof IManualEntry )
				( ( IManualEntry )block ).registerSection( event.getManual() );
		} );
		
		Registries.ITEMS.iterateAll( Items.class, ( item, name ) -> {
			if( item instanceof IManualEntry )
				( ( IManualEntry )item ).registerSection( event.getManual() );
		} );
	}
	
	@SubscribeEvent( priority = EventPriority.LOWEST )
	public static void onManualRegistrationLowest( ManualRegistrationEvent event ) {
		GuiTinyManual.instance = ( GuiTinyManual )event.getManual();
		GuiTinyManual.instance.registerBackPage();
	}
	
	public static class ReloadListener implements IResourceManagerReloadListener {

		@Override
		public void onResourceManagerReload( IResourceManager resourceManager ) {
			MinecraftForge.EVENT_BUS.post( new ManualRegistrationEvent( GuiTinyManual.createInstance() ) );
		}
		
	}

}
