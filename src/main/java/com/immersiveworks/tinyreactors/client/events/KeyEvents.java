package com.immersiveworks.tinyreactors.client.events;

import org.lwjgl.input.Keyboard;

import com.immersiveworks.tinyreactors.common.TinyReactors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber( modid = TinyReactors.ID, value = Side.CLIENT )
public class KeyEvents {
	
	public static KeyBinding overlayGui = new KeyBinding( "key.tinyreactors.overlayGui", Keyboard.KEY_O, "key.tinyreactors.category" );

	@SubscribeEvent
	public static void onKeyInput( KeyInputEvent event ) {
		if( overlayGui.isPressed() )
			Minecraft.getMinecraft().player.openGui( TinyReactors.instance, 0, Minecraft.getMinecraft().world, 0, 0, 0 );
	}
	
}
