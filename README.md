# Laboratory Work No.7: Multithreading and Concurrency

## Overview
This repository contains the implementation of Laboratory Work No.7 for the Moscow Technical University of Communications and Informatics (MTUCI). 

The primary focus of this project is to explore multithreading capabilities and parallel processing architectures in Java using the java.util.concurrent package. The work includes three practical tasks utilizing thread pools, task decomposition, and synchronization mechanisms for shared resources.

## Tasks Description

### Task 1: Thread Pool Array Processing (ThreadPoolArrayProcessing)
A high-performance program designed to calculate the sum of elements in a one-dimensional array using concurrent execution.
* Mechanics: Dynamically divides the array into equal segments based on the available threads. Each segment is assigned to a separate worker thread.
* Concurrency Framework: Utilizes a fixed-capacity ExecutorService. The individual threads return intermediate results via the Callable interface, which are aggregated in the main thread using Future objects.

### Task 2: Thread Pool Matrix Max Finder (ThreadPoolMatrixMax)
A parallel computing utility that locates the maximum value within a large two-dimensional matrix.
* Mechanics: Decomposes the data matrix by distributing individual rows or index blocks across multiple threads.
* Concurrency Framework: Threads execute tasks concurrently through an ExecutorService pool. The main thread collects all thread-specific maximums and performs a final comparison to determine the absolute highest element.

### Task 3: Warehouse Relocation Simulation (WarehouseRelocation)
A complex multithreaded logistics simulation modeled after a real-world warehouse operations scenario.
* Scenario: Three loaders concurrently move items from one warehouse to another. The total capacity of the transport vehicle is strictly limited to 150 kg per trip.
* Synchronization: Employs a thread-safe ConcurrentLinkedQueue to store the queue of inventory items. Access to the shared transport vehicle is controlled using the synchronized keyword to prevent race conditions.
* Mechanics: Once the accumulated weight reaches 150 kg, the vehicle executes a delivery (simulated with Thread.sleep), unloads, and returns. A deliverRemaining method ensures that any remaining weight below the threshold is safely transported at the end of the operation.

## Technologies Used
* Language: Java
* Core Concepts: Multithreading, Concurrency Framework (ExecutorService, Callable, Future), Thread Synchronization (synchronized keyword), Thread-Safe Collections (ConcurrentLinkedQueue), Data Decomposition.

## Key Learnings and Conclusion
This laboratory work successfully demonstrates the principles of asynchronous task execution and resource synchronization in Java. By developing these three programs, practical skills in partitioning large datasets for parallel execution were acquired. Furthermore, the project highlights how to mitigate concurrency issues and handle shared state safely using proper thread locking mechanisms and atomic/thread-safe queue data structures.

---

## Author
* Name: Pavel Petrovich Koshelev
* Group: BST 2401
* Institution: MTUCI
* Year: 2026
