package edu.mit.blocks.renderable;

import java.util.Observable;

import edu.mit.blocks.codeblocks.Block;

public class SaveBlockObservable extends Observable {
	private Block block;
	
	  public SaveBlockObservable(){
		
	  }
	   public void saveBlock(Block block)
	   {
	      this.block = block;
	      setChanged();
	      notifyObservers(this.block);
	   }
	   public Block getBlockToBeSaved()
	   {
	      return block;
	   }

}
