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
		Node.setBlocks(blocks);
	}

	private int heuristic(Node a, Node b) {
		return MapGenerator.abs(a.x - b.x) + MapGenerator.abs(a.y + b.y);
	}


	/**
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @return an array of coordinates to the tiles of the shortest route from (x0, y0)
	 * to (x1, y1) using the aStar algorithm.
	 */
	public int[][] shortestRoute(int x0, int y0, int x1, int y1) {
		this.nodeMap = new Node[weights.length][weights[0].length];

		nodeMap[x0][y0] = new Node(x0, y0);
		nodeMap[x1][y1] = new Node(x1, y1);

		start 	= 	nodeMap[x0][y0];
		goal 	= 	nodeMap[x1][y1];

		gScore = new HashMap<>();
		gScore.put(start, 0);
		fScore = new HashMap<>();

		frontier = new PriorityQueue<>();
		frontier.add(start);

		visited = new HashSet<>();
		visited.add(start);

		cameFrom = new HashMap<>();

		while (frontier.size() != 0) {
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

		LinkedList<Node> path =  reconstructPath(start, goal, cameFrom);
		return coorsFromNodes(path);
	}

	/**
	 * @param start
	 * @param goal
	 * @param cameFrom
	 * @return extracts the path from the map of neighbours.
	 */
	private LinkedList<Node> reconstructPath (Node start, Node goal, HashMap<Node, Node> cameFrom) {
			LinkedList<Node> path = new LinkedList<>();
			Node current = goal;
			while (current != start) {
				path.addFirst(current);
				current = cameFrom.get(current);
			}
			path.add(start); // optional

			return path;
	}

	/**
	 * @param nodes
	 * @return an array of coordinates form a LinkedList of Nodes.
	 */
	public int[][] coorsFromNodes(LinkedList<Node> nodes) {
		int[][] coors = new int[nodes.size()][2];
		int[] coor;
		int i = 0;

		for(Node n : nodes) {
			coor = new int[2];
			coor[0] = n.x;
			coor[1] = n.y;
			coors[i] = coor;
			i ++;
		}

		return coors;
	}

	public void setWeights(int[][] weights) {
		this.weights = weights;
		Node.setWeights(weights);
	}

	public int[][] getWeights() {
		return weights;
	}

	public void setBlocks(boolean[][] blocks) {
		this.blocks = blocks;
		Node.setBlocks(blocks);
	}

	public boolean[][] getBlocks() {
		return blocks;
	}
}
