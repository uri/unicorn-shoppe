package model;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Stores
 * 
 * @author Uri Gorelik
 */
public class Store {

    private Timer timer;
    private float money;
    private Item[] items;
    private int currentlyStocking;

    /**
     * Used when there is no save file found
     */
    public Store() {
        currentlyStocking = -1;
        money = 1234.03f;
        items = new Item[ItemFactory.ITEM_COUNT];
        addItem(ItemFactory.createBrush());
        addItem(ItemFactory.createHorn());
        addItem(ItemFactory.createShampoo());
        addItem(ItemFactory.createPlace2());
        addItem(ItemFactory.createPlace1());
        
        // Make sure the items are linked to this store
        for (Item i : items) {
            i.setStore(this);
        }
        
        initTimer();

    }

    public Store(float money, long gametime, Item[] items) {
        // TODO: figure out currentlyStocking
        currentlyStocking = -1;
        this.money = money;
        this.items = items;
        
        
        // Make sure the items are linked to this store
        for (Item i : items) {
            i.setStore(this);
        }
        
        initTimer();

        // TODO Do some calcs based on time
    }

    private void initTimer() {
        timer = new Timer();
        for (Item i : getItems()) {
            timer.schedule((TimerTask) i, 0l, (long) ItemFactory.TIMER[i.getType()]);
        }
    }


    /**
     * 
     * @param mouse - which button was pressed, 0-4
     */
    public void update(int mouse) {
        // Pass the mouse
        if (mouse >= 0) {
            
            // If we are NOT stocking something then we can do stuff with the mouse
            if (!isStocking()) {

                // Ready to stock
                if (!items[mouse].isReady()) {
                    currentlyStocking = mouse;
                }
                
                items[mouse].update();  
            }
            items[mouse].update();  
            
            
            // TODO: Enable clicks when something is finished.
           
        }

    }

    /**
     * Adds an item TODO Maybe this doesn't need to add a whole item but rather
     * just update the quantity
     * 
     * @param item
     * @return
     */
    public boolean addItem(Item item) {

        if (item.getCost() > money) {
            return false;
        }
        item.setStore(this);
        money -= item.getCost();
        items[item.getType()] = item;

        return true;
    }

    /**************************************************************************************************************
     * Getters
     **************************************************************************************************************/
    public float getMoney() {
        return money;
    }

    public Item[] getItems() {
        return items;
    }

    public boolean isClosed() {
        int sum = 0;
        for (Item i : items) {
            sum += i.getAmount();
        }
        return sum == 0;
    }

    public void addMoney(float money) {
        this.money += money;
    }

    public boolean isStocked() {
        return currentlyStocking > -1;
    }

    public boolean isStocking() {
        for (Item i : items) {
            return i.isStocking() > -1;
        }
        
        return false;
    }
}
