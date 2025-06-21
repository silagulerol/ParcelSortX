package fabric;
import java.io.*;
import java.util.Random;
import java.util.UUID;

import fabric.Parcel.status;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Main {
	
	
	public static void main(String[] args) {
		
		final int MAX_TICKS = ConfigLoader.getIntProperty("MAX_TICKS", 300);
		final int QUEUE_CAPACITY =  ConfigLoader.getIntProperty("QUEUE_CAPACITY", 300);
		final int TERMINAL_ROTATION_INTERVAL =  ConfigLoader.getIntProperty("TERMINAL_ROTATION_INTERVAL", 5);
		final int PARCEL_PER_TICK_MIN = ConfigLoader.getIntProperty("PARCEL_PER_TICK_MIN", 1);
	    final int PARCEL_PER_TICK_MAX = ConfigLoader.getIntProperty("PARCEL_PER_TICK_MAX", 3);
	    final double MISROUTING_RATE = ConfigLoader.getDoubleProperty("MISROUTING_RATE", 0.1);
	    //final double MISROUTING_RATE = 1.3;
	    final String[] CITY_LIST = ConfigLoader.getStringArrayProperty("CITY_LIST", ",");
	    final int return_limit= 3;
	    /*
	    System.out.println(MAX_TICKS);
	    System.out.println(QUEUE_CAPACITY);
	    System.out.println(TERMINAL_ROTATION_INTERVAL);
	    System.out.println(PARCEL_PER_TICK_MIN);
	    System.out.println(PARCEL_PER_TICK_MAX);
	    System.out.println(MISROUTING_RATE);
	    for(int i= 0;i< CITY_LIST.length; i++) {
	    	System.out.println(CITY_LIST[i]);
	    }*/
	    
	    
	    
	    Random rand = new Random();
	    CircularQueue ArrivalBuffer = new CircularQueue(QUEUE_CAPACITY);
	    Stack ReturnStack = new Stack();
	    AVL DestinationSorter = new AVL();
	    HashTable ParcelTracker = new HashTable();
	    CircularLinkedList TerminalRotator = CircularLinkedList.initializeFromCityList();
	    
	    
	    int tick =1;
	    int GeneratedParcelSum=0;
	    //tick initializaton
	    for(tick=1; tick<= MAX_TICKS; tick++) {
	    	System.out.println("\n[" + tick + "]");
	    	int n = rand.nextInt(PARCEL_PER_TICK_MAX-PARCEL_PER_TICK_MIN) + PARCEL_PER_TICK_MIN; 
	    	
	    	// Parcel Generation 
	    	System.out.print("New parcels: ");
	    	for (int i = 0; i < n; i++) {
	    		Parcel parcel = new Parcel(tick);
	    		ArrivalBuffer.enqueue(parcel);
	    		ParcelTracker.insert(parcel.parcelID, parcel);
	    		GeneratedParcelSum = GeneratedParcelSum +n;
	    	}
	    	System.out.println("Queue size: " + ArrivalBuffer.size);
	    	
	    	// Queue Processing
	    	System.out.print("Sorted to AVL: ");
	    	for(int j=0 ;j < ArrivalBuffer.size; j++) {
	    		Parcel dequeued_parcel = ArrivalBuffer.dequeue();
	    		if(dequeued_parcel != null) {
	    			DestinationSorter.insertParcel(dequeued_parcel);
		    		ParcelTracker.updateStatus(dequeued_parcel.parcelID, Parcel.status.Sorted);
		    		System.out.print(dequeued_parcel.parcelID + " to " + dequeued_parcel.destinationCity +", ");
	    		}
	    	}
	    	DestinationSorter.inOrderTraversal();
	    	
	    	//Dispatch Evaluation
	    	String activeTerminal = TerminalRotator.getActiveTerminal();
	    	int parcelCount = DestinationSorter.countCityParcels(activeTerminal);
	    	if(parcelCount != 0) {
	    		
	    		if(MISROUTING_RATE > rand.nextDouble() ) {
	    			Parcel misrouted = DestinationSorter.removeParcel(activeTerminal);
	    			ReturnStack.push(misrouted);
	    			if( misrouted != null) {
	    				ParcelTracker.updateStatus(misrouted.parcelID, Parcel.status.Returned);
		    			ParcelTracker.incrementReturnCounter(misrouted.parcelID);

		    			HashTable.Node node  = ParcelTracker.getParcel(misrouted.parcelID);
		    			node.returnCount++;
		    			System.out.print("\nReturned: " + misrouted.parcelID + " misrouted -> Pushed to ReturnStack");
	    			}
	    			
	    		}else {
	    			Parcel dispatched = DestinationSorter.dispatchParcel(activeTerminal);
	    			if( dispatched != null) {
	    				ParcelTracker.updateStatus(dispatched.parcelID, Parcel.status.Dispatched);
		    			ParcelTracker.incrementDispatchCounter(dispatched.parcelID);
		    			
		    			HashTable.Node node  = ParcelTracker.getParcel(dispatched.parcelID);
		    			node.dispatchTick = tick;
		    			System.out.print("\nDispatcehd: " + dispatched.parcelID + " from AVL to " + dispatched.destinationCity + " -> Success");
		    			
	    			}
	    		}
	    		
	    	}else {
	    		System.out.print("\n" +activeTerminal+" activeTerminal is not in the tree so there is no dipsatch or misrouted  parcel");
	    	}
	    	
	    	//ReturnStack Reprocessing
	    	if(tick %3 == 0) {
	    		for(int limit = 0; limit < return_limit; limit++) {
	    			Parcel returned= ReturnStack.pop();
		    		if( returned != null) {
		    			DestinationSorter.insertParcel(returned);
		    			ParcelTracker.updateStatus(returned.parcelID, Parcel.status.Sorted);
		    			System.out.print("\nSorted again: " + returned.parcelID + " to " + returned.destinationCity + " to AVL");
		    		}
	    		}
	    		
	    	}
	    	
	    	if(tick % TERMINAL_ROTATION_INTERVAL ==0) {
	    		TerminalRotator.advanceTerminal();
	  	
	    	}
	    	System.out.println("\nActive Terminal: " + TerminalRotator.getActiveTerminal());
	    	System.out.println("Stack Size: " + ReturnStack.size);
	    	//System.out.println("\nReturn Counts so far: " + ParcelTracker.returnCounter);
	    	//System.out.println("\nDisptach Counts so far : " + ParcelTracker.dispatchCounter);
	    }
	    
	    System.out.println("\n--- Simulation Overview ---");
	    System.out.println("Total ticks executed: " + (tick-1));
	    System.out.println("Total Generated Parcels: " + GeneratedParcelSum);
        
	    System.out.println("\n--- Parcel Statistics ---");
	    System.out.println("Total Dispatched Parcels: " + ParcelTracker.dispatchCounter);
	    System.out.println("Total Returned Parcels: " + ParcelTracker.returnCounter);
	    System.out.println("The current parcels in stack: " + ReturnStack.size);
	    System.out.println("The current parcels in queue: " + ArrivalBuffer.peek());
	    
	    
	    System.out.println("\n--- Destination Metrics ---");
	    System.out.println("Totla parcels distribution: ");
	    int max=0;
	    String max_city ="";
	    int totalParcelNum = 0;
	    for(int i=0; i<CITY_LIST.length; i++) {
	    	int parcelNum =  DestinationSorter.countCityParcelsTotal(CITY_LIST[i]);
	    	System.out.println(CITY_LIST[i]+ ": "+ parcelNum);
	    	if (parcelNum > max) {
	    		max = parcelNum;
	    		max_city = CITY_LIST[i];
	    	}
	    	totalParcelNum += parcelNum;
	    }
	    System.out.println("The total parcels in AVL in all time: " + totalParcelNum);
	    System.out.println("");
	    System.out.println("Most Frequently Targeted Destination " + max_city + " with " + max + " parcels\n");
	    DestinationSorter.printTreeTopDown();
	    System.out.println("Current parcels in avl now: ");
	    int currentTotalParcel=0;
	    for(int i=0; i<CITY_LIST.length; i++) {
	    	int parcelNum2 = DestinationSorter.countCityParcels(CITY_LIST[i]);
	    	System.out.println(CITY_LIST[i]+ ": "+ parcelNum2);
	    	currentTotalParcel += parcelNum2;
	    }
	    System.out.println("The total current parcels in AVL now: " + currentTotalParcel);
	    System.out.println("\n--- Timing and Delay Metrics ---");
	    int totalDelay =0;
	    int maxDelay = 0;
	    Parcel longestDelayParcel= null;
	    int longestDelay = 0;
	    
	 
	    
	    for(int i=1; i <= GeneratedParcelSum; i++) {
	    	
	    	HashTable.Node node =ParcelTracker.getParcel(i);
	    	if(node==null) {
	    		continue;
	    	}
	    	
	    	if(node.dispatchTick > 0 && node.parcel.arrivalTick >0) {
	    		int delay = node.dispatchTick-node.parcel.arrivalTick;
		    	totalDelay = totalDelay + delay;
		    	if( delay >maxDelay ) {
		    		longestDelayParcel = node.parcel; 
		    		longestDelay =delay;
		    	}
		    	
	    	}
	    	if (node.returnCount > 1) {
	    		System.out.println("The parcel returned more than one ParcelId:  " + node.parcel.parcelID + ", ");
	    	}
	    }
	    
	    double result =totalDelay / (tick-1) ;
	    System.out.println("Average Parcel Processing Time: " + result);
	    System.out.println("Parcel With Longest Delay ParcelId: " + longestDelayParcel.parcelID + " with " + longestDelay );
	    
	    
	    System.out.println("\n--- Data Structure Statistics ---");
	    System.out.println("Maximum Queue Size Observed: "+ ArrivalBuffer.size);
	    System.out.println("Maximum Stack Size Observed: "+ ReturnStack.maxDepth);
	    System.out.println("Final Height of AVL: "+ DestinationSorter.height(DestinationSorter.root));
	    
	    System.out.println("Hash Table Load Factor: " + (ParcelTracker.totalParcel() / ParcelTracker.SIZE) );
	    ParcelTracker.print_table();
	    
	    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	    String reportFileName = "report.txt";

        try (FileWriter fileWriter = new FileWriter(reportFileName); // Creates or overwrites the file
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter); // Buffers output for efficiency
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) { // Provides println, printf methods

        	// Write a header for each log entry
            printWriter.println("--- Application Status Log ---");
            printWriter.println("Timestamp: " + timestamp);
            
            printWriter.println("\n--- Simulation Overview ---");
            printWriter.println("Total ticks executed: " + (tick - 1));
            printWriter.println("Total Generated Parcels: " + GeneratedParcelSum);

            printWriter.println("\n--- Parcel Statistics ---");
            printWriter.println("Total Dispatched Parcels: " + ParcelTracker.dispatchCounter);
            printWriter.println("Total Returned Parcels: " + ParcelTracker.returnCounter);
            printWriter.println("The current parcels in stack: " + ReturnStack.size);
            printWriter.println("The current parcels in queue: " + ArrivalBuffer.peek());

            printWriter.println("\n--- Destination Metrics ---");
            printWriter.println("Totla parcels distribution: ");
            max = 0;
            max_city = "";
            totalParcelNum = 0;
            for (int i = 0; i < CITY_LIST.length; i++) {
                int parcelNum = DestinationSorter.countCityParcelsTotal(CITY_LIST[i]);
                printWriter.println(CITY_LIST[i] + ": " + parcelNum);
                if (parcelNum > max) {
                    max = parcelNum;
                    max_city = CITY_LIST[i];
                }
                totalParcelNum += parcelNum;
            }
            printWriter.println("The total parcels in AVL in all time: " + totalParcelNum);
            printWriter.println("");
            printWriter.println("Most Frequently Targeted Destination " + max_city + " with " + max + " parcels\n");

            
            printWriter.println("NOTE: To check DestinationSorter.printTreeTopDown() please control Sample log output. " );
                               
            
            printWriter.println("Current parcels in avl now: ");
            currentTotalParcel = 0;
            for (int i = 0; i < CITY_LIST.length; i++) {
                int parcelNum2 = DestinationSorter.countCityParcels(CITY_LIST[i]);
                printWriter.println(CITY_LIST[i] + ": " + parcelNum2);
                currentTotalParcel += parcelNum2;
            }
            printWriter.println("The total current parcels in AVL now: " + currentTotalParcel);

            printWriter.println("\n--- Timing and Delay Metrics ---");
            totalDelay = 0;
            longestDelayParcel = null;
            longestDelay = 0;

            for (int i = 1; i <= GeneratedParcelSum; i++) {
                HashTable.Node node = ParcelTracker.getParcel(i);
                if (node == null) {
                    continue;
                }

                if (node.dispatchTick > 0 && node.parcel.arrivalTick > 0) {
                    int delay = node.dispatchTick - node.parcel.arrivalTick;
                    totalDelay = totalDelay + delay;
                    if (delay > longestDelay) { // Changed maxDelay to longestDelay for consistency
                        longestDelayParcel = node.parcel;
                        longestDelay = delay;
                    }
                }
                if (node.returnCount > 1) {
                    printWriter.println("The parcel returned more than one ParcelId:  " + node.parcel.parcelID + ", ");
                }
            }
            double averageProcessingTime = (tick - 1) > 0 ? (double) totalDelay / (tick - 1) : 0.0;
            printWriter.println("Average Parcel Processing Time: " + String.format("%.2f", averageProcessingTime));

            if (longestDelayParcel != null) {
                 printWriter.println("Parcel With Longest Delay ParcelId: " + longestDelayParcel.parcelID + " with " + longestDelay + " ticks delay");
            } else {
                 printWriter.println("No parcels dispatched to calculate longest delay.");
            }


            printWriter.println("\n--- Data Structure Statistics ---");
            printWriter.println("Maximum Queue Size Observed: " + ArrivalBuffer.size); // Assuming 'size' is accessible
            printWriter.println("Maximum Stack Size Observed: " + ReturnStack.maxDepth); // Assuming 'maxDepth' is accessible
            printWriter.println("Final Height of AVL: " + DestinationSorter.height(DestinationSorter.root)); // Assuming 'root' is accessible

            
            double loadFactor = (ParcelTracker.SIZE > 0) ? (double) ParcelTracker.totalParcel() / ParcelTracker.SIZE : 0.0;
            printWriter.println("Hash Table Load Factor: " + String.format("%.2f", loadFactor));

            
            
            printWriter.println("To check  NOTE: ParcelTracker.print_table() please control Sample log output.");
            
            File reportFile = new File(reportFileName);	
            System.out.println("Report written to: " + reportFile.getAbsolutePath());
            System.out.println("Simulation report successfully written to " + reportFileName); // This line still goes to console

        } catch (IOException e) {
            System.err.println("Error writing report to file: " + reportFileName);
            e.printStackTrace();
        }
    }
}



