package controller;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import model.OS;

import org.xml.sax.SAXException;

import utility.SaveLoad;
import view.Draw;
import view.resources.Images;



/**
 * JPanel Stuff
 * 
 */
public class GameFrame extends JFrame {

    
	private GamePanel gamePanel;
	
	public static void main(String args[]) {
		new GameFrame();
	}

	/**
	 * Constructor
	 */
	public GameFrame() {

		int extraWidth = 5;
		int extraHeight = 29;
		
		String temp = System.getProperty("os.name");
		
		// Get's the OS
		if (System.getProperty("os.name").split(" ")[0].toLowerCase().contains("mac")) {
		    Game.OPERATING_SYSTEM = OS.MAC;
		    extraWidth = 0;
			extraHeight = 23;
		} else {
		    Game.OPERATING_SYSTEM = OS.WINDOWS;
		}
		
		// Set the Icon
		setIconImage(Toolkit.getDefaultToolkit().getImage(Images.class.getResource("unicorn.png")));
		
		setTitle("Unicorn Shoppe");
		gamePanel = new GamePanel();
		getContentPane().add(gamePanel);
    	setVisible(true);
    	setSize(Draw.IMG_WIDTH * Draw.IMG_SCALE + extraWidth, (Draw.IMG_HEIGHT + Draw.EXTRA_BUTTON_HEIGHT) * Draw.IMG_SCALE + extraHeight);
    	setResizable(false);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    	setLocationRelativeTo(null);
    	
    	// Causes the game to make save files
    	addWindowListener(new WindowAdapter() {
    		public void windowClosing(WindowEvent e) {
    			try {
					SaveLoad.save(gamePanel.getWorld());
				} catch (ParserConfigurationException e1) {
				} catch (TransformerException e1) {
				} catch (SAXException e1) {
				} catch (IOException e1) {
				}
    		}
		});

	}

	

}
