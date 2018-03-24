package fr.main.view;

import java.util.HashMap;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import java.awt.Dimension;
import java.util.LinkedList;

import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.model.Direction;
import fr.main.model.buildings.AbstractBuilding;
import fr.main.view.render.buildings.BuildingRenderer;
import fr.main.view.MainFrame;
import fr.main.view.render.UniverseRenderer;
import fr.main.view.interfaces.*;
import fr.main.view.render.PathRenderer;
import fr.main.view.render.units.UnitRenderer;
import fr.main.model.units.*;
import fr.main.model.buildings.*;

public class Controller extends KeyAdapter implements MouseMotionListener {

    public final Position.Cursor cursor, unitCursor;
    public final Position.Camera camera;
    public final UniverseRenderer world;
    public final int moveRange = 3;

    private boolean isListening = false, listenMouse = false;
    private final Dimension size;
    private Point mouse;
    private Mode mode;
    private ActionPanel actionPanel, focusedActionPanel;
    private BuildingInterface buildingPanel;
    private AbstractUnit targetUnit;
    private UnitActionPanel unitActionPanel;
    private DayPanel dayPanel;
    public PathRenderer path;

    public enum Mode {
        IDLE(false),
        MOVE(true),
        MENU(false),
        ATTACK(true),
        UNIT(true),
        LOAD(true),
        HEAL(true);

        private boolean moveable;

        private Mode (boolean moveable) {
            this.moveable = moveable;
        }

        public boolean canMove () {
            return moveable;
        }
    }

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
            x = MainFrame.WIDTH - 200;
            y = 10;

            new Index("Finish turn", () -> {
                    world.next();
                    dayPanel.setVisible(true);
            });

