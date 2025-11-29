package ift2015.tp4;

import java.util.*;

public class Q1 {

	private static class Node {
		int value;
		int[] adjancy;
		Node[] adjancyNodes;

		public Node(int val, int[] adj) {
			this.value = val;
			this.adjancy = adj;
			this.adjancyNodes = new Node[adj.length];
		}

		public Node(int val) {
			this.value = val;
			this.adjancy = new int[0];
			this.adjancyNodes = new Node[0];
		}

		public void addAdjacent(Node nodeToAdd) {
			//if (this.adjancy.contains(nodeToAdd.value)) { return; }
			int[] adjancy_extended = new int[adjancy.length + 1];
			for (int i=0; i<adjancy.length; i++) {
				adjancy_extended[i] = adjancy[i];
			}
			adjancy_extended[adjancy_extended.length - 1] = nodeToAdd.value;
			this.adjancy = adjancy_extended;
			return;
		}
	}

	private static class Graph {
		// Attibute members
		Node[] nodes;
		int[][] adj_mat;
		HashMap<Integer, int[]> hm;

		// Constuctors
		public Graph(Node[] nodes) {
			this.nodes = nodes;
			this.adj_mat = this.get_adj_mat();
			this.hm = graphArrToHashMap(this.adj_mat);

		}

		public Graph(Node[] nodes, int[][] adj_mat) {
			this.nodes = nodes;
			this.adj_mat = adj_mat;
			this.hm = graphArrToHashMap(this.adj_mat);
		}


		// Getters
		private int[][] get_adj_mat() {
			int[][] adj_mat = new int[nodes.length][];
			for (int i=0; i<this.nodes.length; i++) {
				Node node = nodes[i];
				adj_mat[i] = node.adjancy;
			}
			return adj_mat;
		}

		private Node get_node(int node_value) {
			int nodes_length = this.nodes.length;
			Node[] node_candidates = new Node[nodes_length];
			int count = 0;
			// Iterate through graph nodes to gather nodes with value equals to node_value
			for (int i=0; i<nodes_length; i++) {
				if (this.nodes[i].value == node_value) {
					node_candidates[count] = this.nodes[i];
					count++;
				}
			}

			// No node with value `node_value` found
			if (count == 0) {
					System.out.println("ERROR: Could not find node with node value " + node_value);
					return null;
			}
			// More than 1 node with value `node_value` found
			// Prompts the user for selection
			if (count > 1) {
				System.out.println("WARNING: Found multiple nodes with node value " + node_value);
				System.out.println("Select which node to keep: ");
				for (int i=0; i<node_candidates.length; i++) {
					System.out.print("\n" + i + ": " + node_candidates[i] + ", with adjancy: " + node_candidates[i].adjancy);
				}
				Scanner prompt_input = new Scanner(System.in);
				int choice = prompt_input.nextInt();
				return node_candidates[choice];
			}
			// Just 1 node found, return it
			return node_candidates[0];
		}


        private LinkedList<Integer> getCycle(Integer nodeValue, boolean[] onStack, Deque<Integer> pathStack) {
                Deque<Integer> onStackQueue = new LinkedList<Integer>();
                LinkedList<Integer> cycle = new LinkedList<Integer>();
                
                boolean started = false;

                for (int i=0; i<pathStack.size(); i++) {
                        int vertex = pathStack.poll();
                        if (vertex == nodeValue) {
                                started = true;
                        }

                        if (started) {
                                cycle.offer(i);
                        }
                }
                return cycle;
        }

		// Setters
		private void set_node_adj(Node node) {
		/*
		 * Convert node adjancy from list of int to list of Node
		 */
			for (int i=0; i<node.adjancy.length; i++) {
				int node_adj_val = node.adjancy[i];
				Node node_adj = this.get_node(node_adj_val);
				node.adjancyNodes[i] = node_adj;
			}
		}


