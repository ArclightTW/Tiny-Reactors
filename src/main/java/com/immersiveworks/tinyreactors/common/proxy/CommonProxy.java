package com.immersiveworks.tinyreactors.common.proxy;

import com.immersiveworks.tinyreactors.client.fx.FXTypes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class CommonProxy implements IProxy {

	@Override
	public void onPreInit() {
	}
	
	@Override
	public void displayFX( FXTypes type, double x, double y, double z, float r, float g, float b, float size, float motionX, float motionY, float motionZ ) {
	}
	
	@Override
	public Object getServerGuiElement( int ID, EntityPlayer player, World world, int x, int y, int z ) {
		return null;
	}
	
	@Override
	public Object getClientGuiElement( int ID, EntityPlayer player, World world, int x, int y, int z ) {
		return null;
	}
	
}
