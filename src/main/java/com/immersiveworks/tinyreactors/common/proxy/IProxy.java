package com.immersiveworks.tinyreactors.common.proxy;

import com.immersiveworks.tinyreactors.client.fx.FXTypes;

import net.minecraftforge.fml.common.network.IGuiHandler;

public interface IProxy extends IGuiHandler {

	void onPreInit();
	
	void displayFX( FXTypes type, double x, double y, double z, float r, float g, float b, float size, float motionX, float motionY, float motionZ );
	
}
