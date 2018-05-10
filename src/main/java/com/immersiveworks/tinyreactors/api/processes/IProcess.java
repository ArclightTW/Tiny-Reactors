package com.immersiveworks.tinyreactors.api.processes;

public interface IProcess {

	public void update();
	public boolean isDead();
	
	public default boolean shouldCompleteOnServerClose() {
		return false;
	}
	
	public default void onDeath( boolean serverClosed ) {
	}
	
}
