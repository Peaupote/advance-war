package fr.main.view.interfaces;

import java.awt.*;

import fr.main.view.MainFrame;
import fr.main.model.Universe;
import fr.main.model.players.Player;
import fr.main.model.commanders.Commander;
import fr.main.view.Position;
import fr.main.view.render.commanders.CommanderRenderer;

/**
 * Panel showing current user informations
 */
public class PlayerPanel extends InterfaceUI {

  private static final Color BACKGROUNDCOLOR = new Color(255,146,0,190);
  private static final Color FOREGROUNDCOLOR = Color.white;
  private static final int WIDTH = 150, HEIGHT = 81, MARGIN = 10;

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
    int halfw = MainFrame.width() / (2 * MainFrame.UNIT),
        halfh = MainFrame.height() / (2 * MainFrame.UNIT),
        x = cursor.getX() - camera.getX() >= halfw && cursor.getY() - camera.getY() <= halfh ? MARGIN : MainFrame.width() - WIDTH - MARGIN,
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

    if (c.getPowerCost(false) <= c.powerBar.getValue()) g.setColor(Color.blue);
    int r = x + c.getPowerCost(false) * WIDTH / c.powerBar.maxValue;
    g.drawLine(r, y + HEIGHT - 20, r, y + HEIGHT);

    if (c.getPowerCost(true) > c.powerBar.getValue()) g.setColor(Color.black);
    r = x + c.getPowerCost(true) * WIDTH / c.powerBar.maxValue;
    g.drawLine(r, y + HEIGHT - 20, r, y + HEIGHT);

    g.setColor(p.color);
    g.fillRect(x + WIDTH - 60, y, 60, 60);
    CommanderRenderer.getRender(p.getCommander().toString())
                     .draw(g, x + WIDTH - 60, y);
  }

}
