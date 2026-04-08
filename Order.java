import java.util.ArrayList;
import java.time.LocalDateTime;

public class Order {
    //If this boolean is false, this is an order the company has placed from a supplier
    //If this boolean is true, this is an order another company has placed with us
    private final boolean sale;
    //The ID of the supplier or client depending on whether this is a sale or a purchase
    private final int otherPartiesID;
    private OrderStatus orderStatus;
    private final ArrayList<StockItem> itemsOrdered;
    private final LocalDateTime orderTime; 

    public Order(int _otherPartiesID, ArrayList<StockItem> _itemsOrdered, LocalDateTime _orderTime, boolean _sale){
        otherPartiesID = _otherPartiesID;
        itemsOrdered = _itemsOrdered;
        orderTime = _orderTime;
        sale = _sale;
        orderStatus = OrderStatus.Placed;
    }

    /**
     * <p>
     * Calculate the total worth of this order
     * </p>
     * @return the total cost of the order
     */
    public float GetOrderCost(){
        float total = 0;
        for (StockItem itemOrder : itemsOrdered){
            total += itemOrder.GetTotalCost();
        }
        return total;
    }

    /**
     * <p>
     * Getter for otherPartiesID
     * </p>
     * @return the value of otherPartiesID
     */
    public int GetID(){
        return otherPartiesID;
    }

    /**
     * <p>
     * Getter for sale
     * </p>
     * @return the value of sale
     */
    public boolean isSale(){
        return sale;
    }

    /**
     * <p>
     * Getter for orderStatus
     * </p>
     * @return the value of orderStatus
     */
    public OrderStatus GetOrderStatus(){
        return orderStatus;
    }

    /**
     * <p>
     * Setter for orderStatus
     * </p>
     * @param newOrderStatus - what orderStatus should change to
     */
    public void SetOrderStatus(OrderStatus newOrderStatus){
        orderStatus = newOrderStatus;
    }

    /**
     * <p>
     * Getter for orderTime
     * </p>
     * @return the value of orderTime
     */
    public LocalDateTime GetOrderTime(){
        return orderTime;
    }

    /**
     * <p>
     * Getter for itemsOrdered
     * </p>
     * @return the contents of itemsOrdered
     */
    public ArrayList<StockItem> GetItemsOrdered(){
        return itemsOrdered;
    }
}