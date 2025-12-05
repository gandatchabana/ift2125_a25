/*
 * Ganda Rioult Tchabana (20125185)
 * Jean-Philippe Salis Routhier (20240919)
*/

/*
 * ATTESTATION D’INTÉGRITÉ ACADÉMIQUE
 *
 * [ ] Je certifie n’avoir utilisé aucun outil d’IA générative
 *     pour résoudre ce problème.
 *
 * [X] J’ai utilisé un ou plusieurs outils d’IA générative.
 *
 * Outil utilisé :
 * - Mistral AI
 *
 * Raisons de l’utilisation :
 * Aide à la compréhension des items findBridgeNodes et longestDependencyChain
 * Aide à la compréhension de l'organisation des classes (nestée, static)
 *
*/

import java.util.*;



public class Q1 {
				private static class Graph {
								// Attributes
								Node[] nodes;
								int numNodes;
								// finishOrder attribute
								int[] stepOrder;
								int stepCount;
								int[] finishOrder;
								int finishCount;
								// Cycle attributes
								private List<List<Integer>> cycles = new ArrayList<>();
								// SCC attributes
								private List<List<Integer>> SCCs = new ArrayList<>();
								private int[] disc;
								private int[] lowlink;
								private Deque<Integer> sccStack;
								private int discCount;

								// Utils functions to convert int|boolean[] <==> List
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

												// Node constructor
												public Node(int val) {
																this.value = val;
																this.adjancy = new LinkedList<Integer>();
												}

												public Node(int val, int[] adj) {
																this.value = val;
																this.adjancy = arrToLinkedList(adj);
												}
								}

								// Graph constructor
								public Graph(int[][] graphArr) {
												this.nodes = arrToNodes(graphArr);
												this.numNodes = this.nodes.length;
												this.finishOrder = new int[numNodes];
												this.finishCount = 0;
												this.stepOrder = new int[numNodes];
												this.stepCount = 1;
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
								/*
								 * Transform an adjancy array int[][] to Node[]
								 */
								private Node[] arrToNodes(int[][] graphArr) {
												nodes = new Node[graphArr.length];
												for (int i=0; i<graphArr.length; i++) {
																nodes[i] = new Node(i, graphArr[i]);
												}
												return nodes;
								}

								/*
								 * Get Node from value
								 */
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

								/*
								 * Return the cycle as a List<Integer> composed of Node node with value `nodeValue`
								 * Triggered after a node is seen again on `onStack`
								 */
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

												// Sort the cycle in ascending order
												Collections.sort(cycle);
												return cycle;
								}

								/*
								 * Sort a List<List<Integer>> by ascending number of elements in each List<Integer>
								 */
								public void sortCyclesByElemCount(List<List<Integer>> supList) {
												class NumElemComparator implements Comparator<List<Integer>> {
																public int compare(List<Integer> list1, List<Integer> list2) {
																				return list1.size() - list2.size();
																}
												}

												Collections.sort(supList, new NumElemComparator());
								}

