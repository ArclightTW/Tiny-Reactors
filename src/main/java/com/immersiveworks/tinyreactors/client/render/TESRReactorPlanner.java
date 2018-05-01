package com.immersiveworks.tinyreactors.client.render;

import java.awt.Color;

import com.immersiveworks.tinyreactors.common.inits.Blocks;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorPlanner;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TESRReactorPlanner extends TileEntitySpecialRenderer<TileEntityReactorPlanner> {

	@Override
	public void render( TileEntityReactorPlanner planner, double srcX, double srcY, double srcZ, float partialTicks, int destroyStage, float alpha ) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
        GlStateManager.blendFunc( SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_COLOR );
		
		BlockPos pos = planner.getPos();
		
		for( int x = -1; x <= 1; x++ )
			for( int z = -1; z <= 1; z++ )
				for( int y = 0; y < 3; y++ ) {
					if( x == 0 && z == 0 && y == 1 )
						continue;
					
					Block block = Blocks.REACTOR_CASING;
					
					if( x == 0 && z == -1 && y == 0 )
						block = Blocks.REACTOR_CONTROLLER;
					
					renderGhostBlock( block, pos.add( x, y, z ), partialTicks );
				}
		
		GlStateManager.enableLighting();
        GlStateManager.popMatrix();
	}
	
	@Override
	public boolean isGlobalRenderer( TileEntityReactorPlanner planner ) {
		return true;
	}
	
	private void renderGhostBlock( Block block, BlockPos position, float partialTicks ) {
		renderGhostBlock( block.getDefaultState(), position, partialTicks );
	}

	private void renderGhostBlock( IBlockState state, BlockPos position, float partialTicks ) {
		renderGhostBlock( state, position, Color.WHITE, true, partialTicks );
	}
	
	private void renderGhostBlock( IBlockState state, BlockPos position, Color color, boolean noDepth, float partialTicks ) {
		renderGhostModel( Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState( state ), position, color, noDepth, partialTicks );
	}
	
	private void renderGhostModel( IBakedModel model, BlockPos position, Color color, boolean noDepth, float partialTicks ) {
		GlStateManager.enableBlend();
		GlStateManager.blendFunc( SourceFactor.ONE, DestFactor.ONE_MINUS_DST_COLOR );
		
		BufferBuilder vb;
		if( noDepth ) {
			GlStateManager.depthFunc( 519 );
			
			GlStateManager.pushMatrix();
			Tessellator tessellator = Tessellator.getInstance();
			vb = tessellator.getBuffer();
			
			EntityPlayer player = Minecraft.getMinecraft().player;
	        double d0 = ( player.lastTickPosX + ( player.posX - player.lastTickPosX ) * ( double )partialTicks );
	        double d1 = ( player.lastTickPosY + ( player.posY - player.lastTickPosY ) * ( double )partialTicks ) + 500;
	        double d2 = ( player.lastTickPosZ + ( player.posZ - player.lastTickPosZ ) * ( double )partialTicks );
	        vb.setTranslation( -d0, -d1, -d2 );
		}
		else {
			GlStateManager.pushMatrix();
			Tessellator tessellator = Tessellator.getInstance();
			vb = tessellator.getBuffer();
			
			EntityPlayer player = Minecraft.getMinecraft().player;
	        double d0 = ( player.lastTickPosX + ( player.posX - player.lastTickPosX ) * ( double )partialTicks );
	        double d1 = ( player.lastTickPosY + ( player.posY - player.lastTickPosY ) * ( double )partialTicks );
	        double d2 = ( player.lastTickPosZ + ( player.posZ - player.lastTickPosZ ) * ( double )partialTicks );
	        vb.setTranslation( -d0, -d1, -d2 );
		}
		
		vb.begin( 7, DefaultVertexFormats.BLOCK );
		
		World world = Minecraft.getMinecraft().world;
		BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
		
		brd.getBlockModelRenderer().renderModel( world, model, Minecraft.getMinecraft().world.getBlockState( position ), position.add( 0, noDepth ? 500 : 0, 0 ), vb, false );
		
		for( int i = 0; i < vb.getVertexCount(); i++ )
			vb.putColorMultiplier( color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, i );
		
		vb.color( 1, 1, 1, 0.1F );
		
		Tessellator.getInstance().draw();
        Tessellator.getInstance().getBuffer().setTranslation( 0, 0, 0 );
        GlStateManager.popMatrix();
		
		GlStateManager.depthFunc( 515 );
		GlStateManager.disableBlend();
	}
	
}
