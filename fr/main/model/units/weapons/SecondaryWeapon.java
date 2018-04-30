package fr.main.model.units.weapons;

import java.util.Map;

import fr.main.model.units.AbstractUnit;
import fr.main.model.Direction;

/**
 * Secondary weapons have infinite ammunition and are contact weapons.
 */
public class SecondaryWeapon extends Weapon{

    /**
	 * Add SecondaryWeapon UID
	 */
	private static final long serialVersionUID = 293126153446291195L;

	public SecondaryWeapon(String name, Map<Class<? extends AbstractUnit>,Integer> damages){
        super(name,damages,true);
    }

    /**
     * Nothing is done when shooting
     */
    @Override
    public void shoot(){}

    @Override
    public boolean isInRange(AbstractUnit shooter, AbstractUnit target){
        return Math.abs(shooter.getX()-target.getX())+Math.abs(shooter.getY()-target.getY())==1;
    }

    @Override
    public void renderTarget(boolean[][] map, AbstractUnit u, int x, int y){
        if (u.getMoveQuantity() != 0)
            for (Direction d : Direction.cardinalDirections())
                if (y + d.x >= 0 && y + d.x < map.length && x + d.y >= 0 && x + d.y < map.length)
                    // if the point is valid, we can attack
                    map[y + d.x][x + d.y] = true;
    }

    @Override
    public boolean canAttack(AbstractUnit shooter, AbstractUnit target){
        return target != null && shooter.getPlayer() != target.getPlayer() && shooter.isEnabled() && canShoot(target) && isInRange(shooter, target);
    }

}