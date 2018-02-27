package fr.main.view.render.units;

import fr.main.model.Direction;
import fr.main.view.render.Renderer;

public interface UnitRenderer extends Renderer {

  public void moveOffset (Direction d);

}

