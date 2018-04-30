package fr.main.model.generator;

import java.util.Comparator;
import java.util.LinkedList;

class Node implements Comparator<Node>, Comparable<Node> {
	private static int[][] weights;
	private static boolean[][] blocks;
	public final int x;
	public final int y;
	LinkedList<Node> neighbours;
	private int weight;
	private boolean isBlock;

	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		if (weights == null)
			weight = 1;
		else
			this.weight = weights[x][y];

		isBlock = blocks != null && blocks[x][y];
	}

	/**
	 * @param nodeMap
	 * Sets it's neighbours.
	 */
	public void setNeighbours(Node[][] nodeMap) {
		if(neighbours == null) neighbours = new LinkedList<>();
		int[][] cross = MapGenerator.getCross(nodeMap.length, nodeMap[0].length, x, y);
		for(int i = 0; i < cross.length; i ++) {
			if(nodeMap[cross[i][0]][cross[i][1]] == null) {
				nodeMap[cross[i][0]][cross[i][1]] = new Node(cross[i][0], cross[i][1]);
			}

			if(!blocks[cross[i][0]][cross[i][1]] && !nodeMap[cross[i][0]][cross[i][1]].isBlock())
				neighbours.add(nodeMap[cross[i][0]][cross[i][1]]);
			else
				nodeMap[cross[i][0]][cross[i][1]].setBlock();
		}


	}

	/**
	 * @param nodeMap
	 * @return returns the list of neighbours.
	 */
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

	@Override
	public int compareTo(Node o) {
		return compare(this, o);
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

	public static void setBlocks(boolean[][] blocks) {
		Node.blocks = blocks;
	}
}
