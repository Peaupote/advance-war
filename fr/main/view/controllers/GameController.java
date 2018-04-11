package fr.main.view.controllers;

import java.awt.event.*;
import java.awt.*;
import java.util.*;

import fr.main.model.players.Player;
import fr.main.model.*;
import fr.main.model.units.*;
import fr.main.model.units.naval.NavalUnit;
import fr.main.model.buildings.*;

import fr.main.view.*;
import fr.main.view.views.GameView;
import fr.main.view.interfaces.*;
import fr.main.view.render.PathRenderer;
import fr.main.view.render.UniverseRenderer;
import fr.main.view.render.units.UnitRenderer;
import fr.main.view.render.buildings.BuildingRenderer;
import fr.main.view.render.sprites.Sprite;
import fr.main.view.sound.Sdfx;

/**
 * Game Controller.
 */
public class GameController extends Controller {

    /**
     * View associated with the game controller.
     */
    protected GameView gameViewController;

    /**
     * Cursor is the cursor used when the player is in
     * mode MOVE or MENU; unitCursor is the cursor used 
     * otherwise.
     */
    public final Position.Cursor cursor, unitCursor;

    /**
     * Camera describing what's the visible part of the
     * Universe.
     */
    public final Position.Camera camera;

    /**
     * Renderer for the map.
     */
    public final UniverseRenderer world;

    /**
     * Minimum distance untill the cursor make the camera move.
     */
    public final int moveRange = 3;

    /**
     * isListening is true if and only if the controller
     * is listening to user inputs
     *
     * listenMouse is true if and only if the controller
     * is listening to user mouse inputs.
     */
    private boolean isListening = false, listenMouse = false;

    /**
     * Dimension of the world.
     */
    private final Dimension size;

    /**
     * Last recorded position of the mouse on the screen.
     */
    private Point mouse;

    /**
     * Current user mode.
     */
    private Mode mode;

    /**
     * actionPanel.
     */
    private ActionPanel focusedActionPanel;

    /**
     * MainActionPanel for user actions at the end ot the turn.
     */
    private MainActionPanel actionPanel;

    /**
     * Panel for choosing what unit to create.
     */
    private BuildingInterface buildingPanel;

    /**
     * Last unit selected by a player
     * can be null if no unit is selected.
     */
    private AbstractUnit targetUnit;

    /**
     * Choose unit to unload from a transport unit.
     */
    private TransportUnitPanel transportUnitPanel;

    /**
     * All units actions.
     */
    private UnitActionPanel unitActionPanel;

    private DayPanel dayPanel;
    public PathRenderer path;

    /**
     * Enumerate all possibles modes for user.
     */
    public enum Mode {

        /**
         * Can't do any action.
         */ 
        IDLE(false),

        /**
         * Can move the cursor.
         */
        MOVE(true),

        /**
         * When a menu is open.
         */
        MENU(false),

        /**
         * When user choose target to attack.
         */
        ATTACK(true),

        /**
         * When user move the targetUnit.
         */
        UNIT(true),

        /**
         * When choosing you want to go in.
         */
        LOAD(true),

        /**
         * When choosing unit to unload.
         */
        UNLOAD_CHOOSE(false),

        /**
         * When choosing where to unload the unit.
         */
        UNLOAD_LOCATE(true),

        /**
         * When choosing target for the missile launcher.
         */
        MISSILE_LAUNCHER(true),

        /**
         * When choosing unit to heal.
         */
        HEAL(true);

        /**
         * Can the cursor move in this mode.
         */
        private boolean moveable;

        private Mode (boolean moveable) {
            this.moveable = moveable;
        }

        public boolean canMove () {
            return moveable;
        }
    }

    /**
     * GameController action panel
     * each action panel's game must inherits from
     * this class to update the focusedActionPanel.
     */
    public class ControllerPanel extends ActionPanel {

        public void onOpen () {
            super.onOpen();
            mode = Mode.MENU;
            focusedActionPanel = this;
        }

        public void onClose () {
            super.onClose();
            mode = Mode.MOVE;
        }

        @Override
        public boolean showOnClose(InterfaceUI com) {
            return com != dayPanel;
        }
    }

