import java.util.*;



public class Graph {
				// Attributes
				Node[] nodes;
				int numNodes;
				// Cycle attributes
				private List<List<Integer>> cycles = new ArrayList<>();
				// SCC attributes
				private List<List<Integer>> SCCs = new ArrayList<>();
				private int[] disc;
				private int[] lowlink;
				private Deque<Integer> sccStack;
				private int discCount = 0;


				private List<Integer> arrToLinkedList(int[] arr) {
								List<Integer> list = new LinkedList<Integer>();
								for (int i=0; i<arr.length; i++){
												list.add(arr[i]);
								}
								return list;
				}

				private List<Boolean> arrToLinkedList(boolean[] arr) {
								List<Boolean> list = new LinkedList<Boolean>();
								for (int i=0; i<arr.length; i++){
												list.add(arr[i]);
								}
								return list;
				}

				private <T> List<T> arrToLinkedList(T[] arr) {
								List<T> list = new LinkedList<T>();
								for (int i=0; i<arr.length; i++){
												list.add(arr[i]);
								}
								return list;
				}

				private List<List<Integer>> adjArrToLinkedList(int[][] adj) {
								List<List<Integer>> adjList = new LinkedList<>();
								for (int i=0; i<adj.length; i++) {
												adjList.add(this.arrToLinkedList(adj[i]));
								}
								return adjList;
				}

				private int[] linkedListToArr(LinkedList<Integer> list) {
								int list_length = list.size();
								int[] arr = new int[list_length];
								for (int i=0; i<list_length; i++) {
												arr[i] = list.get(i);
								}
								return arr;
				}


				// Nested class Node
				private class Node {
								//Attributes
								int value;
								List<Integer> adjancy;

								// Constructors
								public Node(int val) {
												this.value = val;
												this.adjancy = new LinkedList<Integer>();
								}

								public Node(int val, int[] adj) {
												this.value = val;
												this.adjancy = arrToLinkedList(adj);
								}
				}

				// Constructors
				public Graph(int[][] graphArr) {
								this.nodes = arrToNodes(graphArr);
								this.numNodes = this.nodes.length;
								this.cycles = new ArrayList<>();
								this.SCCs = new ArrayList<>();
								this.disc = new int[numNodes];
								this.lowlink = new int[numNodes];
								this.sccStack = new ArrayDeque<>();
								this.discCount = 0;
								// Fill discovery arr with -1 (for not yet visited)
								Arrays.fill(this.disc, -1);
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
								// Get reverse iteration of pathStack
								// ex: pathStack == [2, 1, 0] ==> Iterator[0, 1, 2]

								Iterator<Integer> pathStackIterator = pathStack.descendingIterator();
								while (pathStackIterator.hasNext()) {
												int v = pathStackIterator.next();
												if (v == nodeValue) {
																started = true;
																System.out.println("DEBUG(getCycle): Started cycle ");
												}

												if (started) {
																cycle.addFirst(v);
																System.out.println("DEBUG(getCycle): Added path " + v + "to cycle");
												}
								}
								return cycle;
				}

				public List<Integer> getSCC(Node node, boolean[] onStack, Deque<Integer> pathStack) {
								LinkedList<Integer> scc = new LinkedList<Integer>();
								int nodeValue = node.value;
								int sccStackValue;
								boolean started = false;
								Iterator<Integer> sccStackIterator = sccStack.iterator();
								while (sccStackIterator.hasNext()) {
												int v = sccStackIterator.next();
												if (v == nodeValue) {
																started = true;
																System.out.println("DEBUG(getSCC): Started SCC cycle");
												}
												if (started) {
																scc.addFirst(v);
																System.out.println("DEBUG(getSCC): Added path " + v + "to SCC cycle");
												}
								}
								return scc;
				}

				private List<Integer> getSCC(Node root, boolean[] onStack) {
								List<Integer> scc = new LinkedList<Integer>();
								int rootValue = root.value;
								int nodeValue;
								do {
												nodeValue = sccStack.pop();
												onStack[nodeValue] = false;
												scc.add(nodeValue);
								} while (nodeValue != rootValue);
								return scc;
				}



