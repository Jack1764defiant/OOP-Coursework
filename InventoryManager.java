import java.util.ArrayList;

public class InventoryManager {
    private int lowStockThreshold = 10;
    private final ArrayList<StockItem> inventory;
    private float budget = 0.0f;
    private float profit = 0.0f;

    public InventoryManager(ArrayList<StockItem> _inventory, int _lowStockThreshold){
        inventory = _inventory;
        lowStockThreshold = _lowStockThreshold;
    }

    public void AddNewStockItem(StockItem newStock){
        inventory.add(newStock);
    }

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

    public void OrderRecieved(Order order){
        if (!order.isSale()){
            for (StockItem item : order.GetItemsOrdered())
            {
                StockItem itemAlreadyInStock = GetStockItemByName(item.GetItemName());
                // If we don't currently have any of this item in stock, create a new entry in the inventory, otherwise add to the amount of the existing entry
                if (itemAlreadyInStock == null){
                    inventory.add(item);
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

    // Returns -1 if no stock items have a matching name
    public int GetLevelOfItemStockByName(String name){
        StockItem stockItem = GetStockItemByName(name);
        if (stockItem != null){
            return stockItem.GetAmount();
        }
        else{
            return -1;
        }
    }

    public StockItem GetStockItemByName(String name){
        for (StockItem stockItem : inventory){
            // Converts both names to lowercase to avoid issues where a capitalised name does not match a non-capitalised one
            if (stockItem.GetItemName().toLowerCase().equals(name.toLowerCase())){
                return stockItem;
            }
        }
        return null;
    }

    public void setLowStockThreshold(int newThreshold){
        // Low stock threshold should not go below 0
        if (newThreshold >= 0){
            lowStockThreshold = newThreshold;
        }
        else{
            lowStockThreshold = 0;
        }
    }

    public ArrayList<StockItem> CheckForLowStock(){
        ArrayList<StockItem> itemsLowOnStock = new ArrayList<>();
        for (StockItem stockItem : inventory){
            if (stockItem.GetAmount() <= lowStockThreshold){
                itemsLowOnStock.add(stockItem);
            }
        }
        return itemsLowOnStock;
    }

    public void MakePurchase(float cost){
        budget -= cost;
    }

    public void RecievePayment(float payment){
        profit += payment;
    }

    public void PayIntoBudget(float amount){
        profit -= amount;
        budget += amount;
    }

    public float GetRemainingBudget(){
        return budget;
    }

    public float GetCurrentProfits(){
        return profit;
    }
}
