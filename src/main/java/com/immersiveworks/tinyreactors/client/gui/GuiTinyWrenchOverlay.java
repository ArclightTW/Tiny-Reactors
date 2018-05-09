package com.immersiveworks.tinyreactors.client.gui;

import org.apache.commons.lang3.StringUtils;

import com.immersiveworks.tinyreactors.api.item.IInternalInventory;
import com.immersiveworks.tinyreactors.client.energy.IEnergyNetworkBlockRenderer;
import com.immersiveworks.tinyreactors.client.util.RenderUtils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

@SideOnly( Side.CLIENT )
public class GuiTinyWrenchOverlay extends Gui {
	
	public static GuiTinyWrenchOverlay instance = new GuiTinyWrenchOverlay();
	
	public void render( World world, EntityPlayer player, BlockPos pos, IBlockState state, IEnergyNetworkBlockRenderer block, TileEntity tile, EnumFacing facing, Vec3d hit ) {
		GlStateManager.pushMatrix();

		// Start Tooltip
		GlStateManager.pushMatrix();
		
		String[] display = block.getWrenchOverlayInfo( world, player, pos, state, facing, pos.getX() - ( float )hit.x, pos.getY() - ( float )hit.y, pos.getZ() - ( float )hit.z );
		
		ScaledResolution sr = new ScaledResolution( Minecraft.getMinecraft() );
		int startY = sr.getScaledHeight() / 2 + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT / 2 + 11;
		
		int lineIndex = 0;
		
		for( int i = 0; i < display.length; i++ ) {
			if( StringUtils.isBlank( display[ i ] ) )
				continue;
			
			String[] lines = display[ i ].split( "\n" );
			for( int j = 0; j < lines.length; j++ ) {
				if( StringUtils.isBlank( lines[ j ] ) )
					continue;
				
				GlStateManager.scale( 0.5F, 0.5F, 0.5F );
				GlStateManager.translate( sr.getScaledWidth_double() / 2F, sr.getScaledHeight_double() / 2F, 0F );
				
				Minecraft.getMinecraft().fontRenderer.drawString( lines[ j ], sr.getScaledWidth() / 2 - Minecraft.getMinecraft().fontRenderer.getStringWidth( lines[ j ] ) / 2, startY + ( lineIndex * ( Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3 ) ), 0xFFFFFF );
				lineIndex++;
				
				GlStateManager.translate( -sr.getScaledWidth_double() / 2F, -sr.getScaledHeight_double() / 2F, 0 );
				GlStateManager.scale( 2F, 2F, 2F );
			}
		}
		
		GlStateManager.popMatrix();
		// End Tooltip
		
		// Start Inventory
		GlStateManager.pushMatrix();
		
		RenderHelper.disableStandardItemLighting();
		RenderHelper.enableGUIStandardItemLighting();
		
		if( tile != null && tile instanceof IInternalInventory ) {
			IInternalInventory inventory = ( IInternalInventory )tile;
			
			if( inventory.isAccessible() ) {
				IItemHandler item = inventory.getInternalItem();
				
				int slotCount = 0;
				for( int i = 0; i < item.getSlots(); i++ )
					if( !item.getStackInSlot( i ).isEmpty() )
						slotCount++;
				
				for( int i = 0; i < item.getSlots(); i++ ) {
					ItemStack itemstack = item.getStackInSlot( i );
					if( !itemstack.isEmpty() ) {
						zLevel = 200F;
						RenderUtils.drawItemStack( itemstack, ( int )( sr.getScaledWidth() / 2F - ( 18 * ( slotCount / 2F ) ) + ( i * 18 ) ), ( int )( sr.getScaledHeight() /2F - 2 ) - 21 );
						zLevel = 0F;
					}
				}
			}
		}
		
		GlStateManager.enableLighting();
		RenderHelper.enableStandardItemLighting();
		
		GlStateManager.popMatrix();
		// End Inventory
		
		GlStateManager.popMatrix();
	}
	
	

}
