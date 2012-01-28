package model;

import java.util.HashMap;
import java.util.Map;

public interface Item {

    
    /**
     * @return - if the item is stocked
     */
    public boolean isStocked();
    
    /**
     * @return - If the item is ready to be stocked
     */
    public boolean isReady();
    
    /**
     * @return - The value of the item
     */
    public float getValue();
    
    /**
     * @return - How much is left of the item
     */
    public int getAmount();
    
    /**
     * @return - Cost of the item
     */
    public float getCost();
    
    /**
     * @return - Returns the type of the item as an integer, this value can be compare with constants in ItemFactory
     */
    public int getType();
    
    /**
     * @return - The string name of the item
     */
    public String getTypeName();

    /**
     * Sets the store that an item should be associated with
     * @param store - The store to associate the item to
     */
    public void setStore(Store store);
    
    /**
     * 
     * @return The store which the item is associated with
     */
    public Store getStore();

    /**
     * Returns the number represent what the current stocking item is
     */
    public int isStocking();
    
    /**
     * @return - The time remaining to stock an item
     */
    public long getStockTime();
    
    /************************
     * Methods
     ************************/
    
    /**
     * Updates
     */
    public void update();
    


}
