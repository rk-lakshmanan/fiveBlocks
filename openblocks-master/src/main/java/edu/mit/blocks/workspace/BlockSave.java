package edu.mit.blocks.workspace;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.codeblocks.BlockGenus;
import edu.mit.blocks.renderable.FactoryRenderableBlock;
import edu.mit.blocks.renderable.RenderableBlock;

// The following class handles reading and writing to myBlocks.xml
// which is a user-defined block library
public class BlockSave {
	private ArrayList<MyBlock> myBlockSet = new ArrayList<MyBlock>();
	private static File file;
	private boolean isFileExists = true;

	public boolean isFileExists() {
		return this.isFileExists;
	}

	public BlockSave() {
		// File file = new
		// File("C:\\Users\\Laksh\\Desktop\\myBlocksFolder\\myBlocks.xml");
		JFileChooser fr = new JFileChooser();
		FileSystemView fw = fr.getFileSystemView();
		File tempPath = fw.getDefaultDirectory();
		String path = tempPath.getAbsolutePath() + "\\myBlocks.xml";
		File file = new File(path);

		// File file = new File("myBlocks.xml");
		try {
			if (!file.exists()) {
				file.createNewFile();
				isFileExists = false;
			}
		} catch (IOException e) {
			System.out.println("File cannot be created!");
		}
		this.file = file;
		if (!isFileExists) {

			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

				// root elements
				Document doc = docBuilder.newDocument();
				Element rootElement = doc.createElement("MyBlockSet");
				doc.appendChild(rootElement);

				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory
						.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(
						file.getAbsolutePath()));

				// Output to console for testing
				// StreamResult result = new StreamResult(System.out);

				transformer.transform(source, result);

				// System.out.println("File saved!");

			} catch (ParserConfigurationException e) {

				e.printStackTrace();
			} catch (TransformerException tfe) {
				tfe.printStackTrace();
			}

		}
	}

	public String getCodeOfMyBlock(String genusName) {
		for (int i = 0; i < myBlockSet.size(); i++) {
			if (myBlockSet.get(i).getGenusName().equals(genusName)) {
				return myBlockSet.get(i).getCode();
			}
		}
		return null;
	}
