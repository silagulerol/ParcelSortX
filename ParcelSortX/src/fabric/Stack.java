// Stack implementation based on linked list
package fabric;

public class Stack {
	public class Node{
		public Parcel parcel; 
		public Node next;
		
		public Node(Parcel p) {
			this.parcel = p;
			this.next=null;
		}
	}
	
	public Node top;
	public int size;
    public int maxDepth; 

	public Stack() {
		top=null;
		size=0;
		maxDepth = 0;
	}
	
	public void push(Parcel p) {
		Node newNode= new Node(p);
		
		if(top==null) {
			top = newNode;
		}else {
			newNode.next=top;
			top=newNode;
		}
		size++;
		
		if (size > maxDepth) {
            maxDepth = size;
        }
	}
	
	public Parcel pop() {
		if(top==null) {
			//System.out.println("there is no parcel in the stack");
			return null;
		}
		Node temp = top;
		top = top.next;
		size--;
		return temp.parcel;
	}
	
	public int getMaxDepth() {
        return maxDepth;
    }
	
	public boolean isEmpty() {
		return top==null;
	}
	
	public Parcel peek() {
		if(isEmpty()) {
			return null;
		}
		return top.parcel;
	}
	
	public int size() {
		return size;
	}
	
	public void printStack() {
		Node current= top;
		while(current != null) {
			Parcel.print(current.parcel);
			current=current.next;
		}
	}
}



















