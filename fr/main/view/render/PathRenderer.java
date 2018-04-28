package fr.main.view.render;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import fr.main.model.Direction;
import fr.main.model.Universe;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.Path;
import fr.main.view.MainFrame;
import fr.main.view.Position;
import fr.main.view.render.sprites.Sprite;
import fr.main.view.render.units.UnitRenderer;

/**
 * Arrow renderer for path
 */
public class PathRenderer extends Path {

    /**
	 * Add PathRenderer UID
	 */
	private static final long serialVersionUID = 7566614956368710772L;
	private Position.Camera camera;
    private UniverseRenderer world;
    public boolean visible;
    private static Image[] images;

    static{
        Sprite d = Sprite.get("./assets/ingame/things.png");

        images = new Image[]{
            d.getImage       (12, 32, 16, 16, 2),        // arrow bottom
            d.getImage       (88, 94, 16, 16, 2),        // arrow left
            d.getReverseImage(88, 94, 16, 16, 2, false), // arrow right
            d.getImage       (85, 76, 16, 16, 2),        // arrow top
            d.getImage       (45, 15, 16, 16, 2),        // left bottom
            d.getImage       (28, 32, 16, 16, 2),        // left top
            d.getImage       (62, 33, 16, 16, 2),        // left right
            d.getImage       (28, 15, 16, 16, 2),        // right bottom
            d.getImage       (45, 32, 16, 16, 2),        // right top
            d.getImage       (12, 15, 16, 16, 2)         // top bottom
        };

    }

    public PathRenderer (Position.Camera camera) {
        super();
        this.camera = camera;
        this.world = (UniverseRenderer)Universe.get();
        visible = false;
        Path.instance = this;
    }

    public void draw (Graphics g, int offsetX, int offsetY) {
        g.setColor(Color.red);
        Point point = new Point(unit.getX(), unit.getY());
        
        int size = size();
        if (size == 0) return;
        if (size > 1) {
            Direction d = get(0);
            Image image = null;
            for (int i = 1; i < size; i++) {
                Direction next = get(i);
                d.move(point);

                // choosing right image depending on the i-th and the (i+1)-th
                if ((d == Direction.LEFT && next == Direction.LEFT) ||
                        (d == Direction.LEFT && next == Direction.RIGHT) ||
                        (d == Direction.RIGHT && next == Direction.LEFT) ||
                        (d == Direction.RIGHT && next == Direction.RIGHT)) image = images[6];
                else if ((d == Direction.TOP && next == Direction.TOP) ||
                                 (d == Direction.TOP && next == Direction.BOTTOM) ||
                                 (d == Direction.BOTTOM && next == Direction.TOP) ||
                                 (d == Direction.BOTTOM && next == Direction.BOTTOM)) image = images[9];
                else if ((d == Direction.LEFT && next == Direction.TOP) ||
                                 (d == Direction.BOTTOM && next == Direction.RIGHT)) image = images[8];
                else if ((d == Direction.LEFT && next == Direction.BOTTOM) ||
                                 (d == Direction.TOP && next == Direction.RIGHT)) image = images[7];
                else if ((d == Direction.RIGHT && next == Direction.TOP) ||
                                 (d == Direction.BOTTOM && next == Direction.LEFT)) image = images[5];
                else if ((d == Direction.RIGHT && next == Direction.BOTTOM) ||
                                 (d == Direction.TOP && next == Direction.LEFT)) image = images[4];

                if (image != null)
                    g.drawImage(image,
                                (point.x - camera.getX()) * MainFrame.UNIT - offsetX,
                                (point.y - camera.getY()) * MainFrame.UNIT - offsetY,
                                MainFrame.UNIT, MainFrame.UNIT, null);

                d = next;
            }
        }

        Image arrow = null;
        Direction d = getLast();
        d.move(point);

        // last images
        if (d == Direction.LEFT) arrow = images[1];
        else if (d == Direction.RIGHT) arrow = images[2];
        else if (d == Direction.TOP) arrow = images[3];
        else if (d == Direction.BOTTOM) arrow = images[0];

        if (arrow != null)
            g.drawImage(arrow,
                (point.x - camera.getX()) * MainFrame.UNIT - offsetX,
                (point.y - camera.getY()) * MainFrame.UNIT - offsetY,
                MainFrame.UNIT, MainFrame.UNIT, null);
    }

    /**
     * make the unit move allong the path
     */
    public boolean apply () {
        UnitRenderer.Render render = UnitRenderer.getRender(unit);

        Point pt = unit.position();

        while (!isEmpty()) {
            Direction d = poll();
            render.setOrientation(d);
            d.move(pt);

            AbstractUnit u = world.getUnit(pt);
            // if we meet an opponent unit, we can't go any further
            if (u != null && u.getPlayer() != unit.getPlayer()){
                if (u.canAttack(unit)) {
                    int life = unit.getLife();
                    u.attack(unit, false);
                    world.flash ("" + (unit.getLife() - life),
                    (unit.getX() - camera.getX() + 1) * MainFrame.UNIT + 5,
                    (unit.getY() - camera.getY()) * MainFrame.UNIT + 5, 1000,
                    UniverseRenderer.FlashMessage.Type.ALERT);
                }
                unit.setMoveQuantity(0);
                return false;
            }

            // else we keep going
            for (int i = 0; i < MainFrame.UNIT; i++){
                render.moveOffset(d, false);
                try { Thread.sleep(5); }
                catch (InterruptedException e) { e.printStackTrace(); }
            }
            unit.removeMoveQuantity(unit.moveCost(pt.x, pt.y));
            unit.getFuel().consume(1);
            if (unit.dead()) return false;

            if (u == null){
                world.setUnit(unit.getX(), unit.getY(), null);
                render.cancelOffset();
                unit.setLocation(pt.x, pt.y);
                world.setUnit(unit.getX(), unit.getY(), unit);
                world.updateVision();
            }
        }
        return true;
    }
}
