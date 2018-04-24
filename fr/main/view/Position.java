package fr.main.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import fr.main.model.Direction;
import fr.main.view.render.sprites.Sprite;

/**
 * Represents a moving point on the map.
 */
public abstract class Position {

    /**
     * Represents the user cursor.
     */
    public static class Cursor extends Position {

        /**
         * Dimension of the universe.
         */
        protected final Dimension size;

        /**
         * Camera showing the game.
         */
        private final Camera camera;

        public static final Image cursorBasic;
        public static final Image cursorAttack;

        static{
            Sprite attack = Sprite.get("./assets/ingame/attack.png");
            cursorBasic = attack.getImage(39, 2, 28, 31);
            cursorAttack = attack.getImage(70, 1, 30, 29);
        }

        private Image cursor;

        public Cursor (Camera camera, Dimension size) {
            super(0, 0);

            this.size   = size;
            this.camera = camera;

            cursor = cursorBasic;
        }

        @Override
        public boolean canMove (Direction d) {
            return (d == Direction.LEFT   && position.x > 0) ||
                   (d == Direction.RIGHT  && position.x + 1 < size.width) ||
                   (d == Direction.TOP    && position.y > 0) ||
                   (d == Direction.BOTTOM && position.y + 1 < size.height);
        }

        @Override
        protected boolean hasReachLocation () {
            return (real.x == target.x * MainFrame.UNIT) &&
                         (real.y == target.y * MainFrame.UNIT);
        }

        public void draw (Graphics g, Color color) {
            int offset = (int)(5 * Math.cos(MainFrame.getTimer() / 5)),
                             s = MainFrame.UNIT + offset;
            g.drawImage (cursor, 2 + real.x - camera.real.x - offset / 2 + 1, 2 + real.y - camera.real.y - offset / 2 + 1, s, s, null);
        }

        public void draw (Graphics g) {
            draw (g, Color.black);
        }

        @Override
        public void setLocation (int x, int y) {
            if (x >= 0 && x < size.width && y >= 0 && y < size.height) {
                position.x = x;
                position.y = y;
                target.x = x;
                target.y = y;

                real.x = x * MainFrame.UNIT;
                real.y = y * MainFrame.UNIT;
            }
        }

        public void setCursor(boolean normal){
            cursor = normal ? cursorBasic : cursorAttack;
        }
    }

    /**
     * Class representing the camera, ie what the user can see.
     */
    public static class Camera extends Position {

        /**
         * Dimension of the camera.
         */
        public int width, height;

        /**
         * Dimension of the universe.
         */
        private final Dimension size;

        public Camera (Dimension size) {
            super();
            this.size = size;
        }

        @Override
        public boolean canMove (Direction d) {
            return (d == Direction.LEFT   && position.x > 0) ||
                   (d == Direction.RIGHT  && position.x + width < size.width) ||
                   (d == Direction.TOP    && position.y > 0) ||
                   (d == Direction.BOTTOM && position.y + height < size.height);
        }

        @Override
        protected boolean hasReachLocation () {
            return (real.x == target.x * MainFrame.UNIT) &&
                   (real.y == target.y * MainFrame.UNIT);
        }

        /**
         * @param x the horizontal coordinate of the target
         * @param y the vertical coordinate
         * Center the camera on the coordinates (if the tile is on the edge of the map, it just bring it into the screen)
         */
        @Override
        public void setLocation (int x, int y){
            int xx        = x - width / 2,
                yy        = y - height / 2;

            if (xx < 0) xx = 0;
            else if (xx + width > size.width) xx = size.width - width;

            if (yy < 0) yy = 0;
            else if (yy + height > size.height) yy = size.height - height;

            position.move(xx, yy);
            target.move(xx, yy);
            real.move(xx * MainFrame.UNIT, yy * MainFrame.UNIT);
        }
    }

    /**
     * Point representing respectively the position on the map, 
     * the position on the screen and the target position
     * on the map while moving.
     */
    protected Point position, real, target;

    /**
     * Moving direction.
     */
    protected Direction direction;

    public Position (int x, int y) {
        this.direction = Direction.NONE;
        this.position  = new Point(x, y);
        this.target    = new Point(x, y);
        this.real      = new Point(x * MainFrame.UNIT, y * MainFrame.UNIT);
    }

    public Position () {
        this(0,0);
    }

    public final int getX () {
        return position.x;
    }

    public final int getY () {
        return position.y;
    }

    public final Point position () {
        return position.getLocation();
    }

    public final int getOffsetX () {
        return real.x - position.x * MainFrame.UNIT;
    }

    public final int getOffsetY() {
        return real.y - position.y * MainFrame.UNIT;
    }

    /**
     * Make the position move
     * @return true if the movement is finished, false otherwise
     */
    public final boolean move () {
        direction.move(real, 4);

        if (hasReachLocation()) {
            direction.move(position);
            direction = Direction.NONE;
        }

        return direction != Direction.NONE;
    }

    /**
     * Set the movement of the position if he can move by the given way.
     */
    public final void setDirection (Direction d) {
        if (canMove(d)) {
            direction = d;
            direction.move(target);
        }
    }

    /**
     * @return true if can move by the given direction, false othewise
     */
    public abstract boolean canMove (Direction d);

    /**
     * @return true if real location has reach target location
     */
    protected boolean hasReachLocation () { return true; }

    public abstract void setLocation (int x, int y);

    public void setLocation (Point p){
        setLocation(p.x, p.y);
    }
}