    private class MainActionPanel extends ControllerPanel {

        public MainActionPanel () {
            super();
            x = MainFrame.width() - 200;
            y = 10;

            new Index("Finish turn", () -> {
                    world.next();
                    dayPanel.setVisible(true);
            });

            new Index("Wait", () -> {});
            new Index("Big power", world::bigPower);
            new Index("Small power", world::smallPower);
            new Index("Save", world::save);
            new Index("Quit to menu", () -> {
                MainFrame.setScene(new MenuController());
            });
            new Index("Quit game", () -> { System.exit(0); });
        }

        @Override
        public void onOpen(){
            actions.get(3).setActive(world.getCurrentPlayer().getCommander().canActivate(true));
            actions.get(4).setActive(world.getCurrentPlayer().getCommander().canActivate(false));
            super.onOpen();
        }

    }

    private class TransportUnitPanel extends ControllerPanel {

        private TransportUnit transportUnit;
        public AbstractUnit selected = null;

        public TransportUnitPanel (){
            super();
            x = MainFrame.width() - 200;
            y = 10;
        }

        private void update (TransportUnit t){
            transportUnit = t;
            actions.clear();
            setVisible(true);

            for (AbstractUnit u : t.getUnits())
                new Index(u.getName() + " (" + u.getLife() + ")", () -> {
                    transportUnitPanel.setVisible(false);
                    mode = Mode.UNLOAD_LOCATE;
                    selected = u;
                    world.updateTarget(targetUnit);
                });
        }
    }

    public AbstractUnit getTransportUnit(){
        return transportUnitPanel.selected;
    }

    public class UnitActionPanel extends ControllerPanel {

        public UnitActionPanel () {
            super();
            x = MainFrame.width() - 200;
            y = 10;
         
            new Index("Stay", () -> {});

            new Index("Attack", () -> {
                mode = Mode.ATTACK;
                unitCursor.setCursor(false);
                world.updateTarget(targetUnit);
                unitCursor.setLocation(cursor.position());
            });

            new Index("Capture", () -> {
                if (targetUnit.getX() == unitCursor.getX() &&
                    targetUnit.getY() == unitCursor.getY() &&
                    targetUnit.isEnabled()){
                    AbstractBuilding b = Universe.get().getBuilding(targetUnit.getX(),targetUnit.getY());
                    if (((CaptureBuilding)targetUnit).capture(b))
                        BuildingRenderer.getRender(b).updateState(null);
                }
            });

            new Index("Launch", () -> {
                mode = Mode.MISSILE_LAUNCHER;
                world.updateTarget(targetUnit.position());
            });

            new Index("Supply", () -> {
                SupplyUnit su = ((SupplyUnit)targetUnit);
                su.supply();
                Universe u = Universe.get();
                for (Direction d : Direction.cardinalDirections()){
                    int xx = targetUnit.getX() + d.x, yy = targetUnit.getY() + d.y;
                    AbstractUnit unit = u.getUnit(xx, yy);
                    if (su.canSupply(unit))
                        world.flash("replenished",
                            (xx - camera.getX()) * MainFrame.UNIT + 5,
                            (yy - camera.getY()) * MainFrame.UNIT + 5, 1000,
                            UniverseRenderer.FlashMessage.Type.ALERT);
                }
            });
            new Index("Heal", () -> {
                mode = Mode.HEAL;
                world.updateTarget(targetUnit);
                unitCursor.setLocation(cursor.position());
            });

            new Index("Hide", () -> {
                ((HideableUnit)targetUnit).hide();
            });
            new Index("Reveal", () -> {
                ((HideableUnit)targetUnit).hide();
            });

            new Index("Load", () -> {
                mode = Mode.LOAD;
                world.updateTarget(targetUnit);
                unitCursor.setLocation(cursor.position());
            });
            new Index("Unload", () -> {
                mode = Mode.UNLOAD_CHOOSE;
                transportUnitPanel.update((TransportUnit)targetUnit);
            });
        }
        
