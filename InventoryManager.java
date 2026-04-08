import java.util.ArrayList;

public class InventoryManager {
    private int lowStockThreshold = 10;
    private final ArrayList<StockItem> inventory;
    private float budget = 3000.0f;
    private float profit = 500.0f;

    public InventoryManager(ArrayList<StockItem> _inventory, int _lowStockThreshold){
        inventory = _inventory;
        lowStockThreshold = _lowStockThreshold;
    }

    /**
     * <p>
     * Adds the provided stockitem to the inventory arraylist
     * </p>
     * @param newStock the stock item to add
     */
    public void AddNewStockItem(StockItem newStock){
        inventory.add(newStock);
    }

    
    /**
     * <p>
     * Takes in an order. If it is a sale, it takes payment, and then removes all stockitems in the order from the inventory's stock.
     * If it is a purchase, it payes the amount required.
     * </p>
     * @param order - the order to process
     */
    public void OrderPlaced(Order order){
        if (order.isSale()){
            RecievePayment(order.GetOrderCost());
            for (StockItem item : order.GetItemsOrdered())
            {
                StockItem itemAlreadyInStock = GetStockItemByName(item.GetItemName());
                if (itemAlreadyInStock == null){
                    System.out.print("An item " + item.GetItemName() + " has been ordered that is not in the inventory. This order will fail.");
                }
                else{
                    try {
                        itemAlreadyInStock.SetAmount(itemAlreadyInStock.GetAmount() - item.GetAmount());
                    } catch (StockException e) {
                        System.out.print("A StockException has occured in the OrderPlaced function. Please retry your last update. If this issue persists, contact the developer (22402030@bucks.ac.uk).");
                    }
                }   
            }
        }
        else{
            MakePurchase(order.GetOrderCost());
        }
    }

    /**
     * <p>
     * Takes in an order that has been recieved. If it is a purchase order, it adds the purchased stock items to inventory
     * </p>
     * @param order the order to process
     */
    public void OrderRecieved(Order order){
        if (!order.isSale()){
            for (StockItem item : order.GetItemsOrdered())
            {
                StockItem itemAlreadyInStock = GetStockItemByName(item.GetItemName());
                // If we don't currently have any of this item in stock, create a new entry in the inventory, otherwise add to the amount of the existing entry
                if (itemAlreadyInStock == null){
                    // Create a copy to avoid inventory changes affecting the original order
                    StockItem itemToAdd = new StockItem(item.GetItemName(), item.GetAmount(), item.GetIndividualCost());
                    inventory.add(itemToAdd);
                }
                else{
                    try {
                        itemAlreadyInStock.SetAmount(itemAlreadyInStock.GetAmount() + item.GetAmount());
                    } catch (StockException e) {
                        System.out.print("A StockException has occured in the OrderRecieved function. Please retry your last update. If this issue persists, contact the developer (22402030@bucks.ac.uk).");
                    }
                }   
            }
        }
    }

    /**
     * <p>
     * Get how many items of a particular stock item are left using its name, or -1 if that item does not exist
     * </p>
     * @param name - the name of the item you want the stock for
     * @return the amount of that item in stock, or -1 if that item does not exit
     */
    public int GetLevelOfItemStockByName(String name){
        StockItem stockItem = GetStockItemByName(name);
        if (stockItem != null){
            return stockItem.GetAmount();
        }
        else{
            return -1;
        }
    }

    /**
     * <p>
     * Finds a stock item in the inventory by name
     * </p>
     * @param name - the name we want to find the matching item for
     * @return the stockitem that matches the name, or null if none do
     */
    public StockItem GetStockItemByName(String name){
        for (StockItem stockItem : inventory){
            // Converts both names to lowercase to avoid issues where a capitalised name does not match a non-capitalised one
            if (stockItem.GetItemName().toLowerCase().equals(name.toLowerCase())){
                return stockItem;
            }
        }
        return null;
    }

    /**
     * <p>
     * Sets the amount at which items should be registered as low on stock
     * </p>
     * @param newThreshold - the new amount at which stock items should start being registered as low on stock
     */
    public void setLowStockThreshold(int newThreshold){
        // Low stock threshold should not go below 0
        if (newThreshold >= 0){
            lowStockThreshold = newThreshold;
        }
        else{
            lowStockThreshold = 0;
        }
    }

    /**
     * <p>
     * Iterates through all items in inventory and returns the ones which are low on stock
     * </p>
     * @return an arraylist of the stockitems that are low on stock
     */
    public ArrayList<StockItem> CheckForLowStock(){
        ArrayList<StockItem> itemsLowOnStock = new ArrayList<>();
        for (StockItem stockItem : inventory){
            if (stockItem.GetAmount() <= lowStockThreshold){
                itemsLowOnStock.add(stockItem);
            }
        }
        return itemsLowOnStock;
    }

    /**
     * <p>
     * Subtracts the provided cost from the budget
     * </p>
     * @param cost - the cost of the purchase
     */
    public void MakePurchase(float cost){
        budget -= cost;
    }

    /**
     * <p>
     * Adds the provided amount to the profit
     * </p>
     * @param payment the amount being paid in
     */
    public void RecievePayment(float payment){
        profit += payment;
    }

    /**
     * <p>
     * Takes money from the profit and moves it into the budget
     * </p>
     * @param amount how much money to move from profit to budget
     */
    public void PayIntoBudget(float amount){
        profit -= amount;
        budget += amount;
    }

    /**
     * <p>
     * Getter for budget
     * </p>
     * @return budget's value
     */
    public float GetRemainingBudget(){
        return budget;
    }

    /**
     * <p>
     * Getter for profit
     * </p>
     * @return profit's value
     */
    public float GetCurrentProfits(){
        return profit;
    }
}
