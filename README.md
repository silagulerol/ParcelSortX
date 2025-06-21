# ParcelSortX
ParcelSortX: Smart Package Sorting and Routing Simulation Using Classical Data Structures
# ParcelSortX

**ParcelSortX** is a Java-based logistics simulation system designed to manage and optimize parcel sorting operations using a variety of core data structures. It demonstrates how real-world parcel processing tasks can be efficiently handled using stacks, queues, circular linked lists, AVL trees, and hash tables.

---

## Features

- **Parcel Management**: Handles parcel creation with attributes such as ID, weight, and destination.
- **Stack-Based Sorting**: Simulates last-in-first-out sorting for temporary storage.
- **Circular Queue & Linked List**: Manages parcels in a rotating processing structure.
- **AVL Tree Integration**: Provides efficient parcel lookup and balanced storage.
- **Hash Table Lookup**: Enables constant-time access for parcel IDs.
- **Configuration Loader**: Reads runtime parameters from an external file (`config.txt`).
- **Simulation Dashboard**: Outputs system state via console for visualization.

---

## Project Structure
ParcelSortX/
**├── AVL.java # AVL Tree implementation**
**├── CircularLinkedList.java# Circular linked list for round-robin parcel processing**
**├── CircularQueue.java # Circular queue for parcel buffering**
**├── ConfigLoader.java # Loads configuration from file**
**├── HashTable.java # Hash-based parcel lookup**
**├── Main.java # Entry point with simulation logic**
**├── Parcel.java # Data model for parcels**
**├── Stack.java # Stack implementation for parcel processing**

----

## How It Works:
**-Parcels are first loaded into the system via ConfigLoader.**
**-Sorting begins using a stack-based mechanism.**
**-Processed parcels are routed via a circular queue.**
**-Destinations are assigned using a hash table for O(1) lookup.**
**-For storage and quick retrieval, parcels are inserted into an AVL tree.**
**-Final simulation stats and transitions are printed in the console.**

----

## Concepts Used:
-Data Structures: Stack, Queue, Linked List, AVL Tree, Hash Table
-File I/O: Configuration loading from text file
-Object-Oriented Design: Modular and extensible Java classes
