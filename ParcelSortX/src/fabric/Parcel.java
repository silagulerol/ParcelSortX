package fabric;

import java.util.Random;

public class Parcel {
	public static int globalParcelId = 1;
	public int parcelID;
	public String destinationCity;
	public int priority;
	public String size;
	public int arrivalTick;
	public enum  status
	{
	    InQueue, Sorted, Dispatched, Returned
	}
	
	public status currentStatus;
	
	public Parcel(int tick) {
		Random rand = new Random();
		String[] sizes = {"Small", "Medium", "Large"};
		String[] cities = {"Istanbul","Ankara","Izmir","Bursa","Antalya"};

	    this.parcelID = globalParcelId++;;
	    this.destinationCity= cities[rand.nextInt(cities.length)];
	    this.priority = rand.nextInt(2) + 1; ;
	    this.size = sizes[rand.nextInt(sizes.length)];
	    this.arrivalTick = tick ;
	    this.currentStatus=  status.InQueue;;
	    
	}
	
	public static void print(Parcel p) {
		System.out.println("parcel id:" + p.parcelID);
		System.out.println("parcel destinationCity:" + p.destinationCity);
		System.out.println("parcel priority:" + p.priority);
		System.out.println("parcel size:" + p.size);
		System.out.println("parcel arrivalTick:" + p.arrivalTick );	
		System.out.println("parcel currentStatus:" + p.currentStatus + "\n");
	}
}
