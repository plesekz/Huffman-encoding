import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * TreeBuilder class. This class is used to build a tree from a file full of content.
 * Operate through the "buildTree" method.
 * @author Zdenek Plesek
 *
 */
public class TreeBuilder {
	HashMap<Integer, TreeNode> counts;
	private BufferedInputStream in;
	
	/**
	 * Constructor for the TreeBuilder class.s
	 * @param target: String, name of the file from which the tree is meant to be build.
	 * @throws FileNotFoundException in case the source file cannot be opened.
	 */
	public TreeBuilder(String target) throws FileNotFoundException {
		in = new BufferedInputStream(new FileInputStream(target));
		counts = new HashMap<Integer, TreeNode>();
	}
	
	/**
	 * Call this method to build the Huffman tree.
	 * @return the root node of the Huffman tree.
	 * @throws Exception in case the file cannot be read from.
	 */
	public TreeNode buildTree() throws Exception {
		Integer read = in.read();
		TreeNode tn;
		while(read>-1) {
			if(counts.containsKey(read)) {
				counts.get(read).occured();
			} else {
				tn = new TreeNode(read);
				tn.occured();
				counts.put(read, tn);
			}
			read = in.read();
		}
		tn = new TreeNode(-1);
		counts.put(Integer.MIN_VALUE, tn);
		tn.occured();
		TreeNode[] nodes = new TreeNode[0];
		nodes = (TreeNode[]) counts.values().toArray(nodes);
		int unsortedNodes = nodes.length;
		while(unsortedNodes>1) {
			Arrays.sort(nodes, new nodesComparator());
			tn = new TreeNode(nodes[unsortedNodes-2], nodes[unsortedNodes-1]);
			nodes[unsortedNodes-2] = tn;
			nodes[unsortedNodes-1] = new TreeNode();
			unsortedNodes--;
		}
		nodes[0].makeAddress();
		return nodes[0];
	}
}
