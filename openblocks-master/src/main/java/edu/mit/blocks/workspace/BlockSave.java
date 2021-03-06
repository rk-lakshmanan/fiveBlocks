package edu.mit.blocks.workspace;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
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

	public void save(MyBlock myBlock, Workspace workspace) {
		// String name = JOptionPane.showInputDialog("Enter your MyBlock Name",
		// "");
		if (!isMyBlock(myBlock.getGenusName())) {
			writeToXML(myBlock);
			myBlockSet.add(myBlock);
			// save the block
			BlockGenus.loadMyBlockAfterSave(workspace,
					workspace.getFactoryManager(), myBlock);
		} else {
			// L_EXCEPTION throw error for trying to save block with same
			// name

			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame,
					"You can't save the block with the same name", "Error",
					JOptionPane.ERROR_MESSAGE);

		}
	}

	private void writeToXML(MyBlock myBlock)
			throws TransformerFactoryConfigurationError {
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(file.getAbsolutePath());

			// Get the root element
			Node rootNode = doc.getFirstChild();

			Element myBlockElement = doc.createElement("MyBlock");
			formatElement(myBlockElement, myBlock, rootNode);

			Node myBlockNode = rootNode.getLastChild();
			ArrayList<BaseFunction> baseFunctionList = myBlock
					.getBaseFunctionList();
			for (int i = 0; i < baseFunctionList.size(); i++) {
				BaseFunction bf = baseFunctionList.get(i);
				Element baseFunctionElement = doc.createElement("BaseFunction");
				formatElement(baseFunctionElement, bf, myBlockNode);
			}
			
			appendInternalRenderableBlock(myBlock,myBlockNode, doc);

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(
					file.getAbsolutePath()));
			transformer.transform(source, result);
			JOptionPaneDescription jop = new JOptionPaneDescription();
			jop.showText(myBlock,rootNode,doc);
			
			

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
/*
	public void formatMyBlockElement(Element e, MyBlock bf, Node node) {
		e.setAttribute("name", bf.getGenusName());
		e.setAttribute("parameterList", bf.getFormattedInputParameters());
		e.setAttribute("code", bf.getCode());
		e.setAttribute("returnParameter", bf.getReturnParameter());
		e.setAttribute("setupCode", bf.getFormattedSetupCode());
		System.out.println("bubble text is writing " + bf.getBubbleText());
		//e.setAttribute("bubbleText", bf.getBubbleText());
		node.appendChild(e);
	}*/

	//Appends internalRenderableBlock nodes to myBlockNode in the XML
	private void appendInternalRenderableBlock(MyBlock myBlock, Node myBlockNode, Document doc) {
		
	for(RenderableBlock iRenBlock:myBlock.getInternalRenderableBlockList()){
		/*DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();*/
			
			Node iRenBlockNode = iRenBlock.getSaveNode(doc);
			myBlockNode.appendChild(iRenBlockNode);
	/*	} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		}
		
	}

	public void formatElement(Element e, BaseFunction bf, Node node) {
		e.setAttribute("name", bf.getGenusName());
		e.setAttribute("parameterList", bf.getFormattedInputParameters());
		e.setAttribute("code", bf.getCode());
		e.setAttribute("returnParameter", bf.getReturnParameter());
		e.setAttribute("setupCode", bf.getFormattedSetupCode());
		node.appendChild(e);
	}

	public MyBlock getMyBlock(String genusName) {
		for (int i = 0; i < myBlockSet.size(); i++) {
			if (myBlockSet.get(i).getGenusName().equals(genusName)) {
				return myBlockSet.get(i);
			}
		}
		return null;
	}

	class JOptionPaneDescription {
		private MyBlock myBlock;
		private Node rootNode;
		private Document doc;
		private void display() {
			// String[] items = {"One", "Two", "Three", "Four", "Five"};
			// JComboBox combo = new JComboBox(items);
			ArrayList<JTextField> jTextFieldList = new ArrayList<JTextField>();
			JPanel panel = new JPanel(new GridLayout(0, 1));
			jTextFieldList = new ArrayList<JTextField>();
			JTextField field = new JTextField("");
			panel.add(new JLabel("Block Description for "
					+ myBlock.getGenusName()));
			jTextFieldList.add(field);
			panel.add(field);
			for (int i = 0; i < myBlock.getInputParameterList().size(); i++) {
				String str = myBlock.getInputParameterList().get(i);
				if (str != null && !str.equals("")) {
					JTextField field2 = new JTextField("");
					panel.add(new JLabel("Input parameter " + str));
					jTextFieldList.add(field2);
					panel.add(field2);
				}

			}
			boolean hasReturn = false;
			String str = myBlock.getReturnParameter();
			JTextField field3 = new JTextField("");
			if (str != null && !str.equals("")) {
				panel.add(new JLabel("Return parameter " + str));
				jTextFieldList.add(field3);
				panel.add(field3);
				hasReturn = true;
			}
			// JTextField field1 = new JTextField("1234.56");
			// JTextField field2 = new JTextField("9876.54");

			// panel.add(combo);
			// panel.add(new JLabel("Field 1:"));
			// panel.add(field1);
			// panel.add(new JLabel("Field 2:"));
			// panel.add(field2);
			int result = JOptionPane.showConfirmDialog(null, panel,
					"Enter Description", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				// System.out.println(combo.getSelectedItem()
				// + " " + field1.getText()
				// + " " + field2.getText());
				/*
				 * for (int i = 0; i < jTextFieldList.size(); i++) {
				 * System.out.println("text is " +
				 * jTextFieldList.get(i).getText());
				 * 
				 * }
				 */
				myBlock.setBubbleText(formatTextBubble(jTextFieldList,
						hasReturn));
				modifyXMLforBubbleText(rootNode,myBlock);

			} else {
				System.out.println("Cancelled");
			}
			
		}

		private void modifyXMLforBubbleText(Node node, MyBlock mb) {
			NodeList myBlockNodeList = node.getChildNodes();
			for(int i= 0;i<myBlockNodeList.getLength();i++){
				NamedNodeMap attr = myBlockNodeList.item(i).getAttributes();
				if(attr!=null && attr.getNamedItem("name").getTextContent().equals(mb.getGenusName())){
					((Element) myBlockNodeList.item(i)).setAttribute("bubbleText", mb.getBubbleText());
					//attr.getNamedItem("bubbleText").setTextContent(mb.getBubbleText());
				}
			}
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer;
			try {
				transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(
						file.getAbsolutePath()));
				transformer.transform(source, result);
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		private String formatTextBubble(ArrayList<JTextField> fieldList,
				boolean hasReturn) {
			String textBubble = new String("");
			textBubble += "Block description: " + fieldList.get(0).getText()
					+ "\n\n";
			for (int i = 1; i < fieldList.size(); i++) {
				if (hasReturn&&i==fieldList.size()-1) {
					textBubble += myBlock.getReturnParameter()
							+ /*" description(return parameter):"*/" (return):\n"
							+ fieldList.get(i).getText() + "";
					break;
				}
				textBubble += myBlock.getInputParameterList().get(i - 1)
						+ /*" description(parameter):"*/":\n"
						+ fieldList.get(i).getText() + "\n\n";
			}
			//System.out.println("textBubble is " + textBubble);
			return textBubble;
		}

		public void showText(MyBlock myBlock, Node rootNode, Document doc) {
			this.myBlock = myBlock;
			this.rootNode = rootNode;
			this.doc = doc;
			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					display();
				}
			});
		}
	}

}
