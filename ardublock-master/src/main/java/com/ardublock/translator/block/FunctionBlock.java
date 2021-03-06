package com.ardublock.translator.block;

import java.util.ArrayList;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class FunctionBlock extends TranslatorBlock
{
	Translator translator;
	Long blockId;
	public FunctionBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
		this.translator = translator;
		this.blockId = blockId;
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		
		String functionName = getTranslatorBlockAtSocket(0).toCode();
		
		int i = 1;
		ArrayList<String> paramList = new ArrayList<String>();
		while(!isTranslatorBlockAtSocketNull(i)){
			paramList.add(getTranslatorBlockAtSocket(i).toCode());
			i++;
		}
		String inputParams = new String("(");
		for(int j = 0; j<paramList.size();j++){
			inputParams += paramList.get(j);
			if(j!=paramList.size()-1){
				inputParams+=", ";
			}
		}
		inputParams+=" )";
		i++;
		
		return functionName/*+" "*/+inputParams+";\n";
	}
	public String getCode() throws SocketNullException,SubroutineNotDeclaredException{
		this.translator.getBlock(blockId).resetLocalVariableSet();
		
		String functionName = getTranslatorBlockAtSocket(0).toCode();
		
		int i = 1;
		ArrayList<String> paramList = new ArrayList<String>();
		while(!isTranslatorBlockAtSocketNull(i)){
			paramList.add(getTranslatorBlockAtSocket(i).toCode());
			i++;
		}
		ArrayList<String> varList = translator.getBlock(blockId).getLocalVariableSet();
		
		for(int j = 0;j<paramList.size();j++){
			varList.add(paramList.get(j));
		}
		
		
		
		
		
		
		
		
		String inputParams = new String("(");
		for(int j = 0; j<paramList.size();j++){
			inputParams += "float ";
			inputParams += paramList.get(j);
			if(j!=paramList.size()-1){
				inputParams+=", ";
			}
		}
		inputParams+=" ){\n";
		i++;
		String returnString= new String("");
		String returnType = new String("");
		if(isTranslatorBlockAtSocketNull(i)){
			returnType = "void ";
		}else{
			returnType = "float ";
			returnString =  "return "+ getTranslatorBlockAtSocket(i).toCode()+ ";\n";
		}
		i++;
		String functionCode= new String("");
		if(!isTranslatorBlockAtSocketNull(i)){
			//functionCode = getTranslatorBlockAtSocket(i).toCode();
			TranslatorBlock translatorBlock = this
					.getRequiredTranslatorBlockAtSocket(i);
			while (translatorBlock != null) {
				functionCode = functionCode + translatorBlock.toCode();
				translatorBlock = translatorBlock.nextTranslatorBlock();
			}
		}
		String varDec = new String("");
		ArrayList<String> arr = translator.getBlock(blockId).getLocalVariableSet();
		
		for(int j=0;j<arr.size();j++){
			if(!paramList.contains(arr.get(j))){
			varDec += "float "+ arr.get(j)+" = 0;\n";
			}
		}
		if(translator.isProcessSave()){
			return varDec+functionCode;
		}
		
		return returnType+" "+functionName+" "+inputParams+varDec+functionCode+returnString+"}\n";
		
	}
}