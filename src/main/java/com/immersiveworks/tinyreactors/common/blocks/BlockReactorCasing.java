package com.immersiveworks.tinyreactors.common.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockReactorCasing extends BlockTiny {

	public BlockReactorCasing() {
		super( Material.IRON );
		setSoundType( SoundType.METAL );
		
		setHardness( 5F );
		setResistance( 15F );
	}
	
}
