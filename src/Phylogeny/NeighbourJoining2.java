package Phylogeny;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class NeighbourJoining2 {
	public static class Coord {
	    public final char x;
	    public final char y;
	    public Coord(char x, char y) {
	        this.x = x;
	        this.y = y;
	    }
	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof Coord)) return false;
	        Coord coord = (Coord) o;
	        return x == coord.x && y == coord.y;
	    }
	    @Override
	    public int hashCode() {
	    	return java.util.Objects.hash(x,y);
	    }
	}
	public static class ConnectedTo {
		Node to;
		int weight;
		public ConnectedTo(Node to, int weight) {
			this.to = to;
			this.weight = weight;
		}
	}
	public static class Node {
		Character c;
		List<ConnectedTo> children;
		public Node(char c) {
			children = new ArrayList<>();
		}
		public void addChild(Node c, int weight) {
			children.add(new ConnectedTo(c, weight));
		}
		public List<ConnectedTo> getChildren() {
			return children;
		}
	}
	public static class Tree {
		public HashMap<Character, Node> charToNode;
		public Node root;
		public Tree(Character a, Character b, int weight) {
			//For neatness, rooting the tree
			charToNode = new HashMap<>();
			root = new Node('*');
			charToNode.put('*', root);
			addEdge('*', a, weight/2);
			addEdge('*', b, weight/2);
			System.out.println("cnstructor");
		}
		public void addEdge(Character from, Character to, int weight) {
			System.out.println(from);
			System.out.println(to);
			Node current = new Node(to);
			charToNode.put(to, current);
			charToNode.get(from).addChild(current, weight);
		}
		public void printTree() {
			LinkedList<Node> q = new LinkedList<>();
			q.add(root);
			while (!q.isEmpty()) {
				Node current = q.remove();
				System.out.println(current.c);
				List<ConnectedTo> edges = current.getChildren();
				for (ConnectedTo c : edges) {
					q.add(c.to);
				}
			}
		}
	}
	public static class NodePair {
		public Character i, j;
		public NodePair(Character i, Character j) {
			this.i = i;
			this.j = j;
		}
	}
	public static class DstarAndMin {
		NodePair pair;
		HashMap<Coord, Integer> dstar;
		public DstarAndMin(HashMap<Coord, Integer> dstar, NodePair pair) {
			this.dstar = dstar;
			this.pair = pair;
		}
	}
	public static DstarAndMin computeDstarAndMin(HashMap<Coord, Integer> matrixD, 
			int nElements, HashMap<Character, Integer> totalDistances, List<Character> remainingChars) {
		int min = Integer.MAX_VALUE;
		NodePair minPair = null;
		HashMap<Coord, Integer> dstar = new HashMap<>();
		for (char i : remainingChars) {
			for (char j : remainingChars) {
				if (i==j) dstar.put(new Coord(i,j), 0);
				else {
					dstar.put(new Coord(i,j),
							(nElements-2) * matrixD.get(new Coord(i,j))
							- totalDistances.get(i) - totalDistances.get(j));
					if (dstar.get(new Coord(i,j)) <= min) {
						min = dstar.get(new Coord(i,j));
						minPair = new NodePair(i,j);
					}
				}
			}
		}
		System.out.println(min);
		return new DstarAndMin(dstar, minPair);
	}
	public static HashMap<Character, Integer> computeTotalDistances(
			HashMap<Coord, Integer> matrixD, int nElements, List<Character> remainingChars) {
		
		HashMap<Character, Integer> totalDistances = new HashMap<>();
		for (char i : remainingChars) {
			int sum = 0;
			for (char j : remainingChars) {
				sum += matrixD.get(new Coord(i,j));
			}
			totalDistances.put(i, sum);
		}
		return totalDistances;
	}
	private static HashMap<Coord, Integer> computeNewMatrixD(
			NodePair minPair, List<Character> newRemainingChars) {
		HashMap<Coord, Integer> newMatrixD = new HashMap<>();
		newRemainingChars.remove(minPair.i);
		newRemainingChars.remove(minPair.j);
		for (char i : newRemainingChars) {
			for (char j : newRemainingChars) {
				Coord current = new Coord(i,j);
			}
		}
		
		return newMatrixD;
	}
	public static Tree neighbourJoin(HashMap<Coord, Integer> matrixD, 
			int nElements, List<Character> remainingChars, HashSet<Character> usedChars) {
		System.out.println("Remaining chars");
		for (char c : remainingChars) {
			System.out.print(c);
			System.out.print(" ");
		}
		System.out.println();
		System.out.println("------");
		
		if (nElements == 2) {
			return new Tree(
					remainingChars.get(0), 
					remainingChars.get(1),
					matrixD.get(new Coord(remainingChars.get(0), remainingChars.get(1))));
		}
		
		HashMap<Character, Integer> totalDistances = computeTotalDistances(
				matrixD, nElements, remainingChars);
		DstarAndMin dstarAndMin = computeDstarAndMin(
				matrixD, nElements, totalDistances, remainingChars);
		HashMap<Coord, Integer> dstar = dstarAndMin.dstar;
		NodePair minPair = dstarAndMin.pair;
		int delta = (totalDistances.get(minPair.i) - 
						totalDistances.get(minPair.j)) / (nElements -2);
		int limbLenI = (matrixD.get(new Coord(minPair.i, minPair.j)) + delta) / 2;
		int limbLenJ = (matrixD.get(new Coord(minPair.i, minPair.j)) - delta) / 2;
		printMatrix(dstar, remainingChars);
		System.out.println(limbLenI);
		System.out.println(limbLenJ);
		System.out.println(minPair.i);
		System.out.println(minPair.j);
		
		//add all old nodes unaffected
		List<Character> newRemainingChars = new ArrayList<>(remainingChars);
		HashMap<Coord, Integer> newMatrixD = new HashMap<>();
		newRemainingChars.remove(minPair.i);
		newRemainingChars.remove(minPair.j);
		for (char i : newRemainingChars) {
			for (char j : newRemainingChars) {
				Coord current = new Coord(i,j);
				newMatrixD.put(current, matrixD.get(current));
			}
		}
		printMatrix(matrixD, remainingChars);
		
		//make new char
		Character daNewCharacter = 'm';
		for (int i=0; i<26; i++) { 
			if (!usedChars.contains((char) ('A'+i))) {
				daNewCharacter = (char) ('A'+i);
				usedChars.add(daNewCharacter);
				newRemainingChars.add(daNewCharacter);
				break;
			}
		}
		//add new row and col for new char
		for (char i : newRemainingChars) {
			if (i == daNewCharacter) {
				newMatrixD.put(new Coord(i, daNewCharacter), 0);
				newMatrixD.put(new Coord(daNewCharacter, i), 0);
			} else {
				int newDValForNewChar = ( matrixD.get(new Coord(i, minPair.i)) 
						+ matrixD.get(new Coord(i, minPair.j)) 
						- matrixD.get(new Coord(minPair.i, minPair.j)) ) / 2;
				newMatrixD.put(new Coord(i, daNewCharacter), newDValForNewChar);
				newMatrixD.put(new Coord(daNewCharacter, i), newDValForNewChar);
			}
		}
		Tree current = neighbourJoin(
				newMatrixD, nElements - 1, newRemainingChars, usedChars);
		current.addEdge(daNewCharacter, minPair.i,  limbLenI);
		current.addEdge(daNewCharacter, minPair.j,  limbLenJ);
		return current;
	}
	public static void printMatrix(
			HashMap<Coord, Integer> matrixD, List<Character> remainingChars) {
		for (char i : remainingChars) {
			for (char j : remainingChars) {
				System.out.print(matrixD.get(new Coord(i,j)));
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	public static void printMatrix(HashMap<Coord, Integer> matrixD, int nElements) {
		for (int i=0; i<nElements; i++) {
			for (int j=0; j<nElements; j++) {
				System.out.print(matrixD.get(new Coord((char)('A'+i),(char)('A'+j))));
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	private static void printTable(Integer[][] table) {
		for (int i=0; i<table.length; i++) {
			for (int j=0; j<table[0].length; j++) {
				System.out.print(table[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	public static void main(String[] args) {
		Integer[][] matrixDtmp = new Integer[4][4];
		matrixDtmp[0] = new Integer[]{0,2,4,6};
		matrixDtmp[1] = new Integer[]{2,0,4,6};
		matrixDtmp[2] = new Integer[]{4,4,0,6};
		matrixDtmp[3] = new Integer[]{6,6,6,0};
		int nElements = 4;
		List<Character> remainingChars = new ArrayList<>(); //chars to join
		remainingChars.add('A');
		remainingChars.add('B');
		remainingChars.add('C');
		remainingChars.add('D');
		HashMap<Coord, Integer> matrixD = convertToMatrixMap(matrixDtmp);
		Tree result = neighbourJoin(matrixD, 
				nElements, remainingChars, new HashSet<>(remainingChars));
		result.printTree();
	}
	private static HashMap<Coord, Integer> convertToMatrixMap(
			Integer[][] matrixDtmp) {
		HashMap<Coord, Integer> matrixD = new HashMap<>();
		for (int i=0; i<matrixDtmp.length; i++) {
			for (int j=0; j<matrixDtmp[0].length; j++) {
				matrixD.put(new Coord((char) ('A'+i),(char) ('A'+j)),
						matrixDtmp[i][j]);
			}
		}
		return matrixD;
	}
}
