package fr.main.view.components;

import fr.main.model.terrains.Terrain;
import fr.main.view.Position;

import java.awt.*;

public class InformationPanel extends AWComponents{
    public final Position.Cursor position;
    public final Position.Camera camera;
    public final Terrain[][] map;

    public InformationPanel(Position.Cursor position, Position.Camera camera, Terrain[][] map) {
        this.position = position;
        this.camera = camera;
        this.map = map;
    }
    public void draw(Graphics g) {

    }
}
