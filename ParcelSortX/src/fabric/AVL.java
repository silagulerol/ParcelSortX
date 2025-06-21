package fabric;
import java.lang.Math;

import fabric.Parcel.status;

public class AVL{
	

	public static class Node {
		public static class CityLinkedList{
			
			public class Node_parcel {
				public Parcel parcel;
				public Node_parcel next;
				
				public Node_parcel(Parcel parcel) {
					this.parcel = parcel;
					this.next = null;
				}
			}
			
			Node_parcel head;
			public int size ;
			public int total;
			public CityLinkedList() {
				this.head =null;
				this.size =0;
				this.total = 0;
			}
			
			public void insert(Parcel parcel) {
				Node_parcel newNode = new Node_parcel(parcel);
				if (head == null) {
					head = newNode;		
					this.size= this.size+1;
					this.total= this.total+1;
				}else {
					newNode.next = head;
					head = newNode;
					this.size= this.size+1;
					this.total= this.total+1;
				}
				
				
			}
			
			public Parcel dispacthFIFO() {
				if(head== null) {
					return null;
				}
				Node_parcel current = head;
				Node_parcel before =current;
				
				while(current.next != null) {
					current = current.next;
				}
				
				if(head == current) {
					head = current.next;
				}
				before.next = current.next;
				this.size=this.size-1;
				System.out.println("size" +  this.size);
				return current.parcel;
			}
			
			
			public Parcel remove(int parcelID) {
				Node_parcel current = head;
				Node_parcel before =current;
				while(current != null && current.parcel.parcelID != parcelID ) {
					before= current;
					current = current.next;
				}
				
				if( current == null ) {
					System.out.println("there is no parcel which has ID : " + parcelID+ " in here");
					return null;
				}else {
					if(head == current) {
						head = current.next;
					}
					before.next = current.next;
					this.size--;
					return current.parcel;
				}
			}
			
			public int getSize() {
				return this.size;					
			}
			public int getTotal() {
				return this.total;
			}
			public void printList() {
				Node_parcel current = head;
				while(current != null ) {
					Parcel.print(current.parcel);
					current = current.next;
				}
			}
			
		}
		
		public int data;
		public String cityName;
		public CityLinkedList parcels;
		public Node right;
		public Node left;
		
		public int height;
		public int conversion(String cityName) {
			 if ("Ankara".equals(cityName)) return 1;
			 if ("Antalya".equals(cityName)) return 2;
			 if ("Bursa".equals(cityName)) return 3;
			 if ("Istanbul".equals(cityName)) return 4;
			 if ("Izmir".equals(cityName)) return 5;
			 return 0;
		
		}
		
		public Node(Parcel parcel){
			this.data= conversion(parcel.destinationCity);
			this.cityName =parcel.destinationCity;
			this.parcels =new CityLinkedList();
			this.parcels.insert(parcel);
		}
		
		public CityLinkedList getList() {
			return this.parcels;
		}
	}

	public Node root;
	
	//Special methods for for AVL tree
	public int height(Node root) {
		if (root != null) {
			return root.height;
		}else {
			return -1;
		}

	}
	private void updateHeight(Node root) {
		int leftChildHeight= height(root.left);
		int rightChildHeight= height(root.right);
		root.height= Math.max(rightChildHeight,leftChildHeight)+1;
	}
	private Node rotateRight(Node root) {
		 if (root == null || root.left == null) {
		        return root; // cannot rotate
		    }
		Node leftChild = root.left;
		
		root.left= leftChild.right;
		leftChild.right = root;
		
		updateHeight(root);
		updateHeight(leftChild);
		
		//returning new tree's root
		return leftChild;
	}
	private Node rotateLeft(Node root) {
		 if (root == null || root.right == null) {
		        return root; // cannot rotate
		    }
		 
		Node rightChild = root.right;
		
		root.right = rightChild.left;
		rightChild.left=root;
		
		updateHeight(root);
		updateHeight(rightChild);
		
		return rightChild;
	}
	private int balanceFactor(Node root) {
		return height(root.right)- height(root.left);
	}
	private Node rebalance(Node root) {
		int balanceFactor = balanceFactor(root);
		
		//Left heavy? BF(N)<0
		if(balanceFactor < -1 ) {
			if (balanceFactor(root.left) <= 0) {
				//Rotate right
				root = rotateRight(root);
			}else {
				// Rotate left-right
				root.left= rotateLeft(root.left);
				root= rotateRight(root);
			}
		}
		
		if(balanceFactor >1) {
			if(balanceFactor(root.right) >= 0) {
				//Rotate left
				root = rotateLeft(root);
			}else {
				//Rotate right-left
				root.left = rotateRight(root.left);
				root= rotateLeft(root);
			}
		}
		return root;
	}
	
