#!usr/bin/env python3

from typing import List, Union

class Node:
    def __init__(self, value:int, adj:List=None):
        self.value = value
        self.adj = [ Node(node) if isinstance(node, int) else node for node in adj ] if adj is not None else []


class Graph:
    def __init__(self, nodes: List[Node], adj_mat: List[List[Union[int|Node]]] = None):
        self.nodes = nodes
        self.adj_mat = adj_mat if adj_mat else self._get_adj_mat(nodes)
        self._populate_graph()


    def _get_adj_mat(self, nodes):
        adj_mat = []
        for node in nodes:
            adj_mat.append(node.adj)
        return adj_mat


    def _populate_graph(self):
        if self.adj_mat:
            pass


    def DFS(self, root: Node = None):
        visited = []
        dfs_nodes = []

        if root is None:
            root = self.nodes[0]
        if root not in self.nodes:
            raise Exception(f"Node {root} is not valid")

        self._DFS(root, visited, dfs_nodes)
        return dfs_nodes


    def DFS2(self, root: Node = Node, explore_unvisited: bool = False):
        visited = []
        dfs_nodes = []

        if root is None:
            root = self.nodes[0]
        if root not in self.nodes:
            raise Exception(f"Node {root} is not valid")

        self._DFS(root, visited, dfs_nodes)
        
        if explore_unvisited == True:
            # For possible remaining unvisited nodes (detached nodes from root)
            if (len(visited) != len(self.nodes)):
                nodes_left = self.nodes.copy()
                for node_idx, node in enumerate(nodes_left):
                    if node not in visited:
                        to_visit = nodes_left.pop(node_idx)
                        self._DFS(to_visit, visited, dfs_nodes)

        return dfs_nodes


    def _DFS(self, root: Node, visited: List, dfs_nodes: List):
        # Get iterator of root
        if root.adj is None or len(root.adj) == 0:
            visited.append(root)
            dfs_nodes.append(root)
            return

        root_adj_iter = iter(root.adj)
        visited.append(root)
        while True:
            try:
                root_adj_node = next(root_adj_iter)
                if root_adj_node in visited: continue
                self._DFS(root_adj_node, visited, dfs_nodes)
            except StopIteration:
                break

        dfs_nodes.append(root)
        return
