package controller;


import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Timer;

import javax.swing.JPanel;

import model.World;


public class GamePanel extends JPanel implements ActionListener{

	private BufferedImage imageBackground;

	private Game controller;
	
	private javax.swing.Timer timer;	// 30 FPS
	private Timer controllerTimer;		// Timer for the controller, update every three seconds

	public GamePanel() {
	    
	    // Check if there's a file to load
	    
	    
		timer = new javax.swing.Timer(33, this);		// 30 FPS
		controllerTimer = new java.util.Timer();		
		controller = new Game();
		controllerTimer.schedule(controller, 0, 10);	// 0.5 secs
		timer.start();									// Start the timer
		
		// The mouse listener
		addMouseListener(controller.getMouseAdapter());
	}


	public void paint(Graphics g) {
		super.paint(g);
		controller.draw(g);
	}


	public void actionPerformed(ActionEvent e) {
		update();

	}


	public void update() {
		repaint();					// Calls paint
		// Controller's update gets called every three seconds
//		System.out.println("Redrawing");
	}
	
	
	public World getWorld() {
		return controller.getWorld();
	}



}
