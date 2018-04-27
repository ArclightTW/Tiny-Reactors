package com.immersiveworks.tinyreactors.common;

import com.immersiveworks.tinyreactors.api.temperature.CapabilityTemperature;
import com.immersiveworks.tinyreactors.common.inits.Blocks;
import com.immersiveworks.tinyreactors.common.inits.Configs;
import com.immersiveworks.tinyreactors.common.proxy.IProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod( modid = TinyReactors.ID, name = TinyReactors.NAME, version = TinyReactors.VERSION, acceptedMinecraftVersions = TinyReactors.MINECRAFT, dependencies = TinyReactors.DEPENDENCIES, guiFactory = TinyReactors.GUI_FACTORY, modLanguage = "java" )
public class TinyReactors {
	
	@Mod.Instance( value = TinyReactors.ID )
	public static TinyReactors instance;
	
	@SidedProxy( clientSide = TinyReactors.CLIENT_PROXY, serverSide = TinyReactors.COMMON_PROXY )
	public static IProxy proxy;
	
	@Mod.EventHandler
	public void onPreInit( FMLPreInitializationEvent event ) {
		Configs.initialize( event.getSuggestedConfigurationFile() );
		
		proxy.onPreInit();
		
		CapabilityTemperature.register();
		
		B3DLoader.INSTANCE.addDomain( ID );
		OBJLoader.INSTANCE.addDomain( ID );
	}

	public static final String ID			= "tinyreactors";
	public static final String NAME			= "Tiny Reactors";
	public static final String VERSION		= "@VERSION@";
	public static final String MINECRAFT	= "[1.12.2]";
	public static final String DEPENDENCIES	= "after:forge@[14.23.2.2654,)";
	
	public static final String CLIENT_PROXY	= "com.immersiveworks.tinyreactors.client.proxy.ClientProxy";
	public static final String COMMON_PROXY	= "com.immersiveworks.tinyreactors.common.proxy.CommonProxy";
	public static final String GUI_FACTORY	= "com.immersiveworks.tinyreactors.client.proxy.GuiFactory";
	
	public static final CreativeTabs TAB	= new CreativeTabs( ID ) {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack( Blocks.REACTOR_CONTROLLER );
		}
	};
	
}
