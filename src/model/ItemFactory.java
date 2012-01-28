package model;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * This class follows my idea of what a factory class might be. It contains a
 * private ItemTemplate class which implements the Item interface. To create
 * items the correct methods have to be invoked and the actual constructor for
 * the ItemTemplate class is hidden.
 * 
 * @author Uri Gorelik
 */
public class ItemFactory {
    private static final float COST_MULTI = 1.2f;

    // Types kinda useless
    public static final int TYPE_HORN = 0;
    public static final int TYPE_BRUSH = 1;
    public static final int TYPE_SHAMPOO = 2;
    public static final int TYPE_RAINBOW = 3;
    public static final int TYPE_UNICORN = 4;

    // Game numbers
    public static final String[] TYPE = { "horn", "brush", "shampoo", "rainbow", "unicorn" };
    public static final int[] TIMER = { 20000, 15000, 10000, 5000, 5000 };
    public static final int STOCK_TIME_WEIGHT = 15000; // 600000 for minutes
    public static final int[] STOCK_TIME = { STOCK_TIME_WEIGHT * 1, STOCK_TIME_WEIGHT * 15, STOCK_TIME_WEIGHT * 20, STOCK_TIME_WEIGHT * 30, STOCK_TIME_WEIGHT * 60 }; // TODO: Change horn back to 5
    public static final int ITEM_COUNT = TYPE.length;
    private static final int[] AMOUNT = { 5, 5, 5, 5, 5 };
    private static final float[] VALUE = { 12.25f, 6.72f, 8.9f, 1.0f, 1.0f };
    public static final float[] COST = new float[TYPE.length];
    static {
        for (int i = 0; i < COST.length; i++) {
            COST[i] = VALUE[i] * AMOUNT[i] * COST_MULTI;
        }
    }

    // A map used to find the type number by string
    public static final Map<String, Integer> TYPE_MAP;
    static {
        Map<String, Integer> tempMap = new HashMap();
        for (int i = 0; i < TYPE.length; i++) {
            tempMap.put(TYPE[i], i);
        }
        TYPE_MAP = tempMap;
    }

    // Instance of the factory to be able to create nested classes
    private static ItemFactory factory = new ItemFactory();
    
    // This is used in item template, for some reason it has to be declared here
    private static int stocking = -1;

    /*************************************************************************************************
     * PUBLIC METHODS
     *************************************************************************************************/

    public static Item createItem(int type, int quantity) {
        return factory.new ItemTemplate(type, quantity, COST[type], VALUE[type]);
    }

    public static Item createBrush() {
        return createItem(TYPE_BRUSH);
    }

    public static Item createHorn() {
        return createItem(TYPE_HORN);
    }

    public static Item createShampoo() {
        return createItem(TYPE_SHAMPOO);
    }

    public static Item createPlace1() {
        return createItem(TYPE_RAINBOW);
    }

    public static Item createPlace2() {
        return createItem(TYPE_UNICORN);
    }

    /**
     * The mechanism used for creating items.
     * 
     * @return
     */
    private static Item createItem(int type) {
        return factory.new ItemTemplate(type, 0, COST[type], VALUE[type]);
    }

    /*************************************************************************************************
     * NESTED CLASS
     *************************************************************************************************/

    /**
     * Nested class for items
     * 
     * @author Uri Gorelik
     */
    
    private class ItemTemplate extends TimerTask implements Item {

        // Instance variables
        private int amount;
        private float value;
        private float cost;
        private int type;
        private long stockTime;
        private boolean ready;
        private Store store;
        

        public ItemTemplate(int type, int amount, float cost, float value) {
            this.type = type;
            this.amount = amount;
            this.value = value;
            this.cost = cost;
            
            stockTime = -1;
            ready = false;
            store = null;
        }

        /**
         * Helper method used to 'sell' an item. Deducts 1 from the amount and adds
         * money to the store
         */
        private void sell() {
            if (isStocked()) {
                amount--;
                store.addMoney(getValue());
            }
        }

        /**
         * Gets executed based on time
         */
        public void run() {
            System.out.println("Item::" + TYPE[getType()] + ":: updating...");

            // Check if enough time has passed to for the item to arrive
            // TODO: this was changed from 0 to -1
            if(stockTime > 0) {
                stockTime -= TIMER[type];
            } else {
                // TODO: This would make every single item ready at the start, have to find a way so that this is only triggered when some other condition is met
                if (stockTime != -1)
                    ready = true;
            }
            
            // Always try to sell
            sell();
        }

        /**
         * This method is called on mouse click, handled by Store
         */
        public void update() {

            // If an item is ready, then we stock it
            if (ready) {
                amount = AMOUNT[type];
                stocking = -1;
                ready = false;
                stockTime = -1;
            }

            // if an item is not stocking we stock it
            if (stocking == -1) {
                ready = false; // This might need to change as it's not really
                               // relevant
                stocking = type;
                stockTime = STOCK_TIME[type];

            }

        }

        /**
         * Getters
         */
        public boolean isStocked() {
            return amount > 0;
        }

        public float getValue() {

            return value;
        }

        public int getAmount() {
            return amount;
        }

        public float getCost() {
            return cost;
        }

        public int getType() {
            return type;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public void setCost(float cost) {
            this.cost = cost;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTypeName() {
            return TYPE[type];
        }

        public void setStore(Store store) {
            this.store = store;
        }

        public Store getStore() {
            return store;
        }


        
        public boolean isReady() {
            return ready;
        }

        public int isStocking() {
            return stocking;
        }

        public long getStockTime() {
            return stockTime;
        }
    }

}
