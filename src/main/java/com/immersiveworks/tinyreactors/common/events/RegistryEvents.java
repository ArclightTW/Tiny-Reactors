package com.immersiveworks.tinyreactors.common.events;

import com.immersiveworks.tinyreactors.common.TinyReactors;
import com.immersiveworks.tinyreactors.common.inits.Blocks;
import com.immersiveworks.tinyreactors.common.inits.Entities;
import com.immersiveworks.tinyreactors.common.inits.Items;
import com.immersiveworks.tinyreactors.common.inits.Recipes;
import com.immersiveworks.tinyreactors.common.util.Registries;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
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
	public static void onRegisterIRecipe( RegistryEvent.Register<IRecipe> event ) {
		Registries.RECIPES.registerAll( Recipes.class, ( recipe, name ) -> {
			event.getRegistry().register( recipe.setRegistryName( new ResourceLocation( TinyReactors.ID, name ) ) );
		} );
	}
	
}
