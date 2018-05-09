package com.immersiveworks.tinyreactors.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

public class RenderUtils {

	private static RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
	
	public static void drawItemStack( ItemStack itemstack, int x, int y ) {
		drawItemStack( itemstack, x, y, String.format( "%,d", itemstack.getCount() ) );
	}
	
	public static void drawItemStack( ItemStack itemstack, int x, int y, String text ) {
		GlStateManager.translate( 0, 0, 32 );
		itemRender.zLevel = 200F;
		
		FontRenderer font = itemstack.getItem().getFontRenderer( itemstack );
		if( font == null )
			font = Minecraft.getMinecraft().fontRenderer;
		
		itemRender.renderItemAndEffectIntoGUI( itemstack, x, y );
		itemRender.renderItemOverlayIntoGUI( font, itemstack, x, y, text );
		
		itemRender.zLevel = 0F;
	}
	
}
