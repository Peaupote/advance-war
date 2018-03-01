package fr.main.view;

import java.util.HashMap;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import java.util.LinkedList;

import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.model.Direction;
import fr.main.view.MainFrame;
import fr.main.view.render.UniverseRenderer;
import fr.main.view.interfaces.*;
import fr.main.view.render.PathRenderer;
import fr.main.view.render.units.UnitRenderer;
import fr.main.model.units.*;

public class Controller extends KeyAdapter implements MouseMotionListener {

    public final Position.Cursor cursor, unitCursor;
    public final Position.Camera camera;
    public final UniverseRenderer world;
    public final int moveRange = 3;

    private boolean isListening = false, listenMouse = false;
    private Point mouse;
    private Mode mode;
    private ActionPanel actionPanel, focusedActionPanel;
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

    private class ControllerPanel extends ActionPanel {
    
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
              path.apply();
              mode = Mode.MOVE;
              cursor.setLocation(unitCursor.position());
              path.visible = false;
            });

            new Index("Attack", () -> {
              mode = Mode.ATTACK;
              unitCursor.setLocation(cursor.position());
            });

            new Index("Supply", () -> {});
            new Index("Heal", () -> {});

            new Index("Load", () -> {});
            new Index("Unload", () -> {});

            new Index("Cancel", () -> {});
        }
        
        @Override
        public void onOpen () {
          targetUnit = world.getUnit(cursor.position());
          actions.forEach((key, value) -> value.setActive(false));

          actions.get(7).setActive(true);
          if (targetUnit.getMoveQuantity() > 0) actions.get(1).setActive(true);
          if (targetUnit.canAttack()) actions.get(2).setActive(true);
          if (targetUnit instanceof SupplyUnit) actions.get(3).setActive(true);
          if (targetUnit instanceof HealerUnit) actions.get(4).setActive(true);
          if (targetUnit instanceof HideableUnit)
            if (((HideableUnit)targetUnit).hidden()) actions.get(6).setActive(true);
            else actions.get(5).setActive(true);

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
        camera     = new Position.Camera(world.getDimension());
        cursor     = new Position.Cursor(camera, world.getDimension());
        unitCursor = new Position.Cursor(camera, world.getDimension()) {

          @Override
          public boolean canMove(Direction d) {
            if (!super.canMove(d)) return false;
            boolean[][] map = new boolean[size.height][size.width];
            
            if (mode == Mode.UNIT) targetUnit.reachableLocation(map);
            else if (mode == Mode.ATTACK) targetUnit.renderTarget(map);

            Point target = position();
            d.move(target);
            return map[target.y][target.x];
          }

        };

        mouse           = new Point(1,1);
        actionPanel     = new MainActionPanel();
        dayPanel        = new DayPanel();
        mode            = Mode.MOVE;
        path            = new PathRenderer(camera);
        unitActionPanel = new UnitActionPanel();

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
                    if (targetUnit.canAttack(target)) targetUnit.attack(target);
                    mode = Mode.MOVE;
                } else {
                  if (targetUnit == null || !world.isVisible(cursor.position()))
                    actionPanel.setVisible (true);
                  else if (targetUnit.getPlayer() == world.getCurrentPlayer() && targetUnit.isEnabled()) {
                    mode = Mode.UNIT;
                    path.rebase(targetUnit);
                    path.visible = true;
                    unitCursor.setLocation(cursor.position());
                  }
                }
              } else if (key == KeyEvent.VK_ESCAPE) {
                    mode = Mode.MOVE;
                    path.visible = false;
                }
            } else if (mode == Mode.MENU) {
                if      (key == KeyEvent.VK_UP)     focusedActionPanel.goUp();
                else if (key == KeyEvent.VK_DOWN)   focusedActionPanel.goDown();
                else if (key == KeyEvent.VK_ENTER)  focusedActionPanel.perform();
                else if (key == KeyEvent.VK_ESCAPE) focusedActionPanel.setVisible (false);
            } else if (key == KeyEvent.VK_ESCAPE) {
                mode = Mode.MOVE;
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
        mouse.x = e.getX()  / MainFrame.UNIT;
        mouse.y = e.getY() / MainFrame.UNIT;
        listenMouse = true;
    }

    /**
     * Function called each loop turn
     */
    public void update () {
      isListening = cursor.move() | camera.move() |
                    (mode != Mode.MOVE && mode.canMove() && unitCursor.move());

      if (!isListening && mode == Mode.MOVE && listenMouse) {
          if (mouse.x <= moveRange) camera.setDirection(Direction.LEFT);
          else if (camera.width - mouse.x <= moveRange) camera.setDirection(Direction.RIGHT);
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

}