	public Node insert(Node node) {
		root = insertHelper(root, node);
		return root;
	}
	private Node insertHelper(Node root, Node node) {
		
		int data =node.data;// data of to be inserted node
		
		if (root == null) {
			root = node;
			return root;
		}
		else if (data < root.data) {
			root.left = insertHelper(root.left, node);
		}else {
			root.right = insertHelper(root.right, node);
		}
		updateHeight(root);
		return rebalance(root);
	}
	
	public void display() {
		displayHelper(root);
	}
	private void displayHelper(Node root) {
		
		if (root != null) {
			displayHelper(root.left);// root'u verilen en ağaçtaki en küçük değeğri bulur
			System.out.println("the city name is " + root.cityName + " and the parcels: ");
			root.parcels.printList();
			displayHelper(root.right); //verilen root'ağacının sağ alt ağacını yazdırır
		}
	}
	
	public Node search(int data) {
		return searchHelper(root, data);
	}
	private Node searchHelper(Node root, int data) {
		
		if (root == null) {
			return null;
		}else if(root.data==data ) {
			return root;
		}else if (root.data < data) {
			return searchHelper(root.right, data);
		} else {
			return searchHelper(root.left ,data);
		}
		
	}
	
	public boolean remove(String city) {
		int data = conversion(city);
		if( search(data) != null) {
			removeHelper(root, data);
		}else {
			System.out.println(data + " could not be found");
			return null;
		}
	}
	private Node removeHelper(Node root,  int data) {
		
		if(root == null) {
			return root;
		} 
		else if(root.data < data) {
			root.right= removeHelper(root.right, data);
		} 
		else if(root.data > data) {
			root.left= removeHelper(root.left, data);
		}
		else { // node found
			// if the node is leaf
			if (root.left==null && root.right==null) {
				root =null;
			}// if the node to be deleted has a right child
			//ilk önce root ile değiştirmek için successor arıyor bulamayınca predecessor aramaya başlıyor
			else if (root.right != null) { // find a successor to replace this node 
				root.data = successor(root);
				root.right= removeHelper(root.right, root.data);
			}else if (root.left != null) {// find a predecessor to replace this node 
				root.data = predecessor(root);
				root.left= removeHelper(root.left, root.data);
			}
		}
		if (root != null) {
			updateHeight(root);
			return rebalance(root);
		}
		return null;
	}
	private int successor(Node root) { //find least value below the right child of this root node
		root = root.right; // successor kendinden büyük olduğu için sağ alt ağaçta aramalıyım
		while (root.left != null) { // sağ alt ağaçtaki min değer lazım
			root = root.left;
		}
		return root.data;
	}
	private int predecessor(Node root) { //predecessor kendinden küçük en büyük değer
		root = root.left; // kendinden küçük olduğu için sol alt ağaçta arama yapıyoruz
		while(root.right != null) {
			root=root.right;
		}
		return root.data;
	}

	public void printTreeTopDown() {
	    printTreeTopDownHelper(root, "", true);
	}
  
	private void printTreeTopDownHelper(Node node, String prefix, boolean isLeft) {
	    if (node != null) {
	        // Print current node
	        System.out.println(prefix + (isLeft ? "├── " : "└── ") + node.cityName);

	        // Recurse to left and right children
	        String childPrefix = prefix + (isLeft ? "│   " : "    ");
	        printTreeTopDownHelper(node.left, childPrefix, true);
	        printTreeTopDownHelper(node.right, childPrefix, false);
	    }
	}

	
	/*------- functions which take parcel as argumen----------t*/
	// argüman olarak aldığı parcel'i ağaçta uygun node'a yerleştirir
	public void insertParcel(Parcel parcel) {
		int data = conversion(parcel.destinationCity);
		if(search(data) != null) {
			search(data).parcels.insert(parcel);
		}else {
			Node newNode = new Node(parcel);
			insert(newNode);
		}
	}
	
	public int conversion(String cityName) {
		 if ("Ankara".equals(cityName)) return 1;
		    if ("Antalya".equals(cityName)) return 2;
		    if ("Bursa".equals(cityName)) return 3;
		    if ("Istanbul".equals(cityName)) return 4;
		    if ("Izmir".equals(cityName)) return 5;
		    return 0;
	}

	//argüman olarak aldığı şehir ismine ait parcel linked list'ini döner
	public Node.CityLinkedList getCityParsels(String city) {
		int data = conversion(city);
		if(search(data)!= null) {
			return search(data).getList();
		}else {
			System.out.println("This city in not in the AVL tree");
			return null;
		}
	}
	
	
	
