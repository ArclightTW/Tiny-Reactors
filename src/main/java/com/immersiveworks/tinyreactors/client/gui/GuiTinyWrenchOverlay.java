package com.immersiveworks.tinyreactors.client.gui;

import org.apache.commons.lang3.StringUtils;

import com.immersiveworks.tinyreactors.client.energy.IEnergyNetworkBlockRenderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly( Side.CLIENT )
public class GuiTinyWrenchOverlay extends Gui {
	
	public static GuiTinyWrenchOverlay instance = new GuiTinyWrenchOverlay();
	
	public void render( World world, BlockPos pos, IBlockState state, IEnergyNetworkBlockRenderer block ) {
		GlStateManager.pushAttrib();

		String[] display = block.getWrenchOverlayInfo( world, pos, state );
		
		ScaledResolution sr = new ScaledResolution( Minecraft.getMinecraft() );
		int startY = sr.getScaledHeight() / 2 + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT / 2 + 3;
		
		int lineIndex = 0;
		
		for( int i = 0; i < display.length; i++ ) {
			String[] lines = display[ i ].split( "\n" );
			for( int j = 0; j < lines.length; j++ ) {
				if( StringUtils.isBlank( lines[ j ] ) )
					continue;
				
				Minecraft.getMinecraft().fontRenderer.drawString( lines[ j ], sr.getScaledWidth() / 2 - Minecraft.getMinecraft().fontRenderer.getStringWidth( lines[ j ] ) / 2, startY + ( lineIndex * ( Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3 ) ), 0xFFFFFF );
				lineIndex++;
			}
		}
		
		GlStateManager.popAttrib();
	}

}
