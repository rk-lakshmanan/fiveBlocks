package edu.mit.blocks.workspace;

import java.util.ArrayList;
import java.util.List;

import edu.mit.blocks.codeblocks.Block;

public class BaseFunction {
	// NOTE genusName indicates the name of the function and not "myBlock"
	private String genusName = null;
	// list of input parameters
	private ArrayList<String> inputParametersList = null;
	// list of functions that this myBlock is based upon

	private String returnParameter = null;
	private List<String> setupCode = null;
	private String code = null;

	public BaseFunction() {
	}
	public void generateMyFunctionBlock(Workspace workspace,Block block){
		WorkspaceEnvironment env = workspace.getEnv();
		generateGenusName(env,block);
		generateParameterList(env,block);
		generateReturnParameter(env,block);
	}
	private void generateReturnParameter(WorkspaceEnvironment env, Block block) {
		if(!block.getSocketAt(block.getNumSockets()-2).getBlockID().equals(Block.NULL)){
			this.returnParameter = env.getBlock(block.getSocketAt(block.getNumSockets()-2).getBlockID()).getBlockLabel();
			System.out.println("return is 	"+returnParameter	);
		}
		
	}
	private void generateParameterList(WorkspaceEnvironment env, Block block) {
		this.inputParametersList = new ArrayList<String>();
		int i=1;
		while(!block.getSocketAt(i).getBlockID().equals(Block.NULL)){
			this.inputParametersList.add(env.getBlock(block.getSocketAt(i).getBlockID()).getBlockLabel());
			i++;
		}
		System.out.println("list is"+ this.inputParametersList);
	}
	private void generateGenusName(WorkspaceEnvironment env, Block block) {
		this.genusName = env.getBlock(block.getSocketAt(0).getBlockID()).getBlockLabel();
		System.out.println("name is"+ this.genusName);
	}

	public void addParam(String param){
		this.inputParametersList.add(param);
	}

	public String getGenusName() {
		return genusName;
	}

	public void setGenusName(String genusName) {
		this.genusName = genusName;
	}

	public List<String> getSetupCode() {
		return setupCode;
	}

	public void setSetupCode(List<String> setupCode) {
		this.setupCode = setupCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ArrayList<String> getInputParameterList() {
		return inputParametersList;
	}

	public void setInputParameterList(ArrayList<String> parameters) {
		this.inputParametersList = parameters;
	}


	public String getReturnParameter() {
		return returnParameter;
	}

	public void setReturnParameter(String returnParameter) {
		this.returnParameter = returnParameter;
	}
	//formatting for function save
	public String getFormattedInputParameters(){
		String param = new String("");
		for(int i= 0;i<this.inputParametersList.size();i++){
			param+=inputParametersList.get(i).trim()+" ";
		}
		return param;
	}
	//formatting for function save
	public String getFormattedSetupCode() {
		String str = new String("");
		for(int i= 0;i<this.setupCode.size();i++){
			str+=setupCode.get(i).trim()+" ";
		}
		return str;
	}
}
