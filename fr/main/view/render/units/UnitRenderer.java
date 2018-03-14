package fr.main.view.render.units;

import java.awt.Point;

import fr.main.model.units.AbstractUnit;
import fr.main.model.Direction;
import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;

public interface UnitRenderer extends Renderer, AbstractUnit {

  Point getOffset();
  void setOrientation(Direction d);

  default boolean moveOffset (Direction d) {
    Point offset = getOffset();
    d.move(offset);
    if (Math.abs(offset.x) == MainFrame.UNIT || Math.abs(offset.y) == MainFrame.UNIT) {
      offset.x = 0;
      offset.y = 0;
      return move(d);
    }

    return true;
  }

  default String getDir () {
    return "./assets/" + (getPlayer().id == 1 ? "red" : "blue") + "/";
  }

  default void setState (String state) {}
}

