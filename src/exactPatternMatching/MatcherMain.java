package exactPatternMatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatcherMain {
	//To return all positions in Genome where Pattern appears as substring
	public static void main(String[] args) {
		//Matcher matcher = new BruteForceMatcher();
		Matcher matcher = new SuffixTrieMatcher();
		ArrayList<String> patterns = new ArrayList<>();
		patterns.add("lo");
		patterns.add("He");
		patterns.add("io");
		patterns.add("o");
		String genome = "Hello World";
		HashMap<String, List<Integer>> matched = matcher.match(genome, patterns);
		//System.out.println(genome);
		for (Map.Entry<String, List<Integer>> entry : matched.entrySet()) {
			System.out.print(entry.getKey() + " ");
			System.out.println(entry.getValue());
		}
	}

}
