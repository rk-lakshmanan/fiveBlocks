package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class ForBlock extends TranslatorBlock {
	public ForBlock(Long blockId, Translator translator, String codePrefix,
			String codeSuffix, String label) {
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException,
			SubroutineNotDeclaredException {

		String tempInit = getTranslatorBlockAtSocket(0).toCode();
		System.out.println("tempInit is"+tempInit);
		String init = new String("");
		for (int i = 0; i < tempInit.length(); i++) {
			if (i + 1 != tempInit.length()) {
				if (tempInit.charAt(i) == '=' && tempInit.charAt(i + 1) == '=') {
					i++;
				}
			}
			init += tempInit.charAt(i);
		}
		
		System.out.println(init + "init is ");
		String cond = getTranslatorBlockAtSocket(1).toCode();
		String step = getTranslatorBlockAtSocket(2).toCode();
		String var = new String("");
		for (int i = 0; i < init.length(); i++) {
			if (init.charAt(i) == '=') {
				break;
			}
			var += init.charAt(i);
		}
		step = var + "+= " + step;
		init = "float " + init;
		init = removeBracket(init);
		step = removeBracket(step);
		String execution = "";
		if (!isTranslatorBlockAtSocketNull(3)) {
			execution = getTranslatorBlockAtSocket(3).toCode();
		}
		
		String ret = "for(" + init + " ; " + cond + " ; " + step + " ){\n"
				+ execution + "\n}\n";
		return ret;

	}
	private String removeBracket(String str){
		str = str.replace("(", "");
		str = str.replace(")","");
		return str;
	}
}
