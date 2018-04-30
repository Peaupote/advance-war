package fr.main.view.render;

import java.awt.*;
import java.util.*;

import fr.main.model.*;
import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.buildings.MissileLauncher;
import fr.main.model.players.Player;
import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.HealerUnit;
import fr.main.model.units.TransportUnit;
import fr.main.model.units.naval.NavalUnit;
import fr.main.view.MainFrame;
import fr.main.view.controllers.GameController;
import fr.main.view.controllers.StatController;
import fr.main.view.render.buildings.BuildingRenderer;
import fr.main.view.render.sprites.Sprite;
import fr.main.view.render.terrains.TerrainRenderer;
import fr.main.view.render.units.UnitRenderer;

public class UniverseRenderer extends MapRenderer {

    public final GameController controller;
    private final Color fogColor    = new Color (0,0,0,100),
                        moveColor   = new Color (0, 255, 0, 50),
                        targetColor = new Color (255, 0, 0, 100),
                        loadColor   = new Color (0, 0, 255, 50),
                        healColor   = new Color (0, 255, 0, 50),
                        rangeColor  = new Color (255, 0, 0, 80);


    private final boolean[][] targets;
    private Point upperLeft = new Point(0,0), lowerRight;
    private Color tColor;

    /**
     * A text message displayed for an specific amount of time on a specific position
     */
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

    private final LinkedList<FlashMessage>   flashs;
    private final LinkedList<DeathAnimation> deathAnimation;