								/*
								 * Return the SCC as List<Integer> composed of Node `node`
								 * Triggered after discovery index of `node` is equals its lowlink index,
								 * effectively finding the head of the component
								 */
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
								 * Start DFS from node `root`
								 * Set the variables that will be needed throughout the recursion
								 * Handle graphs with disconnected nodes with boolean `explore_unvisited` set to true
								 */
								public int[] DFS(Node root, boolean explore_unvisited) {
												int nodes_length = this.nodes.length;
												boolean[] onStack = new boolean[nodes_length];
												boolean[] visited = new boolean[nodes_length];
												List<List<Integer>> foundCycles = new LinkedList<>();
												List<List<Integer>> foundSCCs = new LinkedList<>();
												// SCC
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

												// Sort foundCycles in ascending order for number of elemets per list
												sortCyclesByElemCount(foundCycles);

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


								/*
								 * DFS private method
								 * Keep track of different variables needed for cycle detection, SCC detections, discoverying&finishing orders
								 */
								private void DFS(Node root, boolean[] onStack, boolean[] visited, List<List<Integer>> foundCycles, List<List<Integer>> foundSCCs, LinkedList<Integer> dfs_nodes, Deque<Integer> pathStack) {
												// If at "leaf" node, visit and add to dfs_nodes
												if (root.adjancy.size() == 0) {
																System.out.println("DEBUG: At leaf node...");
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
												this.stepCount++;
												// Printing stuff for DEBUG
												System.out.println("DEBUG(DFS): Added to visited " + root.value);
												System.out.println("DEBUG(DFS): onStack " + root.value);
												System.out.println("DEBUG(DFS): Pushed onto sccStack " + root.value);
												System.out.println("DEBUG(DFS): sccStack " + this.sccStack);
												System.out.println("DEBUG(DFS): Incrementing discovery count " + this.discCount);
												System.out.println("DEBUG(DFS): discovery array  " + arrToLinkedList(this.disc));
												System.out.println("DEBUG(DFS): lowlink array " + arrToLinkedList(this.lowlink));

												if (root.adjancy.size() > 0) {
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
																this.stepCount++;
														}

														// SCC Head node
														if (this.lowlink[root.value] == this.disc[root.value]) {
																System.out.println("DEBUG(SCC): Found head node of SCC");
																List<Integer> scc = this.getSCC(root, onStack);
																System.out.println("DEBUG(SCC): Adding SCC " + scc + " to foundSCCs");
																foundSCCs.add(scc);
														}
												}
												onStack[root.value] = false;
												int pathStackPopped = pathStack.pop();
												//int sccStackPopped = sccStack.pop();
												this.finishOrder[root.value] = this.finishCount;
												this.stepOrder[root.value] = this.stepCount;
												this.finishCount++;
												dfs_nodes.add(root.value);
												System.out.println("DEBUG(DFS): Removed " + root.value + " from onStack");
												System.out.println("DEBUG(DFS): Popped " + pathStackPopped + " from pathStack");
												//System.out.println("DEBUG(DFS): Popped " + sccStackPopped + " from sccStack");
												System.out.println("DEBUG(DFS): Added " + root.value + " to dfs_nodes");
												System.out.println("DEBUG(DFS): Added " + root.value + " to finishOrder");
												System.out.println("DEBUG(DFS): Incrementing finishCount: " + this.finishCount);

												return;
								}

								// Simple DFS for bridge detection
								private void bridgeDFS(int u,
												int parent,
												int time,
												boolean[] visited,
												int[] disc,
												int[] lowlink,
												List<Integer> bridges) {

										visited[u] = true;
										disc[u] = lowlink[u] = ++time;

										for (int v : this.nodes[u].adjancy) {
												if (!visited[v]) {
														bridgeDFS(v, u, time, visited, disc, lowlink, bridges);
														//lowlink[u] = Math.min(lowlink[u], lowlink[v]);
														lowlink[u] = lowlink[u] < lowlink[v] ? lowlink[u] : lowlink[v];

														// If the only way from v to an ancestor of u is the edge u‑v,
														// then u is a bridge (in the undirected sense).
														if (lowlink[v] > disc[u]) {
																bridges.add(u);
																System.out.println("DEBUG(bridgeDFS): bridge found at vertex " + u);
														}
												} else if (v != parent) {
														// Back‑edge (undirected) – update lowlink[u]
														//lowlink[u] = Math.min(lowlink[u], disc[v]);
														lowlink[u] = lowlink[u] < disc[v] ? lowlink[u] : disc[v];
												}
										}
								}

								private Map<Integer, Integer> computeFinishingTimes() {
										Map<Integer, Integer> ft = new LinkedHashMap<Integer, Integer>();
										this.DFS(this.nodes[0], true);
										for (int i=0; i<this.numNodes; i++) {
												ft.put(i, this.stepOrder[i]);
										}
										return ft;
								}


								private int[] computeIndegrees() {
										int[] indeg = new int[this.numNodes];
										for (int i=0; i<this.numNodes; i++) {
												int indegNode=0;
												List<Integer> adjExcludeNode = new LinkedList<Integer>();
												// Populate adjExcludeNode with all adjancies but self adjancy
												for (Node adjNode : this.nodes) {
														if (adjNode.value == i) {continue;}
														else {
																List<Integer> adjNodeAdjArr = adjNode.adjancy;
																if (adjNodeAdjArr.size() < 1) {continue;}
																for (int adjNodeElem : adjNodeAdjArr) { adjExcludeNode.add(adjNodeElem); }
														}
												}
												for (int elem : adjExcludeNode) {
														if (elem == i) { indegNode++; }
												}
												indeg[i] = indegNode;
										}
										return indeg;
								}
				}


