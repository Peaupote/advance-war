package fr.main.view.interfaces;

import java.util.HashMap;
import java.util.Set;

import fr.main.model.buildings.*;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.land.LandUnit;
import fr.main.model.units.naval.NavalUnit;
import fr.main.model.units.air.AirUnit;
import fr.main.view.controllers.GameController;
import fr.main.view.MainFrame;

/**
 * Action panel to create unit after selecting a building
 */
public class BuildingInterface extends GameController.ControllerPanel {

  /**
   * Selected building
   */
  private FactoryBuilding building;

  private GameController controller;

  /**
   * Index for a single unit
   */
  class IndexClass extends Index {

    final Class<? extends AbstractUnit> c;

    public IndexClass (Class<? extends AbstractUnit> c)
        throws NoSuchFieldException, IllegalAccessException {
      super(c.getField("NAME").get(null) + ": " + c.getField("PRICE").get(null) + "$", () -> building.create(c));
      this.c = c;
    }

  }

  public BuildingInterface (GameController controller) {
    controller.super();
    this.controller = controller;
    x = MainFrame.WIDTH - 200;
    y = 10;

    try {
      for (Class<? extends AbstractUnit> c: Dock.UNIT_LIST.keySet())
        new IndexClass(c);

      for (Class<? extends AbstractUnit> c: Airport.UNIT_LIST.keySet())
        new IndexClass(c);

      for (Class<? extends AbstractUnit> c: Barrack.UNIT_LIST.keySet())
        new IndexClass(c);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onOpen () {
    super.onOpen();
    building = (FactoryBuilding)controller.world.getBuilding(controller.cursor.position());
    Set<Class<? extends AbstractUnit>> units = building.getUnitList();
    for (Index i: actions.values())
      if (units.contains(((IndexClass)i).c)) i.setActive(true);
      else i.setActive(false);
  }

}
