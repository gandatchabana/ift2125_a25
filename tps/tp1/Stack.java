package ift2015_a25_tp1;

public class Stack {
    private int maxSize;
    private String[] stackArray;
    private int top;

    public Stack(int size) {
        maxSize = size;
        stackArray = new String[maxSize];
        top = -1;
    }
    
    public int size() {
      // TODO
      return this.top+1;
    }
   
    public void push(String s) {
        // TODO
        if (this.stackArray.length <= this.maxSize) {
            this.top++;
            this.stackArray[top] = s;
        }
    }

    public String pop() {
        // TODO
        if (this.isEmpty()) {
            System.out.println("Stack already empty, not performing .pop operation");
            return null;
        }
        String popped = this.stackArray[top];
        this.top--;
        return popped;
   }
    
    public String peek() {
        // TODO
        if (this.isEmpty()) {
            return null;
        }
        return this.stackArray[this.top];
    }

    public boolean isEmpty() {
        // TODO
        if (this.top == -1) {
            return true;
        }
        return false;
    }
    
    // Print stack elements from top to bottom
    public void print_stack() {
        if (isEmpty()) {
            System.out.println("Stack is empty.");
        } else {
            System.out.println("Stack:");
            System.out.println("---------");
            for (int i = top; i >= 0; i--) {
                System.out.println(stackArray[i]);
                System.out.println("---------");
            }
        }
    }

    
    // some tests
    public static void main(String[] args) {
        Stack stack = new Stack(5); 

        
        stack.push("A");
        stack.push("B");
        stack.print_stack();
        
        System.out.println("Top element of the stack: " + stack.peek());

        
        stack.pop();
        stack.pop();
        stack.pop();

        System.out.println("Is stack empty? " + stack.isEmpty());
    }
}
