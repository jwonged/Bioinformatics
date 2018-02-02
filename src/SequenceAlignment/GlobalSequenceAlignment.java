package SequenceAlignment;

import java.util.HashMap;

public class GlobalSequenceAlignment {
	public enum Direction {RIGHT, DOWN, DIAGONAL, START}
	public static class Alignment {
		public String a;
		public String b;
		public Alignment(String a, String b) {
			this.a = a;
			this.b = b;
		}
	}
	public static class ScoreMatrix {
		/**For simplicity, not assigning a full matrix 
		 * for each char comparison
		 */
		int match, mismatch, indel;
		public ScoreMatrix(int match, int mismatch, int indel) {
			this.match = match;
			this.mismatch = mismatch;
			this.indel = indel;
		}
	}
	public Alignment computeGlobalAlignment(String a, 
			String b, ScoreMatrix scoreMatrix) {
		
		int[][] table = new int[a.length() + 1][b.length() + 1];
		Direction[][] pointers = new Direction[a.length() + 1][b.length() + 1];
		table[0][0] = 0;
		pointers[0][0] = Direction.START;
		
		//first row/col
		for (int i=1; i<table.length; i++) {
			table[i][0] = scoreMatrix.indel;
			pointers[i][0] = Direction.RIGHT;
		}
		for (int j=1; j<table[0].length; j++) {
			table[0][j] = scoreMatrix.indel;
			pointers[0][j] = Direction.DOWN;
		}
		
		for (int i=1; i<table.length; i++) {
			for (int j=1; j<table[0].length; j++) {
				int right = table[i-1][j] + scoreMatrix.indel;
				int down = table[i][j-1] + scoreMatrix.indel;
				int diagonal;
				if (a.charAt(i-1) == (b.charAt(j-1))) {
					diagonal = table[i-1][j-1] + scoreMatrix.match;
				} else {
					diagonal = table[i-1][j-1] + scoreMatrix.mismatch;
				}
				
				if (diagonal > right && diagonal > down) {
					table[i][j] = diagonal;
					pointers[i][j] = Direction.DIAGONAL;
				} else if (right > down) {
					table[i][j] = right;
					pointers[i][j] = Direction.RIGHT;
				} else {
					table[i][j] = down;
					pointers[i][j] = Direction.DOWN;
				}
			}
		}
		return backtrack(a,b,table,pointers);
	}
	public Alignment backtrack(String a, 
			String b, int[][] table, Direction[][] pointers) {
		StringBuilder sbA = new StringBuilder();
		StringBuilder sbB = new StringBuilder();
		
		int i = pointers.length-1, j = pointers[0].length-1;
		Direction d = pointers[i][j];
		int aInd = a.length() - 1, bInd = b.length() - 1;
		while (aInd >= 0 || bInd >= 0) {
			if (d == Direction.RIGHT) {
				sbA.append(a.charAt(aInd));
				sbB.append('-');
				aInd--; i--;
			} else if (d == Direction.DOWN) {
				sbA.append('-');
				sbB.append(b.charAt(bInd));
				bInd--; j--;
			} else {
				sbA.append(a.charAt(aInd));
				sbB.append(b.charAt(bInd));
				aInd--; bInd--;
				i--; j--;
			}
			d = pointers[i][j];
		}
		return new Alignment(
				sbA.reverse().toString(),
				sbB.reverse().toString());
	}
	public static void main(String[] args) {
		ScoreMatrix scoreMatrix = new ScoreMatrix(5, -3, -4);
		String a = "CGTGAA";
		String b = "GACTTAC";
		GlobalSequenceAlignment aligner = new GlobalSequenceAlignment();
		Alignment result = aligner.computeGlobalAlignment(a, b, scoreMatrix);
		System.out.println(result.a);
		System.out.println(result.b);
	}

}
