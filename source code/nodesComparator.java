import java.util.Comparator;

public class nodesComparator implements Comparator<TreeNode> {

	public int compare(TreeNode o1, TreeNode o2) {
		if(o1.getOccurances()>o2.getOccurances()) {
			return -1;
		}
		if(o2.getOccurances()>o1.getOccurances()) {
			return 1;
		}
		return 0;
	}

}
