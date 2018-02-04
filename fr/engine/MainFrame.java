package engine;

import java.awt.Image;
import java.awt.Toolkit;
import java.lang.Thread.State;
import java.util.Map;

import javax.swing.JFrame;

public class MainFrame extends JFrame{
	
	public static final int WIDTH = gameUtil.Constants.MainFrameWidth+10;
	public static final int HEIGHT = gameUtil.Constants.MainFrameHeight+10;
	public static final int UNIT = gameUtil.Constants.MainFrameUnit;
	
	/**
	 * Here, frame works for sync, up to 12 will be reset 0.
	 */
	public int frame = 0;
	
	public static Gui gui;
	//public static LoadImages loadimage;
	public static Image imgCity;
	public static Image imgTerrain;
	public static Image imgUnits;
	public static Map map;
	
	public int fps;
	public int fpsCounter;
	
	public Controller controller;
	public static View view;
	
	
	public MainFrame() {
		super("Advance War");
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUndecorated(false);
		setResizable(false);
		setLocationRelativeTo(null);
		
		//add gui
		gui = new Gui();
		this.add(gui);
		gui.setFocusable(true);
		gui.requestFocusInWindow();
		
		controller = new Controller();
		view = new View();
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		this.imgTerrain = toolkit.getImage(getClass().getResource("/img/"+"Terrain"+".png"));
		this.imgCity = toolkit.getImage(getClass().getResource("/img/"+"Units"+".png"));
		this.imgUnits = toolkit.getImage(getClass().getResource("/img/"+"Units"+".png"));
		
		map = null;
		controller = new Controller();
		GameRunning(view);
	}
	
	
	public void GameRunning(View view) {
		
		boolean running = true;
		long last = 0;
		long lastCPSTime = 0;
		long lastCPSTime2 = 0;
		int logics = 0;
		logics++;
		
		while(running) {
			long delay = (System.nanoTime()-last)/1000000;
			delay++;
			last = System.nanoTime();
			/**
			if(System.currentTimeMillis() - lastCPSTime>1000) {
				lastCPSTime = System.currentTimeMillis();
				fpsCounter = fps;
				fps = 0;
				
			}
			**/
			
			if (System.currentTimeMillis() - lastCPSTime2 > 100) {
				lastCPSTime2 = System.currentTimeMillis();
				
				gui.repaint();
			}
			else logics++;
			
			
		}
	}
	

}