            new Index("Wait", () -> {});
        }

    }

    public class UnitActionPanel extends ControllerPanel {

        public UnitActionPanel () {
            super();
            x = MainFrame.WIDTH - 200;
            y = 10;
         
            new Index("Stay", () -> {});

            new Index("Attack", () -> {
                mode = Mode.ATTACK;
                world.updateTarget(targetUnit);
                unitCursor.setLocation(cursor.position());
            });

            new Index("Capture", () -> {
                if (targetUnit.getX() == unitCursor.getX() && targetUnit.getY() == unitCursor.getY() && targetUnit.isEnabled()){
                    AbstractBuilding b = Universe.get().getBuilding(targetUnit.getX(),targetUnit.getY());
                    if (((CaptureBuilding)targetUnit).capture(b))
                        BuildingRenderer.getRender(b).updateState(null);
                }
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
                //TODO
            });
        }
        
        @Override
        public void onOpen () {
            targetUnit = world.getUnit(cursor.position());
            actions.forEach((key, value) -> value.setActive(false));
            actions.get(1).setActive(true);
            if (!targetUnit.isEnabled()) return;
            if (targetUnit.canAttack()) actions.get(2).setActive(true);
            if (targetUnit instanceof CaptureBuilding &&
                        ((CaptureBuilding)targetUnit).canCapture(Universe.get().getBuilding(unitCursor.getX(),unitCursor.getY())))
                actions.get(3).setActive(true);
            if (targetUnit instanceof SupplyUnit) actions.get(4).setActive(true);
            if (targetUnit instanceof HealerUnit && ((HealerUnit)targetUnit).canHeal()) actions.get(5).setActive(true);
            if (targetUnit instanceof HideableUnit)
                if (((HideableUnit)targetUnit).hidden()) actions.get(7).setActive(true);
                else actions.get(6).setActive(true);

            for (Direction d : Direction.cardinalDirections()){
                AbstractUnit u = Universe.get().getUnit(targetUnit.getX() + d.x, targetUnit.getY() + d.y);
                if (u instanceof TransportUnit && ((TransportUnit)u).canCharge(targetUnit)) {
                    actions.get(8).setActive(true);
                    break;
                }
            }

            super.onOpen();
        }

        @Override
        public void onClose () {
            super.onClose();
            path.visible = false;
        }
    }


    public Controller (Player ps[]) {
        world      = new UniverseRenderer("maptest.map", this);
        size       = world.getDimension();
        camera     = new Position.Camera(size);
        cursor     = new Position.Cursor(camera, size);
        unitCursor = new Position.Cursor(camera, size);

        mouse           = new Point(1,1);
        actionPanel     = new MainActionPanel();
        dayPanel        = new DayPanel();
        mode            = Mode.MOVE;
        path            = new PathRenderer(camera);
        unitActionPanel = new UnitActionPanel();

        buildingPanel = new BuildingInterface(this);
        new PlayerPanel (cursor, camera);
        new Minimap (camera, new TerrainPanel (cursor, camera));
        dayPanel.setVisible(true);
    }

    @Override
    public void keyPressed (KeyEvent e) {
        int key = e.getKeyCode();
        if (!isListening) {
            isListening = true;
            if (mode.canMove()) {
                targetUnit = world.getUnit(cursor.position());
                if      (key == KeyEvent.VK_UP)    move(Direction.TOP);
                else if (key == KeyEvent.VK_LEFT)  move(Direction.LEFT);
                else if (key == KeyEvent.VK_RIGHT) move(Direction.RIGHT);
                else if (key == KeyEvent.VK_DOWN)  move(Direction.BOTTOM);
                else if (key == KeyEvent.VK_ENTER) {
                    if (mode == Mode.UNIT) {
                        mode = Mode.IDLE;
                        new Thread(() -> {
                            world.clearTarget();
                            UnitRenderer.Render targetRender = UnitRenderer.getRender(targetUnit);
                            targetRender.setState("move");
                            path.visible = false;
                            boolean b = path.apply();
                            targetRender.setState("idle");
                            cursor.setLocation(unitCursor.position());

                            if (b && targetUnit.isEnabled()) unitActionPanel.setVisible(true);
                            else mode = Mode.MOVE;
                        }).start();
                    } else if (mode == Mode.ATTACK) {
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
                        }
                        mode = Mode.MOVE;
                        world.clearTarget();
                    }else if (mode == Mode.HEAL){
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
                    } else if (mode == Mode.MOVE) {
                        if (targetUnit == null) {
                            AbstractBuilding b = world.getBuilding (cursor.position());
                            if (!world.isVisible(cursor.position()) || b == null 
                                    || !(b instanceof FactoryBuilding)
                                    || ((OwnableBuilding)b).getOwner() != world.getCurrentPlayer())
                                actionPanel.setVisible (true);
                            else buildingPanel.setVisible (true);
                        } else if (targetUnit.getPlayer() == world.getCurrentPlayer() &&
                                     targetUnit.isEnabled()) {
                            mode = Mode.UNIT;
                            world.updateTarget(targetUnit);
                            path.rebase(targetUnit);
                            path.visible = true;
                        }
                        else actionPanel.setVisible(true);
                        unitCursor.setLocation(cursor.position());
                    }else if (mode == Mode.LOAD){
                        AbstractUnit target = world.getUnit(unitCursor.position());
                        if (target instanceof TransportUnit && ((TransportUnit)target).canCharge(targetUnit))
                            ((TransportUnit)target).charge(targetUnit);
                        mode = Mode.MOVE;
                        world.clearTarget();
                    }
                } else if (key == KeyEvent.VK_ESCAPE) {
                    mode = Mode.MOVE;
                    world.clearTarget();
                    path.visible = false;
                }
            } else if (mode == Mode.MENU) {
                    if      (key == KeyEvent.VK_UP)    focusedActionPanel.goUp();
                    else if (key == KeyEvent.VK_DOWN)  focusedActionPanel.goDown();
                    else if (key == KeyEvent.VK_ENTER) focusedActionPanel.perform();
                    else if (key == KeyEvent.VK_ESCAPE){
                        focusedActionPanel.setVisible (false);
                        world.clearTarget();
                    }
            } else if (key == KeyEvent.VK_ESCAPE) {
                mode = Mode.MOVE;
                world.clearTarget();
                path.visible = false;
            }
        }
    }

    @Override
    public void keyReleased (KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseDragged (MouseEvent e) {}

    @Override
    public void mouseMoved (MouseEvent e) {
        if (mode == Mode.MOVE){
            mouse.x = e.getX() / MainFrame.UNIT;
            mouse.y = e.getY() / MainFrame.UNIT;
            listenMouse = true;
        }
    }

    /**
     * Function called each loop turn
     */
    public void update () {
        isListening = cursor.move() | camera.move() |
                            (mode != Mode.MOVE && mode.canMove() && unitCursor.move());

        if (!isListening && mode.canMove() && listenMouse) {
            if (camera.getX() > 0 && mouse.x <= moveRange) camera.setDirection(Direction.LEFT);
            else if (camera.getX() + camera.width < size.width && camera.width - mouse.x <= moveRange) camera.setDirection(Direction.RIGHT);
            else if (mouse.y <= moveRange) camera.setDirection(Direction.TOP);
            else if (camera.height - mouse.y <= moveRange) camera.setDirection(Direction.BOTTOM);

            cursor.setLocation(mouse.x + camera.getX(), mouse.y + camera.getY());
        }
    }

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

}