package com.immersiveworks.tinyreactors.common.events;

import com.immersiveworks.tinyreactors.api.util.Registries;
import com.immersiveworks.tinyreactors.common.TinyReactors;
import com.immersiveworks.tinyreactors.common.inits.Blocks;
import com.immersiveworks.tinyreactors.common.inits.Items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber( modid = TinyReactors.ID )
public class ModelEvents {

	@SubscribeEvent
	public static void onRegisterModel( ModelRegistryEvent event ) {
		Registries.BLOCKS.registerAll( Blocks.class, ( block, name ) -> {
			NonNullList<ItemStack> itemstacks = NonNullList.create();
			block.getSubBlocks( block.getCreativeTabToDisplayOn(), itemstacks );
			
			for( ItemStack itemstack : itemstacks )
				ModelLoader.setCustomModelResourceLocation( itemstack.getItem(), itemstack.getItemDamage(), new ModelResourceLocation( itemstack.getUnlocalizedName().substring( 5 ), "inventory" ) );
		} );
		
		Registries.ITEMS.registerAll( Items.class, ( item, name ) -> {
			NonNullList<ItemStack> itemstacks = NonNullList.create();
			item.getSubItems( item.getCreativeTab(), itemstacks );
			
			for( ItemStack itemstack : itemstacks )
				ModelLoader.setCustomModelResourceLocation( itemstack.getItem(), itemstack.getItemDamage(), new ModelResourceLocation( itemstack.getUnlocalizedName().substring( 5 ), "inventory" ) );
		} );
	}
	
}
