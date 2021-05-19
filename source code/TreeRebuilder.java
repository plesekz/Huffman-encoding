import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

/**
 * TreeRebuilder class. Use this class to rebuild a tree from a saved version.
 * Operate through the "nextByte" and "getTree" dynamic methods.
 * Instance of TreeRebuilder has four byte buffer inside to recognise four '-' encoded in ASCII_US
 * which represents EOF symbol.
 * @author Zdenek Plesek
 * @version 1.0
 */
public class TreeRebuilder {
	private TreeNode rootNode;
	private TreeNode currentNode;
	private TreeNode tmpNode;
	private int hyphensInBuffer;
	private boolean handleContent;
	private boolean onceUponATime;
	private LinkedList<Byte> buffer;
	
	/**
	 * Constructor.
	 */
	public TreeRebuilder() {		
		handleContent = false;
		hyphensInBuffer = 0;
		buffer = new LinkedList<Byte>();
		onceUponATime = false;
	}
	
	/**
	 * Use this to submit every byte read.
	 * @param input: Byte, byte read.
	 * @throws Exception in case the .tree file is corrupted.
	 */
	public void nextByte(Byte input) throws Exception {
		buffer.add(input);
		
		byte[] b = {input};
		char c = new String(b, StandardCharsets.US_ASCII).charAt(0);
		if(c=='-') {
			hyphensInBuffer++;
		}
		if(hyphensInBuffer==4) {
			tmpNode = new TreeNode(Integer.MIN_VALUE);
			if(currentNode.getNode1()==null) {
				currentNode.setNode1(tmpNode);
				
				hyphensInBuffer = 0;
				buffer = new LinkedList<Byte>();
				return;
			}
			if(currentNode.getNode2()==null) {
				currentNode.setNode2(tmpNode);
				
				hyphensInBuffer = 0;
				buffer = new LinkedList<Byte>();
				return;
			}
			throw new Exception("Tree file (.tree) corrupted.");
		}
		if(buffer.size()>=4) {
			handleAnother();
		}
	}
	
	/**
	 * Private method. Called from nextByte method when the buffer is 4 bytes long and no special circumstance
	 * has occurred.
	 * @throws Exception if the .tree file is corrupted.
	 */
	private void handleAnother() throws Exception {
		Byte input = buffer.poll();
		if(handleContent) {
			handleContent(input);
			handleContent = false;
		} else {
			byte[] b = {input};
			char c = new String(b, StandardCharsets.US_ASCII).charAt(0);
			newNode(c);
		}
	}
	/**
	 * WARNING! Deprecated, WILL produce unexpected results and may lead to instability.
	 * Use nextByte instead.
	 * Use to submit every byte read after converting it to a char.
	 * @param input: char, byte read converted into a char.
	 * @throws Exception if the .tree is corrupted.
	 */
	@Deprecated
	void nextChar(char input) throws Exception {
		if(handleContent) {
			handleContent(input);
		} else {
			newNode(input);
		}
	}
	
	/**
	 * Private method.
	 * This method is used to handle byte if the previous byte has been '-' encoded according to ASCII_US
	 * which represents final node.
	 * @param content: Byte, content of final node.
	 * @throws Exception in case the .tree file is corrupted.
	 */
	private void handleContent(Byte content) throws Exception {
		byte[] b = {content};
		char c = new String(b, StandardCharsets.US_ASCII).charAt(0);
		if(c=='-') {
			hyphensInBuffer--;
		}
		
		
		tmpNode = new TreeNode(content);
		if(currentNode.getNode1()==null) {
			currentNode.setNode1(tmpNode);
			return;
		}
		if(currentNode.getNode2()==null) {
			currentNode.setNode2(tmpNode);
			return;
		}
		throw new Exception("Tree file (.tree) corrupted.");
	}
	
	/**
	 * Deprecated, may produce unexpected results.
	 * Private method. Use this method to handle content of a final node.
	 * @param content: char, content of the final node.
	 * @throws Exception in case the .tree file is corrupted.
	 */
	@Deprecated
	private void handleContent(char content) throws Exception {
		String s = String.valueOf(content);
		tmpNode = new TreeNode(s);
		if(currentNode.getNode1()==null) {
			currentNode.setNode1(tmpNode);
			currentNode = tmpNode;
			return;
		}
		if(currentNode.getNode2()==null) {
			currentNode.setNode2(tmpNode);
			currentNode = tmpNode;
			return;
		}
		throw new Exception("Tree file (.tree) corrupted.");
	}
	
	/**
	 * Private method.
	 * Use this method to handle creation of newNode. Handles '{','}' and '-' encoded according to ASCII_US
	 * assuming the previous byte wasn't '-' encoded according to ASCII_US.
	 * @param input: char, '{', '}', '-' encoded according to the ASCII_US.
	 * @throws Exception in case the input isn't any of the defined characters.
	 */
	private void newNode(char input) throws Exception {
		switch(input) {
		case '{':
			tmpNode = new TreeNode();
			if(rootNode == null) {
				rootNode = tmpNode;
				currentNode = rootNode;
				break;
			}
			if(currentNode.getNode1()==null) {
				currentNode.setNode1(tmpNode);
				currentNode = tmpNode;
				break;
			}
			if(currentNode.getNode2()==null) {
				currentNode.setNode2(tmpNode);
				currentNode = tmpNode;
				break;
			}
			throw new Exception("Tree file (.tree) corrupted.");
		case '-':
			handleContent = true;
			hyphensInBuffer--;
			break;
		case '}':
			if(currentNode.isRoot()) {
				if(onceUponATime) {
					throw new Exception("Tree file (.tree) corrupted.");
				} else {
					onceUponATime = true;
				}
				
			} else {
				currentNode = currentNode.getParent();
			}
			break;
		}
	}
	
	/**
	 * Use this method to acquire the created tree after all bytes were read and submitted through nextByte method.
	 * @return TreeNode, the root node of the created tree.
	 * @throws Exception in case the .tree file is corrupted.
	 */
	public TreeNode getTree() throws Exception {
		while(buffer.size()>0) {
			handleAnother();
		}
		if(!(currentNode.equals(rootNode))) {
			throw new Exception("Tree file (.tree) corrupted.");
		}
		rootNode.makeAddress();
		return rootNode;
	}
}
