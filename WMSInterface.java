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

    public void DisplayMenu(){
        ClearTerminal();
        System.out.println("WMS Menu");
        System.out.println("1. Exit WMS interface.");
        System.out.println("2. Suppliers and Clients");
        System.out.println("3. Orders");
        System.out.println("4. Inventory");
        System.out.println("5. Financial");
        if (displayLowStockAlerts){
            //PrintLowStockAlerts();
        }
        String menuChoice = Input(scanner, "Enter option: ");
        switch (menuChoice) {
            case "1" -> {
                running = false;
                break;
            }
            case "2" -> {
                //HandleSuppliersAndClientsMenu(scanner);
                break;
            }
            case "3" -> {
                //HandleOrderMenu(scanner);
                break;
            }
            case "4" -> {
                //HandleInventoryMenu(scanner);
                break;
            }
            case "5" -> {
                //HandleFinancialMenu(scanner);
                break;
            }
            default -> {
                System.out.println(menuChoice + " is not a known option.");
                scanner.nextLine();
                break;
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

    public void RunWMSInterface(){
        while (running){
            DisplayMenu();
        }
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
