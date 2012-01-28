package view;

import java.util.Timer;
import java.util.TimerTask;

import model.World;

public class TextView extends TimerTask {

	private Timer timer;
	private World world;

	public static void main(String[] args) {
		new TextView();
	}

	/**
	 * Constructor
	 */
	public TextView() {
		world = new World();
		timer = new Timer();

		timer.scheduleAtFixedRate(this, 0, 3000);

	}

	public void run() {
		world.update(-1); // TODO: Wtf is going on here
		System.out.println("Money $" + ((int) (world.getStore().getMoney() * 100)) / 100.0f);
		System.out.println("Time: " + (world.time - world.oldTime));

	}
}
