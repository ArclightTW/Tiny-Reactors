package com.immersiveworks.tinyreactors.client.fx;

import java.util.Queue;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Queues;
import com.immersiveworks.tinyreactors.common.TinyReactors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class FXEnergyOrb extends Particle {

	private static final ResourceLocation particles = new ResourceLocation( TinyReactors.ID, "textures/misc/energy_orb.png" );
	
	private static final Queue<FXEnergyOrb> queuedRenders = Queues.newArrayDeque();
	
	private float partialTicks;
	private float rotationX;
	private float rotationZ;
	private float rotationYZ;
	private float rotationXY;
	private float rotationXZ;
	
	public FXEnergyOrb( World world, double x, double y, double z, float size, float r, float g, float b ) {
		super( world, x, y, z, 0, 0, 0 );
		
		particleRed = r;
		particleGreen = g;
		particleBlue = b;
		particleAlpha = 0.5F;
		particleGravity = 0;
		particleScale *= size;
		
		setSpeed( 0, 0, 0 );
		setSize( 0.01F, 0.01F );
	}
	
	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		
		if( particleAge++ >= particleMaxAge )
			setExpired();
		
		motionY -= 0.04 * ( double )particleGravity;
		move( motionX, motionY, motionZ );
		
		motionX *= 0.9800000190734863;
		motionY *= 0.9800000190734863;
		motionZ *= 0.9800000190734863;
	}
	
	@Override
	public void renderParticle( BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ ) {
		this.partialTicks = partialTicks;
		this.rotationX = rotationX;
		this.rotationZ = rotationZ;
		this.rotationYZ = rotationYZ;
		this.rotationXY = rotationXY;
		this.rotationXZ = rotationXZ;
		
		queuedRenders.add( this );
	}
	
	public FXEnergyOrb setSpeed( float motionX, float motionY, float motionZ ) {
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
		return this;
	}
	
	public static void dispatchRenders( Tessellator tessellator ) {
		GlStateManager.color( 1F, 1F, 1F, 0.75F );
		Minecraft.getMinecraft().getTextureManager().bindTexture( particles );

		if( !queuedRenders.isEmpty() ) {
			tessellator.getBuffer().begin( GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR );
			
			for( FXEnergyOrb fx : queuedRenders )
				fx.renderParticle( tessellator.getBuffer() );
			
			tessellator.draw();
		}
		
		queuedRenders.clear();
	}
	
	private void renderParticle( BufferBuilder buffer ) {
		float halfScale = 0.5F * particleScale;
		float x = ( float )( prevPosX + ( posX - prevPosX ) * partialTicks - interpPosX );
		float y = ( float )( prevPosY + ( posY - prevPosY ) * partialTicks - interpPosY );
		float z = ( float )( prevPosZ + ( posZ - prevPosZ ) * partialTicks - interpPosZ );
		int combined = 15 << 20 | 15 << 4;
		int lightX = combined >> 16 & 0xFFFF;
		int lightY = combined & 0xFFFF;
		
		buffer.pos( x - rotationX * halfScale - rotationXY * halfScale, y - rotationZ * halfScale, z - rotationYZ * halfScale - rotationXZ * halfScale ).tex( 0, 1 ).lightmap( lightX, lightY ).color( particleRed, particleGreen, particleBlue, 0.5F ).endVertex();
		buffer.pos( x - rotationX * halfScale + rotationXY * halfScale, y + rotationZ * halfScale, z - rotationYZ * halfScale + rotationXZ * halfScale ).tex( 1, 1 ).lightmap( lightX, lightY ).color( particleRed, particleGreen, particleBlue, 0.5F ).endVertex();
		buffer.pos( x + rotationX * halfScale + rotationXY * halfScale, y + rotationZ * halfScale, z + rotationYZ * halfScale + rotationXZ * halfScale ).tex( 1, 0 ).lightmap( lightX, lightY ).color( particleRed, particleGreen, particleBlue, 0.5F ).endVertex();
		buffer.pos( x + rotationX * halfScale - rotationXY * halfScale, y - rotationZ * halfScale, z + rotationYZ * halfScale - rotationXZ * halfScale ).tex( 0, 0 ).lightmap( lightX, lightY ).color( particleRed, particleGreen, particleBlue, 0.5F ).endVertex();
	}
	
}
