package fr.main.view.render;

import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import fr.main.model.units.Path;
import fr.main.model.Direction;
import fr.main.model.Universe;
import fr.main.model.units.AbstractUnit;
import fr.main.view.MainFrame;
import fr.main.view.Position;
import fr.main.view.render.units.UnitRenderer;

/**
 * Arrow renderer for path
 */
public class PathRenderer extends Path {

    private Position.Camera camera;
    private UniverseRenderer world;
    public boolean visible;
    private Image[] images;

    /**
     * All images path
     * TODO: change for sprites
     */
    private static String[] filepaths = {
            "./assets/arrows/arrow-bottom.png",
            "./assets/arrows/arrow-left.png",
            "./assets/arrows/arrow-right.png",
            "./assets/arrows/arrow-top.png",
            "./assets/arrows/left-bottom.png",
            "./assets/arrows/left-top.png",
            "./assets/arrows/left-right.png",
            "./assets/arrows/right-bottom.png",
            "./assets/arrows/right-top.png",
            "./assets/arrows/top-bottom.png"
    };

    public PathRenderer (Position.Camera camera) {
        super();
        this.camera = camera;
        this.world = (UniverseRenderer)Universe.get();
        visible = false;

        images = new Image[filepaths.length];
        try {
            for (int i = 0; i < filepaths.length; i++)
                images[i] = ImageIO.read(new File(filepaths[i]));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
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
     * make the unit moves allong the path
     */
    public boolean apply () {
        UnitRenderer.Render render = UnitRenderer.getRender(unit);

        Point pt = unit.position();
        boolean previouslyUnit = false;

        while (!isEmpty()) {
            Direction d = poll();
            render.setOrientation(d);
            d.move(pt);

            AbstractUnit u = world.getUnit(pt);
            // if we meet an opponent unit, we can go any further
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
            previouslyUnit = u != null;
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
