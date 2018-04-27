package com.immersiveworks.tinyreactors.client.proxy;

import com.immersiveworks.tinyreactors.client.fx.FXEnergyOrb;
import com.immersiveworks.tinyreactors.client.fx.FXTypes;
import com.immersiveworks.tinyreactors.client.render.TESREnergyCell;
import com.immersiveworks.tinyreactors.common.proxy.IProxy;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityEnergyCell;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy implements IProxy {
	
	@Override
	public void onPreInit() {
		ClientRegistry.bindTileEntitySpecialRenderer( TileEntityEnergyCell.class, new TESREnergyCell() );
	}
	
	@Override
	public void displayFX( FXTypes type, double x, double y, double z, float r, float g, float b, float size, float motionX, float motionY, float motionZ ) {
		if( !doParticle() )
			return;
		
		Particle particle = null;
		switch( type ) {
		case ENERGY_ORB:
			particle = new FXEnergyOrb( Minecraft.getMinecraft().world, x, y, z, size, r, g, b ).setSpeed( motionX, motionY, motionZ );
			break;
		}
		
		if( particle != null )
			Minecraft.getMinecraft().effectRenderer.addEffect( particle );
	}
	
	private boolean doParticle() {
		if( FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER )
			return false;
		
		float chance;
		switch( Minecraft.getMinecraft().gameSettings.particleSetting ) {
		case 1:
			chance = 0.6F;
			break;
		case 2:
			chance = 0.2F;
			break;
		default:
			chance = 1F;
		}
		return chance == 1F || Math.random() < chance;
	}
	
}
