package com.immersiveworks.tinyreactors.common.events;

import com.immersiveworks.tinyreactors.common.TinyReactors;
import com.immersiveworks.tinyreactors.common.inits.Blocks;
import com.immersiveworks.tinyreactors.common.inits.Entities;
import com.immersiveworks.tinyreactors.common.inits.Items;
import com.immersiveworks.tinyreactors.common.util.Registries;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber( modid = TinyReactors.ID )
public class RegistryEvents {
	
	@SubscribeEvent
	public static void onRegisterBlock( RegistryEvent.Register<Block> event ) {
		Registries.BLOCKS.registerAll( Blocks.class, ( block, name ) -> {
			event.getRegistry().register( block.setUnlocalizedName( name ).setRegistryName( name ) );
			
			if( block.hasTileEntity( null ) )
				GameRegistry.registerTileEntity( block.createTileEntity( null, null ).getClass(), name );
		} );
		
		Registries.ENTITIES.registerAll( Entities.class, ( entity, name ) -> {
			EntityRegistry.registerModEntity( new ResourceLocation( name ), entity.getClass(), name, Registries.ENTITIES.counter, TinyReactors.instance, 64, 10, true );
		} );
	}
	
	@SubscribeEvent
	public static void onRegisterItem( RegistryEvent.Register<Item> event ) {
		Registries.BLOCKS.registerAll( Blocks.class, ( block, name ) -> {
			event.getRegistry().register( new ItemBlock( block ).setRegistryName( name ) );
		} );
		
		Registries.ITEMS.registerAll( Items.class, ( item, name ) -> {
			event.getRegistry().register( item.setUnlocalizedName( name ).setRegistryName( name ) );
		} );
	}
	
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
