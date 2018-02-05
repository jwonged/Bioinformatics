package Phylogeny;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Tree {
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
	public static class ConnectedTo {
		Node to;
		int weight;
		public ConnectedTo(Node to, int weight) {
			this.to = to;
			this.weight = weight;
		}
	}
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