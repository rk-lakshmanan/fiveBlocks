package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class MyBlockBlock extends TranslatorBlock
{
	private Translator translator;
	private Long blockId;
	public MyBlockBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator);
		this.translator = translator;
		this.blockId = blockId;
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		return translator.getWorkspace().getBlockSave().getCodeOfMyBlock(translator.getBlock(blockId).getGenusName());
	}
}
