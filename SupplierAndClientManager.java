import java.util.ArrayList;

public class SupplierAndClientManager {
    private final ArrayList<SupplierOrClient> suppliersAndClients;

    public SupplierAndClientManager(ArrayList<SupplierOrClient> _suppliersAndClients){
        suppliersAndClients = _suppliersAndClients;
    }

    public void AddSupplierOrClient(SupplierOrClient supplierOrClient){
        suppliersAndClients.add(supplierOrClient);
    }

    public SupplierOrClient GetSupplierOrClientByName(String supplierName){
        for (int i = 0; i < suppliersAndClients.size(); i++) {
            if (suppliersAndClients.get(i).GetName().toLowerCase().equals(supplierName.toLowerCase())){
                return suppliersAndClients.get(i);
            }
        }
        return null;
    }

    public SupplierOrClient GetSupplierOrClientByID(int ID){
        for (int i = 0; i < suppliersAndClients.size(); i++) {
            if (suppliersAndClients.get(i).GetID() == ID){
                return suppliersAndClients.get(i);
            }
        }
        return null;
    }

    public ArrayList<SupplierOrClient> GetSuppliersAndClients(){
        return suppliersAndClients;
    }

    public void RemoveSupplierOrClient(SupplierOrClient supplierOrClient){
        suppliersAndClients.remove(supplierOrClient);
    }
}
