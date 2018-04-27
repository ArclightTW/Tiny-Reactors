package com.immersiveworks.tinyreactors.client.gui;

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
		
		for( int i = 0; i < display.length; i++ )
			Minecraft.getMinecraft().fontRenderer.drawString( display[ i ], sr.getScaledWidth() / 2 - Minecraft.getMinecraft().fontRenderer.getStringWidth( display[ i ] ) / 2, startY + ( i * ( Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 3 ) ), 0xFFFFFF );
		
		GlStateManager.popAttrib();
	}

}