        @Override
        public void onOpen () {
            targetUnit = world.getUnit(cursor.position());

            if (targetUnit == null){
                setVisible(false);
                onClose();
                return;
            }

            actions.forEach((key, value) -> value.setActive(false));
            actions.get(1).setActive(true);
            if (!targetUnit.isEnabled()) return;

            // enable options associated with the unit
            AbstractBuilding building = Universe.get().getBuilding(unitCursor.getX(), unitCursor.getY());
            if (targetUnit.canAttack()) actions.get(2).setActive(true);
            if (targetUnit instanceof CaptureBuilding)
                if (((CaptureBuilding)targetUnit).canCapture(building))
                    actions.get(3).setActive(true);
                else if (building instanceof MissileLauncher && !((MissileLauncher)building).isFired())
                    actions.get(4).setActive(true);
            if (targetUnit instanceof SupplyUnit) actions.get(5).setActive(true);
            if (targetUnit instanceof HealerUnit && ((HealerUnit)targetUnit).canHeal()) actions.get(6).setActive(true);
            if (targetUnit instanceof HideableUnit)
                if (((HideableUnit)targetUnit).hidden()) actions.get(8).setActive(true);
                else actions.get(7).setActive(true);

            for (Direction d : Direction.cardinalDirections()){
                AbstractUnit u = Universe.get().getUnit(targetUnit.getX() + d.x, targetUnit.getY() + d.y);
                if (u instanceof TransportUnit && ((TransportUnit)u).canCharge(targetUnit)) {
                    actions.get(9).setActive(true);
                    break;
                }
            }
            if (targetUnit instanceof TransportUnit && ((TransportUnit)targetUnit).getOccupation() != 0)
                actions.get(10).setActive(true);

            super.onOpen();
        }

        @Override
        public void onClose () {
            super.onClose();
            path.visible = false;
        }
    }

    // TODO: make clean stuff to avoid copy-past
    public GameController (String mapName){
        world      = new UniverseRenderer(mapName, this);
        size       = world.getDimension();
        camera     = new Position.Camera(size);
        cursor     = new Position.Cursor(camera, size);
        unitCursor = new Position.Cursor(camera, size);
        InterfaceUI.clear();
        MainFrame.setCamera(camera);

        mouse              = new Point(1,1);
        actionPanel        = new MainActionPanel();
        dayPanel           = new DayPanel();
        mode               = Mode.MOVE;
        path               = new PathRenderer(camera);
        unitActionPanel    = new UnitActionPanel();
        transportUnitPanel = new TransportUnitPanel();
        buildingPanel      = new BuildingInterface(this);

        new PlayerPanel (cursor, camera);
        new Minimap (camera, new TerrainPanel (cursor, camera));
    }

    public GameController (String mapName, Player ps[]) {
        world      = new UniverseRenderer(mapName, ps, this);
        size       = world.getDimension();
        camera     = new Position.Camera(size);
        cursor     = new Position.Cursor(camera, size);
        unitCursor = new Position.Cursor(camera, size);
        InterfaceUI.clear();
        MainFrame.setCamera(camera);

        mouse              = new Point(1,1);
        actionPanel        = new MainActionPanel();
        dayPanel           = new DayPanel();
        mode               = Mode.MOVE;
        path               = new PathRenderer(camera);
        unitActionPanel    = new UnitActionPanel();
        transportUnitPanel = new TransportUnitPanel();
        buildingPanel      = new BuildingInterface(this);

        new PlayerPanel (cursor, camera);
        new Minimap (camera, new TerrainPanel (cursor, camera));
    }

    public void onOpen () {
        dayPanel.setVisible(true);
    }

    /**
     * Make targetUnit move to the selected position.
     */
    private void validUnitMove () {
      mode = Mode.IDLE;
      new Thread(() -> { // apply the movement
        world.clearTarget();
        UnitRenderer.Render targetRender = UnitRenderer.getRender(targetUnit);
        targetRender.setState("move");
        path.visible = false;
        boolean b = path.apply();
        targetRender.setState("idle");
        if (targetUnit.dead()) mode = Mode.MOVE;
        else {
          cursor.setLocation(unitCursor.position());
          if (b && targetUnit.isEnabled()){
            cursor.setLocation(targetUnit.position());
            unitCursor.setLocation(targetUnit.position());
            unitActionPanel.setVisible(true);
          } else mode = Mode.MOVE;
        }
      }).start();
    }

