package com.ardublock.core;

import java.util.Observable;
import java.util.Observer;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.BlockException;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.workspace.Workspace;

public class SaveBlockObserver implements Observer{
	Workspace workspace;
	public SaveBlockObserver(Workspace workspace){
		this.workspace = workspace;
	}

	public void update(Observable oberservable, Object block) {
		
		if(block instanceof Block){
			//System.out.println("block name is "+ ((Block) block).getGenusName());
			Translator translator = new Translator(workspace);
			try {
				workspace.getBlockSave().writeToXML(/*(Block)block,*/ translator.translateForSave(((Block) block).getBlockID()),workspace);
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

}
