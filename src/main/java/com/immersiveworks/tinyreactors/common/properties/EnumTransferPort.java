package com.immersiveworks.tinyreactors.common.properties;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

public enum EnumTransferPort implements IStringSerializable {

	ENERGY( TextFormatting.YELLOW + "Energy" ),
	ITEM( TextFormatting.GREEN + "Item" ),
	LIQUID( TextFormatting.AQUA + "Liquid" ) {
		@Override
		public EnumTransferPort next() {
			return values()[ 0 ];
		}
	};
	
	private String display;
	
	private EnumTransferPort( String display ) {
		this.display = display;
	}
	
	@Override
	public String getName() {
		return name().toLowerCase();
	}
	
	public EnumTransferPort next() {
		return values()[ ordinal() + 1 ];
	}
	
	public String getDisplay() {
		return display;
	}
	
	public static PropertyEnum<EnumTransferPort> PROPERTY = PropertyEnum.create( "transfer", EnumTransferPort.class );
	
}
