import java.io.*;
import java.util.*;

public class Crossword{
		static int solutionNumber = 0;
		static int boardSize;
		static StringBuilder rowStr [];
		static StringBuilder colStr [];
		static char gameBoard [][] = null;
		static char [] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
		static DictInterface theDictionary;
		static boolean DLBMode = false;
		static int endIndex;
		static long startTime;
	
	public static void main(String args[]) throws FileNotFoundException { 
		
		if(args.length < 2) {
			System.out.println("You must specify 2 arguments!\nUsage: $ java Crossword <testfile.txt> <dictionary type>");
			System.out.println("Example: $ java Crossword test3a.txt DLB");
			System.exit(0);
		}
		String boardFile = args[0];
		String dictType = args[1];
		
		
		//For use in Eclipse
		//String dictType = "DLB";
		//String boardFile = "test6c.txt";
		
		Scanner fileScan = new Scanner(new FileInputStream("dict8.txt"));
		Scanner boardScan = new Scanner(new FileInputStream("Tests/"+boardFile));
		
		
		// Create the dictionary
		if (dictType.equals("DLB")) {
			theDictionary = new DLB();
			DLBMode = true;
		}
		else {
			theDictionary = new MyDictionary();
		}
		
		while(fileScan.hasNextLine()) {
			theDictionary.add(fileScan.nextLine());
		}
		
		// Read in the crossword format, print it out, and initialize string builder objects
		boardSize = boardScan.nextInt();
		rowStr = new StringBuilder[boardSize];
		colStr = new StringBuilder[boardSize];
		gameBoard = new char[boardSize][boardSize];
		
		System.out.println("Solving with "+dictType+". Crossword format for "+boardFile+":");
		for(int i = 0;i<boardSize;i++) {
			char currentLine[] = boardScan.next().toCharArray();
			colStr[i] = new StringBuilder();
			rowStr[i] = new StringBuilder();
			System.out.println();
			for(int j = 0;j<boardSize;j++) {
				gameBoard[i][j] = currentLine[j];
				System.out.print(gameBoard[i][j]+" ");

			}
		}
		
		fileScan.close();
		boardScan.close();
		
		endIndex = boardSize-1;
		startTime = System.currentTimeMillis();
		buildCrosswordPuzzle(0,0);
		exitPuzzle();
	}
		
	
	public static void buildCrosswordPuzzle(int i,int j) {
			int iNext = 0;
			int jNext = 0;
			char boardChar;
			
			//Determine what the next search index should be
			if(i>endIndex) {
				
				if(solutionNumber % 10000 == 0) {
					printGameboard();
				}
				solutionNumber++;
				//Exit if we are using my dictionary
				if(!DLBMode)
					exitPuzzle();
				
				return;
			}
			else if(j==endIndex) {
				iNext = i+1;
				jNext = 0;
			}
			else { 
				iNext = i;
				jNext = j+1;	
			}
			
			boardChar = gameBoard[i][j];
			
			//Add the character is immutable so add it immediately. If valid move forward, if not backtrack
			if(boardChar != '+') {
				colStr[j].append(boardChar);
				rowStr[i].append(boardChar);
				
				if(safe(i,j)) {	
					buildCrosswordPuzzle(iNext,jNext);
				}
				
				rowStr[i].deleteCharAt(j);
				colStr[j].deleteCharAt(i);
			}
			//Try all characters
			else {
				for(int l=0;l<26;l++) {
					//Attempt to place the letter
					colStr[j].append(alphabet[l]);
					rowStr[i].append(alphabet[l]);
					
					//All of our conditions have been met so the character placement is valid. Recurses
					if(safe(i,j)) {			
						buildCrosswordPuzzle(iNext,jNext);	
					}
					//Backtracking or the letter we added is invalid
					rowStr[i].deleteCharAt(j);
				    colStr[j].deleteCharAt(i);
				   
				}
				
			}
			
		}

private static boolean safe(int i,int j) {	
	/*Condition 1: If we reach an end index or or current character is a '-' the SB must be a word.
	 *NOTE: If a '-' is encountered at index 0 or there are '-' at index i and index i-1 (i.e '--') this is legal too 

	 */
	/*Condition 2: If we are not an end index AND the current character is not a '-' the SB must be
	 * atleast a prefix
	 */
	return checkRow(i,j) && checkCol(i,j);
}

private static boolean checkCol(int i, int j) {	
		int end = i;
		int start = 0;
		boolean dashEncountered = false;
		
		//Current character is a dash so we dont want to include it when we find the bounds for the string were checking
		//If the dash is at index 0 or there are back to back dashes return true
		if(colStr[j].charAt(i) == '-') {
				if(i == 0) {
					return true;
				}
				else if(colStr[j].charAt(i-1) == '-') {
					return true;
				}
				end--;  //Don't want to include the '-' in our word check
			    dashEncountered = true;	
		}
		
		
		
		//Get the starting index for the string
		//Will either be index of most recent '-'+1 or index 0 of StringBuilder
		for(int q = end; q>=0;q--) {
			if(colStr[j].charAt(q) == '-') {
					start = q+1;
					break;
				}
		}
		
		int colResult = theDictionary.searchPrefix(colStr[j],start,end);
		
		//Check above conditions for colStr. Must be a word in this case
		if(i == endIndex || dashEncountered){
			if(colResult > 1) { 
				return true;
			}	
		}
		else if(i < endIndex) {
			if(colResult == 1 || colResult == 3){
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean checkRow(int i,int j) {
		//StringBuilder rowString = new StringBuilder();
		
		int end = j;
		int start = 0;
		boolean dashEncountered = false;
		
		//Current character is a dash so we dont want to include it when we build the string were checking
		//If a dash is at the 0th index then
		if(rowStr[i].charAt(j) == '-') {
				if(j == 0){
					return true;
				}
				else if(rowStr[i].charAt(j-1) == '-') {
					return true;
				}
					
				end--;
			    dashEncountered = true;	
		}
		
		
		//Get the starting index for each string. 
		//Will either be index of most recent '-' or index 0 of StringBuilder
	
		for(int q = end; q>=0;q--) {
			if(rowStr[i].charAt(q) == '-') {
					start = q+1;
					break;
				}
		}
		
		
		int rowResult = theDictionary.searchPrefix(rowStr[i],start,end);
		
		//Must be a word (current character is a '-' or we are at an end index)
		if(j == endIndex || dashEncountered){	
			if(rowResult > 1) { 
				return true;
			}				
		}
		//Less than end index and is not a '-' character so it must be a prefix
		else if(j < endIndex) {
			if(rowResult == 1 || rowResult == 3){
				return true;
			}
		}
		return false;
	}
	private static void printGameboard() {
		System.out.println("\n\n------------ Solution #"+solutionNumber+ " |||| Time Elapsed: "+(System.currentTimeMillis()-startTime)*0.001+" seconds------------");
		for(int i = 0;i<boardSize;i++) {
			System.out.println();
			for(int j = 0;j<boardSize;j++) {
				System.out.print(rowStr[i].charAt(j)+" ");
			}
		}
	}
	
	private static void exitPuzzle() {
		System.out.println("\n\nSearching complete! " +(solutionNumber)+" solutions were found after "+(System.currentTimeMillis() - startTime)*0.001+" seconds.");
		System.exit(0);
	}
	
	
}