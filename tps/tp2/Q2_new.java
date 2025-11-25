package ift2015_a25_tp2;
import java.util.*;

public class Q2_new {
    
    // Node class represents one employee in the hierarchy
    class Node {
        String name;
        Node boss = null;
        int level;
        List<Node> reports = new ArrayList<>();
        
        Node(String name){
	    this.name = name;
	}

	Node(String name, int level) {
            this.name = name;
            this.level = level;
        }
    }
    
    Node root; // top-most boss
    Map<String, Node> nameToNode = new HashMap<>(); // maps employee names to nodes for quick look-up
    
    // 1. Add an employee to the hierarchy
    public void addEmployee(String name, int level, String bossName) {
        if (nameToNode.containsKey(name)) {
            System.out.println("Employee " + name + " already exists!");
            return;
        }
        Node newEmployee = new Node(name, level);
        nameToNode.put(name, newEmployee);
        
        if (level == 1) {
            if (root != null) {
                System.out.println("Root already exists!");
                return;
            }
            root = newEmployee;
        } else {
            if (bossName == null || !nameToNode.containsKey(bossName)) {
                System.out.println("Boss " + bossName + " not found!");
                return;
            }
            
            Node boss = nameToNode.get(bossName);
            if (boss.level != level - 1) {
                System.out.println("Boss level mismatch!");
                return;
            }
            
            newEmployee.boss = boss;
            boss.reports.add(newEmployee);
        }
    }
    
    // 2. Print chart recursively
    public void printChart(Node root) {
        printChartHelper(root, "");
    }
    
    private void printChartHelper(Node node, String prefix) {
        if (node == null) return;
        
        System.out.println(prefix + node.name);
        
        for (int i = 0; i < node.reports.size(); i++) {
            Node report = node.reports.get(i);
            String newPrefix;
            if (i == node.reports.size() - 1) {
                // Last report - use different prefix
                newPrefix = prefix + "    ";
            } else {
                newPrefix = prefix + "|   ";
            }
            System.out.print(prefix + "|-- ");
            printChartHelper(report, newPrefix);
        }
    }
    
    // 3. Move employee under new boss
    public void move(String employee, String boss) {
        if (!nameToNode.containsKey(employee) || !nameToNode.containsKey(boss)) {
            System.out.println("Employee or boss not found!");
            return;
        }
        
        Node empNode = nameToNode.get(employee);
        Node newBossNode = nameToNode.get(boss);
        
        // Remove from current boss
        if (empNode.boss != null) {
            empNode.boss.reports.remove(empNode);
        }
        
        // Add to new boss
        empNode.boss = newBossNode;
        newBossNode.reports.add(empNode);
        empNode.level = newBossNode.level + 1;
        
        // Update levels of all reports recursively
        updateLevels(empNode);
    }
    
    private void updateLevels(Node node) {
        for (Node report : node.reports) {
            report.level = node.level + 1;
            updateLevels(report);
        }
    }
    
    // 4. Remove employee and reassign reports to a new boss
    public void remove(String employee, String boss) {
        if (!nameToNode.containsKey(employee) || !nameToNode.containsKey(boss)) {
            System.out.println("Employee or boss not found!");
            return;
        }
        
        Node empNode = nameToNode.get(employee);
        Node newBossNode = nameToNode.get(boss);
        
        if (empNode == root) {
            System.out.println("Cannot remove root!");
            return;
        }
        
        // Reassign all reports to new boss
        for (Node report : new ArrayList<>(empNode.reports)) {
            move(report.name, boss);
        }
        
        // Remove from current boss
        if (empNode.boss != null) {
            empNode.boss.reports.remove(empNode);
        }
        
        // Remove from map
        nameToNode.remove(employee);
    }
    
    // 5. Print chain of supervisors
    public void printAllBoss(String employee) {
        if (!nameToNode.containsKey(employee)) {
            System.out.println("Employee not found!");
            return;
        }
        
        Node current = nameToNode.get(employee);
        List<String> bosses = new ArrayList<>();
        
        while (current != null) {
            bosses.add(current.name);
            current = current.boss;
        }
        
        // Print in reverse order (from employee to root)
        for (int i = 0; i < bosses.size(); i++) {
            System.out.print(bosses.get(i));
            if (i < bosses.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }

    // 6. Print all levels using queue        
    public void printLevelsQueue(Node root) {
        if (root == null) return;
        
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        
        int currentLevel = 1;
        List<String> currentLevelEmployees = new ArrayList<>();
        
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            
            if (current.level > currentLevel) {
                // Print previous level
                System.out.println(currentLevel + ": " + currentLevelEmployees);
                currentLevelEmployees.clear();
                currentLevel = current.level;
            }
            
            currentLevelEmployees.add(current.name);
            
            // Add all reports to queue
            for (Node report : current.reports) {
                queue.add(report);
            }
        }
        
        // Print the last level
        if (!currentLevelEmployees.isEmpty()) {
            System.out.println(currentLevel + ": " + currentLevelEmployees);
        }
    }
    
    // 7. Get lower common boss
    public String lowestCommonBoss(String e1, String e2) {
        if (!nameToNode.containsKey(e1) || !nameToNode.containsKey(e2)) {
            return null;
        }
        
        Node node1 = nameToNode.get(e1);
        Node node2 = nameToNode.get(e2);
        
        // Get paths to root for both employees
        List<Node> path1 = getPathToRoot(node1);
        List<Node> path2 = getPathToRoot(node2);
        
        // Find the first common boss (starting from root)
        int i = path1.size() - 1;
        int j = path2.size() - 1;
        Node lcb = null;
        
        while (i >= 0 && j >= 0 && path1.get(i) == path2.get(j)) {
            lcb = path1.get(i);
            i--;
            j--;
        }
        
        return lcb != null ? lcb.name : null;
    }
    
    private List<Node> getPathToRoot(Node node) {
        List<Node> path = new ArrayList<>();
        while (node != null) {
            path.add(node);
            node = node.boss;
        }
        return path;
    }
    
    // some tests
    public static void main(String[] args) {
        Q2_new h = new Q2_new();
        
        // add to hierarchy
        h.addEmployee("Claude", 1, null); // Claude is the root boss at level 1.
        h.addEmployee("Bob", 2, "Claude"); // Bob reports to Claude.
        h.addEmployee("Elaine", 2, "Claude"); // Elaine also reports to Claude.
        h.addEmployee("Alice", 3, "Bob"); // Alice reports to Bob.
        h.addEmployee("David", 3, "Elaine"); // David reports to Elaine.
        
        // print the hierarchy chart
        System.out.println("Initial hierarchy:");
        h.printChart(h.root);
        
        // reassign Alice to Elaine
        System.out.println("\nAfter moving Alice to Elaine:");
        h.move("Alice", "Elaine");
        h.printChart(h.root); 
        
        // remove Bob and reassign his reports to Elaine
        System.out.println("\nAfter removing Bob:");
        h.remove("Bob", "Elaine");
        h.printChart(h.root);
        
        // print chain from Alice up to root
        System.out.println("\nBoss chain for Alice:");
        h.printAllBoss("Alice");
        
        // print all levels 
        System.out.println("\nEmployees by level:");
        h.printLevelsQueue(h.root);
        
        // lowest common boss between Alice and David
        System.out.println("\nLowest common boss between Alice and David:");
        System.out.println(h.lowestCommonBoss("Alice", "David"));
    }
}
