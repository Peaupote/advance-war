package fr.main.view.interfaces;

import java.util.HashMap;
import java.util.Set;

import fr.main.model.buildings.*;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.land.LandUnit;
import fr.main.model.units.naval.NavalUnit;
import fr.main.model.units.air.AirUnit;
import fr.main.view.Controller;
import fr.main.view.MainFrame;

public class BuildingInterface extends Controller.ControllerPanel {

  private FactoryBuilding building;
  private Controller controller;

  class IndexClass extends Index {

    final Class c;

    public IndexClass (Class<? extends AbstractUnit> c)
        throws NoSuchFieldException, IllegalAccessException {
      super(c.getField("NAME").get(null) + ": " + c.getField("PRICE").get(null) + "$", () -> building.create(c));
      this.c = c;
    }

  }

  public BuildingInterface (Controller controller) {
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
    Set units = building.getUnitList();
    for (Index i: actions.values())
      if (units.contains(((IndexClass)i).c)) i.setActive(true);
      else i.setActive(false);
  }

}
