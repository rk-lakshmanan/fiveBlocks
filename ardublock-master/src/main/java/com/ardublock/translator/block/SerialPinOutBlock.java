package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class SerialPinOutBlock extends TranslatorBlock
{
	public SerialPinOutBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
	//	translator.addSetupCommand("Serial.begin("+ getTranslatorBlockAtSocket(0).toCode()+");");
		
		//String ret = "Serial.read()";
		translator.addSetupCommand("pinMode("+ getTranslatorBlockAtSocket(1).toCode()+ ",OUTPUT);");
		//return codePrefix+ret+codeSuffix;
		//return "Serial.print("+getTranslatorBlockAtSocket(1).toCode()+");\n";
		return "analogWrite("+getTranslatorBlockAtSocket(1).toCode()+","+getTranslatorBlockAtSocket(0).toCode()+");\n";
	}
	
}
