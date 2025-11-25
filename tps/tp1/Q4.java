package ift2015_a25_tp1;

public class Q4 {
	public static Stack names = new Stack(10);

	public static Node rm_mid(Node head) {
		// TODO
		if (head.next == null) {
			names.push(head.value);
		}
		int mid_idx;
		Node prev_node = new Node("");
		Node mid_node = new Node("");
		Node next_node = new Node("");
		// Get list length
		int list_length = head.length_iter();
		// Get list middle index
		if (list_length % 2 == 0) {
			mid_idx = list_length / 2;
		} else {
		// type cast float -> int equivalent to floor division
			mid_idx = (int) (list_length / 2);
			mid_idx++;
		}
		// DEBUG
		System.out.println("mid_idx: " + mid_idx);
		// Iterate over list
		Node cur_node = head;
		for (int i=1; i<list_length; i++) {
			if (i > 1) { cur_node = cur_node.next; }
			// DEBUG
			System.out.println("At i:" + i + " node is " + cur_node.value);
			// Get node at idx `mid_idx -1`
			if (i == mid_idx - 1) { prev_node = cur_node; continue; }
			// Get node at idx `mid_idx`
			if (i == mid_idx) { mid_node = cur_node; continue; }
			// Get node at idx `mid_idx+1`
			if (i == mid_idx + 1) {next_node = cur_node; break; }
		}
		// Remove mid_node and add it to `names`
		prev_node.next = next_node;
		names.push(mid_node.value);
		// Return head
		return head;
	}


	public static Node rm_k_end_iter(Node head, int k) {
		// TODO
		// Given list length 'N', remove element N-k and push it to stack
		// Get list length
		int list_length = head.length_iter();
		// DEBUG
		System.out.println("Len of list " + list_length);
		int idx_to_remove = list_length - k;
		Node prev_node;
		Node to_remove_node;
		Node next_node;
		Node cur_node = head;
		// If node to remove is not valid
		if (idx_to_remove < 0) {
			names.push("???");
			return head;
		}
		// DEBUG
		System.out.println("Removing element at index " + idx_to_remove );
		// If node to remove is first
		if (idx_to_remove == 0) {
			names.push(cur_node.value);
			head = cur_node.next;
			cur_node.next = null;
			return head;
		}
		// Traverse list until index N-k-1 (node before node N-k to be removed)
		for (int i=0; i<idx_to_remove-1; i++) {
			cur_node = cur_node.next;
		}
		prev_node = cur_node;
		to_remove_node = prev_node.next;
		next_node = to_remove_node.next;
		// Point prev_node (N-k-1) to next_node (N-k+1)
		prev_node.next = next_node;
		// Push removed node to stack
		names.push(to_remove_node.value);
		// Return head
		return head;
	}
	
	public static Node rm_k_end_rec(Node head, int k) {
	//int k_prime = 0;
	Node cur_node = head;
	// DEBUG
	System.out.println("At node " + cur_node.value);
	System.out.println("Next node is " + cur_node.next.value);
	while (cur_node.next != null) {
		cur_node = cur_node.next;
		rm_k_end_rec(cur_node, k);
		}
	if (k != -1) {
		k--;
		// DEBUG
		System.out.println("k == " + k);
	} else {
		// DEBUG
		System.out.println("Adding " + cur_node.next.value + " to the stack");
		names.push(cur_node.next.value);
		cur_node.next = cur_node.next.next;
		cur_node.next.next = null;
	}
	return head;
	}
	
/*	public static Node rm_k_end_rec(Node head, int k) {
		// TODO
		// Recuvrsively traverse list until N-k-1
		Node cur_node = new Node("");
		Node prev_node = new Node("");
		Node to_remove_node = new Node("");
		Node next_node = new Node("");
		if (k-1 != 0) {
			cur_node = head.next;
			k--;
			rm_k_end_rec(cur_node, k);
		}
		prev_node = cur_node;
		to_remove_node = prev_node.next;
		next_node = to_remove_node.next;
		// Point prev_node (N-k-1) to next_node (N-k+1)
		prev_node.next = next_node;
		// Push removed node to stack
		names.push(to_remove_node.value);
		// Return head
		return head;
	}
*/	
	
	public static Node rm_after_k(Node head, int k) {
		// TODO
		Node cur_node = head;
		Node k_node;
		Node to_rm_node;
		// Get list length
		int list_length = head.length_iter();
		// If k is out of bounds
		if (k >= list_length) {
			names.push("???");
			return head;
		}
		// Traverse list until index N-k-1 (node before node N-k to be removed)
		for (int i=1; i<k; i++) {
			cur_node = cur_node.next;
		}
		k_node = cur_node;
		to_rm_node = k_node.next;
		names.push(to_rm_node.value);
		k_node.next = to_rm_node.next;
		// Return head
		return head;
	}
	
	
	// some tests
	public static void main(String[] args) {
        // Create a list
        Node head = new Node("Alice");
        head.add_iter("Bob");
        head.add_iter("Claude");
        head.add_iter("Duke");
        head.add_iter("Elaine");
        
        Node.print_list(head);
        System.out.println();

        // 1. Remove middle person
        System.out.println("---Remove middle person---");
        head = rm_mid(head);
        Node.print_list(head);
        names.print_stack();
        System.out.println();
        
        // 2. Remove kth from end (iterative)
        System.out.println("---Remove 4th person from end---");
        head = rm_k_end_iter(head, 4);
        Node.print_list(head);
        names.print_stack();
        System.out.println();

        // 3. Remove kth from end (recursive)
        System.out.println("---Remove 4nd person from end---");
        head = rm_k_end_rec(head, 4);
        Node.print_list(head);
        names.print_stack();
        System.out.println();

        // 4. Remove the person after k 
        System.out.println("---Remove person after the first---");
        head = rm_after_k(head, 1);
        Node.print_list(head);
        names.print_stack();
        System.out.println();


    }

}
