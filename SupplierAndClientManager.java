import java.util.ArrayList;

public class SupplierAndClientManager {
    private final ArrayList<SupplierOrClient> suppliersAndClients;

    public SupplierAndClientManager(ArrayList<SupplierOrClient> _suppliersAndClients){
        suppliersAndClients = _suppliersAndClients;
    }

    /**
     * <p>
     * Add a supplier or client to the supplierAndCLientManager's internal arraylist
     * </p>
     * @param supplierOrClient the supplier or client you want added
     */
    public void AddSupplierOrClient(SupplierOrClient supplierOrClient){
        suppliersAndClients.add(supplierOrClient);
    }

    /**
     * <p>
     * Get a supplier from the internal list by name, null if not found
     * </p>
     * @param supplierName - the name of the supplier you want
     * @return the supplier matching the name, or null
     */
    public SupplierOrClient GetSupplierOrClientByName(String supplierName){
        for (int i = 0; i < suppliersAndClients.size(); i++) {
            // make both names lower case to avoid issues where the name has been typed with the wrong casing
            if (suppliersAndClients.get(i).GetName().toLowerCase().equals(supplierName.toLowerCase())){
                return suppliersAndClients.get(i);
            }
        }
        return null;
    }

    /**
     * <p>
     * Get a supplier from the internal list by id, null if not found
     * </p>
     * @param ID - the id of the supplier you want
     * @return the supplier matching the id, or null
     */
    public SupplierOrClient GetSupplierOrClientByID(int ID){
        for (int i = 0; i < suppliersAndClients.size(); i++) {
            if (suppliersAndClients.get(i).GetID() == ID){
                return suppliersAndClients.get(i);
            }
        }
        return null;
    }

    /**
     * <p>
     * Getter for suppliersAndClients
     * </p>
     * @return the contents of suppliersAndClients
     */
    public ArrayList<SupplierOrClient> GetSuppliersAndClients(){
        return suppliersAndClients;
    }

    /**
     * <p>
     * Remove a supplier or client from the internal list
     * </p>
     * @param supplierOrClient - the supplier or client you want removed
     */
    public void RemoveSupplierOrClient(SupplierOrClient supplierOrClient){
        suppliersAndClients.remove(supplierOrClient);
    }
}
