package fr.main.model.units.weapons;

import java.util.Map;

import fr.main.model.units.AbstractUnit;

/*
* Secondary weapons have infinite ammunition and are contact weapons.
*/
public class SecondaryWeapon extends Weapon{

    public SecondaryWeapon(String name, Map<Class<? extends AbstractUnit>,Integer> damages){
        super(name,damages);
    }

    public void shoot(){}

    public boolean isInRange(int actualX, int actualY, int targetX, int targetY){
        return Math.abs(actualX-targetX)+Math.abs(actualY-targetY)==1;
    }

    public void canTarget(boolean[][] map, int x, int y){
        for (int i=-1;i<2;i++){
            for (int j=-1;j<2;j++){
                if (x+i>=0 && x+i<map.length && y+j>=0 && y+j<map[0].length && 2+i+j%2==1)
                    map[y+j][x+i]=true;
            }
        }
    }
}