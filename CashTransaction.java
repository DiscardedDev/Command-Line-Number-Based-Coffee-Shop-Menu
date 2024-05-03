import java.util.ArrayList;

public class CashTransaction extends Transaction{
    private float cashTendered;
    private float changeGiven;

    public CashTransaction(float price, ArrayList<CartItem> itemsPurchased, float cashTendered, float changeGiven) {
        super(price, itemsPurchased);
        this.cashTendered = cashTendered;
        this.changeGiven = changeGiven;
    }

    public float getCashTendered() {
        return cashTendered;
    }
    public float getChangeGiven() {
        return changeGiven;
    }
}


