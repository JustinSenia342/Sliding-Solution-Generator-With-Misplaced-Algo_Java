/*
 * Name: Justin Senia
 * E-Number: E00851822
 * Date: 10/20/2017
 * Class: COSC 461
 * Project: #2
 */
import java.io.*;
import java.util.*;

public class SlidingMisplacedTester
{

    //Main method for testing
    public static void main(String[] args) throws IOException
    {

      //creating buffered reader for getting user input
      java.io.BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));

      //message welcoming to the program/giving instructions
      System.out.println("*****************************************");
      System.out.println("*    Welcome to the Sliding program     *");
	  System.out.println("*    (Uses the Misplaced Heuristic)     *");
      System.out.println("*****************************************");
      System.out.println("*  Please enter in a filename to start  *");
      System.out.println("* or type quit to terminate the program *");
      System.out.println("*****************************************");

      //start a loop that continues querying for input as long as user
      //does not enter "quit" (without the quotes)
      while (true)
      {
        System.out.print("Please make your entry now: ");
        String userIn = ""; //used for file entry and/or quitting

        userIn = keyIn.readLine(); //reading user input

        if (userIn.equalsIgnoreCase("quit")) //if user typed quit, stops program
          break;
        else{
              try
              {
                //establishing working directory for file I/O
                String currentDir = System.getProperty("user.dir");
                File fIn = new File(currentDir + '\\' + userIn);

                //using scanner with new input file based on user input
                Scanner scanIn = new Scanner(fIn);

                //creating printwriter for file output
                File fOut = new File("output_" + userIn);
                PrintWriter PWOut = new PrintWriter(fOut, "UTF-8");

                //scanning external file for Board dimensions
                int size = scanIn.nextInt();

                //Zeros are considered empty spaces
                int[][] initialBoard = new int[size][size];
                int[][] finalBoard = new int[size][size];

                //gets information from files to populate initialBoard
                for (int i = 0; i < size; i++)
                {
                  for (int j = 0; j < size; j++)
                  {
                    initialBoard[i][j] = scanIn.nextInt();
					//System.out.print(initialBoard[i][j] + " ");
                  }
				  //System.out.println("");
                }

                //gets information from files to populate finalBoard
                for (int i = 0; i < size; i++)
                {
                  for (int j = 0; j < size; j++)
                  {
                    finalBoard[i][j] = scanIn.nextInt();
					//System.out.print(finalBoard[i][j] + " ");
                  }
				  //System.out.println("");
                }

                //Outputting formatted messages with read info
                System.out.println("Sliding Misplaced Output for " + userIn);
                System.out.println("(" + size + " x " + size + " Board)");
				System.out.println(" ");

                PWOut.println("Sliding Misplaced Output for " + userIn);
                PWOut.println("(" + size + " x " + size + " Board)");
				PWOut.println(" ");

                //Creating new SlidingMisplaced object of SlidingMisplaced class with parameters
                //read in from files
                SlidingMisplaced sM = new SlidingMisplaced(initialBoard, finalBoard, size, PWOut);
                sM.timedSolve(); //starting solve method

                //closing I/O objects
                scanIn.close();
                PWOut.close();
              }
              catch (IOException e)
              {
                ;
              }
            }
      }
    }
}
