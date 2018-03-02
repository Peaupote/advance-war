package fr.main.model.units.weapons;

import java.util.Map;

import fr.main.model.units.AbstractUnit;
import fr.main.model.Direction;

/*
* Secondary weapons have infinite ammunition and are contact weapons.
*/
public class SecondaryWeapon extends Weapon{

    public SecondaryWeapon(String name, Map<Class<? extends AbstractUnit>,Integer> damages){
        super(name,damages,true);
    }

    public void shoot(){}

    public boolean isInRange(int actualX, int actualY, int targetX, int targetY){
        return Math.abs(actualX-targetX)+Math.abs(actualY-targetY)==1;
    }

    public void renderTarget(boolean[][] map, int x, int y, boolean enabled, boolean fullMove){
        if (enabled)
            for (Direction d : Direction.cardinalDirections())
                if (y+d.x>=0 && y+d.x<map.length && x+d.y>=0 && x+d.y<map.length)
                    map[y+d.x][x+d.y]=true;
    }

    public boolean canAttack(AbstractUnit shooter, AbstractUnit target){
        return shooter.isEnabled() && canShoot(target) && isInRange(shooter.getX(),shooter.getY(),target.getX(),target.getY());
    }

}