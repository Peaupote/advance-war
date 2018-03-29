package fr.main.model;

/**
 * Represents an artificial intelligence
 */
public class AIPlayer extends Player{

	private static int num = 1;

	public AIPlayer(){
		super("IA " + num);
		num ++;
	}

	public synchronized void turnBegins(){
		super.turnBegins();
		new Thread(this::play).start();
	}

	private void play(){
		System.out.println(name + " plays");
		try{ Thread.sleep(500); }
		catch(InterruptedException e){}
		System.out.println(name + " is done");
		Universe.get().next();
	}
}