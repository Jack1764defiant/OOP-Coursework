import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Scanner;

public class WMSInterface {
    private final Scanner scanner; 
    private boolean running = true;
    private boolean displayLowStockAlerts = true;
    private final InventoryManager inventoryManager;
    private final SupplierAndClientManager supplierAndClientManager;

    public WMSInterface(){
        scanner = new Scanner(System.in);
        inventoryManager = new InventoryManager(new ArrayList<>(), 10);
        supplierAndClientManager = new SupplierAndClientManager(new ArrayList<>());
    }

    public void RunWMSInterface(){
        while (running){
            DisplayMenu();
        }
    }

    public void DisplayMenu(){
        ClearTerminal();
        System.out.println("WMS Menu");
        System.out.println("1. Exit WMS interface.");
        System.out.println("2. Suppliers and Clients");
        System.out.println("3. Orders");
        System.out.println("4. Inventory");
        System.out.println("5. Financial");
        if (displayLowStockAlerts){
            PrintLowStockAlerts();
        }
        String menuChoice = Input(scanner, "Enter option: ");
        switch (menuChoice) {
            case "1" -> {
                running = false;
                break;
            }
            case "2" -> {
                HandleSuppliersAndClientsMenu(scanner);
                break;
            }
            case "3" -> {
                HandleOrderMenu(scanner);
                break;
            }
            case "4" -> {
                HandleInventoryMenu(scanner);
                break;
            }
            case "5" -> {
                HandleFinancialMenu(scanner);
                break;
            }
            default -> {
                System.out.println(menuChoice + " is not a known option.");
                scanner.nextLine();
                break;
            }
        }
    }

    public void PrintLowStockAlerts(){
        ArrayList<StockItem> LowStockItems = inventoryManager.CheckForLowStock();
        for (StockItem lowStockItem : LowStockItems){
            System.out.println("Low on " + lowStockItem.GetItemName() + " - only " + String.valueOf(lowStockItem.GetAmount()) + " remain.");
        }
        if (LowStockItems.isEmpty()){
            System.out.println("No items low on stock.");
        }
    }

    public void HandleFinancialMenu(Scanner scanner){
        boolean inMenu = true;
        while (inMenu){
            ClearTerminal();
            System.out.println("1. Exit Menu");
            System.out.println("2. Generate expense report between provided dates");
            System.out.println("3. Generate sales report between provided dates");
            System.out.println("4. Generate profit report between provided dates");
            System.out.println("5. Check Remaining budget for inventory");
            System.out.println("6. Top up budget");
            System.out.println("7. Check surplus money");
            String menuChoice = Input(scanner, "Enter option: ");
            switch (menuChoice) {
                case "1" -> {
                    inMenu = false;
                    break;
                }
                case "2" -> {
                    GenerateExpenseReportInPeriod(scanner);
                    break;
                }
                case "3" -> {
                    GenerateSalesReportInPeriod(scanner);
                    break;
                }
                case "4" -> {
                    GenerateProfitReportInPeriod(scanner);
                    break;
                }
                case "5" -> {
                    CheckRemainingBudget(scanner);
                    break;
                }
                case "6" -> {
                    TopUpBudget(scanner);
                    break;
                }
                case "7" -> {
                    CheckSurplusMoney(scanner);
                    break;
                }
                default -> {
                    System.out.println(menuChoice + " is not a known option.");
                    scanner.nextLine();
                    break;
                }
            }
        }
    }

    public void CheckRemainingBudget(Scanner scanner){
        System.out.printf("Remaining budget for inventory: £%,.2f", inventoryManager.GetRemainingBudget());
        System.out.println();
        scanner.nextLine();
    }

    public void CheckSurplusMoney(Scanner scanner){
        System.out.printf("Surplus money: £%,.2f", inventoryManager.GetCurrentProfits());
        System.out.println();
        scanner.nextLine();
    }

