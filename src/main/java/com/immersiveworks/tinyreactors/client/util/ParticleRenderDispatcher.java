package com.immersiveworks.tinyreactors.client.util;

import org.lwjgl.opengl.GL11;

import com.immersiveworks.tinyreactors.client.fx.FXEnergyOrb;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;

public final class ParticleRenderDispatcher {

	public static void dispatch() {
		Tessellator tessellator = Tessellator.getInstance();

		GL11.glPushAttrib( GL11.GL_LIGHTING_BIT );
		GlStateManager.depthMask( false );
		GlStateManager.enableBlend();
		GlStateManager.blendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE );
		GlStateManager.alphaFunc( GL11.GL_GREATER, 0.003921569F );
		GlStateManager.disableLighting();

		FXEnergyOrb.dispatchRenders( tessellator );
		
		GlStateManager.enableLighting();
		GlStateManager.alphaFunc( GL11.GL_GREATER, 0.1F );
		GlStateManager.blendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
		GlStateManager.disableBlend();
		GlStateManager.depthMask( true );
		GL11.glPopAttrib();
	}

}
