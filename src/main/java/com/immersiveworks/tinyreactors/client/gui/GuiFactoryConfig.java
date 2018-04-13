package com.immersiveworks.tinyreactors.client.gui;

import java.util.List;

import com.google.common.collect.Lists;
import com.immersiveworks.tinyreactors.common.TinyReactors;
import com.immersiveworks.tinyreactors.common.inits.Configs;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class GuiFactoryConfig extends GuiConfig {

	public GuiFactoryConfig( GuiScreen parent ) {
		super( parent, getConfigElements(), TinyReactors.ID, false, false, GuiConfig.getAbridgedConfigPath( Configs.config.toString() ) );
	}
	
	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = Lists.newLinkedList();
		
		for( String category : Configs.config.getCategoryNames() )
			list.add( new ConfigElement( Configs.config.getCategory( category ) ) );
		
		return list;
	}
	
}
