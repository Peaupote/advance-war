package fr.main.view.interfaces;

import java.awt.*;

import fr.main.view.MainFrame;
import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.model.commanders.Commander;
import fr.main.view.Position;
import fr.main.view.render.commanders.CommanderRenderer;

/**
 * Panel showing current user informations
 */
public class PlayerPanel extends InterfaceUI {

  private static final Color BACKGROUNDCOLOR = new Color(0,0,0,230);
  private static final Color FOREGROUNDCOLOR = Color.white;
  private static final int WIDTH = 200, HEIGHT = 100, MARGIN = 10,
          HALFW = MainFrame.WIDTH / (2 * MainFrame.UNIT),
          HALFH = MainFrame.HEIGHT / (2 * MainFrame.UNIT);

  protected final Position.Cursor cursor;
  protected final Position.Camera camera;
  protected final Universe world;

  public PlayerPanel (Position.Cursor cursor, Position.Camera camera) {
    this.cursor = cursor;
    this.camera = camera;
    this.world = Universe.get();
  }

  @Override
  protected void draw (Graphics g) {
    int x = cursor.getX() - camera.getX() >= HALFW && cursor.getY() - camera.getY() <= HALFH ? MARGIN : MainFrame.WIDTH - WIDTH - MARGIN,
        y = MARGIN;
    g.setColor (BACKGROUNDCOLOR);
    g.fillRect (x, y, WIDTH, HEIGHT);

    g.setColor (FOREGROUNDCOLOR);
    Player p = world.getCurrentPlayer();
    Commander c = p.getCommander();

    // TODO: don't re-evaluate all values each frames
    g.drawString (p.name, x + 20, y + 20);
    g.drawString (p.getFunds() + "$", x + 20, y + 40);
    
    g.setColor(Color.white);
    g.fillRect(x, y + HEIGHT - 20, WIDTH, 20);
    g.setColor(Color.green);
    g.fillRect(x, y + HEIGHT - 20, c.powerBar.getValue() * WIDTH / c.powerBar.maxValue, 20);
    g.setColor(Color.black);

    if (c.getSmallCost() <= c.powerBar.getValue()) g.setColor(Color.blue);
    int r = x + c.getSmallCost() * WIDTH / c.powerBar.maxValue;
    g.drawLine(r, y + HEIGHT - 20, r, y + HEIGHT);

    if (c.getBigCost() > c.powerBar.getValue()) g.setColor(Color.black);
    r = x + c.getBigCost() * WIDTH / c.powerBar.maxValue;
    g.drawLine(r, y + HEIGHT - 20, r, y + HEIGHT);

    CommanderRenderer.getRender(p.getCommander().toString())
                     .draw(g, x + WIDTH - 70, y + 35);
  }

}
