public class StockItem{
    private final String itemName;
    private int amount;
    private final float individualCost;

    public StockItem(String _itemName, int _amount, float _individualCost){
        itemName = _itemName;
        amount = _amount;
        individualCost = _individualCost;
    }

    /**
     * <p>
     * Getter for itemName
     * </p>
     * @return the value of itemName
     */
    public String GetItemName(){
        return itemName;
    }

    /**
     * <p>
     * Getter for amount
     * </p>
     * @return the quantity of this item
     */
    public int GetAmount(){
        return amount;
    }

    /**
     * <p>
     * Setter for amount, cannot go below 0 - throws a stockexception if you try to
     * </p>
     * @param newAmount the new quantity of this item
     */
    public void SetAmount(int newAmount) throws StockException{
        if (newAmount >= 0){
            amount = newAmount;
        }
        else{
            throw new StockException("Invalid amount of stock - less than 0 (" + String.valueOf(newAmount) + ")");
        }
    }

    /**
     * <p>
     * Getter for invidualCost
     * </p>
     * @return the cost of one of this item
     */
    public float GetIndividualCost(){
        return individualCost;
    }

    /**
     * <p>
     * Calculate the total cost the of quantity of this item
     * </p>
     * @return the cost of 1 of this item multiplied by the amount of this item
     */
    public float GetTotalCost(){
        return individualCost * amount;
    }
}


