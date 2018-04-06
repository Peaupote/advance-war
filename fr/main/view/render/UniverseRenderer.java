package fr.main.view.render;

import java.awt.*;
import java.util.LinkedList;
import java.util.Iterator;

import fr.main.model.TerrainEnum;
import fr.main.model.Node;
import fr.main.model.Direction;
import fr.main.model.MoveZone;
import fr.main.model.Universe;
import fr.main.model.players.Player;
import fr.main.model.Weather;
import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.terrains.Terrain;
import fr.main.model.terrains.land.*;
import fr.main.model.terrains.naval.Reef;
import fr.main.model.terrains.naval.Sea;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.HealerUnit;
import fr.main.model.units.TransportUnit;
import fr.main.model.buildings.MissileLauncher;
import fr.main.model.buildings.AbstractBuilding;

import fr.main.view.MainFrame;
import fr.main.view.controllers.GameController;
import fr.main.view.render.terrains.TerrainLocation;
import fr.main.view.render.units.UnitRenderer;
import fr.main.view.render.terrains.TerrainRenderer;
import fr.main.view.render.buildings.BuildingRenderer;

public class UniverseRenderer extends Universe {

    public final GameController controller;
    private final Color fogColor    = new Color (0,0,0,100),
                        moveColor   = new Color (0, 255, 0, 50),
                        targetColor = new Color (255, 0, 0, 100),
                        loadColor   = new Color (0, 0, 255, 50),
                        healColor   = new Color (0, 255, 0, 50);

    private static final Font font = new Font("Helvetica", Font.PLAIN, 14);

    private final Point[][] coords;
    private final boolean[][] targets;
    private Point upperLeft = new Point(0,0), lowerRight;
    private Color tColor;

    public static class FlashMessage {

        public enum Type {
            ALERT(Color.red),
            SUCCESS(Color.green);

            public final Color color;

            private Type (Color color) {
                this.color = color;
            }
        }

        private final int x, y;
        private int time;
        private final String message;
        private final Type type;

        public FlashMessage (String message, int x, int y, int time, Type type) {
            this.time    = time;
            this.message = message;
            this.x       = x;
            this.y       = y;
            this.type    = type;
        }

    }

    private final LinkedList<FlashMessage> flashs;

    public UniverseRenderer (Universe.Board b, GameController controller){
        super(b);
        
        this.controller = controller;
        controller.makeView().getWeatherController().update(Weather.FOGGY);
        coords = new Point[map.board.length][map.board[0].length];
        for (int i = 0; i < map.board.length; i++)
            for (int j = 0; j < map.board[i].length; j++)
                coords[i][j] = new Point(0, 0);

        targets    = new boolean[map.board.length][map.board[0].length];
        lowerRight = new Point(map.board.length, map.board[0].length);
        flashs     = new LinkedList<FlashMessage>();
        TerrainRenderer.setLocations();
    }

    public UniverseRenderer (AbstractUnit[][] units, AbstractTerrain[][] map, Player[] ps, AbstractBuilding[][] buildings, GameController controller){
        this (new Universe.Board(units, ps, map, buildings), controller);
    }

    public UniverseRenderer (String mapName, GameController controller) {
        this (Universe.restaure(mapName), controller);
    }

    public UniverseRenderer (String mapName, Player[] ps, GameController controller) {
        this (Universe.restaure(mapName).setPlayers(ps), controller);
    }

    public boolean isEnabled(int x, int y){
        return targets[y][x];
    }

    public void draw (Graphics g, int x, int y, int offsetX, int offsetY) {
        g.setFont(font);
        int w = map.board.length,
                h = map.board[0].length,
                firstX = x - (offsetX < 0 ? 1 : 0),
                firstY = y - (offsetY < 0 ? 1 : 0),
                lastX  = x + w + (offsetX > 0 ? 1 : 0),
                lastY  = y + h + (offsetY > 0 ? 1 : 0);

        for (int i = firstY; i < Math.min(lastY, map.board.length); i++)
            for (int j = firstX; j < Math.min(lastX, map.board[i].length); j++) {
                coords[i][j].x = (j - x) * MainFrame.UNIT - offsetX;
                coords[i][j].y = (i - y) * MainFrame.UNIT - offsetY;

                TerrainRenderer.render(g, coords[i][j], new Point(j, i));
                if (map.buildings[i][j] != null) BuildingRenderer.render(g, coords[i][j], map.buildings[i][j]);

                if (targets[i][j]) {
                    g.setColor(tColor);
                    g.fillRect(coords[i][j].x, coords[i][j].y, MainFrame.UNIT, MainFrame.UNIT);
                }
            }

        for (int i = firstY; i < Math.min(lastY, map.board.length); i++)
            for (int j = firstX; j < Math.min(lastX, map.board[i].length); j++) {
                if (!fogwar[i][j]) {
                    g.setColor(fogColor);
                    g.fillRect(coords[i][j].x, coords[i][j].y, MainFrame.UNIT, MainFrame.UNIT);
                }

                if (map.units[i][j] != null)
                    if (map.units[i][j].getPlayer() == current || isVisibleOpponentUnit(j, i))
                        UnitRenderer.render(g, coords[i][j], map.units[i][j]);
            }

        Iterator<FlashMessage> iterator = flashs.iterator();
        while (iterator.hasNext()) {
            FlashMessage message = iterator.next();
            g.setColor(message.type.color);
            g.drawString (message.message, message.x, message.y);
            message.time -= 10;
            if (message.time <= 0) iterator.remove();
        }
    }