    public UniverseRenderer (Universe.Board b, GameController controller){
        super(b);

        this.controller = controller;
        controller.makeView().getWeatherController().update(getWeather());
        
        targets        = new boolean[map.board.length][map.board[0].length];
        lowerRight     = new Point(map.board.length, map.board[0].length);
        flashs         = new LinkedList<FlashMessage>();
        deathAnimation = new LinkedList<DeathAnimation>();
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

    public Frame draw (Graphics g, int x, int y, int offsetX, int offsetY) {
        Frame frame = super.draw(g, x, y, offsetX, offsetY);

        // draw the units
        frame.forEach((i, j) -> {
                if (targets[i][j]) {
                    g.setColor(tColor);
                    g.fillRect(coords[i][j].x, coords[i][j].y, MainFrame.UNIT, MainFrame.UNIT);
                }

                if (!fogwar[i][j]) {
                    g.setColor(fogColor);
                    g.fillRect(coords[i][j].x, coords[i][j].y, MainFrame.UNIT, MainFrame.UNIT);
                }

                if (map.units[i][j] != null)
                    if (map.units[i][j].getPlayer() == getCurrentPlayer() ||
                        isVisibleOpponentUnit(j, i))
                        UnitRenderer.render(g, coords[i][j], map.units[i][j]);
            });

        // draw the flash messages
        Iterator<FlashMessage> iterator = flashs.iterator();
        while (iterator.hasNext()) {
            FlashMessage message = iterator.next();
            g.setColor(message.type.color);
            g.drawString (message.message, message.x, message.y);
            message.time -= 10;
            if (message.time <= 0) iterator.remove();
        }

        // draw the death animations
        Iterator<DeathAnimation> iterator2 = deathAnimation.iterator();
        while (iterator2.hasNext()){
            DeathAnimation d = iterator2.next();
            g.drawImage(d.getImage(), (d.x - controller.camera.getX()) * MainFrame.UNIT, (d.y - controller.camera.getY()) * MainFrame.UNIT, null);
            d.time --;
            if (d.time <= 0) iterator2.remove();
        }

        // draw the missile animation
        if (missileAnimation != null)
            missileAnimation.draw(g);

        return frame;
    }

    /**
     * Goes to the next player
     */
    @Override
    public void next(){
        Weather w = getWeather();
        super.next();
        if (controller != null){
            if (w != getWeather()) controller.makeView().getWeatherController().update(w);
            controller.onOpen();
        }
    }

    @Override
    public void playerLoose(Player p){
      if (players != null && !players.hasNext())
        MainFrame.setScene(new StatController(getDay(), map.players));
    }

    /**
     * Depending on the mode, sets some tiles to true
     * For example in attack mode, it will set to true the tiles that can be attacked
     */
    public void updateTarget (AbstractUnit unit) {
        clearTarget();
        if (controller.getMode() == GameController.Mode.UNIT) {
            MoveZone m = unit.getMoveMap();
            int moveQuantity = unit.getMoveQuantity();
            Node[][] n = m.map;
            upperLeft = m.offset;
            lowerRight.move(upperLeft.x + n[0].length, upperLeft.y + n.length);
            for (int j = upperLeft.y; j < lowerRight.y; j++)
                for (int i = upperLeft.x; i < lowerRight.x; i ++)
                    targets[j][i] = n[j - upperLeft.y][i - upperLeft.x].lowestCost <= moveQuantity;
            tColor = unit.getPlayer() == getCurrentPlayer() ? moveColor : targetColor;
            
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
        } else if (controller.getMode() == GameController.Mode.RANGE){
            tColor = rangeColor;
            unit.renderTargets(targets);
            upperLeft.move(0,0);
            lowerRight.move(targets.length, targets[0].length);
        }
    }

    public boolean updateTarget(Point p){
        clearTarget();
        if (controller.getMode() == GameController.Mode.MISSILE_LAUNCHER){
            int[][] t = Direction.getNonCardinalDirections();
            for (int i = 0 ; i <= 3 ; i++)
                for (int j = 0 ; j <= i ; j ++)
                    for (int[] tab : t){
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

    /**
     * Sets all the booleans in the boolean map to false
     */
    public void clearTarget () {
        for (int i = upperLeft.x; i < lowerRight.x; i++)
            for (int j = upperLeft.y; j < lowerRight.y; j++)
                targets[j][i] = false;
        upperLeft.move(0,0);
        lowerRight.move(coords[0].length, coords.length);
    }

    @Override
    public boolean setUnit(int x, int y, AbstractUnit u){
        AbstractUnit unit = getUnit(x, y);
        if (unit != null && unit.dead()){
            UnitRenderer.remove(unit);
            UnitRenderer.getRender(unit).deathSound();
            displayDeathAnimation(new Point(x, y), unit instanceof NavalUnit);
        }
        return super.setUnit(x, y, u);
    }

    /**
     * Add a flash message
     */
    public void flash (String message, int x, int y, int time) {
        flashs.add(new FlashMessage(message, x, y, time, FlashMessage.Type.SUCCESS));
    }

    public void flash (String message, int x, int y, int time, FlashMessage.Type type) {
        flashs.add(new FlashMessage(message, x, y, time, type));
    }

    private static final Image[] explosion = new Image[10];
    private static final Image[] sink      = new Image[6];

    static{
        Sprite s = Sprite.get("./assets/ingame/death.png");
        sink[0] = s.getImage(5, 59, 24, 32);
        sink[1] = s.getImage(5, 93, 24, 32);
        sink[2] = s.getImage(5, 127, 24, 32);
        sink[3] = s.getImage(5, 161, 24, 32);
        sink[4] = s.getImage(5, 195, 24, 32);
        sink[5] = s.getImage(5, 229, 24, 32);

        explosion[0] = s.getImage(35, 5, 33, 32);
        explosion[1] = s.getImage(35, 34, 33, 32);
        explosion[2] = s.getImage(35, 66, 33, 32);
        explosion[3] = s.getImage(35, 96, 33, 32);
        explosion[4] = s.getImage(35, 127, 33, 32);
        explosion[5] = s.getImage(35, 159, 33, 32);
        explosion[6] = s.getImage(35, 195, 33, 32);
        explosion[7] = s.getImage(35, 227, 33, 32);
        explosion[8] = s.getImage(35, 261, 33, 32);
        explosion[9] = s.getImage(35, 295, 33, 30);


        Sprite d        = Sprite.get("./assets/ingame/missile.png");
        arrival         = d.getImage(31,  1,  8, 19, 2);
        launch          = d.getImage(32, 51,  8, 32, 2);
        fly             = d.getImage(33, 86,  8, 46, 2);
        smokeUp         = d.getImage(2,   2, 12, 28, 2);
        smokeDown       = d.getImage(15,  0, 13, 31, 2);
        explosionBis    = new Image[]{
            d.getImage(0, 33, 30, 30),
            d.getImage(0, 67, 30, 30),
            d.getImage(2, 101, 30, 32)
        };
    }

    public static final Image arrival, launch, fly, smokeUp, smokeDown;
    protected static final Image[] explosionBis;

    /**
     * @param pt the position of the animation to display
     * @param naval is true to display the sink animation and false for the explosion
     */
    public void displayDeathAnimation(Point pt, boolean naval){
        deathAnimation.add(new DeathAnimation(pt, naval ? sink : explosion));
    }

    /**
     * Class representing a death animation
     */
    class DeathAnimation{
        public final int x, y;
        public int time;
        private final Image[] animation;

        public DeathAnimation(Point location, Image[] animation){
            this.x = location.x;
            this.y = location.y;
            this.animation = animation;
            this.time     = animation.length * 5;
        }

        public Image getImage(){
            return animation[(time - 1) / 5];
        }
    }

    private MissileAnimation missileAnimation;

    public void fireMissile(MissileLauncher missile, int x, int y){
        BuildingRenderer.getRender(missile).updateState("inactive");
        missileAnimation = new MissileAnimation(missile, x, y);
    }

    /**
     * Represents a missile animation
     */
    class MissileAnimation{
        private int x, y;
        private boolean goingUp;
        public final int beginX, beginY, endX, endY;

        private final HashMap<AbstractUnit, Integer> damages;
        private final MissileLauncher missile;

        /**
         * used to know what image is displayed.
         */
        private int explosionNumber;

        public MissileAnimation(MissileLauncher missile, int endX, int endY){
            this.missile    = missile;
            this.beginX     = (missile.getX() - controller.camera.getX()) * MainFrame.UNIT;
            this.beginY     = (missile.getY() - controller.camera.getY()) * MainFrame.UNIT;
            this.x          = beginX;
            this.y          = beginY - (controller.camera.getY() - 1) * MainFrame.UNIT - UniverseRenderer.launch.getHeight(controller.makeView());
            this.endX       = (endX - controller.camera.getX()) * MainFrame.UNIT;
            this.endY       = (endY - controller.camera.getY()) * MainFrame.UNIT;
            this.goingUp    = true;
            explosionNumber = 0;
            damages = new HashMap<AbstractUnit, Integer>();

            if (!missile.canFire(endX, endY)){
                UniverseRenderer.this.missileAnimation = null;
                return;
            }

            if (x >= controller.camera.width * MainFrame.UNIT || x < 0){
                    y = arrival.getHeight(controller.makeView());
                    goingUp = false;
                    x = this.endX;
            }


            for (int i = -3; i < 4; i++)
                for (int j = -3; j < 4; j++){
                    AbstractUnit unit = getUnit(endX + i, endY + j);
                    if (Math.abs(i) + Math.abs(j) <= 3 && unit != null && !damages.containsKey(unit))
                        damages.put(unit, unit.getLife());
                }
            if (getUnit(missile.getX(), missile.getY()) != null)
                getUnit(missile.getX(), missile.getY()).dies();
        }

        public void draw(Graphics g){
            if (!goingUp && x == endX && y >= endY){ // end of the animation

                if (explosionNumber < 24){ // display final explosion
                    for (int i = -3; i < 4; i++)
                        for (int j = -3; j < 4; j++){
                            int tmpX = endX + i * MainFrame.UNIT, tmpY = endY + j * MainFrame.UNIT;
                            if (Math.abs(i) + Math.abs(j) <= 3 && tmpX >= 0 && tmpY >= 0 &&
                                    tmpX < (controller.camera.getX() + controller.camera.width)  * MainFrame.UNIT &&
                                    tmpY < (controller.camera.getY() + controller.camera.height) * MainFrame.UNIT)
                                g.drawImage(explosionBis[explosionNumber / 8], tmpX, tmpY, controller.makeView());
                        }

                    explosionNumber ++;
                }else{ // do the actual damages and delete this animation
                    missile.fire(endX / MainFrame.UNIT + controller.camera.getX(), endY / MainFrame.UNIT + controller.camera.getY());

                    for (Map.Entry<AbstractUnit, Integer> e : damages.entrySet())
                        flash ("" + (e.getKey().getLife() - e.getValue()),
                            (e.getKey().getX() - controller.camera.getX() + 1) * MainFrame.UNIT + 5,
                            (e.getKey().getY() - controller.camera.getY()) * MainFrame.UNIT + 5, 1000,
                            FlashMessage.Type.ALERT);
                    UniverseRenderer.this.missileAnimation = null;
                    controller.setMode(GameController.Mode.MOVE);
                }
            }else{ // display the rise and fall of the missile
                if (y <= - fly.getHeight(controller.makeView()) - smokeUp.getHeight(controller.makeView())) {
                    y = 0;
                    goingUp = false;
                    x = endX;
                }

                if (goingUp){ // rise of the missile
                    if (beginY + MainFrame.UNIT - y <= fly.getHeight(controller.makeView()))
                        g.drawImage(launch, x + (MainFrame.UNIT - launch.getWidth(controller.makeView())) / 2, y, controller.makeView());
                    else if (beginY + MainFrame.UNIT - y >= fly.getHeight(controller.makeView()) + smokeUp.getHeight(controller.makeView())) {
                        // engines are turned on
                        g.drawImage(fly, x + (MainFrame.UNIT - fly.getWidth(controller.makeView())) / 2, y, controller.makeView());
                        g.drawImage(smokeUp, x + (MainFrame.UNIT - smokeUp.getWidth(controller.makeView())) / 2, y + fly.getHeight(controller.makeView()), controller.makeView());
                    }
                    else
                        g.drawImage(fly, x + (MainFrame.UNIT - fly.getWidth(controller.makeView())) / 2, y, controller.makeView());
                }else{ // fall of the missile
                    g.drawImage(arrival, x + (MainFrame.UNIT - arrival.getWidth(controller.makeView())) / 2, y - arrival.getHeight(controller.makeView()), controller.makeView());
                    g.drawImage(smokeDown, x + (MainFrame.UNIT - smokeDown.getWidth(controller.makeView())) / 2, y - arrival.getHeight(controller.makeView()) - smokeDown.getHeight(controller.makeView()), controller.makeView());
                }

                y += (goingUp ? -1 : 2) * 2;
            }
        }
    }
}
