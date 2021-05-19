import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

/**
 * TreeNode class. Each instance of this class represents an instance in a Huffman tree.
 * @author Zdenek Plesek
 * @version 1.0
 */
public class TreeNode {
	private TreeNode parent;
	private TreeNode option1Node;
	private TreeNode option2Node;
	private String string;
	private String address;
	private int occurances;
	private Integer content;
	private boolean isRoot;
	private boolean optionFinal;
	
	
	/**
	 * WARNING! May produce unexpected behaviour.
	 * Constructor for the TreeNode object. Occurrences are initialised at zero.
	 * @param node1: TreeNode, option1 node
	 * @param node2: TreeNode, option2 node
	 * @param parent: TreeNode, parent node
	 */
	@Deprecated
	public TreeNode(TreeNode node1, TreeNode node2, TreeNode parent) {
		this.optionFinal = false;
		this.option1Node = node1;
		this.option2Node = node2;
		
		occurances = 0;
		this.string = null;
		
		if(parent == null) {
			isRoot = true;
			parent = null;
		} else {
			this.parent = parent;
			isRoot = false;
		}
		
	}
	
	/**
	 * TreeNode constructor.
	 * @param node1: TreeNode, option 1 node.
	 * @param node2: TreeNode, option 2 node.
	 * 
	 * Created node is not final. (Does not represent a certain byte.)
	 * The node is created as a root node.
	 * Occurrences are initialised at zero.
	 */
	public TreeNode(TreeNode node1, TreeNode node2) {
		this.optionFinal = false;
		this.option1Node = node1;
		node1.setParent(this);
		this.option2Node = node2;
		node2.setParent(this);
		
		occurances = node1.getOccurances() + node2.getOccurances();
		this.string = null;
		this.content = null;
		
		
		isRoot = true;
		parent = null;
		
	}
	/**
	 * Creates a completely empty instance of the TreeNode class.
	 * The tree is not considered final, it does not hold any content.
	 * The tree is considered to be root, since it has no parent node.
	 * Occurrences are initialised at zero.
	 */
	public TreeNode() {
		this.optionFinal = false;
		this.option1Node = null;
		this.option2Node = null;
		this.parent = null;
		this.string = null;
		this.occurances = 0;
		this.isRoot = true;
		this.content = null;
	}
	
	/**
	 * Deprecated; might produce unexpected behaviour.
	 * Constructor for a final TreeNode instance.
	 * @param string: String, content represented by this TreeNode instance. The node is final.
	 * @param parent: TreeNode, node's parent. The node is not root node.
	 * @version 0.5
	 */
	@Deprecated
	public TreeNode(String string, TreeNode parent) {
		option1Node = null;
		option2Node = null;
		this.string = string;
		optionFinal = true;
		
		occurances = 0;
		
		if(parent == null) {
			isRoot = true;
			parent = null;
		} else {
			this.parent = parent;
			isRoot = false;
		}
	}
	/**
	 * Deprecated; might produce unexpected behaviour.
	 * Constructor for an instance of a TreeNode class.
	 * @param string: Content of the node. The Node is final.
	 * Occurences are initialised at zero.
	 * The node is considered a root since it doesn't have a parent object.
	 */
	@Deprecated
	public TreeNode(String string) {
		option1Node = null;
		option2Node = null;
		occurances = 0;
		this.string = string;
		optionFinal = true;
		isRoot = true;
	}
	/**
	 * Constructor for an instance of TreeNode class.
	 * The instance is considered final, since it holds content.
	 * The instance is considered root, since it is created without a parent.
	 * Occurrences are initialised at zero.
	 * @param content; byte held by the node.
	 */
	public TreeNode(Byte content) {
		option1Node = null;
		option2Node = null;
		occurances = 0;
		int tmp = content.byteValue();
		this.content = tmp;
		optionFinal = true;
		isRoot = true;
	}
	
	/**
	  * Constructor for an instance of TreeNode class.
	 * The instance is considered final, since it holds content.
	 * The instance is considered root, since it is created without a parent.
	 * Occurrences are initialised at zero.
	 * @param content; byte held by the node.
	 */
	public TreeNode(Integer content) {
		option1Node = null;
		option2Node = null;
		occurances = 0;
		this.content = content;
		optionFinal = true;
		isRoot = true;
	}
	