    public void TopUpBudget(Scanner scanner){
        System.out.printf("Enter amount of money to transfer from profits to inventory budget (up to £%,.2f): £", inventoryManager.GetCurrentProfits());
        String transferAmount = scanner.nextLine();
        while (!isValidFloat(transferAmount) || Float.parseFloat(transferAmount) > inventoryManager.GetCurrentProfits() || Float.parseFloat(transferAmount) < 0){
            System.out.print("Invalid. Enter amount to transfer: £");
            transferAmount = scanner.nextLine();
        }
        inventoryManager.PayIntoBudget(Float.parseFloat(transferAmount));
    }

    public LocalDate GetDate(Scanner scanner){
        int year = IntInput(scanner, "Enter the year: ");
        int month = IntInput(scanner, "Enter the month: ", 1, 12);
        String day = Input(scanner, "Enter the day: ");
        YearMonth inputDate = YearMonth.of(year, month);
        while (!isValidInteger(day) || !inputDate.isValidDay(Integer.parseInt(day))){
            day = Input(scanner, "Invalid. Enter the day: ");
        }
        LocalDate date = LocalDate.now();
        date.withYear(year);
        date.withMonth(month);
        date.withDayOfMonth(Integer.parseInt(day));
        return date;
    }

    public void GenerateExpenseReportInPeriod(Scanner scanner){
        ClearTerminal();
        System.out.println("Start Date");
        LocalDate startDate = GetDate(scanner);
        System.out.println("End Date");
        LocalDate endDate = GetDate(scanner);
        ArrayList<Order> purchasesInPeriod = new ArrayList<>();
        float totalCost = 0.0f;
        for (SupplierOrClient supplierOrClient : supplierAndClientManager.GetSuppliersAndClients())
        {
            ArrayList<Order> ordersInPeriod = supplierOrClient.GetOrdersBetweenDates(startDate, endDate);
            for (Order order : ordersInPeriod){
                if (!order.isSale()){
                    purchasesInPeriod.add(order);
                    totalCost += order.GetOrderCost();
                }
            }
        }
        for (Order purchase : purchasesInPeriod){
            System.out.printf("Paid £%,.2f at " + purchase.GetOrderTime().toString() + " to " + supplierAndClientManager.GetSupplierOrClientByID(purchase.GetID()).GetName(), purchase.GetOrderCost());
            System.out.println("");
        }
        System.out.printf("Total expenses over period: £%,.2f", totalCost);
        System.out.println();
        scanner.nextLine();
    }

    public void GenerateSalesReportInPeriod(Scanner scanner){
        ClearTerminal();
        System.out.println("Start Date");
        LocalDate startDate = GetDate(scanner);
        System.out.println("End Date");
        LocalDate endDate = GetDate(scanner);
        ArrayList<Order> salesInPeriod = new ArrayList<>();
        float totalSales = 0.0f;
        for (SupplierOrClient supplierOrClient : supplierAndClientManager.GetSuppliersAndClients())
        {
            ArrayList<Order> ordersInPeriod = supplierOrClient.GetOrdersBetweenDates(startDate, endDate);
            for (Order order : ordersInPeriod){
                if (order.isSale()){
                    salesInPeriod.add(order);
                    totalSales += order.GetOrderCost();
                }
            }
        }
        for (Order sale : salesInPeriod){
            System.out.printf("Made £%,.2f at " + sale.GetOrderTime().toString() + " from " + supplierAndClientManager.GetSupplierOrClientByID(sale.GetID()).GetName(), sale.GetOrderCost());
            System.out.println("");
        }
        System.out.printf("Total sales over period: £%,.2f", totalSales);
        System.out.println();
        scanner.nextLine();
    }