public boolean isMyBlock(String genusName) {
	for (int i = 0; i < myBlockSet.size(); i++) {
		if (myBlockSet.get(i).getGenusName().equals(genusName)) {
			return true;
		}
	}
		return false;
	}

	public void readXML(Workspace workspace, FactoryManager manager) {

		final DocumentBuilderFactory factory = DocumentBuilderFactory
				.newInstance();
		factory.setNamespaceAware(true);
		final DocumentBuilder builder;
		final Document doc;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new File(file.getAbsolutePath()));

			// XXX here, we could be strict and only allow valid documents...
			// validate(doc);
			final Element root = doc.getDocumentElement();
			// load the canvas (or pages and page blocks if any) blocks from the
			// save file
			// also load drawers, or any custom drawers from file. if no custom
			// drawers
			// are present in root, then the default set of drawers is loaded
			// from
			// langDefRoot
			/*
			 * NodeList blockList = root.getElementsByTagName("Block");
			 * 
			 * System.out.println("empty");
			 * 
			 * for (int i =0 ; i<blockList.getLength();i++){ Node node =
			 * blockList.item(i);
			 * System.out.println("item is "+node.getTextContent()); }
			 */
			BlockGenus.loadMyBlockFrom(workspace, root, manager, myBlockSet);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Pattern attrExtractor = Pattern.compile("\"(.*)\""); Matcher nameMatcher;
	 * NodeList drawerSetNodes = root.getElementsByTagName("BlockDrawerSet");
	 * Node drawerSetNode; for (int i = 0; i < drawerSetNodes.getLength(); i++)
	 * { drawerSetNode = drawerSetNodes.item(i); if
	 * (drawerSetNode.getNodeName().equals("BlockDrawerSet")) { NodeList
	 * drawerNodes = drawerSetNode.getChildNodes(); Node drawerNode; // retreive
	 * drawer information of this bar for (int j = 0; j <
	 * drawerNodes.getLength(); j++) { drawerNode = drawerNodes.item(j); if
	 * (drawerNode.getNodeName().equals("BlockDrawer")) { String drawerName =
	 * null; Color buttonColor = Color.blue; StringTokenizer col; nameMatcher =
	 * attrExtractor.matcher(drawerNode .getAttributes().getNamedItem("name")
	 * .toString()); if (nameMatcher.find()) {// will be true drawerName =
	 * nameMatcher.group(1); }
	 * 
	 * // get drawer's color: Node colorNode = drawerNode.getAttributes()
	 * .getNamedItem("button-color"); if (colorNode != null) { nameMatcher =
	 * attrExtractor.matcher(colorNode .toString()); if (nameMatcher.find()) {
	 * // will be true col = new StringTokenizer(nameMatcher.group(1)); if
	 * (col.countTokens() == 3) { buttonColor = new Color(
	 * Integer.parseInt(col.nextToken()), Integer.parseInt(col.nextToken()),
	 * Integer.parseInt(col.nextToken())); } else { buttonColor = Color.BLACK; }
	 * } }
	 * 
	 * manager.addStaticDrawer(drawerName, buttonColor);
	 * 
	 * // get block genuses in drawer and create blocks NodeList drawerBlocks =
	 * drawerNode.getChildNodes(); Node blockNode; ArrayList<RenderableBlock>
	 * drawerRBs = new ArrayList<RenderableBlock>(); for (int k = 0; k <
	 * drawerBlocks.getLength(); k++) { blockNode = drawerBlocks.item(k); if
	 * (blockNode.getNodeName().equals( "BlockGenusMember")) { String genusName
	 * = blockNode.getTextContent(); assert workspace.getEnv().getGenusWithName(
	 * genusName) != null : "Unknown BlockGenus: " + genusName; Block newBlock;
	 * // don't link factory blocks to their stubs // because they will //
	 * forever remain inside the drawer and never be // active newBlock = new
	 * Block(workspace, genusName, false); drawerRBs.add(new
	 * FactoryRenderableBlock( workspace, manager, newBlock .getBlockID())); } }
	 * manager.addStaticBlocks(drawerRBs, drawerName); } } } } }
	 */
	public static void saveToFile(ArrayList<String> list) {

		BufferedWriter output = null;
		try {

			output = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));
			for (int i = 0; i < list.size(); i++) {
				output.write(list.get(i));
				output.newLine();
			}

			if (output != null)
				output.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());

		}

	}

	private static void readTheFile(ArrayList<String> list, File file) {
		BufferedReader input = null;

		try {
			input = new BufferedReader(new FileReader(file));
			input.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	public void writeToXML(MyBlock myBlock, Workspace workspace) {
		String name = JOptionPane.showInputDialog("Enter your MyBlock Name", "");
		if(isMyBlock(name)){
			//TODO add code for throwing error
		}else{
			myBlock.setGenusName(name);
			myBlockSet.add(myBlock);
		}

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file.getAbsolutePath());

			// Get the root element
			Node rootNode = doc.getFirstChild();

			Element myBlockElement = doc.createElement("MyBlock");

			// System.out.println("code is "+code);
			myBlockElement.setAttribute("code", myBlock.getCode());
			myBlockElement.setAttribute("name", myBlock.getGenusName());
			myBlockElement.setAttribute("functionCallCode",myBlock.getFunctionCallCode());
			myBlockElement.setAttribute("setupCode",myBlock.getSetupCode());
			myBlockElement.setAttribute("functionDecCode", myBlock.getFunctionDecCode());
			myBlockElement.setAttribute("OtherFunctionCode", myBlock.getOtherFunctionCode());
			rootNode.appendChild(myBlockElement);

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(
					file.getAbsolutePath()));
			transformer.transform(source, result);

			BlockGenus.loadMyBlockAfterSave(workspace,
					workspace.getFactoryManager(), myBlock);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
