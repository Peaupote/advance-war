package fr.main.model;

public class IAPlayer extends Player{

	private static int num = 1;

	public IAPlayer(){
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