import java.util.LinkedList;

/**
 * Decoder class. Create an instance off to decode a file encoded with Encoder Class.
 * Operate through dynamic method "decode".
 * Use static method "intToStringBase2" to convert an int into string of 1s and 0s representing it.
 * @author Zdenek Plesek
 * @version 1.0
 * 
 */
public class Decoder {
	TreeNode rootNode;
	TreeNode activeNode;
	String buffer;
	
	/**
	 * Constructor for Decoder class.
	 * @param rootNode: TreeNode, root node of Huffman tree used for decoding.
	 */
	public Decoder(TreeNode rootNode) {
		this.rootNode = rootNode;
		activeNode = rootNode;
		buffer = "";
	}
	
	/**
	 * Private method. Call this method to add a string of 1s and 0s to buffer.
	 * @param string: String, the string to be appended to buffer.
	 */
	private void addToBuffer(String string) {
		buffer = buffer.concat(string);
	}
	
	/**
	 * Private method. Pops entries in the buffer and changes active node until it reaches a final node.
	 * (Node representing a byte.)
	 * @return boolean, returns true if it has reached a final node. Returns false if the buffer was expended
	 * but not final node has been reached.
	 */
	private boolean reachNext() {
		if(activeNode.isFinal()) {
			return true;
		}
		for(int i = 0; i<buffer.length(); i++) {
			if(popBuffer().equals("0")) {
				activeNode = activeNode.getNode1();
			} else {
				activeNode = activeNode.getNode2();
			}
			if(activeNode.isFinal()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Private method. Use to remove the first letter from the buffer and return it.
	 * @return String, the first letter in the buffer.
	 */
	private String popBuffer() {
		String popped = buffer.substring(0,1);
		buffer = buffer.substring(1);
		return popped;
	}
	/**
	 * Private method.
	 * Returns activeNode back to rootNode. Call after decoding a node to reset the decoder and prepare it
	 * for decoding a new sequence.
	 */
	private void reset() {
		activeNode = rootNode;
	}
	
	/**
	 * The primary method for Decoder object. Supply this method with each read byte converted to 1s and 0s
	 * to decode it into bytes.
	 * @param newByte: String, string of 1s and 0s representing the most recently read byte.
	 * @return Integer[], an array containing bytes of the decoded file. Values will always range fall in <-1;-255>
	 * range. -1 represents EOF.
	 */
	public Integer[] decode(String newByte) {
		LinkedList<Integer> contents = new LinkedList<Integer>();
		addToBuffer(newByte);
		while(reachNext()) {
			contents.add(activeNode.getContent());
			reset();
		}
		Integer[] answer = new Integer[0];
		answer = contents.toArray(answer);
		return answer;
	}
	/**
	 * Public static method. Use this method to convert an Integer/int into a String of 1s and 0s representing it.
	 * The input int must be within 0-255 range.
	 * @param i: Integer/int to be converted.
	 * @return String, String on 1s and 0s representing i. The resulting string will always be 8 characters long.
	 */
	public static String intToStringBase2(Integer i) {
		String answer = "";
		if(i>=128) {
			answer = answer.concat("1");
			i = i - 128;
		} else {
			answer = answer.concat("0");
		}
		if(i>=64) {
			answer = answer.concat("1");
			i = i - 64;
		} else {
			answer = answer.concat("0");
		}
		if(i>=32) {
			answer = answer.concat("1");
			i = i - 32;
		} else {
			answer = answer.concat("0");
		}
		if(i>=16) {
			answer = answer.concat("1");
			i = i - 16;
		} else {
			answer = answer.concat("0");
		}
		if(i>=8) {
			answer = answer.concat("1");
			i = i - 8;
		} else {
			answer = answer.concat("0");
		}
		if(i>=4) {
			answer = answer.concat("1");
			i = i - 4;
		} else {
			answer = answer.concat("0");
		}
		if(i>=2) {
			answer = answer.concat("1");
			i = i - 2;
		} else {
			answer = answer.concat("0");
		}
		if(i>=1) {
			answer = answer.concat("1");
			i = i - 1;
		} else {
			answer = answer.concat("0");
		}
		return answer;
	}
}
