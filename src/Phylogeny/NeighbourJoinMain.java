package Phylogeny;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Phylogeny.NeighbourJoining.Coord;

public class NeighbourJoinMain {
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
		HashMap<Coord, Integer> matrixD = 
				convertToMatrixMap(matrixDtmp, remainingChars);
		
		NeighbourJoining nj = new NeighbourJoining();
		Tree result = 
				nj.neighbourJoin(matrixD, nElements, remainingChars);
		result.printTree();
		
	}
	private static void runExample2() {
		NeighbourJoining nj = new NeighbourJoining();
		Integer[][] matrixDtmp2 = new Integer[4][4];
		matrixDtmp2[0] = new Integer[]{0,13,21,22};
		matrixDtmp2[1] = new Integer[]{13,0,12,13};
		matrixDtmp2[2] = new Integer[]{21,12,0,13};
		matrixDtmp2[3] = new Integer[]{22,13,13,0};
		int nElements2 = 4;
		List<Character> remainingChars2 = new ArrayList<>();
		remainingChars2.add('i');
		remainingChars2.add('j');
		remainingChars2.add('k');
		remainingChars2.add('l');
		HashMap<Coord, Integer> matrixD2 = 
				convertToMatrixMap(matrixDtmp2, remainingChars2);
		Tree result = 
				nj.neighbourJoin(matrixD2, nElements2, remainingChars2);
		result.printTree();
	}
	private static HashMap<Coord, Integer> convertToMatrixMap(
			Integer[][] matrixDtmp, List<Character> remainingChars) {
		//Just for convenience so i don't need to keep using .put()
		HashMap<Coord, Integer> matrixD = new HashMap<>();
		int i=0, j=0;
		for (char x : remainingChars) {
			j=0;
			for (char y : remainingChars) {
				matrixD.put(new Coord(x,y),matrixDtmp[i][j]);
				j++;
			}
			i++;
		}
		return matrixD;
	}
}