	/**
	 * Call this method on root node after the tree has been created to assign all nodes addresses.
	 * It will recursively call methods in its child nodes until the final node is reached.
	 */
	public void makeAddress() {
		if(isRoot) {
			if(optionFinal) {
				address = "0";
			} else {
				address = "";
			}
		} else {
			address = parent.getAddress();
			if(this.isOption1()) {
				address = address.concat("0");
			} else {
				address = address.concat("1");
			}
		}
		if(!(optionFinal)) {
			option1Node.makeAddress();
			option2Node.makeAddress();
		}
		
	}
	/**
	 * Call this method to increase occurances by one.
	 */
	public void occured() {
		occurances++;
	}
	/**
	 * Call this method to generate string representing this node and nodes it contains.
	 * @return String
	 */
	@Deprecated
	public String generateOutput() {
		String output = "";
		if(optionFinal) {
			output = "-"+string;
		} else {
			output = "{"+option1Node.generateOutput()+option2Node.generateOutput()+"}";
		}		
		return output;
	}
	/**
	 * Call this method to generate byte array containing bytes representing all this node
	 * and nodes it contains. Recursive function.
	 * '{' encoded with US_ASCII represents beginning of a node.
	 * Followed by two nodes contained wherein before it is closed by
	 * '}' encoded with US_ASCII representing end of a node.
	 * '-' encoded with US_ASCII represents final node. This character is always followed by a byte
	 * which represents content of this final node.
	 * @return byte[] array representing all the nodes.
	 */
	public byte[] generateOutputContent() {
		LinkedList<Byte> tmp = new LinkedList<Byte>();
		if(optionFinal) {
			tmp.add("-".getBytes(StandardCharsets.US_ASCII)[0]);
			if(content==-1) {
				tmp.add("-".getBytes(StandardCharsets.US_ASCII)[0]);
				tmp.add("-".getBytes(StandardCharsets.US_ASCII)[0]);
				tmp.add("-".getBytes(StandardCharsets.US_ASCII)[0]);
			} else {
				tmp.add((byte) content.intValue());
			}
		} else {
			tmp.add( "{".getBytes(StandardCharsets.US_ASCII)[0]);
			for(Byte b: option1Node.generateOutputContent()) {
				tmp.add(b);
			}
			for(Byte b: option2Node.generateOutputContent()) {
				tmp.add(b);
			}
			tmp.add( "}".getBytes(StandardCharsets.US_ASCII)[0]);
		}
		Byte [] tmp1 = new Byte[0];
		tmp1 = tmp.toArray(tmp1);
		byte[] output = new byte[tmp1.length];
		for(int i = 0; i<tmp1.length; i++) {
			output[i] = tmp1[i].byteValue();
		}
		return output;
	}
	/**
	 * ToString Function.
	 */
	@Override
	public String toString() {
		byte[] tmp = generateOutputContent();
		String answer = new String(tmp, StandardCharsets.US_ASCII);
		return answer;
	}
	/**
	 * Setter.
	 * Sets a parent for this node. Unsets this node as a root node.
	 * @param parent: TreeNode, parent node.
	 */
	public void setParent(TreeNode parent) {
		isRoot = false;
		this.parent = parent;
	}
	
	/**
	 * Setter.
	 * Sets an option 1 node. Automatically sets option 1 node's parent as this.
	 * @param node: TreeNode, child node 1.
	 */
	public void setNode1(TreeNode node) {
		this.option1Node = node;
		optionFinal = false;
		node.setParent(this);
	}
	
	/**
	 * Setter.
	 * Sets an option 2 node. Automatically sets option 2 node's parent as this.
	 * @param node: TreeNode, child node 2.
	 */
	public void setNode2(TreeNode node) {
		this.option2Node = node;
		optionFinal = false;
		node.setParent(this);
	}
	/**
	 * Getter.
	 * @return TreeNode, parent node.
	 */
	public TreeNode getParent() {
		return parent;
	}
	
	/**
	 * Getter.
	 * @return TreeNode, option1 node.
	 */
	public TreeNode getNode1() {
		return option1Node;
	}
	
	/**
	 * Getter.
	 * @return TreeNode, option2 node.
	 */
	public TreeNode getNode2() {
		return option2Node;
	}
	/**
	 * Getter.
	 * @return String, node's content.
	 */
	@Deprecated
	public String getStr() {
		return string;
	}
	
	/**
	 * Getter.
	 * @return String, get nodes address. A string of 1s and 0s. Length may vary.
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Getter.
	 * @return int, number of times this node's content has occured.
	 */
	public int getOccurances() {
		return occurances;
	}
	
	/**
	 * Private function.
	 * Returns boolean depending on whether current node is an option1 for its parent.
	 * @return boolean
	 */
	private boolean isOption1() {
		return parent.getNode1().equals(this);
	}
	
	/**
	 * Getter. 
	 * @return boolean, true if the node is a root, false if the node is not a root.
	 */
	public boolean isRoot() {
		return isRoot;
	}
	/**
	 * Getter.
	 * @return boolean, true if the node is final, false if the node is not final.
	 * Final node is such a node that contains a byte instead of two nodes.
	 */
	public boolean isFinal() {
		return optionFinal;
	}
	
	/**
	 * Getter.
	 * @return Integer, content of the node, if it has one. Otherwise returns null.
	 */
	public Integer getContent() {		
		return content;
	}
}
