package fr.main.model.buildings;

import java.awt.Point;

import fr.main.model.Direction;
import fr.main.model.Universe;
import fr.main.model.units.AbstractUnit;

/*
* Represents a missile launcher
*/
public class MissileLauncher extends Building {

    /**
	 * Add MissileLauncher UID
	 */
	private static final long serialVersionUID = 2599496054309721599L;
	/**
     * set to true if the missile was fired and false otherwise
     */
    private boolean fired;

    public MissileLauncher(Point p) {
        super(p, 2, "Missile");
        this.fired = false;
    }

    public boolean isFired() {
        return fired;
    }

    /**
     * @return true if the missile can be fired at the position (x,y)
     */
    public boolean canFire(int x, int y) {
        return !fired && Universe.get().isValidPosition(x,y);
    }

    /**
     * fire the missile at the position (x,y)
     */
    public void fire(int x, int y) {
        if(!canFire(x, y)) return;
        fired = true;
        Universe u = Universe.get();
        AbstractUnit unit = u.getUnit(getX(), getY());
        if (unit != null)
            unit.dies();
        unit = u.getUnit(x, y);
        if (unit != null) // center of the target
            unit.removeLife(40);
        for (int i = 1; i < 4; i++) // cardinal directions from the target
            for (Direction d : Direction.cardinalDirections()){
                unit = u.getUnit(x + i * d.x, y + i * d.y);
                if (unit != null)
                    unit.removeLife(40);
            }

        int[][] t = Direction.getNonCardinalDirections();
        for (int i = 2 ; i < 4 ; i++) // other tiles
            for (int j = 1 ; j < i ; j ++)
                for (int[] tab : t){
                    unit = u.getUnit(x + j * tab[0], y + (i - j) * tab[1]);
                    if (unit != null)
                        unit.removeLife(40);
                }
    }
}
