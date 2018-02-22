package exactPatternMatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuffixTrieMatcher implements Matcher {
	public static class Node {
		private HashMap<Character, Node> children;
		private int index;
		public boolean isStopNode;
		public Node() {
			this.isStopNode = false;
			this.children = new HashMap<>();
		}
		public void addChild(char c) {
			if (!children.containsKey(c)) children.put(c, new Node());
		}
		public Node getChild(char c) {
			return children.getOrDefault(c, null);
		}
		public boolean hasChild(char c) {
			return (children.containsKey(c));
		}
		public void setIndex(int index) {
			if (isStopNode) this.index = index;
			else System.err.println("Invalid state error");
		}
		public int getIndex() {
			return this.index;
		}
		public HashMap<Character, Node> getChildrenMap() {
			return children;
		}
	}
	
	private List<String> getAllSuffixes(final String genome) {
		List<String> allSuffixes = new ArrayList<>();
		for (int i=0; i<genome.length(); i++) {
			allSuffixes.add(genome.substring(i,genome.length()));
		}
		for (String s : allSuffixes) System.out.println(s);
		return allSuffixes;
	}
	private void addSuffix(final String suffix, final Node root, final int index) {
		Node current = root;
		for (char c : suffix.toCharArray()) {
			current.addChild(c);
			current = current.getChild(c);
		}
		current.isStopNode = true;
		current.setIndex(index);
	}
	public Node constructTrie(final String genome) {
		Node root = new Node();
		int index = 0;
		for (String s : getAllSuffixes(genome)) {
			addSuffix(s, root, index);
			index++;
		}
		return root;
	}
	private void getPositionsRecursively(Node current, List<Integer> positions) {
		if (current == null) return;
		if (current.isStopNode) positions.add(current.index);
		for (Node child : current.getChildrenMap().values()) {
			getPositionsRecursively(child, positions);
		}
	}
	private List<Integer> getPositions(String pattern, Node root) {
		ArrayList<Integer> positions = new ArrayList<>();
		Node current = root;
		for (char c : pattern.toCharArray()) {
			if (current.hasChild(c)) current = current.getChild(c);
			else return positions;
		}
		getPositionsRecursively(current, positions);
		return positions;
	}
	@Override
	public HashMap<String, List<Integer>> match(
			final String genome, final List<String> patterns) {
		HashMap<String, List<Integer>> matchPositions = new HashMap<>();
		Node trieRoot = constructTrie(genome);
		for (String pattern : patterns) {
			List<Integer> match = getPositions(pattern, trieRoot);
			if (match.size() > 0) matchPositions.put(pattern, match);
		}
		return matchPositions;
	}
	public static void main(String[] args) {
		SuffixTrieMatcher matcher = new SuffixTrieMatcher();
		ArrayList<String> patterns = new ArrayList<>();
		patterns.add("TAT");
		patterns.add("TAG");
		String genome = "CATATATAG";
		HashMap<String, List<Integer>> matched = matcher.match(genome, patterns);
		//System.out.println(genome);
		for (Map.Entry<String, List<Integer>> entry : matched.entrySet()) {
			System.out.print(entry.getKey() + " ");
			System.out.println(entry.getValue());
		}
	}
	
	private List<String> getCyclicRotations(final String genome) {
		//For BWT
		List<String> allSuffixes = new ArrayList<>();
		for (int i=genome.length(); i>0; i--) {
			allSuffixes.add(genome.substring(i,genome.length()) 
					+ genome.substring(0,i));
		}
		for (String s : allSuffixes) System.out.println(s);
		return allSuffixes;
	}
}