    /**
     * Make targetUnit fight against the selected unit.
     */
    private void validUnitTarget () {
      if (world.isEnabled(unitCursor.getX(), unitCursor.getY())){
        AbstractUnit target = world.getUnit(unitCursor.position());
        if (targetUnit.canAttack(target)) {
          int aLife = targetUnit.getLife(),
              tLife = target.getLife();
          targetUnit.attack(target);
          world.flash ("" + (targetUnit.getLife() - aLife),
                       (targetUnit.getX() - camera.getX() + 1) * MainFrame.UNIT + 5,
                       (targetUnit.getY() - camera.getY()) * MainFrame.UNIT + 5, 1000,
                       UniverseRenderer.FlashMessage.Type.ALERT);
          world.flash ("" + (target.getLife() - tLife),
                       (target.getX() - camera.getX() + 1) * MainFrame.UNIT + 5,
                       (target.getY() - camera.getY()) * MainFrame.UNIT + 5, 1000,
                       UniverseRenderer.FlashMessage.Type.ALERT);
          Sdfx.EXPLOSION.play();
        } else {
          targetUnit.setMoveQuantity(0);
          if (targetUnit.getPrimaryWeapon() != null) targetUnit.getPrimaryWeapon().shoot();
          else targetUnit.getSecondaryWeapon().shoot();
        }
      }
      unitCursor.setCursor(true);
      displayDamages = false;
      mode = Mode.MOVE;
      world.clearTarget();
    }

    /**
     * Action when click on a tile while the user in MOVE mode.
     */
    public void selectOnMove () {
      if (targetUnit == null) {
        AbstractBuilding b = world.getBuilding (cursor.position());
        if (!world.isVisible(cursor.position()) || b == null 
                || !(b instanceof FactoryBuilding)
                || ((OwnableBuilding)b).getOwner() != world.getCurrentPlayer())
            actionPanel.setVisible (true);
        else buildingPanel.setVisible (true);
      } else if (targetUnit.getPlayer() == world.getCurrentPlayer() &&
                 targetUnit.isEnabled()) {
        Sdfx.SELECT.play();
        mode = Mode.UNIT;
        world.updateTarget(targetUnit);
        path.rebase(targetUnit);
        path.visible = true;
      } else actionPanel.setVisible(true);
      unitCursor.setLocation(cursor.position());
    }

    /**
     * targetUnit heals the selected unit.
     */
    private void validHeal () {
      AbstractUnit target = world.getUnit(unitCursor.position());
      if (((HealerUnit)targetUnit).canHeal(target)) {
        int life = target.getLife();
        ((HealerUnit)targetUnit).heal(target);
        world.flash("+" + (target.getLife() - life),
                    (target.getX() - camera.getX() + 1) * MainFrame.UNIT + 5,
                    (target.getY() - camera.getY()) * MainFrame.UNIT + 5, 1000);
      }
      mode = Mode.MOVE;
      world.clearTarget();
    }

    /**
     * targetUnit loads the selected unit.
     */
    private void validLoad () {
      AbstractUnit target = world.getUnit(unitCursor.position());
      if (target instanceof TransportUnit && ((TransportUnit)target).canCharge(targetUnit))
          ((TransportUnit)target).charge(targetUnit);
      mode = Mode.MOVE;
      world.clearTarget();
    }

    /**
     * targetUnit unloads the selected unit.
     */
    private void validUnloadLocate () {
      ((TransportUnit)targetUnit).remove(getTransportUnit(),
                                         unitCursor.getX(), unitCursor.getY());
      mode = Mode.MOVE;
      world.clearTarget();                        
    }

    /**
     * missile launcher attack the given position.
     */
    private void validMissileLauncher () {
      fireMissile((MissileLauncher)world.getBuilding(targetUnit.getX(), targetUnit.getY()), unitCursor.getX(), unitCursor.getY());
      targetUnit.setMoveQuantity(0);
      world.clearTarget();
    }

    /**
     * Quit current mode to go back to MOVE mode.
     */
    private void escape () {
      mode = Mode.MOVE;
      unitCursor.setCursor(true);
      world.clearTarget();
      path.visible = false;
    }

