package fr.main.model.units;

public interface HideableUnit extends AbstractUnit {

    boolean hidden();

    /*
    * @return true if the unit is now hidden and false if it is now revealed
    */
    boolean hide();
}
