package fr.main.model.buildings;

import java.awt.Point;

import fr.main.model.Player;
import fr.main.model.units.Unit;
import fr.main.model.terrains.Terrain;

public abstract class OwnableBuilding extends Building {

    protected int life;
    public final int maximumLife, income;
    protected Player owner;

    public OwnableBuilding(Player p, Point pos, int defense, int maximumLife, int income, String name){
        super (pos, defense, name);
        this.maximumLife = maximumLife;
        this.life        = maximumLife;
        this.income      = income;
        this.owner       = p;
        if (p != null)
            p.addBuilding(this);
    }

    public void renderVision(boolean[][] fogwar){
        fogwar[location.y][location.x] = true;
    }

    public String toString(){
        return name;
    }

    public Player getOwner(){
        return owner;
    }

    public void setOwner(Player p){
        if (owner != null)
            owner.removeBuilding(this);
        owner = p;
        if (owner != null)
            owner.addBuilding(this);
    }

    public boolean isNeutral(){
        return owner == null;
    }

    public int getLife(){
        return life;
    }

    public int getIncome(){
        return income;
    }

    /*
    * @return true if and only if the building was captured
    */
    public boolean removeLife(Player player, int life){
        if (this.life<=life){
            setOwner(player);
            goBackToMaximumLife();
            return true;
        }else{
            this.life-=life;
            return false;
        }
    }

    public boolean removeLife(Unit u){
        return removeLife(u.getPlayer(),u.getLife());
    }

    public void goBackToMaximumLife(){
        life = maximumLife;
    }

}