    /**
     * Key events.
     */
    @Override
    public void keyPressed (KeyEvent e) {
        int key = e.getKeyCode();
        if (!isListening) {
            isListening = true;
            if (mode.canMove()) {
                targetUnit = world.getUnit(cursor.position());
                if      (key == KeyEvent.VK_UP) move(Direction.TOP);
                else if (key == KeyEvent.VK_LEFT) move(Direction.LEFT);
                else if (key == KeyEvent.VK_RIGHT) move(Direction.RIGHT);
                else if (key == KeyEvent.VK_DOWN) move(Direction.BOTTOM);
                else if (key == KeyEvent.VK_ENTER) {
                    if (mode == Mode.UNIT) validUnitMove();
                    else if (mode == Mode.ATTACK) validUnitTarget();
                    else if (mode == Mode.HEAL) validHeal();
                    else if (mode == Mode.MOVE) selectOnMove();
                    else if (mode == Mode.LOAD) validLoad();
                    else if (mode == Mode.UNLOAD_LOCATE) validUnloadLocate();
                    else if (mode == Mode.MISSILE_LAUNCHER) validMissileLauncher();
                } else if (key == KeyEvent.VK_ESCAPE) escape();
            } else if (mode == Mode.MENU) { // update index and valid menu action for focusedActionPanel
                    if      (key == KeyEvent.VK_UP)    focusedActionPanel.goUp();
                    else if (key == KeyEvent.VK_DOWN)  focusedActionPanel.goDown();
                    else if (key == KeyEvent.VK_ENTER) focusedActionPanel.perform();
                    else if (key == KeyEvent.VK_ESCAPE){
                        focusedActionPanel.setVisible (false);
                        world.clearTarget();
                    }
            } else if (key == KeyEvent.VK_ESCAPE) escape();
        }
    }

    /**
     * Update mouse position on the screen.
     */
    @Override
    public void mouseMoved (MouseEvent e) {
        if (mode.canMove()){
            mouse.x = e.getX() / MainFrame.UNIT;
            mouse.y = e.getY() / MainFrame.UNIT;

            if (mode == Mode.UNIT && targetUnit.canStop(unitCursor.position()))
                path.add(unitCursor.position().getLocation());
            listenMouse = true;
        }
    }

    /**
     * Mouse events.
     */
    public void mouseClicked(MouseEvent e) {
      if (listenMouse) {
        targetUnit = world.getUnit(cursor.position());
        if (e.getButton() == MouseEvent.BUTTON3) escape();
        else {
          if (mode == Mode.UNIT) validUnitMove();
          else if (mode == Mode.ATTACK) validUnitTarget();
          else if (mode == Mode.HEAL) validHeal();
          else if (mode == Mode.MOVE) selectOnMove();
          else if (mode == Mode.LOAD) validLoad();
          else if (mode == Mode.UNLOAD_LOCATE) validUnloadLocate();
          else if (mode == Mode.MISSILE_LAUNCHER) validMissileLauncher();
        }
      }
    }

    /**
     * Called each frame.
     */
    public void update () {
        // isListening if none of the cursors move
        isListening = cursor.move() | camera.move() |
                            (mode != Mode.MOVE && mode.canMove() && unitCursor.move());
        if (!isListening && mode == Mode.MISSILE_LAUNCHER) world.updateTarget(unitCursor.position());

        displayDamages = false;

        if (!isListening && mode == Mode.ATTACK && world.isVisibleOpponentUnit(unitCursor.position())){
            AbstractUnit attacker = world.getUnit(cursor.position()),
                         defender = world.getUnit(unitCursor.position());
            if (!point2.equals(cursor.position())) point1.move(-1,-1);
            if (attacker.canAttack(defender) && !point1.equals(unitCursor.position())) {
                point1  = unitCursor.position(); // the target
                point2  = cursor.position(); // the selected unit
                damage1 = AbstractUnit.damage(attacker, 
                            attacker.getPrimaryWeapon() != null && attacker.getPrimaryWeapon().canAttack(attacker, defender),
                            defender);
                damage2 = AbstractUnit.damage(defender, defender.getLife() - damage1, 
                            defender.getPrimaryWeapon() != null && defender.getPrimaryWeapon().canAttack(defender, attacker),
                            attacker);
            }
            if (attacker.canAttack(defender)) displayDamages = true;
        }

        // make the cursor follow the mouse
        if (!isListening && mode.canMove() && listenMouse) {
            if (camera.getX() > 0 && mouse.x <= moveRange) camera.setDirection(Direction.LEFT);
            else if (camera.getX() + camera.width < size.width && camera.width - mouse.x <= moveRange)
              camera.setDirection(Direction.RIGHT);
            else if (mouse.y <= moveRange) camera.setDirection(Direction.TOP);
            else if (camera.height - mouse.y <= moveRange) camera.setDirection(Direction.BOTTOM);

            Position.Cursor c = mode == Mode.MOVE || mode == Mode.MENU ? cursor : unitCursor;
            c.setLocation(mouse.x + camera.getX(), mouse.y + camera.getY());
        }
    }

