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
        UNIT(true);

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
            
            new Index("Move", () -> {
              mode = Mode.IDLE;
              world.clearTarget();
              UnitRenderer.Render targetRender = UnitRenderer.getRender(targetUnit);
              targetRender.setState("move");
              path.apply();
              targetRender.setState("idle");
              mode = Mode.MOVE;
              cursor.setLocation(unitCursor.position());
              path.visible = false;
            });

            new Index("Attack", () -> {
              mode = Mode.ATTACK;
              world.updateTarget(targetUnit);
              unitCursor.setLocation(cursor.position());
            });

            new Index("Capture", () -> {
              AbstractUnit unit = targetUnit;
              actions.get(1).action.run();
              if (unit.getX() == unitCursor.getX() && unit.getY() == unitCursor.getY() && unit.isEnabled()){
                AbstractBuilding b = Universe.get().getBuilding(unit.getX(),unit.getY());
                if (((CaptureBuilding)unit).capture(b))
                  BuildingRenderer.getRender(b).updateState(null);
              }
            });

            new Index("Supply", () -> {});
            new Index("Heal", () -> {});

            new Index("Hide", () -> {});
            new Index("Reveal", () -> {});

            new Index("Load", () -> {});
            new Index("Unload", () -> {});

            new Index("Cancel", world::clearTarget);
        }
        
        @Override
        public void onOpen () {
          targetUnit = world.getUnit(cursor.position());
          actions.forEach((key, value) -> value.setActive(false));
          actions.get(10).setActive(true);
          if (!targetUnit.isEnabled()) return;

          actions.get(1).setActive(true);
          if (targetUnit.canAttack()) actions.get(2).setActive(true);
          if (targetUnit instanceof CaptureBuilding &&
                ((CaptureBuilding)targetUnit).canCapture(Universe.get().getBuilding(unitCursor.getX(),unitCursor.getY())))
            actions.get(3).setActive(true);
          if (targetUnit instanceof SupplyUnit) actions.get(4).setActive(true);
          if (targetUnit instanceof HealerUnit) actions.get(5).setActive(true);
          if (targetUnit instanceof HideableUnit)
            if (((HideableUnit)targetUnit).hidden()) actions.get(7).setActive(true);
            else actions.get(6).setActive(true);

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
        path            = new PathRenderer(camera) {
         
          Point last = null;
          boolean[][] area;

          @Override
          public void rebase (AbstractUnit unit) {
              area = new boolean[size.height][size.width];
              
              if (mode == Mode.UNIT) unit.reachableLocation(area);
              else if (mode == Mode.ATTACK) unit.renderTarget(area);
              super.rebase(unit);
          }

          @Override
          public boolean add (Direction dir) {

            Point pt = unitCursor.position();
            dir.move(pt);

            if (area[pt.y][pt.x]) {
              if (last == null) return super.add(dir);
              if (!last.equals(pt)) shorten(pt);
              last = null;
            } else last = pt;
            return false;
          }

        };
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
            targetUnit = world.getUnit(cursor.position()); 
            if (mode.canMove()) {
              if      (key == KeyEvent.VK_UP)    move(Direction.TOP);
              else if (key == KeyEvent.VK_LEFT)  move(Direction.LEFT);
              else if (key == KeyEvent.VK_RIGHT) move(Direction.RIGHT);
              else if (key == KeyEvent.VK_DOWN)  move(Direction.BOTTOM);
              else if (key == KeyEvent.VK_ENTER) {
                if (mode == Mode.UNIT) unitActionPanel.setVisible(true);
                else if (mode == Mode.ATTACK) {
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
                } else {
                  if (targetUnit == null) {
                    AbstractBuilding b = world.getBuilding (cursor.position());
                    if (!world.isVisible(cursor.position()) || b == null 
                          || !(b instanceof FactoryBuilding)
                          || ((OwnableBuilding)b).getOwner() != world.getCurrentPlayer())
                      actionPanel.setVisible (true);
                    else
                      buildingPanel.setVisible (true);
                  } else if (targetUnit.getPlayer() == world.getCurrentPlayer() &&
                           targetUnit.isEnabled()) {
                    mode = Mode.UNIT;
                    world.updateTarget(targetUnit);
                    path.rebase(targetUnit);
                    path.visible = true;
                  }
                  else actionPanel.setVisible(true);
                  unitCursor.setLocation(cursor.position());
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
          
          if (mode == Mode.UNIT && unitCursor.canMove(d)) path.add(d);
    }

    public Mode getMode () {
        return mode;
    }

    public void setMode (Mode mode) {
      this.mode = mode;
    }

}

