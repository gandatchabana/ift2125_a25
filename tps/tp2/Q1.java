
package ift2015_a25_tp2;
import java.util.*;

public class Q1 {
	
	public static void main (String[] args) {
		// example input
		int[] books = {2, 1, 3, 4, 3}; // book requested by each participant
		int[] distribution = {1, 3, 3, 2, 4}; // book distribution order
		int n = books.length;
		
		/*
		 * Queue<Integer> distributionQueue
		 * Queue<Integer> finalOrder
		 * LinkedList<Integer> booksList
		 * int participant_position
		 * int books_to_look_for = n
		 *
		 * // Iterate as long as the queue is not empty
		 * while distributionQueue.peek() != null
		 * 	// Safeguard if distributionQueue is empty
		 * 	if books_to_look_for <= 0
		 * 		System.out.println("distributionQueue is empty but books still in line!");
		 *	// Grab next book to be delivered
		 *	book_to_be_delivered = distributionQueue.poll();
		 *	// Initialize particpant_position
		 *	participant_position = 0
		 *	// Iterate over books
		 *	for (book_to_be_matched : books)
		 *		// Increment particpant_position
		 *		participant_position++
		 *		// If value at that index is null, continue
		 *		if booksList.get(book_to_be_matched) == null
		 *			continue;
		 *		// If books match
		 *		if book_to_be_matched == book_to_be_delivered
		 *			// Add participant_position to finalOrder
		 *			finalOrder.offer(participant_position)
		 *			// Remove book from booksList
		 *			booksList.set(book_to_be_matched, null)
		 *			// Decrement the counter of books to search for
		 *			books_to_look_for--
		 *			// Stop loop
		 *			break;
		 *
		 *			
		 *
		 *
		 *
		 *
		 *
		 */


		// TODO
		
		Queue<Integer> distributionQueue = new LinkedList<Integer>();
		Queue<Integer> finalOrder = new LinkedList<Integer>();
		LinkedList<Integer> booksList = new LinkedList<Integer>();
		int participant_position;
		int arr_index;
		int book_to_be_delivered;
		int books_to_look_for = n;
		//ArrayList<Integer> anger_list = new ArrayList<Integer>(n);
		int[] anger_list = { 0, 0, 0, 0, 0 };

		// Populate distributionQueue
		for (int i=0; i<n; i++) { distributionQueue.offer(distribution[i]); }
		System.out.println("distributionQueue: " + distributionQueue);

		// Populate booksList
		for (int book : books) { booksList.add(book); }
		System.out.println("booksList: " + booksList);

		// Init anger_list with 0
		//for (int i=0; i<n; i++) { anger_list.add(0) }

		//Iterate as long as the queue is not empty
		while (! distributionQueue.isEmpty()) {
			if (books_to_look_for <= 0) {
				System.out.println("distributionQueue is empty but books still in line!");
				System.exit(1);
			}
			// Grab next book to be delivered
			book_to_be_delivered = distributionQueue.poll();
			System.out.println("--------------------");
			System.out.println("Searching for person requiring book " + book_to_be_delivered + "..");
			// Initialize participant_position
			arr_index = 0;
			participant_position = 1;
			// Iterate over `books`
			//for (int book_to_be_matched : books) {
			for (arr_index=0; arr_index<n; arr_index++) {
				int book_to_be_matched = books[arr_index];
				// If value at that index is null, continue
				if (booksList.get(arr_index) == null) { participant_position++; continue; }
				// If books match
				if (book_to_be_matched == book_to_be_delivered) {
					System.out.println("At position: " + participant_position + " found corresponding book " + book_to_be_matched);
					// Add participant_position to finalOrder
					finalOrder.offer(participant_position);
					System.out.println("Added participant " + participant_position + " to finalOrder: " + finalOrder);
					// Remove book from booksList
					booksList.set(arr_index, null);
					System.out.println("Removed book " + book_to_be_matched + " from booksList: " + booksList);
					// Decrement the counter of books to search for
					books_to_look_for--;
					System.out.println("Remaining books to look for :" + Integer.toString(books_to_look_for));
					System.out.println("Remaining distibutionQueue: " + distributionQueue);
					participant_position++;
					break;
				}
				// If books do not match, increment anger at arr_index
				else {
					System.out.println("Incrementing anger for person " + participant_position + " with book " + book_to_be_matched);
					//int new_anger = (int) anger_list.get(participant_position) + 1;
					//anger_list.set(participant_position, new_anger);
					anger_list[arr_index]++;
					//System.out.println("At position: " + participant_position + " increment anger to " + new_anger);
					System.out.print("anger_list : [");
					for (int j=0; j<anger_list.length; j++) { System.out.print(anger_list[j]); }
					System.out.print("]");
					System.out.println("");
				}
				// Increment partipant position
				participant_position++;
				//arr_index++;
			}
		}
		// Check that final order contains all participants
		assert finalOrder.size() == n : "Not all participants have been served!";
		// Check that queues are empty
		assert distributionQueue.size() == 0 : "Not all participants have been served!";
		// Print finalOrder
		System.out.println("participant served: " + finalOrder);
		// Print total anger
		int anger_total = 0;
		for (int i=0; i<anger_list.length; i++) { anger_total += anger_list[i]; }
		System.out.println("total anger: " + anger_total);
	}
}
