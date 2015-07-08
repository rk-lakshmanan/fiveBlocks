package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;


public class ForBlock extends TranslatorBlock{
	public ForBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		String init = getTranslatorBlockAtSocket(0).toCode();
		String cond = getTranslatorBlockAtSocket(1).toCode();
		String step = getTranslatorBlockAtSocket(2).toCode();
		String execution = "";
		if(!isTranslatorBlockAtSocketNull(3)){
			execution = getTranslatorBlockAtSocket(3).toCode();
		}
		return "for("+init+" ; "+cond+" ; "+step+" ){\n"+execution+"\n}\n";
		
	}
}
