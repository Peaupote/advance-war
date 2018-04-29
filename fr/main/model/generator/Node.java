package fr.main.model.generator;

import java.util.Comparator;
import java.util.LinkedList;

class Node implements Comparator<Node> {
	public static int[][] weights;
	public final int x;
	public final int y;
	LinkedList<Node> neighbours;
	private int weight;
	private boolean isBlock;
//		Node last;

	public Node(int x, int y) {
//			if(this.last == null) this.last = last;
		this.x = x;
		this.y = y;
		if(weights == null)
			weight = 1;
		else
			this.weight = weights[x][y];
	}

//		public Node(int x, int y) {
//			this(x, y, null);
//		}

	public void setNeighbours(Node[][] nodeMap) {
		int[][] cross = MapGenerator.getCross(nodeMap.length, nodeMap[0].length, x, y);
		for(int i = 0; i < cross.length; i ++) {
			if(nodeMap[cross[i][0]][cross[i][1]] == null) {
				nodeMap[cross[i][0]][cross[i][1]] = new Node(cross[i][0], cross[i][1]);
			}
			neighbours.add(nodeMap[cross[i][0]][cross[i][1]]);
		}


	}

	public LinkedList<Node> getNeighbours(Node[][] nodeMap) {
		if(neighbours == null)
			setNeighbours(nodeMap);
		return neighbours;
	}

	public boolean is(Node n) {
		return n == this || n.x == x && n.y == y;
	}

	@Override
	public int compare(Node o1, Node o2) {
		return Integer.compare(o1.weight, o2.weight);
	}

	public static void setWeights(int[][] w) {
		weights = w;
	}

	public void setWeight(int w) {
		this.weight = w;
	}

	public int getWeight() {
		return weight;
	}

	public boolean isBlock() {
		return isBlock;
	}

	public void setBlock() {
		this.isBlock = true;
	}

	public void setPassable() {
		this.isBlock = false;
	}

}
