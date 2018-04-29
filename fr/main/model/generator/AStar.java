package fr.main.model.generator;

import java.util.*;

public class AStar {

	int[][] weights;
	boolean[][] blocks;
	Node[][] nodeMap;
	Node start;
	Node goal;
	HashMap<Node, Integer> gScore;
	HashMap<Node, Integer> fScore;

//	LinkedList<Node> frontier;
	PriorityQueue<Node> frontier;
	HashSet<Node> visited;

	HashMap<Node, Node> cameFrom;


	public AStar(int[][] weights, boolean[][] blocks){
		this.weights = weights;
		this.blocks = blocks;

		Node.setWeights(weights);

		gScore = new HashMap<>();
		gScore.put(start, 0);
		fScore = new HashMap<>();

		frontier = new PriorityQueue<>();
		frontier.add(start);

		visited = new HashSet<>();
		visited.add(start);

		cameFrom = new HashMap<>();
	}

	private int heuristic(Node a, Node b) {
		return MapGenerator.abs(a.x - b.x) + MapGenerator.abs(a.y + b.y);
	}


	public int[][] shortestRoute(int x0, int y0, int x1, int y1) {
		this.nodeMap = new Node[weights.length][weights[0].length];

		nodeMap[x0][y0] = new Node(x0, y0);
		nodeMap[x1][y1] = new Node(x1, y1);

		start 	= 	nodeMap[x0][y0];
		goal 	= 	nodeMap[x1][y1];

		while (frontier.peek() != null) {
			Node current = frontier.peek();
			frontier.remove(current);

			if(current.is(goal))
				break;

			for (Node next : current.getNeighbours(nodeMap)) {
				int newCost = gScore.get(current) + next.getWeight();
				if(!gScore.containsKey(next)) {
					gScore.put(next, newCost);
					int priority = heuristic(next, goal) + newCost;
					next.setWeight(priority);
					frontier.add(next);
					cameFrom.put(next, current);

				}
				else if(gScore.get(next) > newCost){
					gScore.replace(next, newCost);
					int priority = heuristic(next, goal) + newCost;
					next.setWeight(priority);
					frontier.add(next);
					cameFrom.put(next, current);
				}
			}
		}





		// TODO : finish this path finding function using A* algorithm.
		return null;
	}

	public void setWeights(int[][] weights) {
		this.weights = weights;
	}

	public int[][] getWeights() {
		return weights;
	}

	public void setBlocks(boolean[][] blocks) {
		this.blocks = blocks;
	}

	public boolean[][] getBlocks() {
		return blocks;
	}

	//	public class Elem implements Comparator<Elem> {
//		public final Node n;
//		private int w;
//
//		public Elem(Node elem, int weight) {
//			this.n = elem;
//			this.w = weight;
//		}
//
//		public int getW() {
//			return w;
//		}
//
//		@Override
//		public int compare(Elem o1, Elem o2) {
//			return Integer.compare(o1.getW(), o2.getW());
//		}
//	}


}
