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
	
	public static void main(String args[]) throws FileNotFoundException { 
		
		//String dictType = args[0];
		//String boardFile = args[1];
		String dictType = "DLB";
		String boardFile = "test5a.txt";
		
		Scanner fileScan = new Scanner(new FileInputStream("dict8.txt"));
		Scanner boardScan = new Scanner(new FileInputStream("Tests/"+boardFile));
		
		
		// Create the dictionary
		if (dictType.equals("DLB")) {
			theDictionary = new MyDictionary(); //new DLB();
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
		
		System.out.println("Initial crossword format: ");
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
		
		buildCrosswordPuzzle(0,0,boardSize-1);
		exitPuzzle();
	}
		
	
	public static void buildCrosswordPuzzle(int i,int j,int endIndex) {
			int iNext = 0;
			int jNext = 0;
			char boardChar;
			
			//Determine what the next search index should be
			if(i>endIndex) {
				printGameboard();
				if(!DLBMode) {
					exitPuzzle();
				}
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
				
				if(safe(i,j,endIndex)) {			
					buildCrosswordPuzzle(iNext,jNext,endIndex);
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
					if(safe(i,j,endIndex)) {			
						buildCrosswordPuzzle(iNext,jNext,endIndex);	
					}
					//Backtracking or the letter we added is invalid
					rowStr[i].deleteCharAt(j);
				    colStr[j].deleteCharAt(i);
				}
				
			}
			
		}
	private static boolean safe(int i, int j,int endIndex) {	
		StringBuilder rowString = new StringBuilder();
		StringBuilder colString = new StringBuilder();
		
		int colLast = i;
		int rowLast = j;
		boolean dashEncountered = false;
		
		//Current character is a dash so we dont want to include it when we build the string were checking
		//If a dash is at the 0th index then
		if(rowStr[i].charAt(j) == '-') {
				rowLast--;
				colLast--;
			    dashEncountered = true;	
		}
		
		//Get the starting index for each string. 
		//Will either be index of most recent '-' or index 0 of StringBuilder
		for(int q = rowLast; q>=0;q--) {
			if(rowStr[i].charAt(q) == '-') {
					break;
				}
		   rowString.append(rowStr[i].charAt(q));
		}
		for(int q = colLast;q>=0;q--) {
			if(colStr[j].charAt(q) == '-') {
					break;
				}
		   colString.append(colStr[j].charAt(q));
		}
		//We added the letters in opposite order so now we must reverse them
		rowString.reverse();
		colString.reverse();
		
		
		/*Condition 1: If we reach an end index or or current character is a '-' the SB must be a word 
		 * or "" (back to back '-' characters OR '-' at index 0)
		 */
		
		/*Condition 2: If we are not an end index AND the current character is not a '-' the SB must be
		 * atleast a prefix
		 */
		
		//Check above conditions for rowStr
		if(j == endIndex || dashEncountered){	
			if(!(rowString.length() == 0 || (theDictionary.searchPrefix(rowString) > 1))) { 
				return false;
			}				
		}
		else if(theDictionary.searchPrefix(rowString) != 1 && theDictionary.searchPrefix(rowString) != 3){
				return false;
		}
		
		//Check above conditions for colStr
		if(i == endIndex || dashEncountered){
			if(!(colString.length() == 0 || (theDictionary.searchPrefix(colString) > 1))) { 
				return false;
			}	
		}
		else if(theDictionary.searchPrefix(colString) != 1 && theDictionary.searchPrefix(colString) != 3){
			return false;
		}
		
		return true;
	}
	private static void printGameboard() {
		solutionNumber++;
		System.out.println("\n------------ Solution #"+solutionNumber+" ------------\n");
		for(int i = 0;i<boardSize;i++) {
			System.out.println();
			for(int j = 0;j<boardSize;j++) {
				System.out.print(rowStr[i].charAt(j)+" ");
			}
		}
	}
	
	private static void exitPuzzle() {
		System.out.println("\n\nSearching complete! " +solutionNumber+" solutions were found.");
		System.exit(0);
	}
	
	
}