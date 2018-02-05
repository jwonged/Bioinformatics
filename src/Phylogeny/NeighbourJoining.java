package Phylogeny;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NeighbourJoining {
	public static class Node {
		
	}
	public static class NodePair {
		public int i, j;
		public NodePair(int i, int j) {
			this.i = i;
			this.j = j;
		}
	}
	public static class DstarAndMin {
		NodePair pair;
		Integer[][] dstar;
		public DstarAndMin(Integer[][] dstar, NodePair pair) {
			this.dstar = dstar;
			this.pair = pair;
		}
	}
	public static DstarAndMin computeDstarAndMin(Integer[][] matrixD, 
			int nElements, List<Integer> totalDistances) {
		int min = Integer.MAX_VALUE;
		NodePair minPair = null;
		Integer[][] dStar = new Integer[nElements][nElements];
		for (int i=0; i<nElements; i++) {
			for (int j=0; j<nElements; j++) {
				if (i==j) dStar[i][j] = 0;
				else {
					dStar[i][j] = (nElements-2) * matrixD[i][j] 
							- totalDistances.get(i) - totalDistances.get(j);
					if (dStar[i][j] <= min) {
						min = dStar[i][j];
						minPair = new NodePair(i,j);
					}
				}
			}
		}
		System.out.println(min);
		return new DstarAndMin(dStar, minPair);
	}
	public static List<Integer> computeTotalDistances(Integer[][] matrixD, int nElements) {
		List<Integer> totalDistances = new ArrayList<>();
		for (int i=0; i<nElements; i++) {
			int sum = 0;
			for (int j=0; j<nElements; j++) {
				sum += matrixD[i][j];
			}
			totalDistances.add(sum);
		}
		return totalDistances;
	}
	public static Node neighbourJoin(Integer[][] matrixD, 
			int nElements, HashMap<Integer, Character> mapIndexToChar) {
		List<Integer> totalDistances = computeTotalDistances(matrixD, nElements);
		DstarAndMin dstarAndMin = computeDstarAndMin(matrixD, nElements,totalDistances);
		Integer[][] dstar = dstarAndMin.dstar;
		NodePair minPair = dstarAndMin.pair;
		int delta = (totalDistances.get(minPair.i) - 
						totalDistances.get(minPair.j)) / (nElements -2);
		int limbLenI = (matrixD[minPair.i][minPair.j] + delta)/2;
		int limbLenJ = (matrixD[minPair.i][minPair.j] - delta)/2;
		
		HashMap<Integer, Character> newMapIndexToChar = new HashMap<>(mapIndexToChar);
		Integer[][] newMatrixD = new Integer[nElements-1][nElements-1];
		int row = 0, col = 0;
		for (int i=0; i<matrixD.length; i++) {
			col = 0;
			if (i == minPair.i || i == minPair.j) continue;
			for (int j=0; j<matrixD[0].length; j++) {
				if (j == minPair.j || j == minPair.i) continue;
				newMatrixD[row][col] = matrixD[i][j];
				col++;
			}
			row++;
		}
		//compute new lengths
		printMatrix(newMatrixD);
		return new Node();
	}
	public static void printMatrix(Integer[][] table) {
		for (int i=0; i<table.length; i++) {
			for (int j=0; j<table[0].length; j++) {
				System.out.print(table[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	public static void main(String[] args) {
		HashMap<Integer, Character> mapIndexToChar = new HashMap<>();
		mapIndexToChar.put(0, 'B');
		mapIndexToChar.put(1, 'A');
		mapIndexToChar.put(2, 'C');
		mapIndexToChar.put(3, 'D');
		HashMap<Character, Integer> mapCharToIndex = new HashMap<>();
		mapCharToIndex.put('A', 0);
		mapCharToIndex.put('B', 1);
		mapCharToIndex.put('C', 2);
		mapCharToIndex.put('D', 3);
		Integer[][] matrixD = new Integer[4][4];
		matrixD[0] = new Integer[]{0,2,4,6};
		matrixD[1] = new Integer[]{2,0,4,6};
		matrixD[2] = new Integer[]{4,4,0,6};
		matrixD[3] = new Integer[]{6,6,6,0};
		neighbourJoin(matrixD, matrixD.length, mapCharToIndex);
	}

}
