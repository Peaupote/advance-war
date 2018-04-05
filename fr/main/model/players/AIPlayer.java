package fr.main.model.players;

import fr.main.model.Universe;
import fr.main.model.units.AbstractUnit;
import fr.main.model.commanders.Commander;
import fr.main.model.buildings.OwnableBuilding;

/**
 * Represents an artificial intelligence player
 * This class's role in the decision process is empire AI (it determines the tactics to win and deals with the entire empire)
 * The game has no diplomacy and the only victory condition is taking the opponent's HQ so this class actually doesn't do much
 */
public class AIPlayer extends Player implements ArtificialIntelligence {

    /**
     * Minimum time the ai takes to play its turn in milliseconds
     */
    public static final int AI_TIME_PLAY_MINIMUM = 750;

    private static int num = 1;

    public final UnitControlAI unitControlAI;
    public final EconomicAI    economicAI;

    public AIPlayer(){
        super("IA " + num);
        num ++;

        this.economicAI    = new EconomicAI(this);
        this.unitControlAI = new UnitControlAI(this);
    }

    public synchronized void turnBegins(){
        super.turnBegins();
        new Thread(this::run).start();
    }

    @Override
    public void add(AbstractUnit u){
        super.add(u);
        unitControlAI.add(u);
    }

    @Override
    public void remove(AbstractUnit u){
        super.remove(u);
        unitControlAI.remove(u);
    }

    @Override
    public void addBuilding(OwnableBuilding b){
        super.addBuilding(b);
        economicAI.add(b);
    }
    @Override
    public void removeBuilding(OwnableBuilding b){
        super.removeBuilding(b);
        economicAI.remove(b);
    }

    /**
     * What the empire AI does when it plays
     */
    public void run(){
        System.out.println(name + " plays");
        long time = System.currentTimeMillis();

        // power is activated as soon as it can be
        if (commander.canActivate(true)) commander.activate(true);
        else if (commander.canActivate(false)) commander.activate(false);

        // units play
        unitControlAI.run();

        // creates the units
        economicAI.run();

        // the AI takes at least a certain time (AI_TIME_PLAY_MINIMUM) to play
        if (System.currentTimeMillis() - time < AI_TIME_PLAY_MINIMUM)
            try{ Thread.sleep(AI_TIME_PLAY_MINIMUM - System.currentTimeMillis() + time); }
            catch(InterruptedException e){}

        Universe.get().next();
    }
}