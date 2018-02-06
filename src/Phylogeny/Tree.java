package Phylogeny;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Tree {
	//Phylogeny tree
	public static class Node {
		Character c;
		List<ConnectedTo> children;
		public Node(char c) {
			this.c = c;
			children = new ArrayList<>();
		}
		public void addChild(Node c, int weight) {
			children.add(new ConnectedTo(c, weight));
		}
		public List<ConnectedTo> getChildren() {
			return children;
		}
		public Character getChar() {
			return c;
		}
	}
	public static class ConnectedTo {
		Node to;
		int weight;
		public int depth; //only for printing
		public ConnectedTo(Node to, int weight) {
			this.to = to;
			this.weight = weight;
		}
	}
	public HashMap<Character, Node> charToNode;
	public Node root;
	
	public Tree(Character a, Character b, int weight) {
		//For convenience of structure, I'm making the tree rooted
		charToNode = new HashMap<>();
		root = new Node('*');
		charToNode.put('*', root);
		addEdge('*', a, weight/2);
		addEdge('*', b, weight/2);
	}
	public void addEdge(Character from, Character to, int weight) {
		Node current = new Node(to);
		charToNode.put(to, current);
		charToNode.get(from).addChild(current, weight);
	}
	public void printTree() {
		/* root is '*'
		 * Each layer of depth will start on a new line
		 * Each node is printed in the format:
		 * (node char, weight from parent to node, n if it has no children)
		 */
		System.out.println("------------------------");
		System.out.println("Printing resulting tree:");
		LinkedList<ConnectedTo> q = new LinkedList<>();
		System.out.println(root.getChar());
		int depth = 0;
		for (ConnectedTo c : root.getChildren()) {
			c.depth = depth;
			q.add(c);
		}
		while (!q.isEmpty()) {
			ConnectedTo current = q.remove();
			if (current.depth > depth) {
				System.out.println();
				depth++;
			}
			System.out.print("("+current.to.getChar()+", ");
			System.out.print(current.weight);
			if (current.to.getChildren().isEmpty())
				System.out.print(", n");
			System.out.print(") ");
			for (ConnectedTo c : current.to.getChildren()) {
				c.depth = current.depth + 1;
				q.add(c);
			}
		}
	}
}