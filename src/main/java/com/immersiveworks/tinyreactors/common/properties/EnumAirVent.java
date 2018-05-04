package com.immersiveworks.tinyreactors.common.properties;

import com.immersiveworks.tinyreactors.api.temperature.ITemperatureStorage;
import com.immersiveworks.tinyreactors.common.storage.StorageReactor;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

public enum EnumAirVent implements IStringSerializable {

	EMPTY( 0 ),
	WOOD( 200 ),
	STONE( 350 ),
	IRON( 816 ),
	GOLD( 1094 ),
	DIAMOND( 3316 ) {
		@Override
		public EnumAirVent next() {
			return EMPTY;
		}
	};
	
	public static EnumAirVent[] FANS = { WOOD, STONE, IRON, GOLD, DIAMOND };
	
	private float meltingPoint;
	
	private EnumAirVent( float meltingPoint ) {
		this.meltingPoint = meltingPoint;
	}
	
	@Override
	public String getName() {
		return name().toLowerCase();
	}
	
	public EnumAirVent next() {
		return values()[ ordinal() + 1 ];
	}
	
	public float getReactorScaleFactor() {
		return ordinal() * 0.2F;
	}
	
	public float getMeltingPoint( ITemperatureStorage temperature ) {
		if( temperature == null )
			return meltingPoint;
		
		return meltingPoint + ( ( temperature.getMaximumTemperature() - StorageReactor.BASE_TEMPERATURE ) * getReactorScaleFactor() );
	}
	
	public static PropertyEnum<EnumAirVent> PROPERTY = PropertyEnum.create( "vent", EnumAirVent.class );
	
}
