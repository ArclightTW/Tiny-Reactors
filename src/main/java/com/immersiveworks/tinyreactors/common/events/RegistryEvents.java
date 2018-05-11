package com.immersiveworks.tinyreactors.common.events;

import com.immersiveworks.tinyreactors.api.util.Registries;
import com.immersiveworks.tinyreactors.common.TinyReactors;
import com.immersiveworks.tinyreactors.common.inits.Blocks;
import com.immersiveworks.tinyreactors.common.inits.Entities;
import com.immersiveworks.tinyreactors.common.inits.Items;
import com.immersiveworks.tinyreactors.common.inits.Recipes;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
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
	}
	
	@SubscribeEvent
	public static void onRegisterItem( RegistryEvent.Register<Item> event ) {
		Registries.BLOCKS.iterateAll( Blocks.class, ( block, name ) -> {
			event.getRegistry().register( new ItemBlock( block ).setRegistryName( name ) );
		} );
		
		Registries.ITEMS.registerAll( Items.class, ( item, name ) -> {
			event.getRegistry().register( item.setUnlocalizedName( name ).setRegistryName( name ) );
		} );
	}
	
	@SubscribeEvent
	public static void onRegisterEntity( RegistryEvent.Register<EntityEntry> event ) {
		Registries.ENTITIES.registerAll( Entities.class, ( entity, name ) -> {
			event.getRegistry().register( EntityEntryBuilder.create().entity( entity.getClass() ).id( name, Registries.ENTITIES.counter ).name( name ).tracker( 64, 10, true ).build() );
		} );
	}
	
	@SubscribeEvent
	public static void onRegisterIRecipe( RegistryEvent.Register<IRecipe> event ) {
		Registries.RECIPES.registerAll( Recipes.class, ( recipe, name ) -> {
			event.getRegistry().register( recipe.setRegistryName( new ResourceLocation( TinyReactors.ID, name ) ) );
		} );
	}
	
}
