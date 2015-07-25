package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

import edu.mit.blocks.workspace.BaseFunction;
import edu.mit.blocks.workspace.MyBlock;

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
		MyBlock myBlock = translator.getWorkspace().getBlockSave().getMyBlock(translator.getBlock(blockId).getGenusName());
	for(int i=0;i<myBlock.getBaseFunctionList().size();i++){
		BaseFunction bf = myBlock.getBaseFunctionList().get(i);
		//generate(bf);
	}
		
		
	//	return generateMyBlockFunctionCall(myBlock);
		//return translator.getWorkspace().getBlockSave().getCodeOfMyBlock(translator.getBlock(blockId).getGenusName());
		return "";
	}
/*
	private void generate(BaseFunction bf) {
		generateDeclaration(bf);
		generateSetupCode(bf);
		generateFunctionCode(bf);
	}

	private void generateDeclaration(BaseFunction bf) {
		String returnType = new String("");
		String functionName = new String("");
		String inputParamList = new String("(");
		if(!(bf.getReturnParameter()==null||bf.getReturnParameter().equals(""))){
			returnType="float ";
		}
		functionName = bf.getGenusName().trim()+"_"+blockId;
		for(int i=0;i<bf.getInputParameterList().size();i++){
			inputParamList += " float "+bf.getInputParameterList().get(i);
			if(i!=bf.getInputParameterList().size()-1){
				inputParamList+=" , ";
			}
		}
		inputParamList+=");";
		translator.addFunctionName(blockId, functionName, paramList, returnParam);
	}*/
}
