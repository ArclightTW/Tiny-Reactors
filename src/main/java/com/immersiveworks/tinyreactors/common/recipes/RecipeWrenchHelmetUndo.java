package com.immersiveworks.tinyreactors.common.recipes;

import com.immersiveworks.tinyreactors.common.inits.Configs;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeWrenchHelmetUndo extends ShapelessOreRecipe {

	private ItemStack recipeResult;
	
	public RecipeWrenchHelmetUndo() {
		super( null, ( Item )null );
	}
	
	@Override
	public ItemStack getCraftingResult( InventoryCrafting inv ) {
		ItemStack result = getRecipeOutput();
		if( result == null || result.isEmpty() )
			return ItemStack.EMPTY;
		
		result.getTagCompound().removeTag( "hasInbuiltWrench" );
		return result;
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		if( recipeResult == null || recipeResult.isEmpty() )
			return ItemStack.EMPTY;
		
		return recipeResult.copy();
	}
	
	@Override
	public boolean matches( InventoryCrafting inv, World world ) {
		if( !Configs.WRENCH_ON_HELMETS )
			return false;
		
		ItemStack armorItem = null;
		int itemCount = 0;
		
		for( int i = 0; i < inv.getSizeInventory(); i++ ) {
			ItemStack itemstack = inv.getStackInSlot( i );
			if( itemstack.isEmpty() )
				continue;
			
			itemCount++;
			
			if( itemstack.getItem() instanceof ItemArmor && ( ( ItemArmor )itemstack.getItem() ).armorType == EntityEquipmentSlot.HEAD ) {
				if( armorItem != null )
					return false;
				
				if( !itemstack.hasTagCompound() || !itemstack.getTagCompound().getBoolean( "hasInbuiltWrench" ) )
					return false;
				
				armorItem = itemstack;
				continue;
			}
			
			if( armorItem == null )
				return false;
		}
		
		recipeResult = armorItem;
		return itemCount == 1 && armorItem != null;
	}

}
