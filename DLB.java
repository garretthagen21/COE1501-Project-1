import java.util.*;

public class DLB implements DictInterface{
	
	private Node root;
	private char terminateChar = '^';
	
	private class Node{
		private char letter;
		private Node sibling;
		private Node child;
		
	}
	
	public DLB() {
		root = new Node();
	}
	
	//Search through the nodes like a linked list and if we find the correct char return the Node
	private Node getSibling(Node currentNode,char desiredChar){
		while(currentNode != null) {
			if(currentNode.letter == desiredChar) {
				return currentNode;
			}
			currentNode = currentNode.sibling;	
		}
		return null;
	}
	private Node addSibling(Node currentNode,char desiredChar) {
		
		if(currentNode == null) {
			Node newNode = new Node();
			newNode.letter = desiredChar;
			return newNode;
		}
		
		while(currentNode != null) {
			//We found the correct node
			if(currentNode.letter == desiredChar) {
				return currentNode;
			}
			//We are at the last node and havent found our character so add the node
			if(currentNode.sibling == null) {	
				Node newNode = new Node();
				newNode.letter = desiredChar;
				currentNode.sibling = newNode;
				return newNode;
			}
			//Else keep iterating	
			currentNode = currentNode.sibling;
		}
		return null;
	}
	public boolean add(String s) {
		s+=terminateChar;
		
		Node currentNode = root;
	
		for(int i = 0; i<s.length();i++) {
			Node newNode;
			//Get to the appropriate node in the sibling list for the child node
			if(currentNode.child == null) {
				newNode = new Node();
				newNode.letter = s.charAt(i);
				currentNode.child = newNode;	
			}
			else {
				newNode = addSibling(currentNode.child,s.charAt(i));
			}
			
			//Move to the next level of the tree
			currentNode = newNode; 	
		}
		return true;
	}
	
	

	public int searchPrefix(StringBuilder s) {
		return searchPrefix(s,0,s.length()-1);
	}


	public int searchPrefix(StringBuilder s, int start, int end) {
		Node currentNode = root;
		
		for(int i = start;i<=end;i++) {
			Node newNode = getSibling(currentNode.child,s.charAt(i));
			
			//If we hit a dead end before we reach the end of the SB, it is not a prefix or a word
			if(newNode == null){
				return 0;
			}
			//System.out.println("DLB Letter: "+newNode.letter+" Word letter: "+s.charAt(i));
			currentNode = newNode;
		}
		
		//We reached the end of the SB, so we need to determine if it is a prefix, a word, or a word+prefix
		Node endNode = getSibling(currentNode.child,terminateChar);
		
		//Prefix but not a word
		if(endNode == null) {
			return 1;
		}
		//Terminator char was found,but there are no siblings to it so it is a word only
		else if(endNode.sibling == null) {
			return 2;
		}
		//Terminator char was found and there are siblings in the list so it is a word+prefix
		return 3;
			
	}
	
	
	
	
	
	
	
	
}