		// DFS public method
		// Set the variable that will be needed throughout the recursion
		// Handle edge cases
		// Handle graphs with disconnected nodes
		public int[] DFS(Node root, boolean explore_unvisited) {
			//int[this.nodes.length] visited;
			//int[this.nodes.length] dfs_nodes;
			int nodes_length = this.nodes.length;
			//LinkedList<Node> visited = new LinkedList<Node>();
			//Set<Integer> visited = new HashSet<>();
			LinkedList<Integer> dfs_nodes = new LinkedList<Integer>();
            LinkedList<LinkedList<Integer>> foundCycles = new LinkedList<LinkedList<Integer>>();
            boolean[] onStack = new boolean[nodes_length];
            boolean[] visited = new boolean[nodes_length];
			
			// Edge cases
			if (root == null) { return new int[0]; }
			if (!this.hm.containsKey(root.value)) {
				System.out.println("ERROR: Node (" + root + "): " + root.value + " not found in graph");
				System.out.println("DEBUG: Graph nodes: ");
				for (int i=0; i<this.nodes.length; i++) {
					System.out.print(this.nodes[i] + "(" + this.nodes[i].value + ")\t");
				}
				System.out.println("");
				System.out.println(this.hm);
			}
			
			// DFS private method
            this.DFS(root, onStack, visited, foundCycles, dfs_nodes );
			// If nodes disconnected from `root`, explore them
			if (explore_unvisited == true) {
				for (int i=0; i<this.nodes.length; i++) {
					if (!visited[i]) {
						this.DFS(this.nodes[i], onStack, visited, foundCycles, dfs_nodes );
					}
				}

			}

			// Convert dfs_node LinkedList -> int[]
			Object[] dfs_nodes_obj = dfs_nodes.toArray();
			int[] dfs_nodes_arr = new int[dfs_nodes_obj.length];
			for (int i=0; i<dfs_nodes_arr.length; i++) {
				dfs_nodes_arr[i] = (int) dfs_nodes_obj[i];
			}
		    
			return dfs_nodes_arr;
		}


        private void DFS(Node root, boolean[] onStack, boolean[] visited, LinkedList<LinkedList<Integer>> foundCycles, LinkedList<Integer> dfs_nodes, Deque<Integer> pathStack) {
			// If at "leaf" node, visit and add to dfs_nodes
			if (root.adjancy.length == 0) {
				System.out.println("DEBUG: At leaf node, adding to visited and dfs_nodes and returning...");
				visited[root.value] = true;
				dfs_nodes.add(root.value);
				return;
			}
			
			System.out.println("DEBUG: DFS[" + root + "(" + root.value + ")]");
			// Visit node
			visited[root.value] = true;
            onStack[root.value] = true;
            pathStack.push(root.value); 
			System.out.println("DEBUG: Added to visited");
			System.out.println("DEBUG: onStack");

            for (int nodeValue: root.adjancy) {
                if (!visited[nodeValue]) {
                    pathStack = new ArrayDeque<Integer>();
                    this.DFS(this.get_node(nodeValue), visited, onStack, foundCycles, dfs_nodes, pathStack);
                } else if (onStack[nodeValue]) {
                    // Found a cycle
                    LinkedList<Integer> cycle = this.getCycle(nodeValue, onStack, pathStack);
                    foundCycles.offer(cycle);
                }
            }

            onStack[root.value] = false;
            pathStack.pop();
            dfs_nodes.add(root.value);

            return;


			// // Get adjancy nodes
			//int[] node_adjancy = root.adjancy;
			// // Iterate through adjancy until spent
			//for (int i=0; i<node_adjancy.length; i++) {
			//	int next_node_value = node_adjancy[i];
			//	// If next_node_value is not in graph
			//	if (!hm.containsKey(next_node_value)) {
			//			System.out.println("ERROR: Graph does not contain node " + next_node_value);
			//			continue;
			//	}
			//	// Get node through its value
			//	Node next_node = this.get_node(next_node_value);
			//	// If node has already been visited, move forward to next loop
			//	if (visited[next_node.value]) {
			//			System.out.println("DEBUG: Node " + next_node + "(" + next_node.value + ") " + "already visited, skipping...");
			//			continue;
			//	}
			//	// Else move to DFS call on the node
			//	System.out.println("DEBUG: Calling DSF on node " + next_node + "(" + next_node.value + ")");
			//	this.DFS(next_node, visited, dfs_nodes);
			//}
			// // Add to dfs_nodes
			//System.out.println("DEBUG: Adding node " + root + "(" + root.value + ") to dsf_nodes");
			//dfs_nodes.add(root.value);
			//return;
		}
	}


