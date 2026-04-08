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
        // Example values used to make testing easier
        ArrayList<StockItem> inventoryContents = new ArrayList<>();
        inventoryContents.add(new StockItem("RAM", 5, 200));
        ArrayList<SupplierOrClient> suppliersAndClients = new ArrayList<>();
        suppliersAndClients.add(new SupplierOrClient(2, "Jeff's RAM", "jeff@jeffsram.ac.uk", "07473462346"));
        
        inventoryManager = new InventoryManager(inventoryContents, 10);
        supplierAndClientManager = new SupplierAndClientManager(suppliersAndClients);
    }

    /**
     * <p>
     * Continously display the main menu
     * </p>
     */
    public void RunWMSInterface(){
        while (running){
            DisplayMenu();
        }
    }

    /**
     * <p>
     * Display the main menu, take in the user's input, and select an option based on their input
     * </p>
     */
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

    /**
     * <p>
     * Print each item which as an amount fewer than the lowStockThreshold in the inventoryManager
     * </p>
     */
    public void PrintLowStockAlerts(){
        ArrayList<StockItem> LowStockItems = inventoryManager.CheckForLowStock();
        for (StockItem lowStockItem : LowStockItems){
            System.out.println("Low on " + lowStockItem.GetItemName() + " - only " + String.valueOf(lowStockItem.GetAmount()) + " remain.");
        }
        if (LowStockItems.isEmpty()){
            System.out.println("No items low on stock.");
        }
    }

    /**
     * <p>
     * Print the financial sub menu, and take in user input and process which option they selected
     * </p>
     * @param scanner - the scanner to use to take in user input
     */
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

    /**
     * <p>
     * Print the amount of budget. Press newline to return to the menu
     * </p>
     * @param scanner - the scanner to use to take in user input
     */
    public void CheckRemainingBudget(Scanner scanner){
        System.out.printf("Remaining budget for inventory: £%,.2f", inventoryManager.GetRemainingBudget());
        System.out.println();
        scanner.nextLine();
    }

    /**
     * <p>
     * Print the amount of profit. Press newline to return to the menu
     * </p>
     * @param scanner - the scanner to use to take in user input
     */
    public void CheckSurplusMoney(Scanner scanner){
        System.out.printf("Surplus money: £%,.2f", inventoryManager.GetCurrentProfits());
        System.out.println();
        scanner.nextLine();
    }

    /**
     * <p>
     * Take in a numeric amount from the user to transfer from profit to budget
     * </p>
     * @param scanner - the scanner to use to take in user input
     */
    public void TopUpBudget(Scanner scanner){
        System.out.printf("Enter amount of money to transfer from profits to inventory budget (up to £%,.2f): £", inventoryManager.GetCurrentProfits());
        String transferAmount = scanner.nextLine();
        while (!isValidFloat(transferAmount) || Float.parseFloat(transferAmount) > inventoryManager.GetCurrentProfits() || Float.parseFloat(transferAmount) < 0){
            System.out.print("Invalid. Enter amount to transfer: £");
            transferAmount = scanner.nextLine();
        }
        inventoryManager.PayIntoBudget(Float.parseFloat(transferAmount));
    }

    /**
     * <p>
     * Take in a date from the user in the order year, month, day
     * </p>
     * @param scanner - the scanner to use to take in user input
     * @return the date the user entered
     */
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

    /**
     * <p>
     * Takes in two dates, and prints a report of the outgoings between those dates
     * </p>
     * @param scanner - the scanner to use to take in user input
     */
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

    /**
     * <p>
     * Takes in two dates, and prints a report of the income between those dates
     * </p>
     * @param scanner - the scanner to use to take in user input
     */
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

    /**
     * <p>
     * Takes in two dates, and prints a report of the net profit between those dates
     * </p>
     * @param scanner - the scanner to use to take in user input
     */
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

    /**
     * <p>
     * Print the suppliers and clients sub menu, and take in user input and process which option they selected
     * </p>
     * @param scanner - the scanner to use to take in user input
     */
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


    /**
     * <p>
     * Take in an ID, name, email address and phone number from the user, create a supplierOrCLient from that and add it to the supplierOrClientManager
     * </p>
     * @param scanner - the scanner to use to take in user input
     */
    public void CreateSupplierOrClient(Scanner scanner){
        int ID = IntInput(scanner, "Enter a unique ID number for the supplier or client: ");
        String name = Input(scanner, "Enter name: ");
        String emailAddress = Input(scanner, "Enter email address: ");
        String phoneNumber = Input(scanner, "Enter phone number: ");
        SupplierOrClient newSupplier = new SupplierOrClient(ID, name, emailAddress, phoneNumber);
        supplierAndClientManager.AddSupplierOrClient(newSupplier);
    }

    /**
     * <p>
     * Takes in a name from the user, and returns the client/supplier that matches that name
     * </p>
     * @param scanner - the scanner to use to take in user input
     * @return the client/supplier that matches the name the user entered
     */
    public SupplierOrClient GetSupplierOrClient(Scanner scanner){
        String name = Input(scanner, "Enter name: ");
        return supplierAndClientManager.GetSupplierOrClientByName(name);
    }

    /**
     * <p>
     * Select a supplier/client by name, then enter a new name, email address and phone number for them
     * </p>
     * @param scanner - the scanner to use to take in user input
     */
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

    /**
     * <p>
     * Takes in the name of a supplier/client, and deletes them if a match is found. Otherwise prints that no match is found.
     * </p>
     * @param scanner - the scanner to use to take in user input
     */
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


    /**
     * <p>
     * Gets the name of a client/supplier from the user and prints all information about them
     * </p>
     * @param scanner - the scanner to use to take in user input
     */
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


    /**
     * <p>
     * Print the orders sub menu, and take in user input and process which option they selected
     * </p>
     * @param scanner - the scanner to use to take in user input
     */
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

    /**
     * <p>
     * Takes in a supplier's name and whether this is a sale or a purchase
     * Then it takes in the names, costs and quantities of items repeatedly until the user enters x
     * Then it creates an order from that data, and adds it to the supplier's orderhistory
     * </p>
     * @param scanner - the scanner to use to take in user input
     */
    public void PlaceOrder(Scanner scanner){
        ClearTerminal();
        String supplierName = Input(scanner, "Enter the supplier or client's name being ordered from or sent to: ");
        SupplierOrClient supplierToOrderFrom = supplierAndClientManager.GetSupplierOrClientByName(supplierName);
        // Check the supplier actually exists
        if (supplierToOrderFrom != null){
            String saleOrOrder = Input(scanner, "Enter S if this is a sale, or P if this is a purchase: ");
            while (!saleOrOrder.toLowerCase().equals("p") && !saleOrOrder.toLowerCase().equals("s")){
                saleOrOrder = Input(scanner, "Invalid. Enter S if this is a sale, or P if this is a purchase: ");
            }
            boolean sale = saleOrOrder.toLowerCase().equals("s");
            boolean addingItemsToOrder = true;
            ArrayList<StockItem> itemsToOrder = new ArrayList<>();
            //Allow them to add an indefinite number of items to the order
            while (addingItemsToOrder){
                ClearTerminal();
                String item = Input(scanner, "Enter the item to order or x to quit: ");
                if (item.toLowerCase().equals("x")){
                    addingItemsToOrder = false;
                }
                // If this is a sale, check we actually have the stock to sell
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

    
    /**
     * <p>
     * Select an order using the selectOrderOnDate function, then takes in a number 1-4 to set the order's status
     * 1-placed, 2-in transit, 3-recieved-if this is a purchase, adds contents to inventory, 4-cancelled
     * </p>
     * @param scanner - the scanner to use to take in user input
     */
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

    /**
     * <p>
     * Gets an order using the selectOrderOnDate function, then prints all details about it
     * </p>
     * @param scanner - the scanner to use to take in user input
     */
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

    /**
     * <p>
     * Takes in a name from the user, used to find a supplier/client 
     * </p>
     * @param scanner - the scanner to use to take in user input
     * @return the supplier or client matching the name the user provided
     */
    public SupplierOrClient SelectSupplierOrClientForOrder(Scanner scanner){
        String supplierName = Input(scanner, "Enter the supplier or client's name the order was placed with: ");
        SupplierOrClient orderedFrom = supplierAndClientManager.GetSupplierOrClientByName(supplierName);

        while (orderedFrom == null){

            supplierName = Input(scanner, "Invalid. Enter the supplier or client's name the order was placed with: ");
            orderedFrom = supplierAndClientManager.GetSupplierOrClientByName(supplierName);
            
        }
        return orderedFrom;
    }


    /**
     * <p>
     * Takes in a date from the user, then prints all orders on that date, then allows them to select one, which it returns
     * </p>
     * @param scanner - the scanner to use to take in user input
     * @param supplierToOrderFrom - the supplier or client who's orderhistory we are searching for an order
     * @return the order the user selects
     */
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


    /**
     * <p>
     * Print the inventory sub menu, and take in user input and process which option they selected
     * </p>
     * @param scanner - the scanner to use to take in user input
     */
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

    /**
     * <p>
     * Takes in the name of a stock item from the user, then prints the amount of that item in the inventory
     * </p>
     * @param scanner - the scanner to use to take in user input
     */
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

    
    /**
     * <p>
     * Takes in the name of a stock item from the user, then how many of that item they want to be in stock
     * </p>
     * @param scanner - the scanner to use to take in user input
     */
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


    /**
     * <p>
     * Print a prompt and take in the user's response
     * </p>
     * @param scanner - the scanner to use to take in user input
     * @param prompt - the prompt to display to the user 
     * @return the string the user entered
     */
    public String Input(Scanner scanner, String prompt){
        System.out.print(prompt);
        return scanner.nextLine();
    }

    /**
     * <p>
     * Print a prompt and take in the user's response as an intenger, checking that the value is a valid integer before excepting it
     * </p>
     * @param scanner - the scanner to use to take in user input
     * @param prompt - the prompt to display to the user 
     * @return the number the user entered
     */
    public int IntInput(Scanner scanner, String prompt){
        String value = Input(scanner, prompt);
        while (!isValidInteger(value)){
            value = Input(scanner, "Invalid. " + prompt);
        }
        return Integer.parseInt(value);
    }

    /**
     * <p>
     * Print a prompt and take in the user's response as an intenger, checking that the value is a valid integer and is between the lower and upper bounds provided before excepting it
     * </p>
     * @param scanner - the scanner to use to take in user input
     * @param prompt - the prompt to display to the user
     * @param lowerBound - the lowest acceptable number
     * @param upperBound - the highest acceptable number 
     * @return the number the user entered
     */
    public int IntInput(Scanner scanner, String prompt, int lowerBound, int upperBound){
        String value = Input(scanner, prompt);
        while (!isValidInteger(value) || Integer.parseInt(value) < lowerBound || Integer.parseInt(value) > upperBound){
            value = Input(scanner, "Invalid. " + prompt);
        }
        return Integer.parseInt(value);
    }

    /**
     * <p>
     * Print a prompt and take in the user's response as an float, checking that the value is a valid float before excepting it
     * </p>
     * @param scanner - the scanner to use to take in user input
     * @param prompt - the prompt to display to the user 
     * @return the number the user entered
     */
    public float FloatInput(Scanner scanner, String prompt){
        String value = Input(scanner, prompt);
        while (!isValidFloat(value)){
            value = Input(scanner, "Invalid. " + prompt);
        }
        return Float.parseFloat(value);
    }


    /**
     * <p>
     * Clear the terminal/command prompt
     * </p>
     */
    public void ClearTerminal(){
        //Clears linux, macOS and Visual Studio terminal
        System.out.print("\033\143");

        //Clears Windows Terminal
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * <p>
     * Check if a provided string is a valid integer
     * </p>
     * @param string - the string we want to check
     * @return whether the string is a valid integer
     */
    public boolean isValidInteger(String string) { 
        try {  
            Integer.valueOf(string);  
            return true;
        } catch(NumberFormatException e){  
            return false;  
        }  
    }

    /**
     * <p>
     * Check if a provided string is a valid floating point number
     * </p>
     * @param string - the string we want to check
     * @return whether the value is a valid integer
     */
    public boolean isValidFloat(String string) { 
        try {  
            Float.valueOf(string);  
            return true;
        } catch(NumberFormatException e){  
            return false;  
        }  
    }
}
