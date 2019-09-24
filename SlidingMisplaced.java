/*
 * Name: Justin Senia
 * E-Number: E00851822
 * Date: 10/20/2017
 * Class: COSC 461
 * Project: #2
 */
import java.util.LinkedList;
import java.io.*;
import java.util.*;

//This program solves sliding puzzle using Misplaced heuristic
public class SlidingMisplaced
{
    //Board class (inner class)
    private class Board
	{
		private int[][] array;  	//array
		private int hvalue;			//heuristic value
		private Board parent;       //filled rows

		//constructor of board class
		private Board(int[][] array, int size)
		{
		  this.array = new int[size][size]; //create array

		  for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
			  this.array[i][j] = array[i][j]; 	//array Inherits passed array
				
			this.hvalue = heuristic(this);		//assign heuristic value
			this.parent = null;   				//pointer to it's parent object in list
		}
	}

  private Board initial; //initial board supplied by file input
  private Board goal; //goal board supplied by file input
  private int size; //board dimension size
  private PrintWriter pW; //passed PrintWriter for file I/O
  private long totTime = 0;
  private int swapLength = 0; //Keeps track of how many swaps
  

  //Constructor of SlidingMisplaced class
  public SlidingMisplaced(int[][] initial, int[][] goal, int sizeIn, PrintWriter PWOut)
  {
      this.initial = new Board(initial, sizeIn);
      this.goal = new Board(goal, sizeIn);
      this.size = sizeIn;
      this.pW = PWOut;
  }


  //Method solves sliding puzzle
  public void timedSolve()
  {
	//makes note of initial time before algo begins
    long startTime = System.currentTimeMillis();
	  
    LinkedList<Board> openList = new LinkedList<Board>();		//open list
    LinkedList<Board> closedList = new LinkedList<Board>();		//closed list

    openList.addFirst(initial); //adding initial board to openlist

    while (!openList.isEmpty()) //continues as long as there are states left to search
    {
		int best = selectBest(openList);		//select best board
		
		Board board = openList.remove(best); 	//remove board

		closedList.addLast(board); 				//add board to closed list

      if (complete(board)) 						//checks if goal board has been reached
      {
		//makes note of end time after solve has completed
		long endTime = System.currentTimeMillis();
	
		//calculates total time taken
		totTime = endTime - startTime;
		
        displayPath(board, totTime);			//display path to goal
        return;									//stop search
      }
      else //if path not complete, create children
      {
        LinkedList<Board> children = generate(board); //generate children
        for (int i = 0; i < children.size(); i++)
        {
          Board child = children.get(i); //pop children off list

          //if it is not in open or closed list then add to end of open list
          if (!exists(child, openList) && !exists(child, closedList))
              openList.addLast(child);
        }
      }
    }

	
	//makes note of end time after solve has completed
    long endTime = System.currentTimeMillis();
	
	//calculates total time taken
	totTime = endTime - startTime;
	
    //if no solution is found print no solution
    System.out.println("no solution");
	pW.println("no solution");
	
	System.out.println("Time taken: " + totTime);
	pW.println("Time taken: " + totTime);
  }

  //Method creates children of a board
  private LinkedList<Board> generate(Board board)
  {
    int i = 0, j = 0;
    boolean found = false;

    //loops, locate "0" in board for use in sliding calculations
    for (i = 0; i < size; i++)
    {
      for (j = 0; j < size; j++)
        if (board.array[i][j] == 0)
        {
          found = true;
          break;
        }

      if (found)
        break;
    }

    //create booleans to rule out illegal moves for children
	//by establishing which directions the empty slot has neighbors at
    boolean north, south, east, west;
    north = i == 0 ? false : true;
    south = i == size-1 ? false : true;
    east  = j == size-1 ? false : true;
    west  = j == 0 ? false : true;

    //new linkedlist for children to be attached to
    LinkedList<Board> children = new LinkedList<Board>();

    //creates children based on legal moves
    if (north) children.addLast(createChild(board, i, j, 'N')); //add N, S
    if (south) children.addLast(createChild(board, i, j, 'S'));	//E, W
    if (east) children.addLast(createChild(board, i, j, 'E'));	//children
    if (west) children.addLast(createChild(board, i, j, 'W'));	//if they exist

    return children;											//return children
  }

