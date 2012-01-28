package controller;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.TimerTask;

import model.OS;
import model.World;
import utility.SaveLoad;
import view.Draw;

// TODO game no longer needs to be a timer task
public class Game extends TimerTask{
	
    
	private World world;
	private Draw graphics;
	private int mousePress;
	private MouseAdapter mouseAdapter;
	public static OS OPERATING_SYSTEM = OS.NA;
	public static boolean DEBUG_MODE = true; 
	
	
	public Game() {
	    
	    // Load the game
	    if (new File("stores.xml").exists() && !DEBUG_MODE) {
	        System.out.println("Game:: Save file found. Loading existing world...");
	        world = SaveLoad.load();
	    } else {
	        System.out.println("Game:: No save file found. Creating new world...");
	        world = new World();
	    }
	    
	    // TODO This should never happen so remove me when everything is working correctly
	    if (world == null) {
	        System.out.println("Game:: Something is wrong with the loader, world is null");
	        world = new World();
	    }
	    
	    // Init the mouse press variable
	    mousePress = -1;
	    
	    // Initialize the main drawing object
		graphics = new Draw();
		
		// Init the mouse adapter
		mouseAdapter = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				buttonEvent(e);
				
				// If it's within the bounds
				if (e.getY() > Draw.IMG_HEIGHT * Draw.IMG_SCALE)
				    mousePress = (e.getX() / Draw.BUTTON_WIDTH);
			}
			
			// Reset the button
			public void mouseReleased(MouseEvent e) {
			    mousePress = -1;
			}
		
		};
		
		
		// Init the timers
	}
	
	public void update() {
		world.update(mousePress);
	}
	
	public void draw(Graphics g){
		graphics.draw(world, g, mousePress);
	}

	public void run() {
		update();
//		System.out.println("Controller: updating...");
	}
	
	/**
	 * Handles mouse events
	 * @param e
	 */
	private void buttonEvent(MouseEvent e) {
		
		if (e.getY() > Draw.IMG_HEIGHT * Draw.IMG_SCALE) {
		    System.out.println("Button[" + ((e.getX() / Draw.BUTTON_WIDTH)) + "] was pressed.");
		}
	}
	
	
	public MouseAdapter getMouseAdapter() {
		return mouseAdapter;
	}

	public World getWorld() {
		return world;
	}
}
