package com.immersiveworks.tinyreactors.client.util;

import net.minecraft.util.math.Vec2f;

public class RenderManualOutput {

	public Vec2f boundingBox;
	public int numberLines;
	
	public RenderManualOutput( Vec2f boundingBox, int numberLines ) {
		this.boundingBox = boundingBox;
		this.numberLines = numberLines;
	}
	
}
