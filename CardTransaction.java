import java.util.ArrayList;

public class CardTransaction extends Transaction{
    private String cardType;

    public CardTransaction(float price, ArrayList<CartItem> itemsPurchased, int cardType) {
        super(price, itemsPurchased);
        if (cardType == 1){
            this.cardType = "Visa";
        }
        else if (cardType == 2){
            this.cardType = "MasterCard";
        }
    }

    public String getCardType() {
        return cardType;
    }


}