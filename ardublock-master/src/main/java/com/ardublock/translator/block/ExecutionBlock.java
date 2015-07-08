package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class ExecutionBlock extends TranslatorBlock {
	public ExecutionBlock(Long blockId, Translator translator,
			String codePrefix, String codeSuffix, String label) {
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException,
			SubroutineNotDeclaredException {
		/**
		 * DO NOT add tab in code any more, we'll use arduino to format code, or
		 * the code will duplicated.
		 */

		TranslatorBlock translatorBlock = this
				.getRequiredTranslatorBlockAtSocket(0);

		String variable = translatorBlock.toCode();

		String value = this.getRequiredTranslatorBlockAtSocket(1).toCode();
		if (translator.getNumberVariable(variable) != null) {

			return variable + " = " + value + ";\n";
		} else {
			translator.addNumberVariable(variable, value);
			return "float " + variable + " = " + value + ";\n";

		}
	}
}