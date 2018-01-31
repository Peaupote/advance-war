package fr.main.model;

import java.util.Iterator;
import java.lang.Iterable;

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
            return ps.length > 0;
        }

        /**
         * @return Next player to play
         */
        public Player next () {
            index = (index + 1) % ps.length;
            return ps[index];
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
}
