package com.immersiveworks.tinyreactors.common.blocks;

import com.immersiveworks.tinyreactors.api.manual.pages.ManualPage;
import com.immersiveworks.tinyreactors.api.manual.pages.ManualPageRequirement;
import com.immersiveworks.tinyreactors.api.manual.pages.ManualPageTextDetails;
import com.immersiveworks.tinyreactors.api.manual.pages.ManualPageTextStructure;
import com.immersiveworks.tinyreactors.api.manual.pages.ManualPageRequirement.Requirement;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockReactorCasing extends BlockTiny {

	public BlockReactorCasing() {
		super( Material.IRON );
		setSoundType( SoundType.METAL );
		
		setHardness( 5F );
		setResistance( 15F );
	}
	
	@Override
	public String getManualKey() {
		return "reactor_casing";
	}
	
	@Override
	public ManualPage[] getManualPages() {
		return new ManualPage[] {
				new ManualPageRequirement( Requirement.REQUIRED ),
				new ManualPageTextDetails( this ),
				new ManualPageTextStructure( this )
		};
	}
	
}
