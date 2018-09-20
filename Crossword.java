import java.io.*;
import java.util.*;

public class Crossword{
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
			
			//We have reached the end of the puzzle
			if(j == endIndex && i==endIndex){
				printGameboard();
				System.exit(0);
			}
			
			for(int l=0;l<26;l++) {
				//i is not an end index so colStr[j]+alphabet[l] must be ATLEAST a prefix
				if (i < endIndex) {
					if(theDictionary.searchPrefix(colStr[j].append(alphabet[l])) > 0) {
						System.out.println(alphabet[l]);
						buildCrosswordPuzzle(i+1,j+1,endIndex);
					}
					else {
						colStr[j].deleteCharAt(colStr[j].length()-1);
					}
				}
				//i is an end index so colStr[j]+alphabet[l] must be a word
				else if(i == endIndex){
					if(theDictionary.searchPrefix(colStr[j].append(alphabet[l])) > 1) {
						buildCrosswordPuzzle(i,j+1,endIndex);
					}
					else {
						colStr[j].deleteCharAt(colStr[j].length()-1);
					}
			}
			
		}
		return;
		
		
	}
	private static boolean validCol(char letter,int j) {
		//if(theDictionary.searchPrefix)
	return true;
	}
	private static void printGameboard() {
		for(int i = 0;i<boardSize;i++) {
			System.out.println();
			for(int j = 0;j<boardSize;j++) {
				System.out.print(rowStr[i].charAt(j)+" ");
			}
		}
		
	}
	
	
	
}