package edu.mit.blocks.workspace;

import java.util.ArrayList;
import java.util.List;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.renderable.RenderableBlock;

public class MyBlock extends BaseFunction{
	
	//private String genusName = null;
	//list of input parameters
	//private ArrayList<String> parameterList = null;
	//list of functions that this myBlock is based upon
	private ArrayList<BaseFunction> BaseFunctionList = null;
	//private String returnParameter = null;
	//private List<String> setupCode = null;
	//private String code = null;
	private String bubbleText = new String("");
	
	//IMPORTANT NOTE: All information like position and id are relative 
	//ids that are to be used in relation with others in the same internalBlockList
	//These information cannot be used in workspace as they may conflict with 
	//existing ids/ cause other issues
	private ArrayList<Block> internalBlockList = new ArrayList<Block>();
	private ArrayList<RenderableBlock> internalRenderableBlockList = new ArrayList<RenderableBlock>();
	public MyBlock() {
		
	}

	//public void setGlobalVarSet(String globalCode) {
		/*
		 * int index = 0; String tempGC =new String(""); tempGC += globalCode;
		 * tempGC.trim(); while(true){ if(tempGC.contains("float")){ index =
		 * tempGC.indexOf("float"); index += 5; tempGC = tempGC.substring(index,
		 * tempGC.length()); int eqIndex = tempGC.indexOf('='); String globalVar
		 * =tempGC.substring(0, eqIndex); tempGC =
		 * tempGC.substring(eqIndex+1,tempGC.length()); int semiColonIndex =
		 * tempGC.indexOf(';') ; String globalVal =
		 * tempGC.substring(0,semiColonIndex );
		 * if(globalVarSet.containsKey(globalVar)){ //TODO add code for throwing
		 * error } else{ globalVarSet.put(globalVar, globalVal); } } else{
		 * break; }
		 */
//	}
	


	public ArrayList<BaseFunction> getBaseFunctionList() {
		return BaseFunctionList;
	}

	public void setBaseFunctionList(ArrayList<BaseFunction> baseFunctionList) {
		BaseFunctionList = baseFunctionList;
	}

	public String getBubbleText() {
		return bubbleText;
	}

	public void setBubbleText(String textBubble) {
		this.bubbleText = textBubble;
	}

	public ArrayList<Block> getInternalBlockList() {
		return internalBlockList;
	}

	public void setInternalBlockList(ArrayList<Block> internalBlockList) {
		this.internalBlockList = internalBlockList;
	}

	public ArrayList<RenderableBlock> getInternalRenderableBlockList() {
		return internalRenderableBlockList;
	}

	public void setInternalRenderableBlockList(
			ArrayList<RenderableBlock> internalRenderableBlockList) {
		this.internalRenderableBlockList = internalRenderableBlockList;
	}

}