    /**
     * Tell the cursor to move by the given direction.
     */
    private void move (Direction d) {
        listenMouse = false;
        Position.Cursor c = mode == Mode.MOVE || mode == Mode.MENU ? cursor : unitCursor;
        if ((d == Direction.LEFT && c.getX() - camera.getX() <= moveRange) ||
                (d == Direction.RIGHT && camera.getX() + camera.width - c.getX() <= moveRange + 1) ||
                (d == Direction.TOP && c.getY() - camera.getY() <= moveRange) ||
                (d == Direction.BOTTOM && camera.getY() + camera.height - c.getY() <= moveRange + 1))
            camera.setDirection (d);

        c.setDirection (d);
            
        if (mode == Mode.UNIT && unitCursor.canMove(d)){
            Point p = unitCursor.position().getLocation();
            d.move(p);
            path.add(p);
        }
    }

    public Mode getMode () {
        return mode;
    }

    public void setMode (Mode mode) {
        this.mode = mode;
    }

    public GameView makeView () {
        if (gameViewController == null) gameViewController = new GameView(this);
        return gameViewController;
    }

    private static final Image damageImage;
    private static final Image[] numbers;

    static{
        Sprite d = Sprite.get("./assets/ingame/attack.png");
        damageImage = d.getImage(3, 26, 32, 27);
        numbers = new Image[10];
        numbers[0]  = d.getImage(37, 37, 7, 12);
        numbers[1]  = d.getImage(48, 37, 4, 12);
        numbers[2]  = d.getImage(55, 37, 7, 12);
        numbers[3]  = d.getImage(64, 37, 7, 12);
        numbers[4]  = d.getImage(73, 37, 7, 12);
        numbers[5]  = d.getImage(82, 37, 7, 12);
        numbers[6]  = d.getImage(91, 37, 7, 12);
        numbers[7]  = d.getImage(100, 37, 7, 12);
        numbers[8]  = d.getImage(109, 37, 7, 12);
        numbers[9]  = d.getImage(118, 37, 7, 12);
    }

    private void displayDamages(Graphics g, int damage, Point location){
        int x = (location.x - camera.getX()) * MainFrame.UNIT,
        y = (location.y - camera.getY()) * MainFrame.UNIT - (location.y != 0 ? MainFrame.UNIT / 2 : 0);
        g.drawImage(damageImage, x, y, gameViewController);
        g.drawImage(numbers[damage / 100],       x + 1,  y + 10, gameViewController);
        g.drawImage(numbers[(damage / 10) % 10], x + 9,  y + 10, gameViewController);
        g.drawImage(numbers[damage % 10],        x + 16, y + 10, gameViewController);
    }

    public void displayDamages(Graphics g){
        if (displayDamages){
            displayDamages(g, damage1, point1);
            displayDamages(g, damage2, point2);
        }
    }

    private boolean displayDamages;
    private Point point1 = new Point(-1,-1), point2 = new Point(-1, -1);
    private int damage1, damage2;

    private void fireMissile(MissileLauncher missile, int x, int y){
        mode = Mode.IDLE;
        world.fireMissile(missile, x, y);
    }

}
