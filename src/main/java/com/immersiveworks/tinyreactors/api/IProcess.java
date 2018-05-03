package com.immersiveworks.tinyreactors.api;

public interface IProcess {

	public void update();
	public boolean isDead();
	
	public default boolean shouldCompleteOnServerClose() {
		return false;
	}
	
	public default void onDeath() {
	}
	
}
