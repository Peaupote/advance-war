package fr.main.view.interfaces;

import java.util.HashMap;
import java.util.Set;

import fr.main.model.buildings.*;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.land.LandUnit;
import fr.main.model.units.naval.NavalUnit;
import fr.main.model.units.air.AirUnit;
import fr.main.view.Controller;

public class BuildingInterface extends Controller.ControllerPanel {

  private FactoryBuilding building;
  private Controller controller;

  class IndexClass extends Index {

    final Class c;

    public IndexClass (Class<? extends AbstractUnit> c) {
      super(c.getTypeName(), () -> building.create(c));
      this.c = c;
    }

  }

  public BuildingInterface (Controller controller) {
    controller.super();
    this.controller = controller;

    for (Class<? extends NavalUnit> c: Dock.UNIT_LIST.keySet())
      new IndexClass(c);

    for (Class<? extends AirUnit> c: Airport.UNIT_LIST.keySet())
      new IndexClass(c);

    for (Class<? extends LandUnit> c: Barrack.UNIT_LIST.keySet())
      new IndexClass(c);
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