	private static HashMap<Integer, int[]> graphArrToHashMap(int[][] graph) {
	/*
	 * Convert an array of arrays of int to a HashMap<Integer, int[]>
	 */
	HashMap<Integer, int[]> hm = new HashMap<Integer, int[]>(graph.length);
	for (int node_idx=0; node_idx<graph.length; node_idx++) {
	//for (int[] node : graph) {
		if (hm.containsKey(node_idx)) {
			System.out.println("DEBUG: HashMap already contains " + node_idx);
			System.out.println("Appending...");
			int[] adjacent_nodes = graph[node_idx];
			int[] current_adjacent_nodes = hm.get(node_idx);
			// Concat two lists
			int[] all_nodes = new int[adjacent_nodes.length + current_adjacent_nodes.length];
			for (int idx=0; idx<adjacent_nodes.length; idx++) { all_nodes[idx] = adjacent_nodes[idx]; }
			for (int idx=0; idx<current_adjacent_nodes.length; idx++) { all_nodes[adjacent_nodes.length + idx] = current_adjacent_nodes[idx]; }
			hm.put(node_idx, all_nodes);
		}
		hm.put(node_idx, graph[node_idx]);
	}
	return hm;
	}


	private static boolean DFS(int[][] graph_arr, boolean return_cycles) {
		return false;
	}


	public static boolean hasCycle(int[][] graph) {
		// TODO
		return false;
	}

	public static List<List<Integer>> findAllCycles(int[][] graph) {
		// TODO
		return new ArrayList<>();
	}

	public static List<List<Integer>> stronglyConnectedComponents(int[][] graph) {
		// TODO
		return new ArrayList<>();
	}

	public static int countReachableNodes(int[][] graph, int start) {
		// TODO
		return 0;
	}

	public static List<Integer> findBridgeNodes(int[][] graph) {
		// TODO
		return new ArrayList<>();
	}

	public static Map<Integer, Integer> getFinishingTimes(int[][] graph) {
		// TODO
		return new HashMap<>();
	}

	public static boolean canInstallAll(int[][] graph, List<Integer> broken) {
		// TODO
		return false;
	}

	public static List<Integer> findMinimalDependencySet(int[][] graph, int target) {
		// TODO
		return new ArrayList<>();
	}

	public static int longestDependencyChain(int[][] graph) {
		// TODO
		return 0;
	}

	public static List<Integer> findAllSourceNodes(int[][] graph) {
		// TODO
		return new ArrayList<>();
	}


	public static void main(String[] args) {
	
	Q1 graph_q1 = new Q1();
	int[][] adj = { {1, 2}, {0, 2}, {0, 1} };
	
	Q1.Node node_0 = new Node(0);
	Q1.Node node_1 = new Node(1);
	Q1.Node node_2 = new Node(2);

	Q1.Node[] nodes = {node_0, node_1, node_2};
	Q1.Graph graph = new Graph(nodes, adj);

	System.out.println("DFS (node_1)");
	int[] dfs_arr = graph.DFS(node_1, true);
	System.out.print("[");
	for (int i=0; i<dfs_arr.length; i++) { System.out.print(dfs_arr[i] + ", "); }
	System.out.println("]");
	return;
	}

}
