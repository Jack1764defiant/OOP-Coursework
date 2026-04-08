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

    public ArrayList<Order> GetOrdersOnDate(LocalDate date){
        ArrayList<Order> ordersOnDate = new ArrayList<>();
        for (Order order : orderHistory){
            if (order.GetOrderTime().getYear() == date.getYear() && order.GetOrderTime().getMonthValue() == date.getMonthValue() && order.GetOrderTime().getDayOfMonth() == date.getDayOfMonth()){
                ordersOnDate.add(order);
            }
        }
        return ordersOnDate;
    }

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

    public void AddOrderToHistory(Order order){
        orderHistory.add(order);
    }

    public int GetID(){
        return ID;
    }

    public String GetName(){
        return name;
    }

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
