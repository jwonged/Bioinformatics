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
	public static class NodePair {
		public Character i, j;
		public NodePair(Character i, Character j) {
			this.i = i;
			this.j = j;
		}
	}
	public static NodePair computeDstarAndMin(HashMap<Coord, Integer> matrixD, 
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
		return minPair;
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
	public static Character makeNewChar(HashSet<Character> usedChars) {
		//Assuming that there won't be more than 26 chars used
		Character daNewCharacter = 'm';
		for (int i=0; i<26; i++) { 
			if (!usedChars.contains((char) ('A'+i))) {
				daNewCharacter = (char) ('A'+i);
				return daNewCharacter;
			}
		}
		return daNewCharacter;
	}
	public static HashMap<Coord, Integer> recomputeMatrixD(List<Character> newRemainingChars,
			NodePair minPair, HashMap<Coord, Integer> matrixD, List<Character> remainingChars, Character daNewCharacter) {
		HashMap<Coord, Integer> newMatrixD = new HashMap<>();
		
		for (char i : newRemainingChars) {
			for (char j : newRemainingChars) {
				Coord current = new Coord(i,j);
				newMatrixD.put(current, matrixD.get(current));
			}
		}
		printMatrix(matrixD, remainingChars);
		
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
		return newMatrixD;
	}
	public static Tree neighbourJoin(HashMap<Coord, Integer> matrixD, 
			int nElements, List<Character> remainingChars, HashSet<Character> usedChars) {
		if (nElements == 2) {
			return new Tree(
					remainingChars.get(0), 
					remainingChars.get(1),
					matrixD.get(new Coord(remainingChars.get(0), remainingChars.get(1))));
		}
		
		HashMap<Character, Integer> totalDistances = 
				computeTotalDistances(matrixD, nElements, remainingChars);
		NodePair minPair = 
				computeDstarAndMin(matrixD, nElements, totalDistances, remainingChars);
		int delta = (totalDistances.get(minPair.i) - 
						totalDistances.get(minPair.j)) / (nElements -2);
		int limbLenI = (matrixD.get(new Coord(minPair.i, minPair.j)) + delta) / 2;
		int limbLenJ = (matrixD.get(new Coord(minPair.i, minPair.j)) - delta) / 2;
		
		//add all old nodes unaffected
		List<Character> newRemainingChars = new ArrayList<>(remainingChars);
		
		//make new char
		Character daNewCharacter = makeNewChar(usedChars);
		usedChars.add(daNewCharacter);
		newRemainingChars.add(daNewCharacter);
		
		newRemainingChars.remove(minPair.i);
		newRemainingChars.remove(minPair.j);
		HashMap<Coord, Integer> newMatrixD = recomputeMatrixD(newRemainingChars, 
															minPair, 
															matrixD, 
															remainingChars, 
															daNewCharacter);
		
		
		/*
		for (char i : newRemainingChars) {
			for (char j : newRemainingChars) {
				Coord current = new Coord(i,j);
				newMatrixD.put(current, matrixD.get(current));
			}
		}
		printMatrix(matrixD, remainingChars);
		
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
		*/
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
	public static void main(String[] args) {
		Integer[][] matrixDtmp = new Integer[4][4];
		matrixDtmp[0] = new Integer[]{0,2,4,6};
		matrixDtmp[1] = new Integer[]{2,0,4,6};
		matrixDtmp[2] = new Integer[]{4,4,0,6};
		matrixDtmp[3] = new Integer[]{6,6,6,0};
		int nElements = 4;
		List<Character> remainingChars = new ArrayList<>();
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
		//Just for convenience so i don't need to keep using .put()
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
