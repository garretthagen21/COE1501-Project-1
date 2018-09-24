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
	
	public static void main(String args[]) throws FileNotFoundException { 
		
		//String dictType = args[0];
		//String boardFile = args[1];
		String dictType = "MyDict";
		String boardFile = "test3a.txt";
		
		Scanner fileScan = new Scanner(new FileInputStream("dict8.txt"));
		Scanner boardScan = new Scanner(new FileInputStream("Tests/"+boardFile));
		
		
		// Create the dictionary
		if (dictType.equals("DLB"))
			theDictionary = new MyDictionary(); //new DLB();
		else
			theDictionary = new MyDictionary();
		
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
		buildCrosswordPuzzle(0,0,boardSize-1);
	}
		
	
	public static void buildCrosswordPuzzle(int i,int j,int endIndex) {
			char boardChar = gameBoard[i][j];
			
			for(int l=0;l<26;l++) {
				
				//Attempt to place the letter
				if(boardChar == '+') {
					colStr[j].append(alphabet[l]);
					rowStr[i].append(alphabet[l]);
				}
				else {
					colStr[j].append(boardChar);
					rowStr[i].append(boardChar);
				}
				
				
				//All of our conditions have been met so the character placement is valid. Recurses
				if(safe(i,j,endIndex)) {	
					
					int iNext = 0;
					int jNext = 0;
					//Solution found
					if(j==endIndex && i==endIndex){
						printGameboard();
						//System.exit(0);
					}	
					
					else { //Reached the end of the row (i.e used all columns) so move to the next row and reset j
						if(j==endIndex) {
							iNext = i+1;
							jNext = 0;
						}
						else { 
							iNext = i;
							jNext = j+1;	
						}
						buildCrosswordPuzzle(iNext,jNext,endIndex);
					}	
				}
				//Either we found a solution, we are backtracking, or the letter we added is invalid
				rowStr[i].deleteCharAt(j);
			    colStr[j].deleteCharAt(i);
				
			}
			return;
		}
	private static boolean safe(int i, int j,int endIndex) {	
		StringBuilder rowString;
		StringBuilder colString;
		int colStart = 0,colEnd = i;
		int rowStart = 0,rowEnd = j;
		
		
		//Substring is (inclusive,exclusive) so we want to omit the dash when we build our string to check
		if(colStr[j].charAt(i) != '-')
			colEnd++;
			rowEnd++; 
		
		for(int start = i; start>-1;start--) {
			if(colStr[j].charAt(start) == '-' && start != i) {
					colStart = start+1;
					break;
				}			
		}
		for(int start = j; start>-1;start--) {
			if(rowStr[i].charAt(start) == '-' && start != j) {
					rowStart = start+1;
					break;
				}			
		}
		rowString = new StringBuilder(rowStr[i].substring(rowStart,rowEnd));
		colString = new StringBuilder(colStr[i].substring(colStart,colEnd));
		System.out.println(rowString.toString());
		System.out.println(colString.toString());
		
		if (i < endIndex){ 
			/*if(colStr[j].charAt(i) == '-') {
				int startingDash = 0;
				//Start at the current dash and work backwards til we see another dash
				for(int q = i; q>-1;q--) {
					if(colStr[j].charAt(q) == '-')
						startingDash = q;
						break;
				}
				String subWord = colStr[j].substring(startingDash,i);
				System.out.print(subWord+":");
				if(!subWord.equals("")){
						if(!(theDictionary.searchPrefix(new StringBuilder(subWord)) > 1)){
							System.out.print("FAILED\n");
							return false;			
						}
				    }
				System.out.print("PASSED\n");
				}*/
			if(theDictionary.searchPrefix(colString) < 1){
				return false;
			}
		}
		if (j < endIndex){ 
			/*if(rowStr[i].charAt(j) == '-') {
				int startingDash = 0;
				//Start at the current dash and work backwards til we see another dash
				for(int q = j; q>-1;q--) {
					if(rowStr[i].charAt(q) == '-')
						startingDash = q;
						break;
				}

				String subWord = rowStr[i].substring(startingDash,j);
				System.out.print(subWord+":");
				if(!subWord.equals("")){
						if(!(theDictionary.searchPrefix(new StringBuilder(subWord)) > 1)){
							System.out.print("FAILED\n");
							return false;
						}
				    }
				System.out.print("PASSED\n");

				}*/
			if(theDictionary.searchPrefix(rowString) < 1){
				return false;
			}
		}
	
		if(j == endIndex){
			/*int recentDash = rowStr[i].lastIndexOf("-");
			if(recentDash != -1) {
				String subWord = rowStr[i].substring(recentDash,endIndex);
				System.out.print(subWord+":");
				if(!subWord.equals("")){
						if(!(theDictionary.searchPrefix(new StringBuilder(subWord)) > 1)){
							System.out.print("FAILED\n");
							return false;
						}
				    }
				System.out.print("PASSED\n");
				}*/
			if(!(theDictionary.searchPrefix(rowString) > 1)) { 
				return false;
			}
				
			
		}
		if(i == endIndex){
			/*int recentDash = colStr[j].lastIndexOf("-");
			if(recentDash != -1) {
				String subWord = colStr[j].substring(recentDash,endIndex);
				System.out.print(subWord+":");
				if(!subWord.equals("")){
						if(!(theDictionary.searchPrefix(new StringBuilder(subWord)) > 1)){
							System.out.print("FAILED\n");
							return false;
						}
				    }	
				System.out.print("PASSED\n");
				}*/
			if(!(theDictionary.searchPrefix(colString) > 1)) { 
				return false;
			}
				
			
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
	
	
	
}