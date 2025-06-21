package fabric;

import fabric.Parcel.status;

public class CircularQueue {
	public Parcel[] queue;
	public int front= -1;
	public int rear =-1;
	public int size;
	public int count;
	
	//Constructor
	public CircularQueue(int size){
		this.size= size;
		this.queue= new Parcel[size];
		this.count=0;
	}
	
	public void enqueue(Parcel p) {
		if (this.count == this.size ) {
			System.out.print( p.parcelID + " to " + p.destinationCity + " (Priority "+ p.priority+ ") " + "is not added to the queue, ");
			return;
		}
		
		if(front==-1)
			front=0;
		
		if (rear == size -1) {
			rear=0;
			queue[rear] = p;
		}else {
			queue[++rear]= p;
		}
		this.count++;
		System.out.print(p.parcelID + " to " + p.destinationCity + " (Priority "+ p.priority+ "), ");
		//p.currentStatus = status.InQueue;
	}
		
	public Parcel dequeue() {
		if(isEmpty()) {
			return null;
		}
			
		Parcel temp = queue[front];
		queue[front]=null;
		
		if(front == size-1) {
			front = 0;
		}else {
			front++;
		}
		this.count--;
		
		return temp;
	}
	
	public boolean isEmpty() {
		return this.count == 0;
	}
	
	public boolean isFull() {
		return this.count == this.size;
	}
	
	public Parcel peek() {
		if(isEmpty())
			System.out.println("Queue is empty.");
			
		return queue[front];
	}

	
	/*
	public static void main(String[] args) {
	    try {
	        // Since SimpleArrayTypedQueue is not static, we must create an instance of Main to access it
	    	CircularQueue queue = new CircularQueue(5);

	        // enqueue elements
	        queue.enqueue("Apple");
	        queue.enqueue("Banana");
	        queue.enqueue("Cherry");
	        queue.enqueue("hello");
	        queue.enqueue("hi");


	        // Peek at the front element
	        System.out.println("Front of queue: " + queue.peek());

	        // dequeue elements
	        System.out.println("dequeued: " + queue.dequeue());
	        System.out.println("dequeued: " + queue.dequeue());

	        // Peek again
	        System.out.println("Front after removals: " + queue.peek());
	        
	        queue.enqueue("hi");
	        
	        // Check if empty
	        System.out.println("Is queue empty? " + queue.isEmpty());

	        // dequeue remaining elements
	        System.out.println("dequeued: " + queue.dequeue());

	        // Try removing from empty queue to test exception
	        System.out.println("dequeued: " + queue.dequeue()); // This will throw an exception

	    } catch (Exception e) {
	        System.err.println("Error: " + e.getMessage());
	    }
	
	}*/
}

