package com.immersiveworks.tinyreactors.client.gui;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.immersiveworks.tinyreactors.api.item.IInternalInventory;
import com.immersiveworks.tinyreactors.client.energy.IEnergyNetworkBlockRenderer;
import com.immersiveworks.tinyreactors.client.util.RenderUtils;
import com.immersiveworks.tinyreactors.common.inits.Configs;

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
import net.minecraftforge.common.util.TextTable.Alignment;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

@SideOnly( Side.CLIENT )
public class GuiTinyWrenchOverlay extends Gui {
	
	public static GuiTinyWrenchOverlay instance = new GuiTinyWrenchOverlay();
	
	private GuiTinyWrenchOverlay() {
	}
	
	private static GridAlignment anchor;
	
	public void render( World world, EntityPlayer player, BlockPos pos, IBlockState state, IEnergyNetworkBlockRenderer block, TileEntity tile, EnumFacing facing, Vec3d hit ) {
		List<ItemStack> inventory = Lists.newLinkedList();
		if( tile != null && tile instanceof IInternalInventory ) {
			IInternalInventory internal = ( IInternalInventory )tile;
			
			if( internal.isAccessible() ) {
				IItemHandler item = internal.getInternalItem();
				for( int i = 0; i < item.getSlots(); i++ )
					inventory.add( item.getStackInSlot( i ) );
			}
		}
		
		render( block.getWrenchOverlayInfo( world, player, pos, state, facing, pos.getX() - ( float )hit.x, pos.getY() - ( float )hit.y, pos.getZ() - ( float )hit.z ), inventory.toArray( new ItemStack[ inventory.size() ] ) );
	}
	
