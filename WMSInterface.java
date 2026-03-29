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
            String menuChoice = Input(scanner, "Enter option: ");
            switch (menuChoice) {
                case "1" -> {
                    inMenu = false;
                    break;
                }
                case "2" -> {
                    //GenerateExpenseReportInPeriod(scanner);
                    break;
                }
                case "3" -> {
                    //GenerateSalesReportInPeriod(scanner);
                    break;
                }
                case "4" -> {
                    //GenerateProfitReportInPeriod(scanner);
                    break;
                }
                case "5" -> {
                    //CheckRemainingBudget(scanner);
                    break;
                }
                case "6" -> {
                    //TopUpBudget(scanner);
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
                    //CreateSupplierOrClient(scanner);
                    break;
                }
                case "3" -> {
                    //EditSupplierOrClient(scanner);
                    break;
                }
                case "4" -> {
                    //DeleteSupplierOrClient(scanner);
                    break;
                }
                case "5" -> {
                    //ViewSupplierOrClient(scanner);
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


    public void HandleOrderMenu(Scanner scanner){
        boolean inOrderMenu = true;
        while (inOrderMenu){
            ClearTerminal();
            System.out.println("1. Exit Order Menu");
            System.out.println("2. Place Order");
            System.out.println("3. Update Order Status");
            System.out.println("3. Update Order Status");
            System.out.println("4. View Order");
            String menuChoice = Input(scanner, "Enter option: ");
            switch (menuChoice) {
                case "1" -> {
                    inOrderMenu = false;
                    break;
                }
                case "2" -> {
                    //PlaceOrder(scanner);
                    break;
                }
                case "3" -> {
                    //UpdateOrderStatus(scanner);
                    break;
                }
                case "4" -> {
                    //ViewOrder(scanner);
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
                    //CheckItemStock(scanner);
                    break;
                }
                case "3" -> {
                    //UpdateItemStock(scanner);
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
