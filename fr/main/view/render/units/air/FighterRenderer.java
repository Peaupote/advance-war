package fr.main.view.render.units.air;

import java.io.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.util.LinkedList;

import fr.main.model.terrains.Terrain;
import fr.main.model.units.Path;
import fr.main.model.Direction;
import fr.main.view.render.sprites.*;
import fr.main.view.render.animations.*;
import fr.main.view.MainFrame;
import fr.main.view.render.units.UnitRenderer;
import fr.main.model.units.air.Fighter;

public class FighterRenderer extends Fighter implements UnitRenderer {

  private Point offset;
  private Direction direction;
  private String state;

  private transient Animation anim; 

  public FighterRenderer (Point location) {
    super (null, location);

    offset = new Point(0, 0);
  }

  public void draw (Graphics g, int x, int y) {
    anim.draw(g, x + offset.x, y + offset.y);
  }

  @Override
  public void update () {
    direction = Direction.RIGHT;

    LinkedList<ScaleRect> areas = new LinkedList<>();
    areas.add(new ScaleRect(90, 6, 15, 16, 2));
    areas.add(new ScaleRect(111, 7, 15, 16, 2));
    AnimationState idleRight = new AnimationState(new SpriteList("./assets/red/air.png", areas), 50);

    areas = new LinkedList<>();
    areas.add(new ScaleRect(90, 6, 15, 16, 2, ScaleRect.Flip.VERTICALY));
    areas.add(new ScaleRect(111, 7, 15, 16, 2, ScaleRect.Flip.VERTICALY));
    AnimationState idleLeft = new AnimationState(new SpriteList("./assets/red/air.png", areas), 50);

    areas = new LinkedList<>();
    areas.add(new ScaleRect(86, 25, 19, 19, 2));
    areas.add(new ScaleRect(113, 25, 19, 20, 2));
    AnimationState moveRight = new AnimationState(new SpriteList("./assets/red/air.png", areas), 15);

    areas = new LinkedList<>();
    areas.add(new ScaleRect(86, 25, 19, 19, 2, ScaleRect.Flip.VERTICALY));
    areas.add(new ScaleRect(113, 25, 19, 20, 2, ScaleRect.Flip.VERTICALY));
    AnimationState moveLeft = new AnimationState(new SpriteList("./assets/red/air.png", areas), 15);

    areas = new LinkedList<>();
    areas.add(new ScaleRect(90, 50, 15, 20, 2, ScaleRect.Flip.HORIZONTALLY));
    areas.add(new ScaleRect(90, 52, 15, 20, 2, ScaleRect.Flip.HORIZONTALLY));
    AnimationState idleTop = new AnimationState(new SpriteList("./assets/red/air.png", areas), 50);

    areas = new LinkedList<>();
    areas.add(new ScaleRect(90, 50, 15, 20, 2));
    areas.add(new ScaleRect(90, 52, 15, 20, 2));
    AnimationState idleBottom = new AnimationState(new SpriteList("./assets/red/air.png", areas), 50);

    anim = new Animation();

    anim.put("idle-RIGHT", idleRight);
    anim.put("idle-LEFT", idleLeft);
    anim.put("idle-TOP", idleTop);
    anim.put("idle-BOTTOM", idleBottom);

    anim.put("move-RIGHT", moveRight);
    anim.put("move-LEFT", moveLeft);
    anim.setState("idle-RIGHT");
  }

  public void setState (String state) {
    this.state = state;
    updateAnim();
  }

  private void updateAnim () {
    anim.setState(state + "-" + direction.toString());
  }

  @Override
  public String getFilename() {
    return getDir() + "fighter.png";
  }

  public void setImage (Image image) {
  }

  @Override
  public Point getOffset () {
    return offset;
  }

  public void setOrientation (Direction d) {
    this.direction = d;
    updateAnim();
  }

}
