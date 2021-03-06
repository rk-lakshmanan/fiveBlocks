package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class SerialPortInBlock extends TranslatorBlock
{
	public SerialPortInBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		translator.addSetupCommand("Serial.begin("+ getTranslatorBlockAtSocket(0).toCode()+");");
		
		//String ret = "Serial.read()";
		
		//return codePrefix+ret+codeSuffix;
		return  getTranslatorBlockAtSocket(1).toCode() + " = Serial.read();\n";
	}
}
