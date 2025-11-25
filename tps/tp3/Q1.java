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



public class Q1 {
    

    private class CommitNode {
        int timestamp;
        String commitHash;
        String author;
        CommitNode left, right;
        
        public CommitNode(int timestamp, String commitHash, String author) {
		this.timestamp = timestamp;
		this.commitHash = commitHash;
		this.author = author;
		this.left = null;
		this.right = null;
        }
    }
    
    private CommitNode root;
    public Queue<CommitNode> nodes;

    public Q1() {
	   root = null;
	   nodes = null;
    }
    

    private Queue<CommitNode> levelOrderTraversal(CommitNode root, int level) {
	Queue<CommitNode> lorder_q = new LinkedList<CommitNode>(); 
	Queue<CommitNode> nodes = new ArrayDeque<CommitNode>(1000);
	if (root == null) {return null;}
	if (root.left == null && root.right == null) { nodes.offer(root); return nodes; }
	lorder_q.offer(root);
	//if (root.left != null)  { lorder_q.offer(root.left); }
	//if (root.right != null) { lorder_q.offer(root.right); }
	while (!lorder_q.isEmpty()) {
		//System.out.print("lOrder: [" );
		//for (CommitNode node : lorder_q) { System.out.println(node.commitHash); }
		CommitNode cur_node = lorder_q.poll();
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


    public void displayNodes() {
	System.out.print("Nodes : [");
	for (CommitNode node : nodes) { System.out.print("(" + node.commitHash + "," + node.timestamp + ") "); }
	System.out.println("]");
    }


    public void insert(int timestamp, String commitHash, String author) {
        // TODO:
	if (root == null) {
		System.out.println("DEBUG: Inserting node " + commitHash + "(" + timestamp + ", " + author + ") as root node");
		root = new CommitNode(timestamp, commitHash, author);
		return; 
	}
	// Init node
	CommitNode to_insert = new CommitNode(timestamp, commitHash, author);
	CommitNode cur_node = root;
	CommitNode parent_cur_node = null;
    	//while (cur_node.left != null || cur_node.right != null) {
    	//Traverse the BST until leaf
	while (cur_node != null) {
		parent_cur_node = cur_node;
		if (to_insert.timestamp < cur_node.timestamp) {
			cur_node = cur_node.left;
		} else {
			cur_node = cur_node.right;
		}
	}
	if (to_insert.timestamp < parent_cur_node.timestamp) {
		parent_cur_node.left = to_insert;
	} else {
		parent_cur_node.right = to_insert;
	}
	// Recompute the levelOrder nodes
	nodes = levelOrderTraversal(root, 0);
    }
    

    public String findCommit(int timestamp) {
        // TODO:
	if (root == null) { return null; }
	if (root.timestamp == timestamp) { return root.commitHash; }
	
	CommitNode cur_node = root;
	while (cur_node != null) {
		if (timestamp == cur_node.timestamp) {
			return cur_node.commitHash;
		}else if (timestamp < cur_node.timestamp) {
	cur_node = cur_node.left;
		} else {
			cur_node = cur_node.right;
		}
	}
	System.out.println("ERROR: Could not find commit with given timestamp: " + timestamp);
        return null;
    }

    public List<String> getCommitsBetween(int startTime, int endTime) {
        // TODO: 
        //Queue<CommitNode> lOrder_q = new LinkedList<CommitNode>();
	if (root == null) {return null;}
	//Queue<CommitNode> nodes = levelOrderTraversal(root, 0);
	displayNodes();
	if (nodes == null) { System.out.println("ERROR: Could not find any tree node"); displayTree(); return null;}
	List<String> filteredNodes = new ArrayList<String>();
	for (CommitNode node : nodes) {
		System.out.println("DEBUG: Checking for node " + node.commitHash + " from author " + node.author + " with timestamp " + node.timestamp);
		if (startTime <= node.timestamp && node.timestamp <= endTime) {
			filteredNodes.add(node.commitHash);
			System.out.println("Added node " + node.commitHash + "(" + node.timestamp + ")" + " to the list" );
		}
	}
	System.out.print("Commits: [ ");
	for (String f_node : filteredNodes) { System.out.print(f_node + " "); }
	System.out.println("]");
	return filteredNodes;
    }
    
 
    public String _findNearestCommit(int timestamp) {
	if (root == null) {return null;}
	CommitNode closest = root;

	for (CommitNode cur_node : nodes) {
		if ((timestamp - cur_node.timestamp) < (timestamp - closest.timestamp)) {
			closest = cur_node;
			if ((timestamp - closest.timestamp) == 0) {return closest.commitHash;}
		}
	}
    	return closest.commitHash;
    }


    public String findNearestCommit(int timestamp) {
        // TODO: 
	if (root == null) {return null;}
	//Queue<CommitNode> nodes = levelOrderTraversal(root, 0);
	//List<String> filteredNodes = new ArrayList<String>();
	CommitNode cur_node = root;
	CommitNode closest = root;
	while (cur_node != null) {
		System.out.println("DEBUG: timestamp: " + "(" + timestamp + ")");
		System.out.println("DEBUG: closest: " + closest.commitHash + "(" + closest.timestamp + ")");
		System.out.println("DEBUG: cur_node: " + cur_node.commitHash + "(" + cur_node.timestamp + ")");
		System.out.println("DEBUG: cur_node - timestamp: " + (cur_node.timestamp - timestamp));
		System.out.println("DEBUG: closest - timestamp: " + (closest.timestamp - timestamp));
		int diff_cur_node = (cur_node.timestamp - timestamp); 
		int diff_closest = (closest.timestamp - timestamp);
		// If the diff is negative, get the absolute value by substracting the magnitude twice
		// Ex : diff = -50;
		// diff - diff - diff = -50 - (-50) - (-50)
		// diff - diff - diff = -50 +   50  +   50
		// diff - diff - diff =     0       +   50
		// diff - diff - diff = 50                 = - (diff)
		if (diff_cur_node < 0) { diff_cur_node = (diff_cur_node - diff_cur_node - diff_cur_node); } 
		if (diff_closest < 0) { diff_closest = (diff_closest - diff_closest - diff_closest);} 
		// If diff at cur_node is less than current best (closest), then replace closest
		if (diff_cur_node < diff_closest) {
			System.out.println("Found closer timestamp from node " + cur_node.commitHash + " with timestamp " + cur_node.timestamp);
			closest = cur_node;
			// If closest == timestamp provided, then it's the best value possible
			if ((closest.timestamp - timestamp) == 0) {break;}
		}
		// Go left subtree
		if (timestamp <= cur_node.timestamp ) {
			cur_node = cur_node.left;
			continue;
		}
		// Go right subtree
		if (timestamp > cur_node.timestamp ) {
			cur_node = cur_node.right;
			continue;
		}
	}
	return closest.commitHash;
    }
    

    public int countCommitsByAuthor(String author) {
        // TODO: 
	//Queue<CommitNode> nodes = levelOrderTraversal(root, 0);
        int count = 0;
	for (CommitNode node : nodes) {
		if (node.author == author) { count++; }
	}
	return count;
    }
    
 
    public String getMostActiveAuthor() {
        // TODO:
	HashMap<String, Integer> authors_commits = new HashMap<String, Integer>();
	//Queue<CommitNode> nodes = levelOrderTraversal(root, 0);
	String greatest_author = null;
	int max_commits = 0;

	for (CommitNode node : nodes) {
		if (! authors_commits.containsKey(node.author)) {
			authors_commits.put(node.author, 1);
		} else {
			int pre_count = authors_commits.get(node.author);
			int count = pre_count + 1;
			authors_commits.put(node.author, count);
		}
	}

	for (Map.Entry<String, Integer> entry : authors_commits.entrySet()) {
		//System.out.println(entry.getKey() + ":" + entry.getValue());
		if (entry.getValue() > max_commits) {
			greatest_author = entry.getKey();
			max_commits = entry.getValue();
		}
	}
	
	System.out.println("Most active author: " + greatest_author);

	return greatest_author;
    }
    
  
    private void deleteFromNode(CommitNode node, int level) {
	if (node == null) {return;}
	level++;
	deleteFromNode(node.left, level);
	deleteFromNode(node.right, level);
	if (level == 1) {
		node.left = null;
		node.right = null;
		// Recomputes levelOrder nodes
		nodes = levelOrderTraversal(node, level);
	} 
	if (level > 1) { 
		System.out.println("Level: " + level + " Removing node " + node.commitHash + " from tree");
		//node.commitHash = null;
		//node.author = null;
		//node.timestamp = -1;
		node.left = null;
		node.right = null;
		node = null;
	}
	return;
    }

    public void revertToCommit(int timestamp) {
        // TODO:
	if (root == null) {return;}
	// If `timestamp` is lower than root, delete the whole tree
	if (timestamp < root.timestamp) {
		deleteFromNode(root, 0);
		return;
	}
	
	// Initialise at root
	CommitNode cur_node = root;
	// Traverse tree until node with strictly greater timestamp
	while (timestamp > cur_node.timestamp) {
		cur_node = cur_node.right;
	}
	// Recursively deletes nodes from cur_node
	deleteFromNode(cur_node, 0);

	// Print current tree
    	displayTree();
    }
    
   
    public List<String> getTimeline() {
        // TODO: 
	List<String> timeline_str = new ArrayList<String>();
	List<CommitNode> timeline_nodes = new ArrayList<CommitNode>();
	for (CommitNode node : nodes) {
		//timeline.add("["+ node.timestamp + "] " + node.commitHash + " - " + node.author);
		timeline_nodes.add(node)
	}
	
	Collections.sort(timeline_nodes, )
	timeline.sort();
	displayList(timeline);
	return timeline;
    }
   

    public void displayTree() {
	System.out.println("Displaying Tree");
	displayTree(root);
    }


    private CommitNode findParent(String commitHash) {
	if (commitHash == null) { return null; }
	if (commitHash == root.commitHash) { return root; }
	// Initalise at root
	//CommitNode cur_node = root;
	CommitNode parent_cur_node = null;
	// Get levelOrder queue
	for (CommitNode cur_node : nodes) {
		CommitNode cur_node_left = cur_node.left;
		CommitNode cur_node_right = cur_node.right;
		if (cur_node_left != null && cur_node_left.commitHash == commitHash) {
			System.out.println("Found parent of node " + commitHash);
			return cur_node;
		}
		if (cur_node_right != null && cur_node_right.commitHash == commitHash) {
			System.out.println("Found parent of node " + commitHash);
			return cur_node;
		}

	}
	System.out.println("Could not find parent of node " + commitHash);
	return null;
    }


    private void displayTree(CommitNode node) {
	if (node == null) {return;}
	CommitNode cur_node = node;
	// Recursively print left node if exists
	displayTree(node.left);
	System.out.println("-----");
	System.out.print("|\t" + cur_node.timestamp);
	System.out.print(" " + cur_node.commitHash);
	System.out.println(" " + cur_node.author);
	System.out.println("-----");
	// Recursively print right node if exists
	displayTree(node.right);
    	return;
    }

    public static void main(String args[]) {
	Q1 commitTree = new Q1();
	
	System.out.println("Inserting commit v4hf2 to the tree");
	commitTree.insert(100, "v4hf2", "besson");
	System.out.println("Inserting commit v3ff2 to the tree");
	commitTree.insert(300, "v3ff2", "tchab");
	System.out.println("Inserting commit v4d22 to the tree");
	commitTree.insert(400, "v4d22", "besson");
	System.out.println("Inserting commit d4dc2 to the tree");
	commitTree.insert(450, "d4dc2", "besson");
	System.out.println("Inserting commit d8f20 to the tree");
	commitTree.insert(640, "d8f20", "tchab");
	System.out.println("Inserting commit d2d02 to the tree");
	commitTree.insert(780, "d2d02", "tchab");
	
	commitTree.displayTree();
	commitTree.displayNodes();

	System.out.println("Searching for commit with timestamp 450...");
	String hash = commitTree.findCommit(450);
	System.out.println("Commit hash for timestamp 450: " + hash);
	
	System.out.println("Searching for commit with timestamp 150...");
	commitTree.findCommit(150);
	
	System.out.println("Parent of d2d02");
	System.out.println(commitTree.findParent("d2d02").commitHash);
	
	System.out.println("Searching for commits between timestamps 200 and 500");
	commitTree.getCommitsBetween(200, 500);
	
	System.out.println("Searching for commit with timestamp close to 299...");
	String closest = commitTree.findNearestCommit(299);
	System.out.println("Closest : " + closest);
	
	System.out.print("Number of commits by author besson: ");
	System.out.println(commitTree.countCommitsByAuthor("besson"));
	
	System.out.println("Number of commits by authors: ");
	commitTree.getMostActiveAuthor();
	
	System.out.println("Inserting commit d8f0o to the tree");
	commitTree.insert(780, "d8f0o", "besson");
	commitTree.displayNodes();

	commitTree.getMostActiveAuthor();

	System.out.println("Reverting to commit v3ff2");
	commitTree.revertToCommit(300);
	
	System.out.print("Level Order nodes: ");
	for (CommitNode node: commitTree.nodes) { System.out.print(node.commitHash + " ");}
	
	return;
    } 
    
}
