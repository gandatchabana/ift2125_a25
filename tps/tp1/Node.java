package ift2015_a25_tp1;

public class Node {
	String value; // data of the node
	Node next; // pointer to the next node
	
	public Node(String s) {
		value = s;
		next = null;
	}
	
	public Node(String s, Node next) {
		value = s;
		next = next;
	}
	
	public void add_iter(String s) {
		if (this.next == null) {
			this.next = new Node(s);
			return;
		}
		Node n = this;
		Node last = new Node(s);
		while (n.next != null) {
			n = n.next;
		}
		n.next = last;
	}
	
	public void add_rec(String s) {
		Node n = this;
		if (n.next != null) {
			n = n.next;
			n.add_rec(s);
		}
		else {
			Node last = new Node(s);
			n.next = last;
		}
	}
	
	public int length_iter() {
		int len = 1;
		Node n = this;
		while (n.next != null) {
			len++;
			n = n.next;
		}
		return len;
	}
	
	public int length_rec() {
		Node n = this;
		if (n.next == null) {
			return 1;
		} else {
			n = n.next;
			return 1 + n.length_rec();
		}
	}
	
	// Print entire list
    public static void print_list(Node n) {
        System.out.print("List: ");
        while (n != null) {
            System.out.print(n.value + "->");
            n = n.next;
        }
        System.out.println();
    }
    
	// some tests
	public static void main(String[] args) {
        // Create the head node
        Node head = new Node("A");
        
        // Add elements using iterative method
        head.add_iter("B");
        // Add elements using recursive method
        head.add_rec("C");
        // Print list elements
        print_list(head);
        
        // Test length methods
        System.out.println("Length (iterative): " + head.length_iter());
        System.out.println("Length (recursive): " + head.length_rec());
    }
}
