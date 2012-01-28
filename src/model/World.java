package model;
import java.sql.Time;

/**
 * The World class basically holds stores. It also keeps track of time
 * @author Uri Gorelik
 */
public class World {
	
	private Store store; 
	public int time;
	public int oldTime;
	
	private float money;
	
	// TODO: World needs a money variable
	
	
	/**
	 * Constructor
	 */
	public World() {
		store = new Store();
		time = 0;
		oldTime = 0;
	}
	
	/**
	 * Constructor
	 * @param time
	 * @param money
	 * @param store
	 */
	public World(long time, float money, Store store) {
		// TODO: do some calculations to adjust the stock
		// ... adjust money
		// ... adjust stock
		this.money += money;
		this.store = store;
	}
	
	/**
	 * Update
	 */
	public void update(int mouse) {
		oldTime = time;
//		time = (int)(System.currentTimeMillis() / 1000L);
		store.update(mouse);
		money = store.getMoney();
	}

	public float getMoney() {
		return money;
	}
	
	public Store getStore() {
		return store;
	}
}
