package fr.main.model.buildings;

import fr.main.model.Universe;
import fr.main.model.Direction;
import fr.main.model.units.AbstractUnit;
import fr.main.model.terrains.Terrain;

import java.awt.Point;

public class MissileLauncher extends Building {

    private boolean fired;

    public MissileLauncher(Point p) {
        super(p, 2, "Missile Launcher");
        this.fired = false;
    }

    public boolean isFired() {
        return fired;
    }

    public boolean canFire(int x, int y) { // can fire everywhere
        return !fired;
    }

    public void fire(int x, int y) {
        if(!canFire(x, y)) return;
        fired = true;
        Universe u = Universe.get();
        AbstractUnit unit = u.getUnit(x, y);
        if (unit != null) // center of the target
            unit.removeLife(30);
        for (int i = 1; i < 4; i++) // cardinal directions from the target
            for (Direction d : Direction.cardinalDirections()){
                unit = u.getUnit(x + i * d.x, y + i * d.y);
                if (unit != null)
                    unit.removeLife(30);
            }

        int[][] t = {
            {1,1},{1,-1},{-1,-1},{-1,1}
        };
        for (int i = 2 ; i < 4 ; i++) // other tiles
            for (int j = 1 ; j < i ; j ++)
                for (int[] tab : t){
                    unit = u.getUnit(x + j * tab[0], y + (i - j) * tab[1]);
                    if (unit != null)
                        unit.removeLife(30);
                }
    }
}
