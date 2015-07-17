package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class SerialPortOutBlock extends TranslatorBlock
{
	public SerialPortOutBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		translator.addSetupCommand("Serial.begin("+ getTranslatorBlockAtSocket(0).toCode()+");");
		
		//String ret = "Serial.read()";
		
		//return codePrefix+ret+codeSuffix;
		//return "Serial.write((byte*)&"+getTranslatorBlockAtSocket(1).toCode()+",4);\n";
		return "Serial.println("+getTranslatorBlockAtSocket(1).toCode()+");\n";
	}
}
