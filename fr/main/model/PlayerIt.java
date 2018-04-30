package fr.main.model;

import java.util.Iterator;
import java.lang.Iterable;

import fr.main.model.players.Player;

/**
 * Class managing the cycle of players:
 */
public class PlayerIt implements Iterable<Player> {


    /**
     * A kind of Players' Iterator
     * Represent an infinite loop of players
     */
    public static class Cycle implements Iterator<Player> {

        /**
         * List of all players
         */
        private Player[] ps;

        /**
         * Number of the player whose turn it is
         */
        private int index;

        public Cycle (Player[] ps) {
            index = ps.length - 1;
            this.ps = ps;
        }

        /**
         * Can an other player play
         * @return true while there is at least a single player
         */
        public boolean hasNext() {
            int n = 0;
            for (Player p : ps){
                if (!p.hasLost()) n ++;
                if (n > 1) return true;
            }
            return false;
        }

        /**
         * @return Next player to play
         */
        public Player next () {
            index = (index + 1) % ps.length;
            return ps[index];
        }

        public boolean isFirst(Player p){
            for (Player pl : ps){
                if (p == pl) return true;
                if (!pl.hasLost()) return false;
            }
            return false;
        }

        public void setCurrent(Player current){
            index = 0;
            for (Player p : ps){
                if (p == current) break;
                index ++;
            }

            if (index == ps.length) System.out.println("error unknown player");
        }
    }

    /**
     * Kind of Iterator<Player>
     */
    private Cycle players;

    public PlayerIt(Player[] ps) {
        players = new Cycle(ps);
    }

    public Iterator<Player> iterator() {
        return players;
    }

    public Cycle cycle(){
        return players;
    }

}
