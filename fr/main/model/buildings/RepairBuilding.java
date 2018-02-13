package fr.main.model.buildings;

import fr.main.model.units.AbstractUnit;

/*
* Represents buildings that can repair and replenish specific units
*/
public interface RepairBuilding<T extends AbstractUnit> extends AbstractBuilding{
    // example : the airport is the only building to repair air units ; it doesn't repair land unit
    public boolean canRepair(AbstractUnit u);
    public void repair (T u);
}