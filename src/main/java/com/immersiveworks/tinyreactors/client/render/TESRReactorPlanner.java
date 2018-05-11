package com.immersiveworks.tinyreactors.client.render;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.immersiveworks.tinyreactors.api.util.Reactor;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorPlanner;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorPlanner.EnumColorOverlay;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class TESRReactorPlanner extends TileEntitySpecialRenderer<TileEntityReactorPlanner> {

	private int counterScaleDown, counterScaleUp;
	private int blockCounter, tickCounter;
	
	public TESRReactorPlanner() {
		counterScaleDown = counterScaleUp = -1;
	}
	
	@Override
	public void render( TileEntityReactorPlanner planner, double srcX, double srcY, double srcZ, float partialTicks, int destroyStage, float alpha ) {
		tickCounter++;
		if( tickCounter > 100 ) {
			tickCounter = 0;
			blockCounter++;
			if( blockCounter > 1000 )
				blockCounter = 0;
		}
		
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.translate( srcX + 0.5, srcY + 0.5, srcZ + 0.5 );
		
		int li = planner.getWorld().getCombinedLight( planner.getPos(), 15728640 );
		OpenGlHelper.setLightmapTextureCoords( OpenGlHelper.lightmapTexUnit, ( float )li % 65536, ( float ) li / 65536 );
		
		GlStateManager.disableLighting();
		GlStateManager.blendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
		GlStateManager.enableBlend();
		
		GlStateManager.color( 1F, 1F, 1F, 0.5F );
		GlStateManager.shadeModel( Minecraft.isAmbientOcclusionEnabled() ? GL11.GL_SMOOTH : GL11.GL_FLAT );
		
		float scale = 0.35F;
		
		if( planner.getPrevMinX() != planner.getMinX() || planner.getPrevMinY() != planner.getMinY() || planner.getPrevMinZ() != planner.getMinZ() || planner.getPrevMaxX() != planner.getMaxX() || planner.getPrevMaxY() != planner.getMaxY() || planner.getPrevMaxZ() != planner.getMaxZ() ) {
			if( counterScaleDown == -1 && counterScaleUp == -1 )
				counterScaleDown = 20;
			
			if( counterScaleUp == -1 && counterScaleDown > -1 ) {
				counterScaleDown--;
				if( counterScaleDown > -1 )
					scale = 0.35F * ( counterScaleDown / 20F );
				else {
					scale = 0.01F;
					counterScaleDown = -1;
					counterScaleUp = 0;
				}
			}
			
			if( counterScaleDown == -1 && counterScaleUp >= 0 ) {
				counterScaleUp++;
				if( counterScaleUp < 20 )
					scale = 0.35F * ( counterScaleUp / 20F );
				else {
					scale = 0.35F;
					counterScaleDown = -1;
					counterScaleUp = -1;
					
					planner.setPrevMinX( planner.getMinX() );
					planner.setPrevMinY( planner.getMinY() );
					planner.setPrevMinZ( planner.getMinZ() );
					planner.setPrevMaxX( planner.getMaxX() );
					planner.setPrevMaxY( planner.getMaxY() );
					planner.setPrevMaxZ( planner.getMaxZ() );
				}
			}
		}
		
		if( planner.getColorOverlay() != EnumColorOverlay.OFF ) {
			for( int x = planner.getMinX(); x <= planner.getMaxX(); x++ ) {
				for( int z = planner.getMinZ(); z <= planner.getMaxZ(); z++ ) {
					for( int y = planner.getMinY(); y <= planner.getMaxY(); y++ ) {
						IBlockState state = Minecraft.getMinecraft().world.getBlockState( planner.getPos().add( x, y, z ) );
						if( x <= planner.getMinX() || x >= planner.getMaxX() || y <= planner.getMinY() || y >= planner.getMaxY() || z <= planner.getMinZ() || z >= planner.getMaxZ() )
							continue;
						
						if( state.getBlock() == net.minecraft.init.Blocks.AIR )
							continue;
						
						GlStateManager.pushMatrix();
						GlStateManager.translate( x, y, z );
						
						GlStateManager.color( 244 / 255F, 170 / 255F, 66 / 255F, 0.75F );
						
						GlStateManager.disableTexture2D();
						renderShadedBlock( state, 0.5F );
						GlStateManager.enableTexture2D();
						
						GlStateManager.color( 1F, 1F, 1F, 0.5F );
						GlStateManager.popMatrix();
					}
				}
			}
		}
		
		for( Map.Entry<BlockPos, List<IBlockState>> p : planner.getBlocks().entrySet() ) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(
					( planner.getPos().getX() - p.getKey().getX() ) * -1.0F,
					( planner.getPos().getY() - p.getKey().getY() ) * -1.0F,
					( planner.getPos().getZ() - p.getKey().getZ() ) * -1.0F
					);
			
			IBlockState current = Minecraft.getMinecraft().world.getBlockState( p.getKey() );
			if( !Reactor.contained( current, p.getValue() ) ) {
				renderShadedBlock( p.getValue().get( blockCounter % p.getValue().size() ), scale );
				
				if( planner.getColorOverlay() != EnumColorOverlay.OFF && current.getBlock() != Blocks.AIR ) {
					GlStateManager.color( 244 / 255F, 170 / 255F, 66 / 255F, 0.75F );
					
					GlStateManager.disableTexture2D();
					renderShadedBlock( current, 0.5F );
					GlStateManager.enableTexture2D();
				}
			}
			else if( planner.getColorOverlay() == EnumColorOverlay.ALL ) {
				GlStateManager.color( 110 / 255F, 244 / 255F, 66 / 255F, 0.75F );
				
				GlStateManager.disableTexture2D();
				renderShadedBlock( current, 0.5F );
				GlStateManager.enableTexture2D();
			}
			
			GlStateManager.color( 1F, 1F, 1F, 0.5F );
			GlStateManager.popMatrix();
		}
		
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
	}
	
	@Override
	public boolean isGlobalRenderer( TileEntityReactorPlanner planner ) {
		return true;
	}
	
	private void renderShadedBlock( IBlockState state, float size ) {
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder bb = tess.getBuffer();
		
		for( BakedQuad quad : Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState( state ).getQuads( state, null, 0L ) ) {
			Minecraft.getMinecraft().getTextureManager().bindTexture( new ResourceLocation( quad.getSprite().getIconName().replace( "tinyreactors:", "tinyreactors:textures/" ) + ".png" ) );
			bb.begin( GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX );
			
			switch( quad.getFace() ) {
			case NORTH:
				bb.pos( -size - 0.001, -size - 0.001, -size - 0.001 ).tex( 1, 1 ).endVertex();
				bb.pos( -size - 0.001,  size + 0.001, -size - 0.001 ).tex( 1, 0 ).endVertex();
				bb.pos(  size + 0.001,  size + 0.001, -size - 0.001 ).tex( 0, 0 ).endVertex();
				bb.pos(  size + 0.001, -size, - 0.001 -size - 0.001 ).tex( 0, 1 ).endVertex();
				break;
			case SOUTH:
				bb.pos( -size - 0.001, -size - 0.001,  size + 0.001 ).tex( 0, 1 ).endVertex();
				bb.pos(  size + 0.001, -size - 0.001,  size + 0.001 ).tex( 1, 1 ).endVertex();
				bb.pos(  size + 0.001,  size + 0.001,  size + 0.001 ).tex( 1, 0 ).endVertex();
				bb.pos( -size - 0.001,  size + 0.001,  size + 0.001 ).tex( 0, 0 ).endVertex();
				break;
				
			case EAST:
				bb.pos(  size + 0.001, -size - 0.001, -size - 0.001 ).tex( 1, 1 ).endVertex();
				bb.pos(  size + 0.001,  size + 0.001, -size - 0.001 ).tex( 1, 0 ).endVertex();
		        bb.pos(  size + 0.001,  size + 0.001,  size + 0.001 ).tex( 0, 0 ).endVertex();
		        bb.pos(  size + 0.001, -size - 0.001,  size + 0.001 ).tex( 0, 1 ).endVertex();
				break;
			case WEST:
				bb.pos( -size - 0.001, -size - 0.001, -size - 0.001 ).tex( 0, 1 ).endVertex();
				bb.pos( -size - 0.001, -size - 0.001,  size + 0.001 ).tex( 1, 1 ).endVertex();
				bb.pos( -size - 0.001,  size + 0.001,  size + 0.001 ).tex( 1, 0 ).endVertex();
				bb.pos( -size - 0.001,  size + 0.001, -size - 0.001 ).tex( 0, 0 ).endVertex();
				break;
				
			case UP:
				bb.pos( -size - 0.001,  size + 0.001, -size - 0.001 ).tex( 0, 0 ).endVertex();
		        bb.pos( -size - 0.001,  size + 0.001,  size + 0.001 ).tex( 0, 1 ).endVertex();
		        bb.pos(  size + 0.001,  size + 0.001,  size + 0.001 ).tex( 1, 1 ).endVertex();
		        bb.pos(  size + 0.001,  size + 0.001, -size - 0.001 ).tex( 1, 0 ).endVertex();
				break;
			case DOWN:
				bb.pos( -size - 0.001, -size - 0.001, -size - 0.001 ).tex( 1, 1 ).endVertex();
		        bb.pos(  size + 0.001, -size - 0.001, -size - 0.001 ).tex( 0, 1 ).endVertex();
		        bb.pos(  size + 0.001, -size - 0.001,  size + 0.001 ).tex( 0, 0 ).endVertex();
		        bb.pos( -size - 0.001, -size - 0.001,  size + 0.001 ).tex( 1, 0 ).endVertex();
				break;
			}
			
			tess.draw();
		}
	}
	
}