    public void GenerateProfitReportInPeriod(Scanner scanner){
        ClearTerminal();
        LocalDate startDate = GetDate(scanner);
        System.out.println("End Date");
        LocalDate endDate = GetDate(scanner);
        ArrayList<Order> allOrdersInPeriod = new ArrayList<>();
        float totalProfit = 0.0f;
        for (SupplierOrClient supplierOrClient : supplierAndClientManager.GetSuppliersAndClients())
        {
            ArrayList<Order> ordersInPeriod = supplierOrClient.GetOrdersBetweenDates(startDate, endDate);
            allOrdersInPeriod.addAll(ordersInPeriod);
            for (Order order : ordersInPeriod){
                if (order.isSale()){
                    totalProfit += order.GetOrderCost();
                }
                else{
                    totalProfit -= order.GetOrderCost();
                }
            }
        }
        for (Order order : allOrdersInPeriod){
            if (order.isSale()){
                System.out.printf("Made £%,.2f at " + order.GetOrderTime().toString() + " from " + supplierAndClientManager.GetSupplierOrClientByID(order.GetID()).GetName(), order.GetOrderCost());
                System.out.println("");
            }
            else{
                System.out.printf("Paid £%,.2f at " + order.GetOrderTime().toString() + " to " + supplierAndClientManager.GetSupplierOrClientByID(order.GetID()).GetName(), order.GetOrderCost());
                System.out.println("");
            }
        }
        System.out.printf("Total profit over period: £%,.2f", totalProfit);
        System.out.println();
        scanner.nextLine();
    }



    public void HandleSuppliersAndClientsMenu(Scanner scanner){
        boolean inMenu = true;
        while (inMenu){
            ClearTerminal();
            System.out.println("1. Exit Menu");
            System.out.println("2. Create new Supplier or Client");
            System.out.println("3. Edit Supplier or Client");
            System.out.println("4. Delete Supplier or Client");
            System.out.println("5. View Supplier or Client details");
            String menuChoice = Input(scanner, "Enter option: ");
            switch (menuChoice) {
                case "1" -> {
                    inMenu = false;
                    break;
                }
                case "2" -> {
                    CreateSupplierOrClient(scanner);
                    break;
                }
                case "3" -> {
                    EditSupplierOrClient(scanner);
                    break;
                }
                case "4" -> {
                    DeleteSupplierOrClient(scanner);
                    break;
                }
                case "5" -> {
                    ViewSupplierOrClient(scanner);
                    break;
                }
                default -> {
                    System.out.println(menuChoice + " is not a known option.");
                    scanner.nextLine();
                    break;
                }
            }
        }
    }


    public void CreateSupplierOrClient(Scanner scanner){
        int ID = IntInput(scanner, "Enter a unique ID number for the supplier or client: ");
        String name = Input(scanner, "Enter name: ");
        String emailAddress = Input(scanner, "Enter email address: ");
        String phoneNumber = Input(scanner, "Enter phone number: ");
        SupplierOrClient newSupplier = new SupplierOrClient(ID, name, emailAddress, phoneNumber);
        supplierAndClientManager.AddSupplierOrClient(newSupplier);
    }

    
    public SupplierOrClient GetSupplierOrClient(Scanner scanner){
        String name = Input(scanner, "Enter name: ");
        return supplierAndClientManager.GetSupplierOrClientByName(name);
    }

    
    public void EditSupplierOrClient(Scanner scanner){
        SupplierOrClient toEdit = GetSupplierOrClient(scanner);
        if (toEdit == null){
            System.out.print("No matching supplier found.");
            scanner.nextLine();
        }
        else{
            String supplierName = Input(scanner, "Enter supplier/client name (currently " + toEdit.GetName() + "): ");
            String emailAddress = Input(scanner, "Enter email address (currently " + toEdit.GetEmailAddress() + "): ");
            String phoneNumber = Input(scanner, "Enter phone number (currently " + toEdit.GetPhoneNumber() + "): ");
            toEdit.SetName(supplierName);
            toEdit.SetEmailAddress(emailAddress);
            toEdit.SetPhoneNumber(phoneNumber);
        }
    }

    
    public void DeleteSupplierOrClient(Scanner scanner){
        SupplierOrClient supplierToDelete = GetSupplierOrClient(scanner);
        if (supplierToDelete == null){
            System.out.print("No matching supplier found.");
            scanner.nextLine();
        }
        else{
            supplierAndClientManager.RemoveSupplierOrClient(supplierToDelete);
        }
    }

    
    public void ViewSupplierOrClient(Scanner scanner){
        SupplierOrClient supplier = GetSupplierOrClient(scanner);
        if (supplier == null){
            System.out.print("No matching supplier found.");
            scanner.nextLine();
        }
        else{
            System.out.println("Name: " + supplier.GetName());
            System.out.println("Supplier ID: " + supplier.GetID());
            System.out.println("Email Address: " + supplier.GetEmailAddress());
            System.out.println("Phone Number: " + supplier.GetPhoneNumber());
            scanner.nextLine();
        }
    }


