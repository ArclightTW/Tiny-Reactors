package com.immersiveworks.tinyreactors.client.proxy;

import java.util.Set;

import com.immersiveworks.tinyreactors.client.gui.GuiFactoryConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class GuiFactory implements IModGuiFactory {

	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui( GuiScreen parent ) {
		return new GuiFactoryConfig( parent );
	}

	
	@Override
	public void initialize( Minecraft minecraft ) {
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

}