				/* DFS public method
				 *
				 * Set the variable that will be needed throughout the recursion
				 *Handle edge cases
				 *Handle graphs with disconnected nodes
				 */
				public int[] DFS(Node root, boolean explore_unvisited) {
								int nodes_length = this.nodes.length;
								boolean[] onStack = new boolean[nodes_length];
								boolean[] visited = new boolean[nodes_length];
								List<List<Integer>> foundCycles = new LinkedList<>();
								List<List<Integer>> foundSCCs = new LinkedList<>();
								int[] disc = new int[nodes_length];
								int[] low = new int[nodes_length];
								LinkedList<Integer> dfs_nodes = new LinkedList<Integer>();
								Deque<Integer> pathStack = new LinkedList<Integer>();

								// Edge cases
								if (root == null) { return new int[0]; }


								System.out.println("\t\nDEBUG(DFS): Started DFS on node " + root.value);
								// DFS private method
								this.DFS(root, onStack, visited, foundCycles, foundSCCs, dfs_nodes, pathStack);
								// If nodes disconnected from `root`, explore them
								if (explore_unvisited == true) {
												for (int i=0; i<this.nodes.length; i++) {
																if (!visited[i]) {
																				System.out.println("DEBUG(DFS): Started DFS on disconnected node " + this.nodes[i].value);
																				this.DFS(this.nodes[i], onStack, visited, foundCycles, foundSCCs, dfs_nodes, pathStack);
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
								this.SCCs = foundSCCs;

								return dfs_nodes_arr;
				}


				private void DFS(Node root, boolean[] onStack, boolean[] visited, List<List<Integer>> foundCycles, List<List<Integer>> foundSCCs, LinkedList<Integer> dfs_nodes, Deque<Integer> pathStack) {
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
								// Keep track disc and low values for SCC
								this.sccStack.push(root.value);
								this.disc[root.value] = this.discCount;
								this.lowlink[root.value] = this.discCount;
								this.discCount++;
								// Printing stuff for DEBUG
								System.out.println("DEBUG(DFS): Added to visited " + root.value);
								System.out.println("DEBUG(DFS): onStack " + root.value);
								System.out.println("DEBUG(DFS): Pushed onto sccStack " + root.value);
								System.out.println("DEBUG(DFS): sccStack " + this.sccStack);
								System.out.println("DEBUG(DFS): Incrementing discovery count " + this.discCount);
								System.out.println("DEBUG(DFS): discovery array  " + arrToLinkedList(this.disc));
								System.out.println("DEBUG(DFS): lowlink array " + arrToLinkedList(this.lowlink));

								for (int nodeValue: root.adjancy) {
												if (!visited[nodeValue]) {
																this.DFS(this.getNode(nodeValue), onStack, visited, foundCycles, foundSCCs, dfs_nodes, pathStack);
																// SCC
																System.out.print("DEBUG(SCC): Setting lowlink["  + root.value + "] to ");
																this.lowlink[root.value] = this.lowlink[root.value] < this.lowlink[nodeValue] ? this.lowlink[root.value] : this.lowlink[nodeValue];
																System.out.println(this.lowlink[root.value]);
												} else if (onStack[nodeValue]) {
																// SCC
																System.out.print("DEBUG(SCC): Setting lowlink["  + root.value + "] to ");
																this.lowlink[root.value] = this.lowlink[root.value] < this.disc[nodeValue] ? this.lowlink[root.value] : this.disc[nodeValue];
																System.out.println(this.lowlink[root.value]);
																// Found a cycle
																System.out.println("DEBUG(Cycle): Found cycle");
																LinkedList<Integer> cycle = this.getCycle(nodeValue, onStack, pathStack);
																System.out.println("DEBUG(Cycle): Adding cycle " + cycle + " to foundCycles");
																foundCycles.add(cycle);
												}
								}

								// SCC Head node
								if (this.lowlink[root.value] == this.disc[root.value]) {
												System.out.println("DEBUG(SCC): Found head node of SCC");
												List<Integer> scc = this.getSCC(root, onStack);
												System.out.println("DEBUG(SCC): Adding SCC " + scc + " to foundSCCs");
												foundSCCs.add(scc);
								}
								onStack[root.value] = false;
								int pathStackPopped = pathStack.pop();
								//int sccStackPopped = sccStack.pop();
								dfs_nodes.add(root.value);
								System.out.println("DEBUG(DFS): Removed " + root.value + " from onStack");
								System.out.println("DEBUG(DFS): Popped " + pathStackPopped + " from pathStack");
								//System.out.println("DEBUG(DFS): Popped " + sccStackPopped + " from sccStack");
								System.out.println("DEBUG(DFS): Added " + root.value + " to dfs_nodes");

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


				public static List<List<Integer>> stronglyConnectedComponents(int[][] graph) {
								Graph graphObj = new Graph(graph);

								for (Node nIdx : graphObj.nodes) {
												if (graphObj.disc[nIdx.value] != -1) {
																continue;
												}
												graphObj.DFS(nIdx, false);
								}
								return graphObj.SCCs;
				}

				public static void main(String[] args) {
								int[][] graphArr = {
												{1, 2, 3},
												{2, 3},
												{0,3},
												{0}
								};

								Graph graph = new Graph(graphArr);
								System.out.println("Num of nodes: " + graph.numNodes);
								System.out.println("Graph " + graph.adjArrToLinkedList(graphArr));

								graph.DFS(graph.nodes[0], false);

								System.out.println("\t\nhasCycle");
								System.out.println(graph.hasCycle(graphArr));

								System.out.println("\t\nnumCycles");
								System.out.println(graph.findAllCycles(graphArr).size());

								System.out.println("\t\nnumSCCs");
								System.out.println(graph.stronglyConnectedComponents(graphArr));

								return;
				}

}
