package edu.mit.blocks.renderable;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

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
	public void explode() throws ParserConfigurationException{
		MyBlock oriMyBlock = blockSaveManager.getMyBlock(rootRBlock.getGenus());
		ArrayList<Block> internalBlockList = oriMyBlock.getInternalBlockList();
		ArrayList<RenderableBlock> internalRenderableBlocks = oriMyBlock.getInternalRenderableBlockList();
		ArrayList<RenderableBlock> newRBlockList = new ArrayList<RenderableBlock>();
		HashMap<Long, Long> OldAndNewIDPair = new HashMap<Long, Long>();
		boolean isFirst = false;
		for(RenderableBlock iRenBlock: internalRenderableBlocks){
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
			
			Node iRenBlockNode = iRenBlock.getSaveNode(document);
			RenderableBlock newRBlock = RenderableBlock.loadBlockNode(workspace, iRenBlockNode, rootRBlock.getParentWidget(), OldAndNewIDPair);
			if(isFirst){
				newRBlock.setLocation(rootRBlock.getX(), rootRBlock.getY());
				isFirst = false;
			}
			newRBlockList.add(newRBlock);
		}
		
		
		
		//old version of loading nodes
	/*	for(Block iBlock : internalBlockList){
			Block newIBlock = new Block(workspace,iBlock.getGenusName(),iBlock.getBlockLabel());
			RenderableBlock newRBlock = new RenderableBlock(workspace, rootRBlock.getParentWidget(), newIBlock.getBlockID());
			newRBlockList.add(newRBlock);
			OldAndNewIDPair.put(iBlock.getBlockID(), newIBlock.getBlockID());
			
		}*/
		
		
		//Code to get sort out the connectors
		/*for(RenderableBlock newRBlock:newRBlockList){
			for(BlockConnector iConnectors:iBlock.getSockets()){
				newRBlock.getBlock().
			}
		}
		
		//code to add block into workspace view
		/*newRb.moveConnectedBlocks();
		  rootRBlock.getParentWidget().addBlock(newRBlock);
		workspace.notifyListeners(new WorkspaceEvent(workspace, newRBlock
				.getParentWidget(), newIBlock.getBlockID(),
				WorkspaceEvent.BLOCK_ADDED));*/
		
		for(RenderableBlock newRBlock: newRBlockList){
			//newRBlock.moveConnectedBlocks();
			rootRBlock.getParentWidget().addBlock(newRBlock);
		}
		
		//TODO:Modify this and use BLOCK_ADDED. Using Block_Cloned because
		//some blocks are missing when using block_Added. 
		newRBlockList.get(0).cloneMe();
		for(RenderableBlock newRBlock: newRBlockList){
			workspace.notifyListeners(new WorkspaceEvent(workspace, newRBlock
					.getParentWidget(), newRBlock.getBlockID(),
					WorkspaceEvent.BLOCK_CLONED));
		}
	}
}
