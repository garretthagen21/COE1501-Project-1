import java.io.*;
import java.util.*;

public class Assignment1{

	
	public static void main(String args[]) throws FileNotFoundException { 
		Scanner fileScan = new Scanner(new FileInputStream("dict8.txt"));
		String dictType = args[0];
		
		StringBuilder sb;
		DictInterface theDictionary;
		
		/**Task2**
		if (dictType.equals("DLB"))
			theDictionary = new DLB();
		else**/
		
		theDictionary = new MyDictionary();
		
		while(fileScan.hasNextLine()) {
			theDictionary.add(fileScan.nextLine());
		}
	
	
	
	
	
	
	
	
	
	}
	
	
	
	
	
	
	
	
}