  //Method creates a child of a board by swapping empty slot in a
  //given direction
  private Board createChild(Board board, int i, int j, char direction)
  {
    Board child = copy(board);

    if (direction == 'N')
    {
      child.array[i][j] = child.array[i-1][j];
      child.array[i-1][j] = 0;
    }
    else if (direction == 'S')
    {
      child.array[i][j] = child.array[i+1][j];
      child.array[i+1][j] = 0;
    }
    else if (direction == 'E')
    {
      child.array[i][j] = child.array[i][j+1];
      child.array[i][j+1] = 0;
    }
    else
    {
      child.array[i][j] = child.array[i][j-1];
      child.array[i][j-1] = 0;
    }
	
	child.hvalue = heuristic(child);	//assign a heuristic value
	
    child.parent = board;				//assign parent to child

    return child;						//return child
  }
  
  //Method computes heuristic value of board based on misplaced values
  private int heuristic(Board board)
  {
	  int value = 0;						//initial heuristic value
	  
	  for (int i = 0; i < size; i++)		//go thru board and
		for (int j = 0; j < size; j++)		//count misplaced values
			if (board.array[i][j] != goal.array[i][j])
				value += 1;
			
	  return value;							//return heuristic value
  }

  //method locates the board with the minimum heuristic value in a list of boards
  private int selectBest(LinkedList<Board> list)
  {
	  int minValue = list.get(0).hvalue;		//initialize minimum
	  int minIndex = 0;							//value and location
	  
	  for (int i = 0; i < list.size(); i++)
	  {
		  int value = list.get(i).hvalue;
		  if (value < minValue)					//updates minimum if
		  {										//board with smaller
			  minValue = value;					//heuristic value is found
			  minIndex = i;
		  }
	  }
	  
	  return minIndex;							//returns minimum location
  }
  
  //Method creates copy of a board
  private Board copy(Board board)
  {
    return new Board(board.array, size);
  }

  //Method decides whether a board is complete
  private boolean complete(Board board)
  {
    return identical(board, goal);				//compare board with goal
  }

  //Method decides whether a board exists in a list
  private boolean exists(Board board, LinkedList<Board> list)
  {
    for (int i = 0; i < list.size(); i++)		//compare board with each element in list
      if (identical(board, list.get(i)))
        return true;

    return false;
  }

  //Method decides whether two boards are identical
  private boolean identical(Board p, Board q)
  {
    for (int i = 0; i < size; i++)
      for (int j = 0; j < size; j++)
        if (p.array[i][j] != q.array[i][j])
          return false;							//there is a mismatch then false

    return true;								//otherwise true
  }

  //Method displays path from initial to current board
  private void displayPath(Board board, long elapsedTime)
  {
    LinkedList<Board> list = new LinkedList<Board>();
	
	System.out.println("Sequence of boards: ");
	pW.println("Sequence of boards: ");

    Board pointer = board;						//start at current board

    while (pointer != null)						//go back towards initial board
    {
      list.addFirst(pointer);					//add boards to beginning of list

      pointer = pointer.parent;					//keep going back
    }

    for (int i = 0; i < list.size(); i++)		//print boards in list
      displayBoard(list.get(i));
	  
	//printing number of swaps to find answer and elapsed time to screen
	System.out.println("Number of Swaps: " + (list.size() - 1));
	System.out.println("Run Time: " + elapsedTime + " Milliseconds");
	
	//printing number of swaps to find answer and elapsed time to external file
	pW.println("Number of Swaps: " + (list.size() - 1));
	pW.println("Run Time: " + elapsedTime + " Milliseconds");
  }

  //Method displays board
  private void displayBoard(Board board)
  {
    for (int i = 0; i < size; i++)
    {
      for (int j = 0; j < size; j++)
      {
          System.out.printf("%2d ", board.array[i][j]);
          pW.printf("%2d ", board.array[i][j]);
      }
        System.out.println();
        pW.println();
    }
    System.out.println();
    pW.println();
  }
}
