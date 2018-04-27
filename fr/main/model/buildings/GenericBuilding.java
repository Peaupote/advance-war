package fr.main.model.buildings;

import java.awt.*;

public class GenericBuilding extends Building implements AbstractBuilding {

	private static final long serialVersionUID = 2599496054309710000L;

	public GenericBuilding(int x, int y) {
		super(new Point(x, y), 0, "Generic");
	}
}