	// şehirleri alfabetik olarak gezer
	public void inOrderTraversal() {
		System.out.print("\nVisit all cities in sorted order alphabetically." );
		inOrderTraversalHelper(root);
	}
	private void inOrderTraversalHelper(Node root) {
		if (root != null) {
			inOrderTraversalHelper(root.left);// root'u verilen en ağaçtaki en küçük değeğri bulur
			System.out.print(root.cityName + ", ");
			inOrderTraversalHelper(root.right); //verilen root'ağacının sağ alt ağacını yazdırır
	
		}
	}
	
	
	int max;
	String max_root_city ="";
	int sumInTree = 0;
	public void getTable() {
		max = root.parcels.getSize();
		System.out.println();
		System.out.println("the city name\t Parcel count ");
		getTableHelper(root, max);
		//System.out.println("the max is" + max + " in " + max_root_city );
	}
	
	public void getTableHelper(Node root, int max) {
		if (root != null) {
			getTableHelper(root.left, max);// root'u verilen en ağaçtaki en küçük değeğri bulur
			System.out.println(root.cityName + "\t\t\t" +root.parcels.getSize());
			sumInTree = sumInTree + root.parcels.getSize();
			if (root.parcels.head != null) {
				if (root.parcels.getSize() > max) {
					max= root.parcels.getSize();
					max_root_city = root.cityName;
					
				}
			}
			getTableHelper(root.right, max); //verilen root'ağacının sağ alt ağacını yazdırır
		}
	}
	
	
	// //argüman olarak aldığı şehir node'undaki parcel sayısını döner 
	public int countCityParcels(String city) {
		int data = conversion(city);
		if(search(data) == null) {
			return 0;
		}else {
			return search(data).parcels.getSize();
		}
	}
	
	public int countCityParcelsTotal(String city) {
		int data = conversion(city);
		if(search(data) == null) {
			return 0;
		}else {
			return search(data).parcels.getTotal();
		}
	}
	/*
	//argüman olarak aldığı şehir node'unu ağaçta bulur o node'daki parcel linked list'ten (parcels) ilgili parcel'li siler 
	public Parcel removeParcel(String city, int parcelID) {
		int data = conversion(city);
		if (search(data) != null) {
			return search(data).parcels.remove(parcelID);
		}else {
			return null;
		}
	}*/
	public Parcel removeParcel(String city) {
		int data = conversion(city);
		if (search(data) != null) {
			return search(data).parcels.dispacthFIFO();
		}else {
			return null;
		}
	}
	
	public Parcel dispatchParcel(String city) {
		int data = conversion(city);
		if (search(data) != null) {
			return search(data).parcels.dispacthFIFO();
		}else {
			return null;
		}
	}
	
	public void SystemLogs() {
		System.out.println("------------------\nthe height of tree is " + this.height(root));
		System.out.println("the braekdown table of tree is " );
		this.getTable();
		
	}
	
	public static void main(String[] args) {
		Parcel p1= new Parcel(0);
		p1.destinationCity= "Bursa";
		p1.priority= 1;
		p1.size= "Small";
		p1.arrivalTick=0;
		
		Parcel p2= new Parcel(0);
		p2.destinationCity= "Izmir";
		p2.priority= 1;
		p2.size= "Large";
		p2.arrivalTick=0;
		
		Parcel p3= new Parcel(0);
		p3.destinationCity= "Istanbul";
		p3.priority= 1;
		p3.size= "Large";
		p3.arrivalTick=0;
		
		Parcel p4= new Parcel(0);
		p4.destinationCity= "Bursa";
		p4.priority= 1;
		p4.size= "Large";
		p4.arrivalTick=0;
		
		Parcel p5= new Parcel(0);
		p5.destinationCity= "Antalya";
		p5.priority= 1;
		p5.size= "Large";
		p5.arrivalTick=0;
		
		AVL tree= new AVL();
		tree.insertParcel(p1);
		tree.insertParcel(p2);
		tree.insertParcel(p3);
		tree.insertParcel(p4);
		tree.insertParcel(p5);
		
		tree.display();
		
		Node.CityLinkedList list= tree.getCityParsels("Bursa");
		System.out.println("with using getCityParsels: ");
		list.printList();
		
		tree.inOrderTraversal();
		
		
		String city ="Izmir";
		System.out.println("the number of parcels in " + city +" is "+ tree.countCityParcels(city));
		String removedCity ="Bursa";
		/*
		System.out.println("the removed parcel from " + removedCity +" is ");
		Parcel.print(tree.removeParcel(removedCity, 4));
		*/ 
		System.out.println("the dispacthed parcel from " + removedCity +" is ");
		Parcel.print(tree.dispatchParcel(removedCity));
		System.out.println("after removal ");
		tree.display();
		
		tree.SystemLogs();
		
	}
}






















