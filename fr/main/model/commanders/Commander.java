package fr.main.model.commanders;

import java.io.Serializable;
import fr.main.model.Player;
import fr.main.model.Universe;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.weapons.PrimaryWeapon;

/**
 * Represents a commander.
 * A commander has at most two powers, he has a power bar that charges when he takes damages and he can improve or deteriorate any characteristic of any unit as a passive or active power.
 */
public abstract class Commander implements Serializable {

    public final PowerBar powerBar;

    /**
     * The powers of the commander
     */
    private final Power small, big;

    /**
     * The player who uses this commander
     */
    public final Player player;

    /**
     * The power bar of the commander, showing how much points he has to activate its powers
     */
    public class PowerBar implements Serializable {
        public final int maxValue;
        private int value;

        public PowerBar(int max){
            this.maxValue = max;
            this.value    = 0;
        }

        public int getValue(){
            return value;
        }
        public void addValue(int i){
            value = Math.min(maxValue, value + i);
        }
        public void removeValue(int i){
            value = Math.max(0, value - i);
        }
    }

    /**
     * A power of a commander
     */
    protected static class Power implements Serializable {
        /**
         * Set to true if and only if the power is activated this turn
         */
        private boolean activated;

        /**
         * The cost to activate power
         */
        public final int value;

        /**
         * The action done when activating the power
         */
        private final PowerAction powerAction;

        public Power(int value){
            this(value, p -> {});
        }

        public Power(int value, PowerAction pa){
            this.activated   = false;
            this.value       = value;
            this.powerAction = pa;
        }

        /**
         * @param Player the player who activates the power
         */
        public boolean activate(Player p){
            if (activated) return false;

            activated = true;
            powerAction.activate(p);
            return true;
        }
    }

    public Commander(Player player, Power small, Power big){
        player.setCommander(this);
        this.small    = small;
        this.big      = big;
        this.player   = player;
        int bar       = Math.max(small == null ? 0 : small.value, big == null ? 0 : big.value);
        this.powerBar = bar == 0 ? null : new PowerBar(bar);
    }

    /**
     * @param boolean true to activate the big power and false for the small one
     * @return true if and only if the matching power was activated
     */
    public boolean activate(boolean bigPower){
        if (canActivate(bigPower)){
            powerBar.removeValue(getPowerCost(bigPower));
            return bigPower ? big.activate(player) : small.activate(player);
        }
        return false;
    }

    /**
     * @param boolean true to return if the big power is activated and false for the small one
     * @return true if and only if the matching power is activated
     */
    public boolean activated(boolean bigPower){
        return bigPower ? big.activated : small.activated;
    }

    /**
     * @param boolean true to return if the big power can be activated and false for the small one
     * @return true if and only if the matching power can be activated
     */
    public boolean canActivate(boolean bigPower){
        return !activated(bigPower) && powerBar.value >= (bigPower ? big.value : small.value);
    }

    /**
     * @param boolean true to return the big power's cost and false for the small one
     * @return the value of the matching power
     */
    public final int getPowerCost (boolean takeBig) {
        return takeBig ? (big == null ? 0 : big.value) : (small == null ? 0 : small.value);
    }

    /**
     * at the beginning of the turn the power are inactivated
     */
    public void turnBegins(){
        small.activated = false;
        big.activated   = false;
    }

    /**
     * @param AbstractUnit the unit we want to know the attack value
     * @return the attack value (as a percentage) of the commander for the given unit
     */
    public int getAttackValue(AbstractUnit u){
        return 100;
    }

    /**
     * @param AbstractUnit the unit we want to know the defense value
     * @return the defense value (as a percentage) of the commander for the given unit
     */
    public int getDefenseValue(AbstractUnit u){
        return 100;
    }

    /**
     * @param AbstractUnit the unit we want to know the vision
     * @return the vision of the given unit
     */
    public int getVision(AbstractUnit u){
        return u.getBaseVision() + Universe.get().getTerrain(u.getX(), u.getY()).getBonusVision(u);
    }

    /**
     * @param AbstractUnit the unit we want to know the minimum range
     * @param PrimaryWeapon the weapon of the unit we want to know the minimum range
     * @return the minimum range of the unit
     */
    public int getMinimumRange(AbstractUnit u, PrimaryWeapon p){
        return p.getBaseMinimumRange();
    }

    /**
     * @param AbstractUnit the unit we want to know the maximum range
     * @param PrimaryWeapon the weapon of the unit we want to know the maximum range
     * @return the maximum range of the unit
     */
    public int getMaximumRange(AbstractUnit u, PrimaryWeapon p){
        return p.isContactWeapon() ? 1 : p.getBaseMaximumRange() + Universe.get().getTerrain(u.getX(), u.getY()).getBonusRange(u, p);
    }
}
