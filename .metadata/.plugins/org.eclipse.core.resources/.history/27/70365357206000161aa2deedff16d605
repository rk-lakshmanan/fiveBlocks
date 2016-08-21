package edu.mit.blocks.renderable;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.codeblocks.BlockConnector;
import edu.mit.blocks.workspace.BlockSave;
import edu.mit.blocks.workspace.MyBlock;
import edu.mit.blocks.workspace.Workspace;
import edu.mit.blocks.workspace.WorkspaceEnvironment;
import edu.mit.blocks.workspace.WorkspaceEvent;
//TODO:Shift this code to RenderableBlock/Refactor
public class BlockExplodeManager {

	private Workspace workspace;
	private WorkspaceEnvironment env;
	private RenderableBlock rootRBlock;
	private BlockSave blockSaveManager;
	public BlockExplodeManager(Workspace wkspace,RenderableBlock rblock){
		this.workspace = wkspace;
		this.env = wkspace.getEnv();
		this.rootRBlock = rblock;
		this.blockSaveManager = wkspace.getBlockSave();
	}
	public void explode(){
		MyBlock oriMyBlock = blockSaveManager.getMyBlock(rootRBlock.getGenus());
		ArrayList<Block> internalBlockList = oriMyBlock.getInternalBlockList();
		ArrayList<RenderableBlock> newRBlockList = new ArrayList<RenderableBlock>();
		HashMap<Long, Long> OldAndNewIDPair = new HashMap<Long, Long>();
		for(Block iBlock : internalBlockList){
			Block newIBlock = new Block(workspace,iBlock.getGenusName(),iBlock.getBlockLabel());
			RenderableBlock newRBlock = new RenderableBlock(workspace, rootRBlock.getParentWidget(), newIBlock.getBlockID());
			newRBlockList.add(newRBlock);
			OldAndNewIDPair.put(iBlock.getBlockID(), newIBlock.getBlockID());
			
		}
		//Code to get sort out the connectors
		/*for(RenderableBlock newRBlock:newRBlockList){
			newRBlock.getBlock().removeAllSockets();
			for(BlockConnector iConnectors:iBlock.getSockets()){
				newRBlock.getBlock().addSocket(kind, positionType, label, isLabelEditable, isExpandable, blockID);
			}
		}*/
		//code to add block into workspace view
		/*newRb.moveConnectedBlocks();
		  rootRBlock.getParentWidget().addBlock(newRBlock);
		workspace.notifyListeners(new WorkspaceEvent(workspace, newRBlock
				.getParentWidget(), newIBlock.getBlockID(),
				WorkspaceEvent.BLOCK_ADDED));*/
	}
}
