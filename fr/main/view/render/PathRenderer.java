package fr.main.view.render;

import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import fr.main.model.units.Path;
import fr.main.model.Direction;
import fr.main.view.MainFrame;
import fr.main.view.Position;

public class PathRenderer extends Path {

  private Position.Camera camera;
  public boolean visible;
  private Image[] images = new Image[10];

  public PathRenderer (Position.Camera camera) {
    super();
    this.camera = camera;
    visible = false;

    try {
      images[0] = ImageIO.read(new File("./assets/arrows/arrow-bottom.png"));
      images[1] = ImageIO.read(new File("./assets/arrows/arrow-left.png"));
      images[2] = ImageIO.read(new File("./assets/arrows/arrow-right.png"));
      images[3] = ImageIO.read(new File("./assets/arrows/arrow-top.png"));
      images[4] = ImageIO.read(new File("./assets/arrows/left-bottom.png"));
      images[5] = ImageIO.read(new File("./assets/arrows/left-top.png"));
      images[6] = ImageIO.read(new File("./assets/arrows/left-right.png"));
      images[7] = ImageIO.read(new File("./assets/arrows/right-bottom.png"));
      images[8] = ImageIO.read(new File("./assets/arrows/right-top.png"));
      images[9] = ImageIO.read(new File("./assets/arrows/top-bottom.png"));
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  public void draw (Graphics g) {
    g.setColor(Color.red);
    Point point = new Point(unit.getX(), unit.getY());

    int size = size();
    if (size == 0) return;
    if (size > 1) {
      Direction next = get(1);
      Image image = null;
      for (int i = 0; i < size - 1; i++) {
        Direction d = get(i);
        d.move(point);

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
                      (point.x - camera.getX()) * MainFrame.UNIT,
                      (point.y - camera.getY()) * MainFrame.UNIT,
                      MainFrame.UNIT, MainFrame.UNIT, null);

        next = d;
      }
    }

    Image arrow = null;
    Direction d = getLast();
    d.move(point);
    if (d == Direction.LEFT) arrow = images[1];
    else if (d == Direction.RIGHT) arrow = images[2];
    else if (d == Direction.TOP) arrow = images[3];
    else if (d == Direction.BOTTOM) arrow = images[0];

    if (arrow != null)
      g.drawImage(arrow,
                  (point.x - camera.getX()) * MainFrame.UNIT,
                  (point.y - camera.getY()) * MainFrame.UNIT,
                  MainFrame.UNIT, MainFrame.UNIT, null);
  }

}

