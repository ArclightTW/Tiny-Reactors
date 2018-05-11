package com.immersiveworks.tinyreactors.api.events;

import com.immersiveworks.tinyreactors.api.manual.ITinyManual;

import net.minecraftforge.fml.common.eventhandler.Event;

public class ManualRegistrationEvent extends Event {
	
	private ITinyManual manual;
	
	public ManualRegistrationEvent( ITinyManual manual ) {
		this.manual = manual;
	}
	
	public ITinyManual getManual() {
		return manual;
	}
	
	@Override
	public boolean isCancelable() {
		return false;
	}
	
}