    public void next(){
        Weather w = weather;
        super.next();
        if (w != weather && controller != null) controller.makeView().getWeatherController().update(w);
    }

    public void updateTarget (AbstractUnit unit) {
        clearTarget();
        if (controller.getMode() == GameController.Mode.UNIT) {
            MoveZone m = unit.getMoveMap();
            int moveQuantity = unit.getMoveQuantity();
            Node[][] n = m.map;
            upperLeft = m.offset;
            lowerRight = new Point(upperLeft.x + n[0].length, upperLeft.y + n.length);
            for (int j = upperLeft.y; j < lowerRight.y; j++)
                for (int i = upperLeft.x; i < lowerRight.x; i ++)
                    targets[j][i] = n[j - upperLeft.y][i - upperLeft.x].lowestCost <= moveQuantity;
            tColor = unit.getPlayer() == current ? moveColor : targetColor;
        } else if (controller.getMode() == GameController.Mode.ATTACK) {
            unit.renderTarget(targets);
            upperLeft.move(0,0);
            lowerRight.move(targets.length, targets[0].length);
            tColor = targetColor;
        } else if (controller.getMode() == GameController.Mode.HEAL || 
                   controller.getMode() == GameController.Mode.LOAD || 
                   controller.getMode() == GameController.Mode.UNLOAD_LOCATE) {
            int x = unit.getX(), y = unit.getY();
            HealerUnit healer = controller.getMode() == GameController.Mode.HEAL ? (HealerUnit)unit : null;
            TransportUnit transporter = controller.getMode() == GameController.Mode.UNLOAD_LOCATE ? (TransportUnit)unit : null;

            for (Direction d : Direction.cardinalDirections()){
                int xx = x + d.x, yy = y + d.y;
                if ((controller.getMode() == GameController.Mode.HEAL && healer.canHeal(getUnit(xx, yy))) ||
                    (controller.getMode() == GameController.Mode.LOAD && getUnit(xx, yy) instanceof TransportUnit && ((TransportUnit)getUnit(xx, yy)).canCharge(unit)) ||
                    (controller.getMode() == GameController.Mode.UNLOAD_LOCATE && transporter.canRemove(controller.getTransportUnit(), xx, yy)))
                    targets[yy][xx] = true;
            }
            upperLeft.move(Math.max(0, unit.getX() - 1), Math.max(0, unit.getY() - 1));
            lowerRight.move(Math.min(getMapWidth(), unit.getX() + 2), Math.min(getMapHeight(), unit.getY() + 2));
            tColor = controller.getMode() == GameController.Mode.HEAL ? healColor : loadColor;
        }
    }

    private static final int[][] directions = {
        {1,1},{1,-1},{-1,-1},{-1,1}
    };

    public boolean updateTarget(Point p){
        clearTarget();
        if (controller.getMode() == GameController.Mode.MISSILE_LAUNCHER){
            for (int i = 0 ; i <= 3 ; i++)
                for (int j = 0 ; j <= i ; j ++)
                    for (int[] tab : directions){
                        int xx = p.x + tab[0] * j, yy = p.y + tab[1] * (i - j);
                        if (isValidPosition(xx, yy))
                            targets[yy][xx] = true;
                    }
            upperLeft.move(Math.max(0, p.x - 3), Math.max(0, p.y - 3));
            lowerRight.move(Math.min(getMapWidth(), p.x + 4), Math.min(getMapHeight(), p.y + 4));
            tColor = targetColor;
            return true;
        }
        return false;
    }

    public void clearTarget () {
        for (int i = upperLeft.x; i < lowerRight.x; i++)
            for (int j = upperLeft.y; j < lowerRight.y; j++)
                targets[j][i] = false;
        upperLeft.move(0,0);
        lowerRight.move(coords[0].length, coords.length);
    }

    public void flash (String message, int x, int y, int time) {
        flashs.add(new FlashMessage(message, x, y, time, FlashMessage.Type.SUCCESS));
    }

    public void flash (String message, int x, int y, int time, FlashMessage.Type type) {
        flashs.add(new FlashMessage(message, x, y, time, type));
    }

}

