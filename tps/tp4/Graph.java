import java.util.*;



public class Graph {
				// Attributes
				Node[] nodes;
				int numNodes;
				private List<List<Integer>> cycles = new ArrayList<>();

				// Nested class Node
				private static class Node {
								//Attributes
								int value;
								LinkedList<Integer> adjancy;

								// Constructors
								public Node(int val) {
												this.value = val;
												this.adjancy = new LinkedList<Integer>();
								}

								public Node(int val, int[] adj) {
												this.value = val;
												this.adjancy = this.arrToLinkedList(adj);
								}

								private LinkedList<Integer> arrToLinkedList(int[] arr){
												LinkedList<Integer> list = new LinkedList<Integer>();
												for (int i=0; i<arr.length; i++){
																list.add(arr[i]);
												}
												return list;
								}

								private int[] linkedListToArr(LinkedList<Integer> list) {
												int list_length = list.size();
												int[] arr = new int[list_length];
												for (int i=0; i<list_length; i++) {
																arr[i] = list.get(i);
												}
												return arr;
								}
				}

				// Constructors
				public Graph(int[][] graphArr) {
								this.nodes = arrToNodes(graphArr);
								this.numNodes = this.nodes.length;
								this.cycles = new ArrayList<>();
				}


				// Methods
				private Node[] arrToNodes(int[][] graphArr) {
								nodes = new Node[graphArr.length];
								for (int i=0; i<graphArr.length; i++) {
												nodes[i] = new Node(i, graphArr[i]);
								}
								return nodes;
				}

				public Node getNode(int nodeValue) {
								int nodes_length = this.nodes.length;
								Node[] node_candidates = new Node[nodes_length];
								int count = 0;
								// Iterate through graph nodes to gather nodes with value equals to node_value
								for (int i=0; i<nodes_length; i++) {
												if (this.nodes[i].value == nodeValue) {
																node_candidates[count] = this.nodes[i];
																count++;
												}
								}

								// No node with value `nodeValue` found
								if (count == 0) {
												System.out.println("ERROR: Could not find node with node value " + nodeValue);
												return null;
								}

								// More than 1 node with value `nodeValue` found
								// Prompts the user for selection
								if (count > 1) {
												System.out.println("WARNING: Found multiple nodes with node value " + nodeValue);
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

				public LinkedList<Integer> getCycle(Integer nodeValue, boolean[] onStack, Deque<Integer> pathStack) {
								Deque<Integer> onStackQueue = new LinkedList<Integer>();
								LinkedList<Integer> cycle = new LinkedList<Integer>();
								boolean started = false;

								for (int v : pathStack) {
												if (v == nodeValue) {
																started = true;
												}

												if (started) {
																cycle.addFirst(v);
												}
								}
								return cycle;
				}


		/* DFS public method
		 *
		 * Set the variable that will be needed throughout the recursion
		 *Handle edge cases
		 *Handle graphs with disconnected nodes
		*/
				public int[] DFS(Node root, boolean explore_unvisited) {
								int nodes_length = this.nodes.length;
								LinkedList<Integer> dfs_nodes = new LinkedList<Integer>();
								List<List<Integer>> foundCycles = new LinkedList<>();
								boolean[] onStack = new boolean[nodes_length];
								boolean[] visited = new boolean[nodes_length];
								Deque<Integer> pathStack = new LinkedList<Integer>();

								// Edge cases
								if (root == null) { return new int[0]; }
			
								// DFS private method
								this.DFS(root, onStack, visited, foundCycles, dfs_nodes, pathStack);
								// If nodes disconnected from `root`, explore them
								if (explore_unvisited == true) {
												for (int i=0; i<this.nodes.length; i++) {
																if (!visited[i]) {
																				this.DFS(this.nodes[i], onStack, visited, foundCycles, dfs_nodes, pathStack);
																}
												}
								}

								// Convert dfs_node LinkedList -> int[]
								Object[] dfs_nodes_obj = dfs_nodes.toArray();
								int[] dfs_nodes_arr = new int[dfs_nodes_obj.length];
								for (int i=0; i<dfs_nodes_arr.length; i++) {
												dfs_nodes_arr[i] = (int) dfs_nodes_obj[i];
								}

								this.cycles = foundCycles;

								return dfs_nodes_arr;
				}


				private void DFS(Node root, boolean[] onStack, boolean[] visited, List<List<Integer>> foundCycles, LinkedList<Integer> dfs_nodes, Deque<Integer> pathStack) {
								// If at "leaf" node, visit and add to dfs_nodes
								if (root.adjancy.size() == 0) {
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
																this.DFS(this.getNode(nodeValue), visited, onStack, foundCycles, dfs_nodes, pathStack);
												} else if (onStack[nodeValue]) {
																// Found a cycle
																LinkedList<Integer> cycle = this.getCycle(nodeValue, onStack, pathStack);
																foundCycles.add(cycle);
												}
								}

								onStack[root.value] = false;
								pathStack.pop();
								dfs_nodes.add(root.value);

								return;
				}



				public static boolean hasCycle(int[][] graph) {
								// TODO
								Graph graphObj = new Graph(graph);
								graphObj.DFS(graphObj.nodes[0], false);
								if (graphObj.cycles.size() > 0) {
												return true;
								}
								return false;
				}


				public static List<List<Integer>> findAllCycles(int[][] graph) {
								// TODO
								Graph graphObj = new Graph(graph);
								graphObj.DFS(graphObj.nodes[0], false);
								return graphObj.cycles;
				}


				public static void main(String[] args) {
								int[][] graphArr = {
												{1, 2, 3},
												{2, 3},
												{0,3},
												{0}
								};

								Graph graph = new Graph(graphArr);
								System.out.println(graph.numNodes);

								graph.DFS(graph.nodes[0], false);

								System.out.println("hasCycle");
								System.out.println(graph.hasCycle(graphArr));

								System.out.println("numCycles");
								System.out.println(graph.findAllCycles(graphArr).size());

								return;
				}

}
