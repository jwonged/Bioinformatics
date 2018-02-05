package SequenceAlignment;

import java.util.HashMap;

public class GlobalSequenceAlignment {
	public enum Direction {RIGHT, DOWN, DIAGONAL, START}
	public static class Alignment {
		public String a;
		public String b;
		int score;
		public Alignment(String a, String b, int score) {
			this.a = a;
			this.b = b;
			this.score = score;
		}
	}
	public static class ScoreMatrix {
		/**For simplicity, not assigning a full matrix 
		 * for each char comparison
		 */
		int match, mismatch, indel;
		public ScoreMatrix(final int match, 
				final int mismatch, final int indel) {
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
			table[i][0] = table[i-1][0] + scoreMatrix.indel;
			pointers[i][0] = Direction.RIGHT;
		}
		for (int j=1; j<table[0].length; j++) {
			table[0][j] = table[0][j-1] + scoreMatrix.indel;
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
		printTable(table);
		return backtrack(a,b,table,pointers);
	}
	public Alignment backtrack(String a, 
			String b, int[][] table, Direction[][] pointers) {
		StringBuilder sbA = new StringBuilder();
		StringBuilder sbB = new StringBuilder();
		
		int i = pointers.length-1, j = pointers[0].length-1;
		int score = table[i][j];
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
				sbB.reverse().toString(),
				score);
	}
	private static void printTable(int[][] table) {
		for (int i=0; i<table.length; i++) {
			for (int j=0; j<table[0].length; j++) {
				System.out.print(table[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	public static void main(String[] args) {
		//match, mismatch, indel
		ScoreMatrix scoreMatrix = new ScoreMatrix(5, -3, -4);
		String a = "CGTGAA"; //15 - 6 - 12
		String b = "GACTTAC";
		GlobalSequenceAlignment aligner = new GlobalSequenceAlignment();
		Alignment result = aligner.computeGlobalAlignment(b, a
				, scoreMatrix);
		System.out.println(result.a); //Prints --CGTGAA
		System.out.println(result.b); //Prints GACTT-AC
		System.out.println(result.score);
	}

}