    public void HandleOrderMenu(Scanner scanner){
        boolean inOrderMenu = true;
        while (inOrderMenu){
            ClearTerminal();
            System.out.println("1. Exit Order Menu");
            System.out.println("2. Place Order");
            System.out.println("3. Update Order Status");
            System.out.println("4. View Order");
            String menuChoice = Input(scanner, "Enter option: ");
            switch (menuChoice) {
                case "1" -> {
                    inOrderMenu = false;
                    break;
                }
                case "2" -> {
                    PlaceOrder(scanner);
                    break;
                }
                case "3" -> {
                    UpdateOrderStatus(scanner);
                    break;
                }
                case "4" -> {
                    ViewOrder(scanner);
                    break;
                }
                default -> {
                    System.out.println(menuChoice + " is not a known option.");
                    scanner.nextLine();
                    break;
                }
            }
        }
    }

        public void PlaceOrder(Scanner scanner){
        ClearTerminal();
        String supplierName = Input(scanner, "Enter the supplier or client's name being ordered from or sent to: ");
        SupplierOrClient supplierToOrderFrom = supplierAndClientManager.GetSupplierOrClientByName(supplierName);
        if (supplierToOrderFrom != null){
            String saleOrOrder = Input(scanner, "Enter S if this is a sale, or P if this is a purchase: ");
            while (!saleOrOrder.toLowerCase().equals("p") && !saleOrOrder.toLowerCase().equals("s")){
                saleOrOrder = Input(scanner, "Invalid. Enter S if this is a sale, or P if this is a purchase: ");
            }
            boolean sale = saleOrOrder.toLowerCase().equals("s");
            boolean addingItemsToOrder = true;
            ArrayList<StockItem> itemsToOrder = new ArrayList<>();
            while (addingItemsToOrder){
                ClearTerminal();
                String item = Input(scanner, "Enter the item to order or x to quit: ");
                if (item.toLowerCase().equals("x")){
                    addingItemsToOrder = false;
                }
                else if (inventoryManager.GetStockItemByName(item) != null){
                    String quantity = Input(scanner, "Enter quantity of the item to order: ");
                    while (!isValidInteger(quantity) || inventoryManager.GetStockItemByName(item).GetAmount() < Integer.parseInt(quantity)){
                        quantity = Input(scanner, "Invalid. Enter quantity of the item to order: ");
                    }
                    Float cost = FloatInput(scanner, "Enter the total cost of the items: £");
                    int numericQuantity = Integer.parseInt(quantity);
                    float costPerItem = cost/numericQuantity;
                    itemsToOrder.add(new StockItem(item, numericQuantity, costPerItem));
                }
                else if (!sale){
                    String quantity = Input(scanner, "Enter quantity of the item to order: ");
                    while (!isValidInteger(quantity)){
                        quantity = Input(scanner, "Invalid. Enter quantity of the item to order: ");
                    }
                    Float cost = FloatInput(scanner, "Enter the total cost of the items: £");
                    int numericQuantity = Integer.parseInt(quantity);
                    float costPerItem = cost/numericQuantity;
                    itemsToOrder.add(new StockItem(item, numericQuantity, costPerItem));
                }
                else{
                    System.out.println("Item not in stock and thus cannot be ordered.");
                    scanner.nextLine();
                }
            }
            Order placedOrder = new Order(supplierToOrderFrom.GetID(), itemsToOrder, LocalDateTime.now(), sale);
            supplierToOrderFrom.AddOrderToHistory(placedOrder);
            inventoryManager.OrderPlaced(placedOrder);
            
        }
        else{
            System.out.print("Supplier not found.");
            scanner.nextLine();
        }
    }

    
    public void UpdateOrderStatus(Scanner scanner){
        ClearTerminal();
        SupplierOrClient supplierOrClientOrderIsWith = SelectSupplierOrClientForOrder(scanner);
        Order order = SelectOrderOnDate(scanner, supplierOrClientOrderIsWith);

        System.out.println("Choose a new status");
        System.out.println("1. Reset to status to Placed");
        System.out.println("2. In Transit");
        System.out.println("3. Recieved (if purchase, adds contents to stock automatically)");
        System.out.println("4. Cancelled");
        String menuChoice = Input(scanner, "Enter option: ");

        switch (menuChoice) {
            case "1" -> {
                order.SetOrderStatus(OrderStatus.Placed);
                break;
            }
            case "2" -> {
                order.SetOrderStatus(OrderStatus.InTransit);
                break;
            }
            case "3" -> {
                order.SetOrderStatus(OrderStatus.Recieved);
                inventoryManager.OrderRecieved(order);
                break;
            }
            case "4" -> {
                order.SetOrderStatus(OrderStatus.Cancelled);
                break;
            }
            default -> {
                System.out.println(menuChoice + " is not a known option.");
                scanner.nextLine();
                break;
            }
        }
    }

    
    public void ViewOrder(Scanner scanner){
        ClearTerminal();
        SupplierOrClient supplierOrClientToOrderFrom = SelectSupplierOrClientForOrder(scanner);
        Order order = SelectOrderOnDate(scanner, supplierOrClientToOrderFrom);
        System.out.println("Order from supplier " + supplierOrClientToOrderFrom.GetName() + " at " + order.GetOrderTime().toString());
        System.out.println("Current Order Status: " + order.GetOrderStatus().toString());
        for (StockItem item : order.GetItemsOrdered()){
            System.out.print(item.GetItemName() + ": " + item.GetAmount() + ".");
        }
        System.out.printf("Total Cost: £%,.2f", order.GetOrderCost());
        System.out.println("");
        scanner.nextLine();
    }

    
    public SupplierOrClient SelectSupplierOrClientForOrder(Scanner scanner){
        String supplierName = Input(scanner, "Enter the supplier or client's name the order was placed with: ");
        SupplierOrClient orderedFrom = supplierAndClientManager.GetSupplierOrClientByName(supplierName);

        while (orderedFrom == null){

            supplierName = Input(scanner, "Invalid. Enter the supplier or client's name the order was placed with: ");
            orderedFrom = supplierAndClientManager.GetSupplierOrClientByName(supplierName);
            
        }
        return orderedFrom;
    }

    
    public Order SelectOrderOnDate(Scanner scanner, SupplierOrClient supplierToOrderFrom){
        System.out.println("Enter the date the order was placed on.");
        LocalDate date = GetDate(scanner);
        ArrayList<Order> ordersOnDate = supplierToOrderFrom.GetOrdersOnDate(date);
        int count = 1;
        for (Order order : ordersOnDate){
            System.out.println("Order " + count);
            System.out.print("Order placed on ");
            System.out.println(order.GetOrderTime().toString());
            for (StockItem item : order.GetItemsOrdered()){
                System.out.println(item.GetItemName() + ": " + item.GetAmount() + ".");
            }
            count++;
        }

        String orderNumber = Input(scanner, "Enter the number of the order you want to select:");
        while (!isValidInteger(orderNumber) || Integer.parseInt(orderNumber) < 1 || Integer.parseInt(orderNumber) > ordersOnDate.size()){
            orderNumber = Input(scanner, "Invalid. Enter the number of the order you want to select:");
        }

        return ordersOnDate.get(Integer.parseInt(orderNumber) - 1);
    }


