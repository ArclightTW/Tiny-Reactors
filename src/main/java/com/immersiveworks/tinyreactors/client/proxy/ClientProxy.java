package com.immersiveworks.tinyreactors.client.proxy;

import com.immersiveworks.tinyreactors.client.events.KeyEvents;
import com.immersiveworks.tinyreactors.client.fx.FXEnergyOrb;
import com.immersiveworks.tinyreactors.client.fx.FXTypes;
import com.immersiveworks.tinyreactors.client.gui.GuiTinyWrenchOverlayOptions;
import com.immersiveworks.tinyreactors.client.render.TESREnergyCell;
import com.immersiveworks.tinyreactors.client.render.TESRReactorController;
import com.immersiveworks.tinyreactors.client.render.TESRReactorPlanner;
import com.immersiveworks.tinyreactors.client.render.TESRReactorSurgeProtector;
import com.immersiveworks.tinyreactors.common.TinyReactors;
import com.immersiveworks.tinyreactors.common.proxy.IProxy;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityEnergyCell;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorController;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorPlanner;
import com.immersiveworks.tinyreactors.common.tiles.TileEntityReactorSurgeProtector;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy implements IProxy {
	
	@Override
	public void onPreInit() {
		B3DLoader.INSTANCE.addDomain( TinyReactors.ID );
		OBJLoader.INSTANCE.addDomain( TinyReactors.ID );
		
		ClientRegistry.bindTileEntitySpecialRenderer( TileEntityReactorController.class, new TESRReactorController() );
		ClientRegistry.bindTileEntitySpecialRenderer( TileEntityEnergyCell.class, new TESREnergyCell() );
		
		ClientRegistry.bindTileEntitySpecialRenderer( TileEntityReactorSurgeProtector.class, new TESRReactorSurgeProtector() );
		ClientRegistry.bindTileEntitySpecialRenderer( TileEntityReactorPlanner.class, new TESRReactorPlanner() );
		
		ClientRegistry.registerKeyBinding( KeyEvents.overlayGui );
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
	
	@Override
	public Object getServerGuiElement( int ID, EntityPlayer player, World world, int x, int y, int z ) {
		return null;
	}
	
	@Override
	public Object getClientGuiElement( int ID, EntityPlayer player, World world, int x, int y, int z ) {
		switch( ID ) {
		case 0:
			return new GuiTinyWrenchOverlayOptions();
		default:
			return null;
		}
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
