package com.ardublock.translator.block;

import java.util.ArrayList;
import java.util.Map;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.workspace.Workspace;

public class ExecutionBlock extends TranslatorBlock {
	Translator translator;
	Long blockId;

	public ExecutionBlock(Long blockId, Translator translator,
			String codePrefix, String codeSuffix, String label) {
		super(blockId, translator, codePrefix, codeSuffix, label);
		this.translator = translator;
		this.blockId = blockId;
	}

	@Override
	public String toCode() throws SocketNullException,
			SubroutineNotDeclaredException {
		/**
		 * DO NOT add tab in code any more, we'll use arduino to format code, or
		 * the code will duplicated.
		 */
		Block block = translator.getWorkspace().getEnv().getBlock(blockId);
		Block beforeBlock = block;
		ArrayList<String> localVariableSet = getLocalVariable(beforeBlock);
	

		

		String variable = this.getRequiredTranslatorBlockAtSocket(0).toCode();

		String value = this.getRequiredTranslatorBlockAtSocket(1).toCode();
		if (localVariableSet.contains(variable)) {
			return variable + " = " + value + "	;\n";
		} else {
			localVariableSet.add(variable);

			return/* "float " +*/variable + " = " + value + ";\n";

		}
	}

	private ArrayList<String> getLocalVariable(Block beforeBlock) {
		while (true) {
			if (!beforeBlock.getBeforeBlockID().equals(Block.NULL)) {
				beforeBlock = translator.getWorkspace().getEnv()
						.getBlock(beforeBlock.getBeforeBlockID());
			} else if (!beforeBlock.getPlugBlockID().equals(Block.NULL)) {
				beforeBlock = translator.getWorkspace().getEnv()
						.getBlock(beforeBlock.getPlugBlockID());
			}else{
				break;
			}
			if(beforeBlock!=null&&			
					beforeBlock.getBeforeBlockID().equals(Block.NULL)&&
					beforeBlock.getPlugBlockID().equals(Block.NULL)){
				return beforeBlock.getLocalVariableSet();
			}
			
			
			
		}
		return null;
	}
}