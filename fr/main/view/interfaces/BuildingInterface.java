package fr.main.view.interfaces;

import java.util.HashMap;
import java.util.Set;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;

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
        x = MainFrame.width() - 200;
        y = 10;

        try {
            ArrayList<Class<? extends AbstractUnit>> list;
            Comparator<Class<? extends AbstractUnit>> comparator= Comparateur.instance;

            list = new ArrayList<Class<? extends AbstractUnit>>(Dock.getUnits());
            Collections.sort(list, comparator);
            for (Class<? extends AbstractUnit> c : list)
                new IndexClass(c);

            list = new ArrayList<Class<? extends AbstractUnit>>(Barrack.getUnits());
            Collections.sort(list, comparator);
            for (Class<? extends AbstractUnit> c : list)
                new IndexClass(c);

            list = new ArrayList<Class<? extends AbstractUnit>>(Airport.getUnits());
            Collections.sort(list, comparator);
            for (Class<? extends AbstractUnit> c : list)
                new IndexClass(c);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Class used to sort the list of units that can be created
     */
    static class Comparateur implements Comparator<Class<? extends AbstractUnit>>{
        public static final Comparateur instance = new Comparateur();

        public boolean equals(Object obj){
            return this.equals(obj);
        }

        public int hashCode(){
            return super.hashCode();
        }

        public int compare(Class<? extends AbstractUnit> o1, Class<? extends AbstractUnit> o2) {
            try{
                return (Integer)o1.getField("PRICE").get(null) - (Integer)o2.getField("PRICE").get(null);
            }catch(NoSuchFieldException | IllegalAccessException e){
                throw new RuntimeException();
            }
        }
    }

    @Override
    public void onOpen () {
        super.onOpen();
        building = (FactoryBuilding)controller.world.getBuilding(controller.cursor.position());
        Set<Class<? extends AbstractUnit>> units = building.getUnitList();
        for (Index i: actions.values())
            i.setActive(units.contains(((IndexClass)i).c));
    }
}
