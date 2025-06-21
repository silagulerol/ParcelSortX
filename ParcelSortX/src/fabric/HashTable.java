package fabric;

public class HashTable {
	int SIZE =10;
	
	public class Node{
		public Parcel parcel;
		public Node next; 
		public int dispatchTick;;
		public int returnCount;
		
		public Node(Parcel p) {
			this.parcel = p;
			this.dispatchTick =0;;
			this.returnCount = 0;
			this.next =null;
		}
	}
	
	public int returnCounter;
	public int dispatchCounter;
	public Node[] Array = new Node[SIZE];
	
	public HashTable() {
		this.returnCounter = 0;
		this.dispatchCounter =0;
		for (int i = 0;i < SIZE; i++) {
			this.Array[i] = null;
		}
		
	}
	
	public void insert(int parcelID, Parcel parcel) {
		Node newNode = new Node(parcel);
		int index= parcelID % SIZE; 
		
		if(this.Array[index] == null) {
			this.Array[index] = newNode;
		}
		else {
			newNode.next = Array[index];
			Array[index] = newNode;
		}
	}
	
	public Node getParcel(int parcelID) {
		int index= parcelID % SIZE; 
		Node current = this.Array[index];
		while (current != null && current.parcel.parcelID != parcelID) {
			current = current.next;
		}
		if( current == null ) {
			//System.out.println("the parcel is not in the table");
			return null;
		}
		return current;
	}
	
	public void print_parcel(int parcelID) {
		Parcel.print(getParcel(parcelID).parcel);
	}
	
	public Boolean exists(int parcelID) {
		if(getParcel(parcelID) == null) {
			return false;
		}
		return true;
	}
	
	public void updateStatus(int parcelID, Parcel.status newStatus) {
		if(getParcel(parcelID) != null) {
			getParcel(parcelID).parcel.currentStatus = newStatus;
		}
	}
	
	public void incrementReturnCounter(int ParcelID) {
		this.returnCounter++;
	}
	
	public void incrementDispatchCounter(int ParcelID) {
		this.dispatchCounter++;
	}

	public void print_table() {
		for ( int i = 0; i < SIZE; i++) {
			
			Node current = this.Array[i];
			System.out.print("Array["+i+"] ----> ");
			while (current != null ) {
				System.out.print(current.parcel.parcelID + " "+current.parcel.destinationCity +"-->");
				current = current.next;
			}
			System.out.println("");
		}
		
	}
	public int totalParcel() {
		int counter = 0;
		for ( int i = 0; i < SIZE; i++) {
			
			Node current = this.Array[i];
			
			while (current != null ) {
				counter++;
				current = current.next;
			}
		}
		return counter;
	}
	public static void main(String[] args) {
		Parcel p1= new Parcel(0);
		p1.destinationCity= "bursa";
		p1.priority= 1;
		p1.size= "Small";
		p1.arrivalTick=0;
		p1.currentStatus = Parcel.status.InQueue;
		
		Parcel p2= new Parcel(0);
		p2.destinationCity= "izmir";
		p2.priority= 1;
		p2.size= "Large";
		p2.arrivalTick=0;
		p2.currentStatus = Parcel.status.InQueue;
		
		Parcel p3= new Parcel(0);
		p3.destinationCity= "istanbul";
		p3.priority= 1;
		p3.size= "Large";
		p3.arrivalTick=0;
		p3.currentStatus = Parcel.status.InQueue;
		
		Parcel p4= new Parcel(0);
		p4.destinationCity= "bursa";
		p4.priority= 1;
		p4.size= "Large";
		p4.arrivalTick=0;
		p4.currentStatus = Parcel.status.InQueue;
		
		Parcel p5= new Parcel(0);
		p5.destinationCity= "antalya";
		p5.priority= 1;
		p5.size= "Large";
		p5.arrivalTick=0;
		p5.currentStatus = Parcel.status.InQueue;
		
		HashTable ParceTracker = new HashTable();
		ParceTracker.insert(1, p1);
		ParceTracker.insert(2, p2);
		ParceTracker.insert(3, p3);
		ParceTracker.insert(4, p4);
		ParceTracker.insert(5, p5);
		
		ParceTracker.getParcel(3);
		ParceTracker.print_parcel(3);
		
		System.out.println(	"-------");
		ParceTracker.print_table();
		System.out.println(	"-------");
		
		ParceTracker.updateStatus(2, Parcel.status.Dispatched);
		ParceTracker.print_parcel(2);
		
		System.out.println(ParceTracker.exists(9));
		System.out.println(ParceTracker.exists(4));
		
	}
	
	
	
}
