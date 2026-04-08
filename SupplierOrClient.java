import java.time.LocalDate;
import java.util.ArrayList;

public class SupplierOrClient {
    private final int ID;
    private String name;
    private String emailAddress;
    private String phoneNumber;
    private final ArrayList<Order> orderHistory;

    public SupplierOrClient(int _ID, String _name, String _emailAddress, String _phoneNumber){
        ID = _ID;
        name = _name;
        emailAddress = _emailAddress;
        phoneNumber = _phoneNumber;
        orderHistory = new ArrayList<>();
    }

    public SupplierOrClient(int _ID, String _name, String _emailAddress, String _phoneNumber, ArrayList<Order> _orderHistory){
        ID = _ID;
        name = _name;
        emailAddress = _emailAddress;
        phoneNumber = _phoneNumber;
        orderHistory = _orderHistory;
    }

    /**
     * <p>
     * Get list of orders from this company on the provided date
     * </p>
     * @param date - the date we want the orders from
     * @return an arraylist of orders that occured on the specified date
     */
    public ArrayList<Order> GetOrdersOnDate(LocalDate date){
        ArrayList<Order> ordersOnDate = new ArrayList<>();
        for (Order order : orderHistory){
            if (order.GetOrderTime().getYear() == date.getYear() && order.GetOrderTime().getMonthValue() == date.getMonthValue() && order.GetOrderTime().getDayOfMonth() == date.getDayOfMonth()){
                ordersOnDate.add(order);
            }
        }
        return ordersOnDate;
    }

    /**
     * <p>
     * Get list of orders from this company between the provided dates
     * </p>
     * @param beginningDate - the dates we want the orders after (inclusive)
     * @param endDate - the dates we want the orders before (inclusive)
     * @return an arraylist of orders that occured between the specified dates
     */
    public ArrayList<Order> GetOrdersBetweenDates(LocalDate beginningDate, LocalDate endDate){
        ArrayList<Order> ordersInPeriod = new ArrayList<>();
        for (Order order : orderHistory){
            if (order.GetOrderTime().getYear() >= beginningDate.getYear() && order.GetOrderTime().getYear() <= endDate.getYear() 
                && order.GetOrderTime().getMonthValue() >= beginningDate.getMonthValue() && order.GetOrderTime().getMonthValue() <= endDate.getMonthValue()
            && order.GetOrderTime().getDayOfMonth() >= beginningDate.getDayOfMonth() && order.GetOrderTime().getDayOfMonth() <= endDate.getDayOfMonth())
            {
                ordersInPeriod.add(order);
            }
        }
        return ordersInPeriod;
    }

    /**
     * <p>
     * Add an order to this supplier or client's order history
     * </p>
     * @param order - the order we want added
     */
    public void AddOrderToHistory(Order order){
        orderHistory.add(order);
    }

    /**
     * <p>
     * Getter for ID
     * </p>
     * @return the ID of this supplier/client
     */
    public int GetID(){
        return ID;
    }

    /**
     * <p>
     * Getter for name
     * </p>
     * @return the name of this client/supplier
     */
    public String GetName(){
        return name;
    }

    /**
     * <p>
     * Setter for name
     * </p>
     * @param newName - the supplier/client's new name
     */
    public void SetName(String newName){
        name = newName;
    }

    public String GetPhoneNumber(){
        return phoneNumber;
    }

    public void SetPhoneNumber(String newPhoneNumber){
        phoneNumber = newPhoneNumber;
    }

    public String GetEmailAddress(){
        return emailAddress;
    }

    public void SetEmailAddress(String newEmailAddress){
        emailAddress = newEmailAddress;
    }
}
