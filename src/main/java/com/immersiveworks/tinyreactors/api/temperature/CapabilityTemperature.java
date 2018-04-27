package com.immersiveworks.tinyreactors.api.temperature;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityTemperature {

    @CapabilityInject( ITemperatureStorage.class )
    public static Capability<ITemperatureStorage> TEMPERATURE = null;

    public static void register()
    {
        CapabilityManager.INSTANCE.register( ITemperatureStorage.class, new IStorage<ITemperatureStorage>() {
            @Override
            public NBTBase writeNBT( Capability<ITemperatureStorage> capability, ITemperatureStorage instance, EnumFacing side )
            {
                return new NBTTagFloat( instance.getCurrentTemperature() );
            }

            @Override
            public void readNBT( Capability<ITemperatureStorage> capability, ITemperatureStorage instance, EnumFacing side, NBTBase nbt )
            {
                if( !( instance instanceof ITemperatureStorage ) )
                    throw new IllegalArgumentException( "Can not deserialize to an instance that isn't the default implementation" );
                ( ( TemperatureStorage )instance ).setCurrentTemperature( ( ( NBTTagFloat )nbt ).getFloat() );
            }
        },
        () -> new TemperatureStorage( 1000 ) );
    }
	
}
