package·ift2015_a25_tp2;
import·java.util.*;

	// Instantiate Queue using LinkedList
	Queue<Integer> final_order = new LinkedList<>();
	Queue<Integer> books_queue = new LinkedList<>(books);
	Queue<Integer> distribution_queue = new LinkedList<>(distribution);
	int[] anger = new int[n];
	int anger_total;
	/*
	For example input, order of distribution will be
	person2, person3, person5, person1, person4

	Leading to a total anger of 4
	With anger(p1)=3, anger(p4)=1

	Using Queue for 
	*/

	for (int i=0, i<distribution.length, i++) {
		// Get book distribution order
		int book = distribution[i];
		for (int j=0, j<n, j++) {
			if (books[j] != book) {
				// Increment anger at position j
				anger[j]++;
				anger_total++;
			} else {
				// Add participant order to Queue
				final_order.add(j);
				// Remove book from books queue
				books_queue.poll();
				// Remove distribution from distribution queue
				distribution_queue.poll();
			}
		}
		// Print final_order
		System.out.println("participant served: " + final_order);
		// Print total anger
		System.out.println("total anger: " + anger_total);
	}
