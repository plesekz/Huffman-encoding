import java.util.HashMap;
import java.util.LinkedList;

/**
 * Encoder class. Create an instance off to encode a file that can be decoded with Decoder Class.
 * Operate through dynamic methods: "encodeByteToBuffer", "getNextByte", "hasNextByte" and "flush".
 * Use static method "parseStringAsBinaryByte" to convert a string of 1s and 0s into an integer.
 * @author Zdenek Plesek
 * @version 1.0
 * 
 */
public class Encoder {
	private HashMap<Integer, TreeNode> startingPoints;
	private TreeNode defaultNode;
	private String buffer;
	
	/**
	 * Constructor for the Encoder Class.
	 * @param rootNode: TreeNode, root node of the Huffman tree to be used for encoding.
	 */
	public Encoder(TreeNode rootNode){
		startingPoints = new HashMap<Integer, TreeNode>();
		defineDefaultNode(rootNode);
		buildStartingPoint(rootNode);	
		buffer = "";
	}
	/**
	 * Private recursive method.
	 * Call this method with the Huffman tree root node and it will reapeatedly call itself until it reaches
	 * final node defined by a sequence of 0s.
	 * @param node
	 */
	private void defineDefaultNode(TreeNode node) {
		if(node.isFinal()) {
			defaultNode = node;
		} else {
			defineDefaultNode(node.getNode1());
		}
	}
	
	/**
	 * Private recursive method. Call this method with the Huffman tree root node and it will
	 * repeatedly call itself until it reaches all final nodes of the tree. (Nodes containing a byte.)
	 * It will populate a HashMap with the bytes as keys and their nodes as values.
	 * @param node: TreeNode, node to be analyse.
	 */
	private void buildStartingPoint(TreeNode node) {
		if(node.isFinal()) {
			startingPoints.put(node.getContent(), node);
		} else {
			buildStartingPoint(node.getNode1());
			buildStartingPoint(node.getNode2());
		}
	}
	
	/**
	 * Use this method to populate buffer with values representing bits which represent byte passed as argument.
	 * @param arg: Integer, integer representing the last read byte. Behaviour for integers non-defined in the tree
	 * is undefined.
	 */
	public void encodeByteToBuffer(Integer arg) {
		TreeNode node = startingPoints.getOrDefault(arg, defaultNode);		
		buffer = buffer.concat(node.getAddress());
	}
	
	/**
	 * Use this method to discover whether next byte is ready to written.
	 * @return boolean, true if next byte is read, false if buffer isn't populated with 8 bits yet.
	 */
	public boolean hasNextByte() {
		if(buffer.length()>7) {
			return true;
		}
		return false;
	}
	/**
	 * Use this method to obtain the next byte to be written. The byte is returned in the form of String of 1s and 0s.
	 * @return String, String of 1s and 0s representing the next byte.
	 */
	public String getNextByte() {
		if(buffer.length()>7) {
			String tmp = buffer.substring(0, 8);
			buffer = buffer.substring(8);
			return tmp;
		}
		return "";
	}
	/**
	 * This method should be called before writing is finished. It encodes eof symbol into the buffer
	 * and appends 0s to the end to make a byte.
	 * @return String[], an array of strings containing all bytes left in the buffer aas strings of 1s and 0s.
	 */
	public String[] flush() {
		LinkedList<String> strings = new LinkedList<String>();
		encodeByteToBuffer(-1);
		while(buffer.length()>7) {
			strings.add(getNextByte());
		}
		for(int i = buffer.length(); i < 8; i++) {
			buffer = buffer.concat("0");
		}
		strings.add(buffer);
		buffer = "";
		String[] answers = new String[0];
		answers = strings.toArray(answers);
		return answers;
	}
	
	/**
	 * Static method. Call this method with a string of 1s and 0s to read it as binary
	 * to convert it into an integer it represents.
	 * The function is undefined for strings of different lenghts than 8 characters.
	 * It will always result in a number ranging from 0 to 255.
	 * @param s, String to be parse.
	 * @return Integer, representing number obtained by parsing the string.
	 */
	public static Integer parseStringAsBinaryByte(String s) {
		Integer i = 0;
		if(s.charAt(0)=='1') {
			i = i+128;
		}
		if(s.charAt(1)=='1') {
			i = i+64;
		}
		if(s.charAt(2)=='1') {
			i = i+32;
		}
		if(s.charAt(3)=='1') {
			i = i+16;
		}
		if(s.charAt(4)=='1') {
			i = i+8;
		}
		if(s.charAt(5)=='1') {
			i = i+4;
		}
		if(s.charAt(6)=='1') {
			i = i+2;
		}
		if(s.charAt(7)=='1') {
			i = i+1;
		}
		return i;
	}
}
