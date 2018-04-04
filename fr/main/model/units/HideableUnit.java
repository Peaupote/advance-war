package fr.main.model.units;

/**
 * Interface to represent the units that can hide
 */
public interface HideableUnit extends AbstractUnit {

	/**
	 * @return true if and only if the unit is hidden
	 */
    boolean hidden();

    /**
     * @return true if the unit is now hidden and false if it is now revealed
     */
    boolean hide();
}
