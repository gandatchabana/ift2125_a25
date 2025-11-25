package ift2015_a25_tp2;
import java.util.*;

public class Q2 {
	
	// Node class represents one employee in the hierarchy
	class Node {
		String name;
		Node boss = null;
		int level;
		List<Node> reports = new ArrayList<>();
		
		Node(String name, int level) {
			this.name = name;
			this.level = level;
		}
	}
	
	Node root; // top-most boss
	Map<String, Node> nameToNode = new HashMap<>(); // maps employee names to nodes for quick look-up
	
	// 1. Add an employee to the hierarchy
	public void addEmployee(String name, int level, String bossName) {
		// TODO
		// If employee already exists
		if (nameToNode.containsKey(name)) {
			System.out.println("Employee " + name + " already exists in the hierarchy");
			return;
		}
		// If trying to add root whereas root already exists
		if (level == 1 && root != null) {
			System.out.println("A root node " + root.name + " already exists");
			return;
		}
		
		// Adding employee node if boss or not
		if (level == 1) {
			assert bossName == null : "Cannot add a boss to a boss";
			// Set root node
			root = new Node(name, level);
			System.out.println("Node root is now set to " + root.name);
			// Add root to hashmap nameToNode
			nameToNode.put(name, root);
			return;
		} else {
			// Set employee node
			Node employee = new Node(name, level);
			// Get employee's boss in hashmap
			Node boss_node = nameToNode.get(bossName);
			if (boss_node != null) {
				// Add employee to boss `reports` list
				boss_node.reports.add(employee);
				//Add boss to employee boss attribute
				employee.boss = boss_node;
				// Add employee to hashmap nameToNode
				nameToNode.put(name, employee);
			}
		}
	}
	

	// 2. Print chart recursively
	public void printChart(Node root) {
		printChartHelper(root, "");
	}
	
	// Implement helper printChartHelper
	private void printChartHelper(Node node, String prefix) {
		//Safeguard
		if (node == null) return;
		// Print argument node with desired prefix
		// In our case, prefix is ""
		System.out.println(prefix + node.name);

		// Loop over all employees that reports to `node`
		for (int i = 0; i < node.reports.size(); i++) {
			// Recursively get employee own reports
			Node report = node.reports.get(i);
			// Change prefix from that point on
			String newPrefix = prefix + "|  ";
			System.out.print(prefix + "|-- ");
			printChartHelper(report, newPrefix);
		}
	}


	// 3. Move employee under new boss
	public void move(String employee, String boss) {
		// TODO
		//Get employee and new_boss nodes
		Node employee_node = nameToNode.get(employee);
		Node current_boss_node = employee_node.boss;
		Node new_boss_node = nameToNode.get(boss);
		// If any is null
		if (employee_node == null || current_boss_node == null || new_boss_node == null) {
			System.out.print("Couldnt find a node: ");
			System.out.println("employee_node: " + employee_node == null);
			System.out.println("current_boss_node: " + current_boss_node == null);
			System.out.println("new_boss_node: " + new_boss_node == null);
			return;
		}
		// If same boss
		if (current_boss_node == new_boss_node) {
			System.out.println("Warning: same boss, no changes made");
			return;
		}
		// If `employee` is over `boss` (aka boss in employee `reports`)
		if (employee_node.reports.contains(new_boss_node)) {
			System.out.println("Error: supposed boss " + boss + " actually reports to employee" + employee);
			return;
		}
		// Change employee boss to new_boss
		employee_node.boss = new_boss_node;
		// Remove employee from current_boss reports
		current_boss_node.reports.remove(employee_node);
		// Add employee to new_boss reports
		new_boss_node.reports.add(employee_node);
	}
	
	// 4. Remove employee and reassign reports to a new boss
	public void remove(String employee, String boss) {
		// TODO
		if (! nameToNode.containsKey(employee)) {
			System.out.println("Could not find " + employee);
			return;
		}
		if (! nameToNode.containsKey(boss)) {
			System.out.println("Could not find " + boss);
			return;
		}
		Node employee_node = nameToNode.get(employee);
		Node current_boss_node = employee_node.boss;
		Node new_boss_node = nameToNode.get(boss);
		
		// Get `employee` reports
		ArrayList<Node> reports_list = new ArrayList<Node>(employee_node.reports);

		// If `employee` has reports
		if (reports_list.size() > 0) {
			for (Node report : reports_list) {
				String report_name = report.name;
				move(report_name, boss);
				System.out.println("Moved " + report_name + " to new boss " + boss + " reports");
			}
		}

		// Remove employee from nameToNode
		nameToNode.remove(employee);
		System.out.println("Removed employee " + employee + " from nameToNode");
		// Remove employee reports
		assert employee_node.reports.size() <= 0 : "Employee " + employee + " to be removed still have reports!";
		employee_node.boss = null;
		employee_node.level = -1;
		// Remove employee from boss' reports
		current_boss_node.reports.remove(employee_node);
		System.out.println("Removed employee " + employee + " from boss " + current_boss_node.name + " reports");
	}


