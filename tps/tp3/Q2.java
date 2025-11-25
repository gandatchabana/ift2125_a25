package ift2015.tp3;

import java.util.*;


/*
* ACADEMIC INTEGRITY ATTESTATION
*
* [ x ] I certify that I have not used any generative AI tool
*
to solve this problem .
*
* [ ] I have used one or more generative AI tools .
*
Details below :
*
*
Tool ( s ) used : _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _
*
*
Reason ( s ) for use :
*
___________________________________________________
*
___________________________________________________
*
*
Affected code sections :
*
___________________________________________________
*
___________________________________________________
*/



public class Q2 {
    
    // TreeNode clas
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        // Constructor
        public TreeNode(int val) {
            this.val = val;
        }
    }
    
    private TreeNode root;

    // Constructors
    public Q2(TreeNode root) { this.root = root; }
    public Q2() { this.root = null; }
   

    private Queue<TreeNode> levelOrderTraversal(TreeNode root, int level) {
	Queue<TreeNode> lorder_q = new LinkedList<TreeNode>(); 
	Queue<TreeNode> nodes = new ArrayDeque<TreeNode>(1000);
	if (root == null) {return null;}
	if (root.left == null && root.right == null) { nodes.offer(root); return nodes; }
	lorder_q.offer(root);
	//if (root.left != null)  { lorder_q.offer(root.left); }
	//if (root.right != null) { lorder_q.offer(root.right); }
	while (!lorder_q.isEmpty()) {
		//System.out.print("lOrder: [" );
		//for (TreeNode node : lorder_q) { System.out.println(node.commitHash); }
		TreeNode cur_node = lorder_q.poll();
		//System.out.println("Visiting node " + cur_node.commitHash);
		nodes.offer(cur_node);
		//System.out.println("Added node " + cur_node.commitHash + " to nodes stack");
		if (cur_node.left != null)  {
			lorder_q.offer(cur_node.left);
			//System.out.println("Added left child node " + cur_node.left.commitHash + "to lOrder");
		}
		if (cur_node.right != null) {
			lorder_q.offer(cur_node.right);
			//System.out.println("Added right child node " + cur_node.right.commitHash + "to lOrder");
		}
	}
	return nodes;
    }


    private <T> void displayList(List<T> lst) {
	T _elem = lst.get(0);
	if (! (_elem instanceof String) || ! (_elem instanceof Integer)) {
		System.out.println("Incompatible type for displaying");
		System.out.println("Only accepts String or Integer");
		return;
	}
	System.out.print("Elements: [");
	for (T elem : lst) { System.out.print("(" + elem + ") "); }
	System.out.println("]");
	return;
    }


    private int computeSumBST(TreeNode root, int sum) {
	if (root == null) {return 0;}
	if (root.left == null && root.right == null) {
		//System.out.println("DEBUG: At leaf " + root.val + ", returning its value");
		return root.val;
	}

	int sum_left = 0;
	int sum_right = 0;

	if (root.left != null) {
		sum_left = computeSumBST(root.left, sum);
		//System.out.println("DEBUG: Adding left " + sum_left); 
		//sum += sum_left;
	}
	if (root.right != null) {
		sum_right = computeSumBST(root.right, sum);
		//System.out.println("DEBUG: Adding right " + sum_right); 
		//sum += sum_right;
	}

	sum += (sum_left + sum_right);
	sum += root.val;
	//System.out.println("DEBUG: Adding val " + root.val + " to total sum " + sum);

	return sum;
    }


    public int maxSumBST(TreeNode root) {
        // TODO
	// Get valid BST subtrees from `root` as List
	// For every valid BST subtree, get sum
	// Keep greatest sum in a var
	// Update greatest if sum is greater than current greatest
        
	if (root == null) {return 0;}

	Queue<TreeNode> nodes = levelOrderTraversal(root, 0);
	LinkedList<TreeNode> nodes_valid = new LinkedList<TreeNode>();

	if (nodes.size() <= 0) { return 0; }

	// Add all valid BST nodes to nodes_valid
	for (TreeNode node : nodes) {
		if (isValidBST(node)) {
			//System.out.println("DEBUG: Node " + node.val + " valid BST");
			nodes_valid.add(node);
		}
	}

	if (nodes_valid.size() == 0) { return 0; }
	//System.out.print("DEBUG: Valid BSTs: ");
	nodes_valid.forEach( (n) -> { System.out.print(n.val + " "); } );

	// Compute sum BST for every node in `nodes_valid`
	int max_sum = 0;
	for (TreeNode node : nodes_valid) {
		int sum = computeSumBST(node, 0);
		//System.out.println("DEBUG: For node " + node.val + ": sum == " + sum);
		if (sum > max_sum) {
			//System.out.println("DEBUG: Better max sum found for node " + node.val);
			max_sum = sum;
		}
	}
	System.out.println("Final max sum: " + max_sum);
	return max_sum;
    }
    

    public boolean isValidBST(TreeNode root) {
        // TODO
	// Valid BST:
	// 	- left subtree contains nodes with keys strictly less than root key
	//	- right subtree contains nodes with keys strictly greater than root key
	//	- left&right valid BSTs
	// Recursively call isValidBST on nodes until leaf
	// 	A leaf is valid BST (if TreeNode.left and TreeNode.right both null --> return valid(0?))
	// 	Once return, check if return value is valid
	// 		If not valid, return 1
	// 		else, check that TreeNode.left.val < TreeNode.val and TreeNode.right.val > TreeNode.val
	// 		If AND condition fails, return 1
	//
	
	if (root == null) {return false;}
	if (root.left == null && root.right == null) {return true;}
	


	if (root.left != null) {
		boolean valid_left = isValidBST(root.left);
		if (valid_left == false) {return false;}
		if (root.left.val > root.val) {return false;}
	}

	if (root.right != null) {
		boolean valid_right = isValidBST(root.right);
		if (valid_right == false) {return false;}
		if (root.right.val < root.val) {return false;}
	}
	
	// Check root and root children keys
	//if (root.left.val > root.val || root.right.val < root.val) {return false;}

        return true;
    }
    
    public List<Integer> findAllBSTRoots() {
        // TODO
	ArrayList<Integer> bst_roots = new ArrayList<Integer>();
	if (root == null) {return bst_roots;}

	Queue<TreeNode> nodes = levelOrderTraversal(root, 0);
	ArrayList<Integer> nodes_valid = new ArrayList<Integer>();

	if (nodes.size() <= 0) { return nodes_valid; }

	// Add all valid BST nodes to nodes_valid
	for (TreeNode node : nodes) {
		if (isValidBST(node)) {
			//System.out.println("DEBUG: Node " + node.val + " valid BST");
			nodes_valid.add(node.val);
		}
	}

	nodes_valid.sort(null);
	//displayList(nodes_valid);

        return nodes_valid;
    }
    

    public int countValidBSTs() throws Exception {
        // TODO
	
	if (root == null) { throw new Exception("root is null"); }

	Queue<TreeNode> nodes = levelOrderTraversal(root, 0);
	LinkedList<TreeNode> nodes_valid = new LinkedList<TreeNode>();

	if (nodes == null) { throw new Exception("TreeNode is empty"); }
	if (nodes.size() <= 0) { return 0; }

	// Add all valid BST nodes to nodes_valid
	for (TreeNode node : nodes) {
		if (isValidBST(node)) {
			//System.out.println("DEBUG: Node " + node.val + " valid BST");
			nodes_valid.add(node);
		}
	}

        return nodes_valid.size();
    }
   

    public int getMinBSTSum() {
        // TODO
	if (root == null) {return Integer.MAX_VALUE;}

	Queue<TreeNode> nodes = levelOrderTraversal(root, 0);
	LinkedList<TreeNode> nodes_valid = new LinkedList<TreeNode>();

	if (nodes.size() <= 0) { return Integer.MAX_VALUE; }

	// Add all valid BST nodes to nodes_valid
	for (TreeNode node : nodes) {
		if (isValidBST(node)) {
			//System.out.println("DEBUG: Node " + node.val + " valid BST");
			nodes_valid.add(node);
		}
	}

	if (nodes_valid.size() == 0) { return Integer.MAX_VALUE; }

	// Compute sum BST for every node in `nodes_valid`
	int min_sum = root.val;
	for (TreeNode node : nodes_valid) {
		int sum = computeSumBST(node, 0);
		if (sum < min_sum) {
			min_sum = sum;
		}
	}

	return min_sum;
    }
    

    public Map<Integer, Integer> getBSTSizeDistribution() {
        // TODO
	if (root == null) {return new HashMap<>();}

	Queue<TreeNode> nodes = levelOrderTraversal(root, 0);
	LinkedList<TreeNode> nodes_valid = new LinkedList<TreeNode>();
	HashMap<Integer, Integer> bst_map = new HashMap<Integer, Integer>();

	if (nodes.size() <= 0) { return bst_map; }

	// Add all valid BST nodes to nodes_valid
	for (TreeNode node : nodes) {
		if (isValidBST(node)) {
			int num_nodes = countNodesBST(node, 0);
			if (bst_map.containsKey(num_nodes)) {
				int pre_val = bst_map.get(num_nodes);
				int post_val = pre_val + 1;
				bst_map.put(num_nodes, post_val);
			} else {
				bst_map.put(num_nodes, 1);
			}
		}
	}

        return bst_map;
    }
    

    private int countNodesBST(TreeNode root, int count) {
	if (root == null) {return 0;}
	if (root.left == null && root.right == null) {
		//System.out.println("DEBUG: At leaf " + root.val + " : incrementing count by 1");
		count++;
		return count;
	}

	int count_left = 0;
	int count_right = 0;

	if (root.left != null) {
		count_left = countNodesBST(root.left, count);
		//System.out.println("DEBUG: At node " + root.val + " count_left == " + count_left);
		//count += count_left;
	}
	if (root.right != null) {
		count_right = countNodesBST(root.right, count);
		//System.out.println("DEBUG: At node " + root.val + " count_right == " + count_right);
		//count += count_right;
	}

	//System.out.println("DEBUG: count(" + count + ") = count(" + count + ") + count_left(" + count_left + ") + count_right(" + count_right + ") + 1" );
	count = count + count_left + count_right + 1;
	//count++;

	return count;
    }


    public TreeNode findLargestBST() {
       // TODO
 	Queue<TreeNode> nodes = levelOrderTraversal(root, 0);
	LinkedList<TreeNode> nodes_valid = new LinkedList<TreeNode>();

	if (nodes.size() <= 0) { return null; }

	// Add all valid BST nodes to nodes_valid
	for (TreeNode node : nodes) {
		if (isValidBST(node)) {
			//System.out.println("DEBUG: Node " + node.val + " valid BST");
			nodes_valid.add(node);
		}
	}

	if (nodes_valid.size() == 0) { return null; }

	// Rationale: As `nodes` is levelOrderTraversal and `nodes_valid` iterates though `nodes`
	// to find valid BST, then the first element of `nodes_valid` should be the largest BST

	TreeNode supposed_largest_bst_node = nodes_valid.get(0);
	TreeNode supposed_second_largest_bst_node = nodes_valid.get(1);

	assert countNodesBST(supposed_largest_bst_node, 0) >= countNodesBST(supposed_second_largest_bst_node, 0) : "second > first";

        return nodes_valid.get(0);
    }


    public List<Integer> getInorderBST(TreeNode root) {
        // TODO
	// Check if `root` is valid BST
	if (! isValidBST(root)) {
		System.out.println("ERROR: Node " + root.val + " is not valid BST"); return new ArrayList<>();
	}
	
	TreeNode cur_node = root;
	ArrayList<Integer> nodes_val = new ArrayList<Integer>();

	while (root.left != null) {
		cur_node = cur_node.left;
	}
	nodes_val.add(cur_node.val);
	

        return new ArrayList<>();
    }


    public static void main(String[] args) throws Exception {
	// Constructing valid BST
	Q2 treenode1 = new Q2();
	treenode1.root = new Q2.TreeNode(10);
	treenode1.root.left = new Q2.TreeNode(8);
	treenode1.root.right = new Q2.TreeNode(12);
	treenode1.root.left.left = new Q2.TreeNode(2);
	treenode1.root.left.right = new Q2.TreeNode(9);
	treenode1.root.right.left = new Q2.TreeNode(11);
	treenode1.root.right.right = new Q2.TreeNode(15);

	// Constructing invalid BST
	Q2 treenode2 = new Q2();
	treenode2.root = new Q2.TreeNode(10);
	treenode2.root.left = new Q2.TreeNode(18);
	treenode2.root.right = new Q2.TreeNode(12);
	treenode2.root.left.left = new Q2.TreeNode(2);
	treenode2.root.left.right = new Q2.TreeNode(6);
	treenode2.root.right.left = new Q2.TreeNode(11);
	treenode2.root.right.right = new Q2.TreeNode(15);


	//System.out.println("computeSumBST(root, 0)");
	//System.out.println(q2.computeSumBST(root, 0));
	//System.out.println("computeSumBST(root2, 0)");
	//System.out.println(q2.computeSumBST(root2, 0));

	//System.out.println("\n\tmaxSumBST(root)");
	//q2.maxSumBST(root);
	//System.out.println("\n\t\n\tmaxSumBST(root2)");
	//q2.maxSumBST(root2);

	System.out.println("\n\tisValidBST(treenode2.root.left)\t Node 18 (invalid)");
	System.out.println(treenode2.isValidBST(treenode2.root.left));
	
	System.out.println("\n\tfindAllBSTRoots(treenode1)");
	System.out.println(treenode1.findAllBSTRoots());

	System.out.println("\n\tcountValidBSTs(treenode1)");
	System.out.println(treenode1.countValidBSTs());
	
	System.out.println("\n\tcountValidBSTs(treenode2)");
	System.out.println(treenode2.countValidBSTs());

	System.out.println("\n\tgetMinBSTSum(treenode1) === 2");
	System.out.println(treenode1.getMinBSTSum());

	System.out.println("\n\tgetBSTSizeDistribution(treenode1)");
	System.out.println(treenode1.getBSTSizeDistribution());
	
	System.out.println("\n\tgetBSTSizeDistribution(treenode2)");
	System.out.println(treenode2.getBSTSizeDistribution());

	System.out.println("\n\tcountNodesBST(treenode1.root, 0)");
	System.out.println(treenode1.countNodesBST(treenode1.root, 0));
	
	System.out.println("\n\tcountNodesBST(treenode2.root.left, 0)");
	System.out.println(treenode2.countNodesBST(treenode2.root.left, 0));

	System.out.println("\n\tfindLargestBST(treenode1)");
	System.out.println("Largest BST in treenode1 : " + treenode1.findLargestBST().val);
	System.out.println("\n\tfindLargestBST(treenode2)");
	System.out.println("Largest BST in treenode2 : " + treenode2.findLargestBST().val);

	return;
    }
}
