package exactPatternMatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BruteForceMatcher implements Matcher{
	public int checkWindow(final int index, final String pattern, final String genome) {
		return 0;
	}
	//Sliding window matcher
	public HashMap<String, List<Integer>> match(final String genome, 
			final List<String> allPatterns) {
		HashMap<String, List<Integer>> resultMap = new HashMap<>();
		
		for (String pattern : allPatterns) {
			ArrayList<Integer> matchedIndexes = new ArrayList<>();
			int patLen = pattern.length();
			
			for (int i=0; i<genome.length() - patLen + 1; i++) {
				if (genome.substring(i, i + patLen).equals(pattern)) {
					matchedIndexes.add(i);
				}
			}
			resultMap.put(pattern, matchedIndexes);
		}
		return resultMap;
	}
}
