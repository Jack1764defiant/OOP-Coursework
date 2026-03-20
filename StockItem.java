public class StockItem{
    private final String itemName;
    private int amount;
    private final float individualCost;

    public StockItem(String _itemName, int _amount, float _individualCost){
        itemName = _itemName;
        amount = _amount;
        individualCost = _individualCost;
    }

    public String GetItemName(){
        return itemName;
    }

    public int GetAmount(){
        return amount;
    }

    public void SetAmount(int newAmount) throws StockException{
        if (newAmount >= 0){
            amount = newAmount;
        }
        else{
            throw new StockException("Invalid amount of stock - less than 0 (" + String.valueOf(newAmount) + ")");
        }
    }

    public float GetIndividualCost(){
        return individualCost;
    }

    public float GetTotalCost(){
        return individualCost * amount;
    }
}