	public void render( String[] display, ItemStack[] inventory ) {
		ScaledResolution sr = new ScaledResolution( Minecraft.getMinecraft() );
		
		GlStateManager.pushMatrix();

		// Start Tooltip
		GlStateManager.pushMatrix();
		
		int anchorX = anchor.alignment == Alignment.LEFT ? 0 : anchor.alignment == Alignment.CENTER ? sr.getScaledWidth() / 2 : sr.getScaledWidth();
		int anchorY =
				anchor == GridAlignment.LEFT_TOP || anchor == GridAlignment.CENTER_TOP || anchor == GridAlignment.RIGHT_TOP ? 0 :
					anchor == GridAlignment.LEFT_MIDDLE || anchor == GridAlignment.CENTER_MIDDLE || anchor == GridAlignment.RIGHT_MIDDLE ? sr.getScaledHeight() / 2 :
						sr.getScaledHeight();
		
//		float scaleFactor = Configs.WRENCH_OVERLAY_SCALE == 0 ? 0.5F : Configs.WRENCH_OVERLAY_SCALE == 1 ? 1 : 2;
//		float translateFactor = Configs.WRENCH_OVERLAY_SCALE == 0 ? 0.5F : Configs.WRENCH_OVERLAY_SCALE == 1 ? 0 : -0.25F;
		int startY = anchorY + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT / 2 + 11;		
		
		int lineIndex = 0;
		int lineCount = 0;
		
		for( int i = 0; i < display.length; i++ ) {
			if( StringUtils.isBlank( display[ i ] ) )
				continue;
			
			String[] lines = display[ i ].split( "\n" );
			for( int j = 0; j < lines.length; j++ ) {
				if( StringUtils.isBlank( lines[ j ] ) )
					continue;
				
				lineCount++;
			}
		}
		
		int offsetY = anchorY == sr.getScaledHeight() ? lineCount * Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 45 : 0;
		
		for( int i = 0; i < display.length; i++ ) {
			if( StringUtils.isBlank( display[ i ] ) )
				continue;
			
			String[] lines = display[ i ].split( "\n" );
			for( int j = 0; j < lines.length; j++ ) {
				if( StringUtils.isBlank( lines[ j ] ) )
					continue;
				
				int offsetX = 0;
				
				switch( anchor.alignment ) {
				case LEFT:
					offsetX = 0;
					break;
				case CENTER:
					offsetX = Minecraft.getMinecraft().fontRenderer.getStringWidth( lines[ j ] ) / 2;
					break;
				case RIGHT:
					offsetX = Minecraft.getMinecraft().fontRenderer.getStringWidth( lines[ j ] );
					break;
				}
				
//				GlStateManager.scale( scaleFactor, scaleFactor, scaleFactor );
//				GlStateManager.translate( sr.getScaledWidth_double() * translateFactor, sr.getScaledHeight_double() * translateFactor, 0F );
				
				Minecraft.getMinecraft().fontRenderer.drawString( lines[ j ], anchorX - offsetX, startY - offsetY + ( lineIndex * ( Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3 ) ), 0xFFFFFF );
				lineIndex++;
				
//				GlStateManager.translate( -sr.getScaledWidth_double() * translateFactor, -sr.getScaledHeight_double() * translateFactor, 0 );
//				GlStateManager.scale( 1F / scaleFactor, 1F / scaleFactor, 1F / scaleFactor );
			}
		}
		
		GlStateManager.popMatrix();
		// End Tooltip
		
		// Start Inventory
		GlStateManager.pushMatrix();
		
		RenderHelper.disableStandardItemLighting();
		RenderHelper.enableGUIStandardItemLighting();
		
		int slotCount = 0;
		for( int i = 0; i < inventory.length; i++ )
			if( !inventory[ i ].isEmpty() )
				slotCount++;
		
		int offsetX = 0;
		float adjustmentX = 0;
		switch( anchor.alignment ) {
		case LEFT:
			offsetX = 0;
			adjustmentX = 0;
			break;
		case CENTER:
			offsetX = sr.getScaledWidth() / 2;
			adjustmentX = 18 * ( slotCount / 2F );
			break;
		case RIGHT:
			offsetX = sr.getScaledWidth();
			adjustmentX = 18 * slotCount;
			break;
		}
		
		if( anchorY == sr.getScaledHeight() )
			offsetY = sr.getScaledHeight() - ( lineCount * 18 ) - 29;
		else if( anchorY == sr.getScaledHeight() / 2 )
			offsetY = sr.getScaledHeight() / 2 - 29;
		else
			offsetY = lineCount * 18;
		
		for( int i = 0; i < inventory.length; i++ ) {
			ItemStack itemstack = inventory[ i ];
			if( !itemstack.isEmpty() ) {
				zLevel = 200F;
				RenderUtils.drawItemStack( itemstack, ( int )( offsetX - adjustmentX + ( i * 18 ) ), offsetY );
				zLevel = 0F;
			}
		}
		
		GlStateManager.enableLighting();
		RenderHelper.enableStandardItemLighting();
		
		GlStateManager.popMatrix();
		// End Inventory
		
		GlStateManager.popMatrix();
	}
	
	public static void setAnchor( GridAlignment anchor ) {
		GuiTinyWrenchOverlay.anchor = anchor;
		
		Configs.config.get( "UI", "Wrench Overlay Anchor", 0 ).set( anchor.name() );
		Configs.config.save();
	}
	
	public static GridAlignment getAnchor() {
		return anchor;
	}

	public static enum GridAlignment {
		LEFT_TOP( Alignment.LEFT ),
		CENTER_TOP( Alignment.CENTER ),
		RIGHT_TOP( Alignment.RIGHT ),
		LEFT_MIDDLE( Alignment.LEFT ),
		CENTER_MIDDLE( Alignment.CENTER ),
		RIGHT_MIDDLE( Alignment.RIGHT ),
		LEFT_BOTTOM( Alignment.LEFT ),
		CENTER_BOTTOM( Alignment.CENTER ),
		RIGHT_BOTTOM( Alignment.RIGHT );
		
		public Alignment alignment;
		
		private GridAlignment( Alignment alignment ) {
			this.alignment = alignment;
		}
	}
	
}