	// 5. Print chain of supervisors
	public void printAllBoss(String employee) {
		// TODO
		// If employee not found in nameToNode
		if (!nameToNode.containsKey(employee)) {
			System.out.println("Could not find " + employee);
			return;
		}

		// Get employee node
		Node employee_node = nameToNode.get(employee);
		// Instantiate list for bossChain
		List<String> bossChain = new LinkedList<String>();
		// Set current_node to employee's
		Node current_node = employee_node;
		// Add current employee to bossChain
		bossChain.add(employee_node.name);
		// Iterate until root
		while (current_node.boss != null) {
			current_node = current_node.boss;
			bossChain.add(current_node.name);
		}

		// Print the chain in order from employee to root boss
		for (int i = 0; i < bossChain.size(); i++) {
			System.out.print(bossChain.get(i));
			if (i < bossChain.size() - 1) {
				System.out.print(" -> ");
			}
		}
		System.out.println();
	}


	// 6. Print all levels using queue        
	public void printLevelsQueue(Node root) {
		// TODO
		// If hierarchy is empty
		if (root == null) {
			System.out.println("Hierarchy is empty!");
			return;
		}

		// Initialize queue for BFS traversal
		Queue<Node> queue = new LinkedList<>();
		queue.add(root);
		
		// At level 1: only root
		// At level 2: root.reports
		// At level 3: root.reports.reports
		// etc...
		// At each level add all reports

		int currentLevel = 0;
		List<String> levelEmployees = new ArrayList<>();

		// Process nodes level by level using BFS
		while (!queue.isEmpty()) {
			Node current = queue.poll();

			// Check if we moved to a new level
			if (current.level > currentLevel) {
				// Print previous level if it has employees
				if (!levelEmployees.isEmpty()) {
					System.out.println(currentLevel + ": " + levelEmployees);
					levelEmployees.clear();
				}
				currentLevel = current.level;
			}

			// Add current employee to current level list
			levelEmployees.add(current.name);

			// Add all direct reports to queue for processing
			for (Node report : current.reports) {
				queue.add(report);
			}
		}

		// Print the last level after queue is empty
		if (!levelEmployees.isEmpty()) {
			System.out.println(currentLevel + ": " + levelEmployees);
		}
	}


	// 7. Get lower common boss
	public String lowestCommonBoss(String e1, String e2) {
		// TODO
		// If either employee not found
		if (!nameToNode.containsKey(e1) || !nameToNode.containsKey(e2)) {
			System.out.println("Could not find one or both employees");
			return null;
		}
		
		// Get both employee nodes
		Node node1 = nameToNode.get(e1);
		Node node2 = nameToNode.get(e2);

		// The logic of printAllBoss can be useful here
		ArrayList<String> bossChain1 = new ArrayList<String>();
		ArrayList<String> bossChain2 = new ArrayList<String>();
		
		// For e1
		// Set current_node to employee's
		Node current_node = node1;
		// Add current employee to bossChain
		bossChain1.add(node1.name);
		// Iterate until root
		while (current_node.boss != null) {
			current_node = current_node.boss;
			bossChain1.add(current_node.name);
		}
		
		current_node = null;

		// For e2
		// Set current_node to employee's
		current_node = node2;
		// Add current employee to bossChain
		bossChain2.add(node2.name);
		// Iterate until root
		while (current_node.boss != null) {
			current_node = current_node.boss;
			bossChain2.add(current_node.name);
		}

		System.out.println("Employee " + e1 + " : " + bossChain1);
		System.out.println("Employee " + e2 + " : " + bossChain2);

		for (int i=0; i<bossChain1.size(); i++) {
			String e1_boss = bossChain1.get(i);
			String e2_boss = bossChain2.get(i);

			//System.out.println("Checking if " + e1_boss + " and " + e2_boss + " are equal: " + Objects.equals(e1_boss, e2_boss));

			if (Objects.equals(e1_boss, e2_boss)) {
				return e1_boss;
			}
		}
		
		System.out.println("Could not find common boss between " + e1 + " and " + e2);
		return null;
	}

	// Helper method for lowestCommonBoss - gets path from node to root
	private List<Node> getPathToRoot(Node node) {
		List<Node> path = new ArrayList<>();
		// Traverse up the hierarchy until reaching root
		while (node != null) {
			path.add(node);
			node = node.boss;
		}
		return path;
	}


    // some tests
	public static void main(String[] args) {
        Q2 h = new Q2();
        
        // add to hierarchy
        h.addEmployee("Claude", 1, null); // Claude is the root boss at level 1.
        h.addEmployee("Bob", 2, "Claude"); // Bob reports to Claude.
        h.addEmployee("Elaine", 2, "Claude"); // Elaine also reports to Claude.
        h.addEmployee("Alice", 3, "Bob"); // Alice reports to Bob.
        h.addEmployee("David", 3, "Elaine"); // David reports to Elaine.
        
        // print the hierarchy chart
        System.out.println("------------");
        h.printChart(h.root);
        
        // reassign Alice to Elaine
        System.out.println("reassign Alice to Elaine");
        System.out.println("------------");
        h.move("Alice", "Elaine");
        h.printChart(h.root); 
        
        // remove Bob and reassign his reports to Elaine
        System.out.println("remove Bob and reassign his reports to Elaine");
        System.out.println("------------");
        h.remove("Bob", "Elaine");
        h.printChart(h.root);
        
        // print chain from Alice up to root
        System.out.println("------------");
        h.printAllBoss("Alice");
        
        // print all levels 
        System.out.println("------------");
        h.printLevelsQueue(h.root);
        
        // lowest common boss between Alice and David
        System.out.println("------------");
        System.out.println(h.lowestCommonBoss("Alice", "David"));

	}
	
}