				public static boolean hasCycle(int[][] graph) {
								Graph graphObj = new Graph(graph);
								graphObj.DFS(graphObj.nodes[0], true);
								if (graphObj.cycles.size() > 0) {
												return true;
								}
								return false;
				}


				public static List<List<Integer>> findAllCycles(int[][] graph) {
								Graph graphObj = new Graph(graph);
								graphObj.DFS(graphObj.nodes[0], false);
								return graphObj.cycles;
				}


				public static List<List<Integer>> stronglyConnectedComponents(int[][] graph) {
								Graph graphObj = new Graph(graph);
								graphObj.DFS(graphObj.nodes[0], true);
								return graphObj.SCCs;
				}

				public static int countReachableNodes(int[][] graph, int start) {
								Graph graphObj = new Graph(graph);
								graphObj.DFS(graphObj.nodes[start], false);
								return graphObj.discCount;
				}

				public static List<Integer> findBridgeNodes(int[][] graph) {
						Graph graphObj = new Graph(graph);
						int[] disc = new int[graphObj.numNodes];
						int[] lowlink  = new int[graphObj.numNodes];
						boolean[] visited = new boolean[graphObj.numNodes];
						boolean[] parent = new boolean[graphObj.numNodes];   // true if vertex has a parent
						List<Integer> bridges = new LinkedList<>();

						// Initialise disc to -1 (unvisited)
						Arrays.fill(disc, -1);
						int time = 0;

						for (int i = 0; i < graphObj.numNodes; i++) {
								if (!visited[i]) {
										graphObj.bridgeDFS(i, -1, time, visited, disc, lowlink, bridges);
								}
						}
						System.out.println("DEBUG(findBridgeNodes): bridges = " + bridges);
						return bridges;
				}

				public static Map<Integer,Integer> getFinishingTimes(int[][] graph) {
						Graph graphObj = new Graph(graph);
						Map<Integer,Integer> ft = graphObj.computeFinishingTimes();
						System.out.println("DEBUG(getFinishingTimes): " + ft);
						return ft;
				}

				public static boolean canInstallAll(int[][] graph, List<Integer> broken) {
						// To implement
						return false;
				}

				public static List<Integer> findMinimalDependencySet(int[][] graph, int target) {
						Graph graphObj= new Graph(graph);

						// Build the reverse adjacency list (incoming edges)
						List<Integer>[] revAdj = new LinkedList[graphObj.numNodes];
						for (int i=0; i<graphObj.numNodes; i++) {
								revAdj[i] = new LinkedList<Integer>();
						}

						for (int nodeIdx=0; nodeIdx<graphObj.numNodes; nodeIdx++){
								for (int nodeAdj : graphObj.nodes[nodeIdx].adjancy) {
										if (!revAdj[nodeAdj].contains(nodeIdx)) {
												revAdj[nodeAdj].add(nodeIdx);
										}
								}
						}

						// DFS on the reverse graph starting from target
						boolean[] visited = new boolean[graphObj.numNodes];
						Deque<Integer> stack = new ArrayDeque<>();
						stack.push(target);
						visited[target] = true;
						List<Integer> deps = new LinkedList<>();

						while (!stack.isEmpty()) {
								int cur = stack.pop();
								for (int pred : revAdj[cur]) {
										if (!visited[pred]) {
												visited[pred] = true;
												deps.add(pred);
												stack.push(pred);
										}
								}
						}


						// Sort `deps` in ascending order
						Collections.sort(deps, null);

						System.out.println("DEBUG(findMinimalDependencySet): target=" + target +
										", deps=" + deps);
						return deps;
				}

