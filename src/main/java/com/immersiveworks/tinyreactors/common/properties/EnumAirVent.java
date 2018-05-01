package com.immersiveworks.tinyreactors.common.properties;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

public enum EnumAirVent implements IStringSerializable {

	EMPTY,
	WOOD,
	STONE,
	IRON,
	GOLD,
	DIAMOND {
		@Override
		public EnumAirVent next() {
			return EMPTY;
		}
	};
	
	public static EnumAirVent[] FANS = { WOOD, STONE, IRON, GOLD, DIAMOND };
	
	private EnumAirVent() {
	}
	
	@Override
	public String getName() {
		return name().toLowerCase();
	}
	
	public EnumAirVent next() {
		return values()[ ordinal() + 1 ];
	}
	
	public static PropertyEnum<EnumAirVent> PROPERTY = PropertyEnum.create( "vent", EnumAirVent.class );
	
}
