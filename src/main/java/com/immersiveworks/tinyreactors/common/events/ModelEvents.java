package com.immersiveworks.tinyreactors.common.events;

import com.immersiveworks.tinyreactors.common.TinyReactors;
import com.immersiveworks.tinyreactors.common.inits.Blocks;
import com.immersiveworks.tinyreactors.common.inits.Items;
import com.immersiveworks.tinyreactors.common.util.Registries;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber( modid = TinyReactors.ID )
public class ModelEvents {

	@SubscribeEvent
	public static void onRegisterModel( ModelRegistryEvent event ) {
		Registries.BLOCKS.registerAll( Blocks.class, ( block, name ) -> {
			ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( block ), 0, new ModelResourceLocation( name, "inventory" ) );
		} );
		
		Registries.ITEMS.registerAll( Items.class, ( item, name ) -> {
			ModelLoader.setCustomModelResourceLocation( item, 0, new ModelResourceLocation( name, "inventory" ) );
		} );
	}
	
}