				public static int longestDependencyChain(int[][] graph) {
						// Check for cycles
						// If any exist the longest chain is undefined so return -1
						if (hasCycle(graph)) {
								System.out.println("DEBUG(longestDependencyChain): graph has a cycle");
								System.out.println("DEBUG(longestDependencyChain): Returning -1");
								return -1;
						}

						Graph graphObj = new Graph(graph);

						// Topological order = reverse of finishing times
						List<Integer> topologicalOrder = new LinkedList<>(graphObj.computeFinishingTimes().keySet());
						Collections.reverse(topologicalOrder);

						int[] dep = new int[graphObj.numNodes];
						int best = 0;

						for (int v : topologicalOrder) {
								for (int nb : graphObj.nodes[v].adjancy) {
										if (dep[nb] < dep[v] + 1) {
												dep[nb] = dep[v] + 1;
												//best = Math.max(best, dp[nb]);
												best = best > dep[nb] ? best : dep[nb];
										}
								}
						}

						System.out.println("DEBUG(longestDependencyChain): longest length = " + best);
						return best;
				}


				public static List<Integer> findAllSourceNodes(int[][] graph) {
						Graph graphObj = new Graph(graph);
						int[] indeg = graphObj.computeIndegrees();
						List<Integer> sources = new LinkedList<>();
						for (int i = 0; i < indeg.length; i++) {
								if (indeg[i] == 0) {
										sources.add(i);
								}
						}
						System.out.println("DEBUG(findAllSourceNodes): sources = " + sources);
						return sources;
				}


				public static void main(String[] args) {
				/*
								int[][] graphArr = {
												{1, 2, 3},
												{2, 3},
												{0,3},
												{0}
								};

								Graph graph = new Graph(graphArr);
								System.out.println("Graph " + graph.adjArrToLinkedList(graphArr));
								System.out.println("Num of nodes: " + graph.numNodes);

								//graph.DFS(graph.nodes[0], false);

								//System.out.println("\t\nhasCycle");
								//System.out.println(hasCycle(graphArr));

								//System.out.println("\t\nnumCycles");
								//System.out.println(findAllCycles(graphArr).size());

								//System.out.println("\t\nnumSCCs");
								//System.out.println(stronglyConnectedComponents(graphArr));

								int[][] graphDisArr = {
												{},
												{2},
												{1}
								};
								Graph graphDisconnect = new Graph(graphDisArr);
								System.out.println("GraphDisconnected " + graphDisconnect.adjArrToLinkedList(graphDisArr));
								System.out.println("Num of nodes: " + graphDisconnect.numNodes);


								//System.out.println("\t\ncountReachableNodes(graphDisconnect, start==0)");
								//System.out.println(countReachableNodes(graphDisArr, 0));

								//System.out.println("\t\ngetFinishingTimes(graphDisconnect)");
								//System.out.println(getFinishingTimes(graphDisArr));

								//System.out.println("\t\ncanInstallAll(graph, broken==[0])");
								//List<Integer> broken = new LinkedList<Integer>(Arrays.asList(0));
								//System.out.println(canInstallAll(graphArr, broken));

								//System.out.println("\t\nfindMinimalDependencySet(graphDisArr, target==0)");
								//System.out.println(findMinimalDependencySet(graphDisArr, 0));

								//System.out.println("\t\nlongestDependencyChain(graphDisconnect)");
								//System.out.println(longestDependencyChain(graphDisArr));

								//System.out.println("\t\nfindAllSourceNodes(graphDisconnect)");
								//System.out.println(findAllSourceNodes(graphDisArr));

								return;
				*/
				}

}
