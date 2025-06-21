package fabric;

public class CircularLinkedList {
	public class Node{
		public String cityName;
		public Node next;
		
		public Node(String cityName){
			this.cityName=cityName;
			this.next=null;
		}
	}
	
	public Node head;
	public Node tail;
	public int size;
	
	public CircularLinkedList(int size){
		this.head=null;
		this.tail=null;
		this.size=size;
	}
	
	public boolean isEmpty() {
		return head==null && tail==null;
	}
	
	public void add(String cityName) {
		Node newNode= new Node(cityName);
		if(isEmpty()) {
			head= newNode;
			tail= newNode;
			tail.next=head;
			size++;
		}else {
			tail.next=newNode;
			tail=newNode;
			tail.next=head;
			size++;
		}
	}
	
	public String remove() {
		Node current= head;
		while(current.next != tail) {
			current= current.next;
		}
		String cityName= tail.cityName;
		tail=current;
		current.next=head;
		size--;
		return cityName;
	}
	
	public String[] rotate() {
		tail= head;
		head=head.next;
		
		String[] array = new String[size];
		array[0]= head.cityName;
		Node current=head.next;
		int i=1;
		while(current != head) {
			array[i]= current.cityName;
			current=current.next;
			i++;
		}
		return array;
	}
	
	//----------
	
	public static CircularLinkedList initializeFromCityList() {
		String[] array = ConfigLoader.getStringArrayProperty("CITY_LIST", ",");
		CircularLinkedList list = new CircularLinkedList(array.length);
		for(int i=0; i< array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}
	
	public void printTerminalOrder() {
		System.out.println(head.cityName);
		Node current=head.next;
		while(current!=head) {
			System.out.println(current.cityName);
			current=current.next;
		}
	}
	
	public void advanceTerminal() {
		tail= head;
		head=head.next;
		
		String[] array = new String[size];
		array[0]= head.cityName;
		Node current=head.next;
		int i=1;
		while(current != head) {
			array[i]= current.cityName;
			current=current.next;
			i++;
		}
	}
	
	public String getActiveTerminal() {
		return head.cityName;
	}
	
	
	public static void main(String[] args) 
	{
		/*
		CircularList c = new CircularList();
		c.add('1');
		c.add('2');
		c.add('3');
		c.add('4');
		c.add('5');
		c.add('6');
		c.tostring();
		System.out.println("");
		char[] arr= c.rotate();
		c.tostring();
		for(int i=0;i<arr.length;i++) {
			System.out.println(arr[i]);
		}
		
		String[] array = ConfigLoader.getStringArrayProperty("cities", ",");
		for(int i=0; i< array.length; i++) {
			System.out.println(array[i]);
		}*/
		
		CircularLinkedList list = CircularLinkedList.initializeFromCityList();
		list.printTerminalOrder();
		System.out.println("----");
		System.out.println("active terminal:"+ list.getActiveTerminal());
		list.advanceTerminal(); 
		System.out.println("active terminal:"+ list.getActiveTerminal());
		System.out.println("----");
		list.printTerminalOrder();
	}
}

























