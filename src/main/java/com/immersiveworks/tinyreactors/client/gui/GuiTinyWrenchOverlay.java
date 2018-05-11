package com.immersiveworks.tinyreactors.client.gui;

import java.util.List;

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
		
		String combined = "";
		for( int i = 0; i < display.length; i++ )
			combined += display[ i ] + "\n";
		
		RenderUtils.drawString(
				combined,
				anchor.alignment,
				anchor.isBottom(),
				anchor.alignment == Alignment.LEFT ? 4 : anchor.alignment == Alignment.CENTER ? sr.getScaledWidth() / 2 : sr.getScaledWidth() - 4,
				anchor.isTop() ? 25 : anchor.isBottom() ? sr.getScaledHeight() - 43 : sr.getScaledHeight() / 2 + 10,
				0xFFFFFF,
				Configs.WRENCH_OVERLAY_SCALE_FACTOR
				);
		
		GlStateManager.popMatrix();
		// End Tooltip
		
		// Start Inventory
		GlStateManager.pushMatrix();
		
		int inventoryCount = 0;
		for( int i = 0; i < inventory.length; i++ )
			if( !inventory[ i ].isEmpty() )
				inventoryCount++;
		
		int startX = anchor.alignment == Alignment.LEFT ? 4 : anchor.alignment == Alignment.CENTER ? sr.getScaledWidth() / 2 - ( int )( ( inventoryCount / 2F ) * 18 ) : sr.getScaledWidth() - 4 - ( inventoryCount * 18 );
		int startY = anchor.isTop() ? 4 : anchor.isBottom() ? sr.getScaledHeight() - 43 : sr.getScaledHeight() / 2 - 25;
		
		int slotCount = 0;
		for( int i = 0; i < inventory.length; i++ ) {
			if( inventory[ i ].isEmpty() )
				continue;
			
			RenderUtils.drawItemStack(
					inventory[ i ],
					startX + ( slotCount * 18 ),
					startY
					);
			slotCount++;
		}
		
		GlStateManager.popMatrix();
		// End Inventory
		
		GlStateManager.popMatrix();
	}
	
	public static void setAnchor( GridAlignment anchor ) {
		GuiTinyWrenchOverlay.anchor = anchor;
		
		Configs.config.get( "DO_NOT_CHANGE", "Wrench Overlay Anchor", 0 ).set( anchor.name() );
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
		
		public boolean isTop() {
			return this == LEFT_TOP || this == CENTER_TOP || this == RIGHT_TOP;
		}
		
		public boolean isBottom() {
			return this == LEFT_BOTTOM || this == CENTER_BOTTOM || this == RIGHT_BOTTOM;
		}
	}
	
}
