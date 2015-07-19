package com.ardublock.translator.block;

import java.util.ArrayList;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class LoopBlock extends TranslatorBlock
{
	public LoopBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		translator.getBlock(blockId).resetLocalVariableSet();
		String ret;
		ret = "void loop()\n{\n";
		String code = new String("");
		if (translator.isGuinoProgram())
		{
			code += "GUINO_GERER_INTERFACE();\n";
		}
		TranslatorBlock translatorBlock = getTranslatorBlockAtSocket(0);
		while (translatorBlock != null)
		{
			code = code + translatorBlock.toCode();
			translatorBlock = translatorBlock.nextTranslatorBlock();
		}
		if (translator.isScoopProgram())
		{
			code += "yield();\n";
		}
		String varDec = new String("");
		ArrayList<String> arr = translator.getBlock(blockId).getLocalVariableSet();
		for(int j=0;j<arr.size();j++){
			varDec += "float "+ arr.get(j)+" = 0\n";
		}
		return ret+varDec+code+"}\n\n";
		//ret = ret + "}\n\n";
		//return ret;
	}
}
