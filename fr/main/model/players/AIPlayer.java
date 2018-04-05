package fr.main.model.players;

import fr.main.model.Universe;
import fr.main.model.units.AbstractUnit;
import fr.main.model.commanders.Commander;
import fr.main.model.buildings.OwnableBuilding;

import fr.main.view.interfaces.DayPanel;

/**
 * Represents an artificial intelligence player
 * This class's role in the decision process is empire AI (it determines the tactics to win and deals with the entire empire)
 * The game has no diplomacy and the only victory condition is taking the opponent's HQ so this class actually doesn't do much
 */
public class AIPlayer extends Player implements ArtificialIntelligence {

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
        try{ Thread.sleep(DayPanel.PANEL_TIME + 150); }
        catch(InterruptedException e){}

        // power is activated as soon as it can be
        if (commander.canActivate(true)) commander.activate(true);
        else if (commander.canActivate(false)) commander.activate(false);

        // units play
        unitControlAI.run();

        // creates the units
        economicAI.run();


        Universe.get().next();
    }
}