import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Encode class. The main class to be run in order to encode a file.
 * @author Zdenek Plesek
 * @version 1.0
 */
public class Encode {
	private static BufferedInputStream in;
	private static BufferedOutputStream out;
	
	/**
	 * The main method of the Encode class. Call from terminal.
	 * This method handles input from terminal and calls auxiliary methods to fetch Huffman tree
	 * and perform actual decoding.
	 * @param args: String[], passed from console.
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis(); 
		TreeNode rootTreeNode;
		switch(args.length) {
		case 1:
			rootTreeNode = makeTree(args[0]);
			encode(rootTreeNode, args[0], args[0].concat(".hf"));
			break;
		case 2:
			if(args[1].substring(0, 7).equals("-using:")) {
				rootTreeNode = extractTree(args[1].substring(7));
				encode(rootTreeNode, args[0], args[0].concat(".hf"));
			} else {
				rootTreeNode = makeTree(args[0]);
				encode(rootTreeNode, args[0], args[1]);
			}
			break;
		case 3:
			if(args[1].substring(0, 7).equals("-using:")) {
				rootTreeNode = extractTree(args[1].substring(7));
				encode(rootTreeNode, args[0], args[2]);
				break;
			}
			System.out.println("Invalid input.\n\n");
		case 0:
		default:
			System.out.println(
					"This command may take up to two three arguments.\n"
					+ "In case only one is provided, it will be understood to be the name of the file to be encoded.\n"
					+ "In case there are two arguments, the first one is understood to be the name of the file to be encoded.\n"
					+ "The second argument can either specify tree to be used, preface the name of the file with \"-using:\"\n"
					+ "Or it can be a name of the output file.\n"
					+ "In case three arguments are provided, the first one will be understood to be the name of the file to be encoded.\n"
					+ "The second one the name of the .tree file, preface its name with -using:\n"
					+ "The third argument will become the new name of the file.\n"
					+ "\n"
					+ "All arguments must be separated by spaces. The file names may not contain spaces."); 
		}
		long timeElapsed = System.currentTimeMillis() -  startTime;
		System.out.println(timeElapsed+" miliseconds elapsed.");
	}
	
	/**
	 * Private method called to encode a file.
	 * @param rootTreeNode: TreeNode, root node of the Huffman tree.
	 * @param inputFile: String, name of the file to be decoded.
	 * @param outputFile: String, name of the resulting file.
	 */
	private static void encode(TreeNode rootTreeNode, String originFile, String outputFile) {
		Encoder enc = new Encoder(rootTreeNode);
		try {
			in = new BufferedInputStream(new FileInputStream(originFile));
			out = new BufferedOutputStream(new FileOutputStream(outputFile));
			
		} catch (Exception e) {
			System.out.println(e);
			System.exit(126);
		}
		Integer b;
		String record;
		try {
			b = in.read();
			while(b>-1) {
				enc.encodeByteToBuffer(b);
				while(enc.hasNextByte()) {
					record = enc.getNextByte();
					out.write(Encoder.parseStringAsBinaryByte(record));
				}
				b = in.read();
			}
			String[] remains = enc.flush();
			for(String rem: remains) {
				out.write(Encoder.parseStringAsBinaryByte(rem));
			}
		}catch(Exception e) {
			System.out.println(e);
			System.exit(1);
		}
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		}
		
	}
	
	/**
	 * Private method that can be used to build a Huffman tree from an unencoded file.
	 * @param originFile: String, name of the file according to which the tree will be built.
	 * @return TreeNode, returns the root node of the Huffman tree.
	 */
	private static TreeNode makeTree(String originFile) {
		int tmp = originFile.lastIndexOf('.');
		String treefile = originFile;
		if(tmp!=-1) {
			treefile = originFile.substring(0, originFile.lastIndexOf('.'));
		}		
		treefile = treefile.concat(".tree");
		TreeBuilder tr;
		try {
			tr = new TreeBuilder(originFile);
			
			//String s = originFile.substring(0,originFile.lastIndexOf("."));
			BufferedOutputStream ot = new BufferedOutputStream(new FileOutputStream(treefile));
			TreeNode root = tr.buildTree();
			ot.write(root.generateOutputContent());
			ot.close();
			return root;
		} catch (Exception e) {
			System.out.println(e);
			System.exit(126);
		}
		
		return null;
	}
	
	/**
	 * Private method to parse a tree from a .tree file created as a product of makeTree method.
	 * @param originFile: String, name of the file from which the tree will be parsed.
	 * @return TreeNode, returns the root node of the Huffman tree.
	 */
	private static TreeNode extractTree(String originFile) {
		TreeRebuilder tr = new TreeRebuilder();
		BufferedInputStream ot = null;
		try {
			 ot = new BufferedInputStream(new FileInputStream(originFile));
		} catch (Exception e) {
			System.out.println("Cannot open the .tree file.");
			System.out.println(e);
			System.exit(126);
		}
		try {			
			Integer read;
			read = ot.read();
			while(read>-1) {
				tr.nextByte( (byte) read.intValue() );
				read = ot.read();
			}
		} catch(Exception e) {
			System.out.println(e);
			System.exit(1);
		}
		
		try {
			return tr.getTree();
		} catch(Exception e) {
			System.out.println(e);
			System.exit(1);
		}
		return null;
	}
}
