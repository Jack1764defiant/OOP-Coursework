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

    public float GetOrderCost(){
        float total = 0;
        for (StockItem itemOrder : itemsOrdered){
            total += itemOrder.GetTotalCost();
        }
        return total;
    }

    public int GetID(){
        return otherPartiesID;
    }

    public boolean isSale(){
        return sale;
    }

    public OrderStatus GetOrderStatus(){
        return orderStatus;
    }

    public void SetOrderStatus(OrderStatus newOrderStatus){
        orderStatus = newOrderStatus;
    }

    public LocalDateTime GetOrderTime(){
        return orderTime;
    }

    public ArrayList<StockItem> GetItemsOrdered(){
        return itemsOrdered;
    }
}