package com.ardublock.translator.block;

import java.util.ArrayList;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.BlockException;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

import edu.mit.blocks.workspace.BaseFunction;
import edu.mit.blocks.workspace.MyBlock;

public class MyBlockBlock extends TranslatorBlock {
	private Translator translator;
	private Long blockId;

	public MyBlockBlock(Long blockId, Translator translator, String codePrefix,
			String codeSuffix, String label) {
		super(blockId, translator);
		this.translator = translator;
		this.blockId = blockId;
	}

	@Override
	public String toCode() throws SocketNullException,
			SubroutineNotDeclaredException {
		MyBlock myBlock = translator.getWorkspace().getBlockSave()
				.getMyBlock(translator.getBlock(blockId).getGenusName());
		ArrayList<BaseFunction> bfList = myBlock.getBaseFunctionList();
		for (int i = 0; i < bfList.size(); i++) {
			BaseFunction bf = bfList.get(i);
			generate(bf,bfList);
		}
		generate(myBlock,bfList);
		String myBlockCallCode = generateMyBlockCallCode(myBlock);
		/*myBlockCallCode = myBlockCallCode.replaceAll("void", "");
		myBlockCallCode = myBlockCallCode.replaceAll("float", "");
		myBlockCallCode = myBlockCallCode.replaceAll("\\{", ";");*/
		
		// return generateMyBlockFunctionCall(myBlock);
		// return
		// translator.getWorkspace().getBlockSave().getCodeOfMyBlock(translator.getBlock(blockId).getGenusName());
		return myBlockCallCode;
	}



	private String generateMyBlockCallCode(MyBlock myBlock) throws SocketNullException, SubroutineNotDeclaredException, BlockException {
		String functionName = myBlock.getGenusName().trim() + "_" + blockId;
		String code = new String("(");
		for(int i = 0;i<translator.getBlock(blockId).getNumSockets();i++){
			code += this.getRequiredTranslatorBlockAtSocket(i).toCode();
			if(i<translator.getBlock(blockId).getNumSockets()-1){
				code+=" , ";
			}
		}
		code+=");";
		return functionName+code;
	}

	private void generate( BaseFunction bf,ArrayList<BaseFunction> bfList) {
		String topCode = generateTopCode(bf);
		String code = generateCode(bf, bfList);
		String returnCode = "return "+ bf.getReturnParameter()+";\n";
		translator.addDefinitionCommand(topCode + code + "}\n");
		translator.addSetupCommand(bf.getFormattedSetupCode());
	}

	private String generateCode(BaseFunction bf, ArrayList<BaseFunction> bfList) {
		String code = new String("");
		code = bf.getCode();
		for (int i = 0; i < bfList.size(); i++) {
			code = code.replaceAll(bfList.get(i).getGenusName() + "\\(", bfList.get(i).getGenusName() + "_"
					+ blockId + "(");
		}
		return code;
	}

	private String generateTopCode(BaseFunction bf) {
		String returnType = new String("");
		String functionName = new String("");
		String inputParamList = new String("(");

		if (!(bf.getReturnParameter() == null || bf.getReturnParameter()
				.equals(""))) {
			returnType = "float ";
		}else{
			returnType = "void ";
		}
		functionName = bf.getGenusName().trim() + "_" + blockId;
		for (int i = 0; i < bf.getInputParameterList().size(); i++) {
			inputParamList += " float " + bf.getInputParameterList().get(i);
			if (i != bf.getInputParameterList().size() - 1) {
				inputParamList += " , ";
			}
		}
		inputParamList += ") {\n";
		return returnType + functionName + inputParamList;
	}

}