    public void HandleInventoryMenu(Scanner scanner){
        boolean inInventoryMenu = true;
        while (inInventoryMenu){
            ClearTerminal();
            System.out.println("1. Exit Inventory Menu");
            System.out.println("2. Check Stock of Item");
            System.out.println("3. Update Item Amount Manually");
            System.out.println("4. Toggle Display Low Stock Alerts: Currently " + String.valueOf(displayLowStockAlerts));
            String menuChoice = Input(scanner, "Enter option: ");
            switch (menuChoice) {
                case "1" -> {
                    inInventoryMenu = false;
                    break;
                }
                case "2" -> {
                    CheckItemStock(scanner);
                    break;
                }
                case "3" -> {
                    UpdateItemStock(scanner);
                    break;
                }
                case "4" -> {
                    displayLowStockAlerts = !displayLowStockAlerts;
                    break;
                }
                default -> {
                    System.out.println(menuChoice + " is not a known option.");
                    scanner.nextLine();
                    break;
                }
            }
        }
    }

        public void CheckItemStock(Scanner scanner){
        ClearTerminal();
        String item = Input(scanner, "Enter the name of the item you want to check the stock of: ");
        int stockAmount = inventoryManager.GetLevelOfItemStockByName(item);
        if (stockAmount == -1){
            System.out.println("Item not found.");
            scanner.nextLine();
        }
        else{
            System.out.println("Quantity of item remaining: " + stockAmount + ".");
            scanner.nextLine();
        }
    }

    
    public void UpdateItemStock(Scanner scanner){
        ClearTerminal();
        String item = Input(scanner, "Enter the name of the item you want to update the stock of: ");
        int stockAmount = inventoryManager.GetLevelOfItemStockByName(item);
        if (stockAmount == -1){
            System.out.println("Item not found.");
            scanner.nextLine();
        }
        else{
            System.out.println("Current stock: " + stockAmount + ".");
            String newAmount = Input(scanner, "Enter new amount: ");
            while (!isValidInteger(newAmount) || Integer.parseInt(newAmount) < 0){
                newAmount = Input(scanner, "Invalid. Enter new amount: ");
            }
            try {
                inventoryManager.GetStockItemByName(item).SetAmount(Integer.parseInt(newAmount));
            } catch (StockException e) {
                System.out.println("A StockException has occured in the UpdateItemStock function. Please retry your last update. If this issue persists, contact the developer (22402030@bucks.ac.uk).");
            }
        }
    }


