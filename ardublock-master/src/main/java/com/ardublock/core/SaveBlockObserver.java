package com.ardublock.core;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.BlockException;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.renderable.RenderableBlock;
import edu.mit.blocks.workspace.BaseFunction;
import edu.mit.blocks.workspace.MyBlock;
import edu.mit.blocks.workspace.Workspace;

public class SaveBlockObserver implements Observer {
	Workspace workspace;

	public SaveBlockObserver(Workspace workspace) {
		this.workspace = workspace;
	}

	public void update(Observable oberservable, Object block) {

		if (block instanceof Block) {
			// System.out.println("block name is "+ ((Block)
			// block).getGenusName());
			Translator translator = new Translator(workspace);
			ArrayList<Long> baseFunctionIDSet = new ArrayList<Long>();
			ArrayList<String> myBlockBaseSet = new ArrayList<String>();
			ArrayList<Block> internalBlockList = new ArrayList<Block>();
			traverseAccel((Block)block,baseFunctionIDSet,myBlockBaseSet,internalBlockList);
			
			
			MyBlock myBlock = new MyBlock();
			myBlock.generateMyFunctionBlock(workspace, (Block)block);
			//System.out.println("name of MYBLOCK IS" +myBlock.getGenusName());
			myBlock.setBaseFunctionList(createBaseFunctionList(workspace,baseFunctionIDSet,myBlockBaseSet/*,((Block)block).getBlockID()*/));
			myBlock.setInternalRenderableBlockList(generateRenderableBlocks(internalBlockList));
			myBlock.setInternalBlockList(internalBlockList);
			try {
				String code = translator.translateForSave(((Block) block).getBlockID());
				code = setupCodeForReturn(myBlock, code);
				myBlock.setCode(code);
				//System.out.println("code of myBlock is "+code);
				myBlock.setSetupCode(translator.getSetupCommand());
				workspace.getBlockSave().save(myBlock, workspace);
			} catch (SocketNullException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SubroutineNotDeclaredException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BlockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			
			
			
			
			// try {
			// workspace.getBlockSave().writeToXML(/*(Block)block,*/new
			// MyBlock(),/* translator.translateForSave(((Block)
			// block).getBlockID()),*/workspace);
			// } catch (SocketNullException e) {
			// TODO Auto-generated catch block
			/*
			 * e.printStackTrace(); } catch (SubroutineNotDeclaredException e) {
			 * // TODO Auto-generated catch block e.printStackTrace(); } catch
			 * (BlockException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
		}
	}

	private ArrayList<RenderableBlock> generateRenderableBlocks(
			ArrayList<Block> internalBlockList) {
		ArrayList<RenderableBlock> internalRenderableBlocks = new ArrayList<RenderableBlock>();
		for(Block block: internalBlockList){
			for(RenderableBlock rBlock: workspace.getRenderableBlocksFromGenus(block.getGenusName())){
				if(block.getBlockID().compareTo(rBlock.getBlockID())== 0){
					internalRenderableBlocks.add(rBlock);
				}
			}
		}
		
		return internalRenderableBlocks;
	}

	private ArrayList<BaseFunction> createBaseFunctionList(Workspace workspace,ArrayList<Long> baseFunctionIDSet,ArrayList<String> myBlockBaseSet) {
		ArrayList<BaseFunction> baseFunctionsList = new ArrayList<BaseFunction>();
		processBaseFunctionIDSet(workspace, baseFunctionIDSet,
				baseFunctionsList);
		//demotes all myBlocks that are found in the traverse method earlier to baseFunction
		for(int i=0;i<myBlockBaseSet.size();i++){
			MyBlock myBlock = workspace.getBlockSave().getMyBlock(myBlockBaseSet.get(i));
			//transfer all baseFunctions in myBlock
			for(int j = 0;j<myBlock.getBaseFunctionList().size();j++){
				baseFunctionsList.add(myBlock.getBaseFunctionList().get(j));
			}
			//transfer myBlocks as a baseFunction itself
			BaseFunction bf = new BaseFunction();
			bf.setGenusName(myBlock.getGenusName());
			bf.setCode(setupCodeForReturn(myBlock, myBlock.getCode()));
			bf.setInputParameterList(myBlock.getInputParameterList());
			bf.setReturnParameter(myBlock.getReturnParameter());
			bf.setSetupCode(myBlock.getSetupCode());
			baseFunctionsList.add(bf);
		}
		
		
		return baseFunctionsList;
	}
	//convert all the block ids to baseFunctions and add them to baseFunction list
	private void processBaseFunctionIDSet(Workspace workspace,
			ArrayList<Long> baseFunctionIDSet,
			ArrayList<BaseFunction> baseFunctionsList) {
		for(int i = 0;i<baseFunctionIDSet.size();i++){
			Translator translator = new Translator(workspace);
			BaseFunction baseFunction = new BaseFunction();
			baseFunction.generateMyFunctionBlock(workspace, workspace.getEnv().getBlock(baseFunctionIDSet.get(i)));
			try {
				String code = translator.translateForSave(baseFunctionIDSet.get(i));
				code = setupCodeForReturn(baseFunction, code);
				baseFunction.setCode(code);
				baseFunction.setSetupCode(translator.getSetupCommand());
				baseFunctionsList.add(baseFunction);
			} catch (SocketNullException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SubroutineNotDeclaredException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BlockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String setupCodeForReturn(BaseFunction baseFunction, String code) {
		if(baseFunction.getReturnParameter()!=null&&!baseFunction.getReturnParameter().trim().equals("")){
			code += "return "+baseFunction.getReturnParameter()+";\n";
		}
		return code;
	}

	//accelerator for traverse recursion method (start from the code blocks)
	//Overloaded with internalBlockList argument
	private void traverseAccel(Block block,ArrayList<Long> baseFunctionIDSet,ArrayList<String> myBlockBaseSet,
								ArrayList<Block> internalBlockList){
		if(!block.getSocketAt( block.getNumSockets()-1).getBlockID().equals(Block.NULL)){
			Block nextBlock = workspace.getEnv().getBlock( block.getSocketAt( block.getNumSockets()-1).getBlockID());
			traverse((Block) nextBlock,baseFunctionIDSet,myBlockBaseSet,internalBlockList);
		}
		for (int i = 0; i < block.getNumSockets()-1; i++) {
			// if there is a block at the connector, then recurse
			if (!block.getSocketAt(i).getBlockID().equals(Block.NULL)) {
				traverse(workspace.getEnv().getBlock(
						block.getSocketAt(i).getBlockID()),new ArrayList<Long>(),new ArrayList<String>(),internalBlockList);
			}
		}
		internalBlockList.add(block);
	}
	//accelerator for traverse recursion method (start from the code blocks)
		private void traverseAccel(Block block,ArrayList<Long> baseFunctionIDSet,ArrayList<String> myBlockBaseSet){
			if(!block.getSocketAt( block.getNumSockets()-1).getBlockID().equals(Block.NULL)){
				Block nextBlock = workspace.getEnv().getBlock( block.getSocketAt( block.getNumSockets()-1).getBlockID());
				traverse((Block) nextBlock,baseFunctionIDSet,myBlockBaseSet);
			}
		
		}
	//recursion to traverse through the blocks 
	//Overloaded method with internalBlockList argument
	private void traverse(Block block,ArrayList<Long> baseFunctionIDSet,ArrayList<String> myBlockBaseSet, 
					ArrayList<Block> internalBlockList) {
		// System.out.println("block name is "+block.getGenusName()+"  and blockID is "+block.getBlockID());
		Long blockID = block.getBlockID();
		for (int i = 0; i < block.getNumSockets(); i++) {
			// if there is a block at the connector, then recurse
			if (!block.getSocketAt(i).getBlockID().equals(Block.NULL)) {
				traverse(workspace.getEnv().getBlock(
						block.getSocketAt(i).getBlockID()),baseFunctionIDSet,myBlockBaseSet,internalBlockList);
			}
		}
		if (block.getAfterBlockID() != Block.NULL) {
			traverse(workspace.getEnv().getBlock(block.getAfterBlockID()),baseFunctionIDSet,myBlockBaseSet,internalBlockList);
		}
		if (block.getGenusName().equals("function")
				&& lastConnectorKind(block).equals("cmd")) {
			// L_EXCEPTION THROW ERROR BECAUSE THIS IS NOT SUPPOSED TO BE ATTACHED TO THE
			// BLOCK
		} else if (block.getGenusName().equals("function")
				&& !lastConnectorKind(block).equals("cmd")) {
			findBaseFunction(block,baseFunctionIDSet,myBlockBaseSet);
			internalBlockList.add(block);
		}else{
			internalBlockList.add(block);
		}
		if(workspace.getBlockSave().isMyBlock(block.getGenusName())&&!myBlockBaseSet.contains(block.getGenusName())){
			myBlockBaseSet.add(block.getGenusName());
		}
	}

	//recursion to traverse through the blocks 
	private void traverse(Block block,ArrayList<Long> baseFunctionIDSet,ArrayList<String> myBlockBaseSet) {
		// System.out.println("block name is "+block.getGenusName()+"  and blockID is "+block.getBlockID());
		Long blockID = block.getBlockID();
		for (int i = 0; i < block.getNumSockets(); i++) {
			// if there is a block at the connector, then recurse
			if (!block.getSocketAt(i).getBlockID().equals(Block.NULL)) {
				traverse(workspace.getEnv().getBlock(
						block.getSocketAt(i).getBlockID()),baseFunctionIDSet,myBlockBaseSet);
			}
		}
		if (block.getAfterBlockID() != Block.NULL) {
			traverse(workspace.getEnv().getBlock(block.getAfterBlockID()),baseFunctionIDSet,myBlockBaseSet);
		}
		if (block.getGenusName().equals("function")
				&& lastConnectorKind(block).equals("cmd")) {
			// L_EXCEPTION THROW ERROR BECAUSE THIS IS NOT SUPPOSED TO BE ATTACHED TO THE
			// BLOCK
		} else if (block.getGenusName().equals("function")
				&& !lastConnectorKind(block).equals("cmd")) {
			findBaseFunction(block,baseFunctionIDSet,myBlockBaseSet);
		}
		if(workspace.getBlockSave().isMyBlock(block.getGenusName())&&!myBlockBaseSet.contains(block.getGenusName())){
			myBlockBaseSet.add(block.getGenusName());
		}
	}

	private void findBaseFunction(Block block, ArrayList<Long> baseFunctionIDSet,ArrayList<String> myBlockBaseSet) {
		String functionName = getBlockLabelFrom(block);
		 System.out.println("blockLabel/functionName LOOKING IS __________________"+functionName);
		Iterable<RenderableBlock> renderableBlocks = workspace
				.getRenderableBlocks();
		for (RenderableBlock renderableBlock : renderableBlocks) {

			Block possibleBlock = renderableBlock.getBlock();

			// finds all block that is not connected to any prior blocks
			if (!possibleBlock.hasPlug()
					&& (Block.NULL.equals(possibleBlock.getBeforeBlockID()))) {
				//System.out.println("possibleBlock are "
					//	+ possibleBlock.getGenusName() + " and their id is "
					//	+ possibleBlock.getBlockID());
				// System.out.println(workspace.getEnv().getBlock(possibleBlock.getSocketAt(0).getBlockID()).getBlockLabel());
				if (possibleBlock.getGenusName().equals("function")) {
					if (getBlockLabelFrom(possibleBlock).equals(functionName)
							&& lastConnectorKind(possibleBlock).equals("cmd")) {
						 System.out.println("FOUND ------------------>function name is "+functionName+" and bingo!");
						//L_EXCEPTION needs to be modified to ensure that there is no circular reference and to handle recursion
						 //L_EXCEPTION need add code to prevent two function with same name to be on the page
						if(!baseFunctionIDSet.contains(possibleBlock.getBlockID())){
							baseFunctionIDSet.add(possibleBlock.getBlockID());
							 traverseAccel(possibleBlock,baseFunctionIDSet,myBlockBaseSet);
							 
						}
						
						 
					}
				}
			}
		}
	}

	private String lastConnectorKind(Block possibleBlock) {
		return possibleBlock.getSocketAt(possibleBlock.getNumSockets() - 1)
				.getKind();
	}

	private String getBlockLabelFrom(Block possibleBlock) {
		return workspace.getEnv()
				.getBlock(possibleBlock.getSocketAt(0).getBlockID())
				.getBlockLabel();
	}

}
