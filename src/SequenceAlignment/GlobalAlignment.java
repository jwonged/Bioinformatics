package SequenceAlignment;
/*
 * Incomplete -- view GlobalSequenceAlignment instead
 */
public class GlobalAlignment {
	public static class Alignment {
		public String seqA;
		public String seqB;
		public int score;
		public Alignment(final String seqA, final String seqB, final int score) {
			this.seqA = seqA;
			this.seqB = seqB;
			this.score = score;
		}
	}
	public static class Action {
		public int score;
		public Direction direction;
		public Action(int score, Direction direction) {
			this.score = score;
			this.direction = direction;
		}
	}
	private static Alignment makeSubsequence(
			final Action[][] table, final String a, final String b) {
		StringBuilder sbA = new StringBuilder();
		StringBuilder sbB = new StringBuilder();
		
		Action current = table[a.length()][b.length()];
		int score = current.score;
		int tableA = a.length(), tableB = b.length();
		int aInd = a.length()-1, bInd = b.length()-1; 
		while (current.direction != null) {
			if (current.direction == Direction.RIGHT) {
				//del
				sbA.append('-');
				sbB.append(b.charAt(bInd));
				current = table[tableA][tableB-1];
				bInd--; tableB--;
			} else if (current.direction == Direction.DOWN) {
				sbB.append('-');
				sbA.append(a.charAt(aInd));
				current = table[tableA-1][tableB];
				aInd--; tableA--;
			} else if (current.direction == Direction.DIAGONAL) {
				sbA.append(Character.toLowerCase(a.charAt(aInd)));
				sbB.append(Character.toLowerCase(b.charAt(bInd)));
				current = table[tableA-1][tableB-1];
				bInd--; tableB--;
				bInd--; tableB--;
			} 
		}
		return new Alignment(
				sbA.reverse().toString(),
				sbB.reverse().toString(),
				score);
		
	}
	public static Alignment getGlobalAlignment(String a, String b) {
		a = a.toUpperCase();
		b = b.toUpperCase();
		int del = 0;
		int ins = 0;
		int match = 1;
		int mismatch = 100;
		
		
		Action[][] table = new Action[a.length() + 1][b.length() + 1];
		table[0][0] = new Action(0, null);
		for (int i=1; i<table[0].length; i++) 
			table[0][i] = new Action(table[0][i-1].score + del, Direction.RIGHT);
		for (int i=1; i<table.length; i++) 
			table[i][0] = new Action(table[i-1][0].score + ins, Direction.DOWN);
		
		for (int i=1; i<table.length; i++) {
			for (int j=1; j<table[0].length; j++) {
				table[i][j] = getMaxStep(
						table[i][j-1].score + ins, 
						table[i-1][j].score + del, 
						table[i-1][j-1].score + (a.charAt(i-1) == b.charAt(j-1) ? match : mismatch));
			}
		}
		
		return makeSubsequence(table,  a,  b);
	}
	
	private static Action getMaxStep(int doInsert, int doDelete, int doMatch) {
		int max = Math.max(Math.max(doInsert, doDelete), doMatch);
		if (max == doInsert) {
			return new Action(max, Direction.DOWN);
		} else if (max == doDelete) {
			return new Action(max, Direction.RIGHT);
		} else {
			return new Action(max, Direction.DIAGONAL);
		}
	}
	public static void main(String[] args) {
		String a = "ATGTTATA";
		String b = "ATCGTCCA";
		
		Alignment result = getGlobalAlignment(a,b);
		System.out.println(result.seqA);
		System.out.println(result.seqB);
		System.out.println(result.score);
	}

}