    public String Input(Scanner scanner, String prompt){
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int IntInput(Scanner scanner, String prompt){
        String value = Input(scanner, prompt);
        while (!isValidInteger(value)){
            value = Input(scanner, "Invalid. " + prompt);
        }
        return Integer.parseInt(value);
    }

    public int IntInput(Scanner scanner, String prompt, int lowerBound, int upperBound){
        String value = Input(scanner, prompt);
        while (!isValidInteger(value) || Integer.parseInt(value) < lowerBound || Integer.parseInt(value) > upperBound){
            value = Input(scanner, "Invalid. " + prompt);
        }
        return Integer.parseInt(value);
    }

    public float FloatInput(Scanner scanner, String prompt){
        String value = Input(scanner, prompt);
        while (!isValidFloat(value)){
            value = Input(scanner, "Invalid. " + prompt);
        }
        return Float.parseFloat(value);
    }


    public void ClearTerminal(){
        //Clears linux, macOS and Visual Studio terminal
        System.out.print("\033\143");

        //Clears Windows Terminal
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public boolean isValidInteger(String str) { 
        try {  
            Integer.valueOf(str);  
            return true;
        } catch(NumberFormatException e){  
            return false;  
        }  
    }

    public boolean isValidFloat(String str) { 
        try {  
            Float.valueOf(str);  
            return true;
        } catch(NumberFormatException e){  
            return false;  
        }  
    }
}
