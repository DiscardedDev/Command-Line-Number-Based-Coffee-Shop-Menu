import java.util.ArrayList;

public class Cart{
    private ArrayList<CartItem> cartItems = new ArrayList<CartItem>();
    //add product 
    public void addItem(menuItem item) {
        CartItem itemtobeadded = new CartItem(item.getName(), item.getPrice());
        //check if item is already in cart
        if(cartItems.contains(itemtobeadded)){
            //increase quantity
            System.out.println("item already in cart");
            cartItems.get(cartItems.indexOf(itemtobeadded)).addToQuantity();
        }
        else{
            cartItems.add(new CartItem(item.getName(), item.getPrice()));
        }
    }
    
    public float getTotalCost(){
        float totalCost = 0;
        for(int i = 0; i < cartItems.size(); i++){
            totalCost += cartItems.get(i).getAccumulatedPrice();
        }
        return totalCost;
    }
    public int cartSize() {
        return cartItems.size();
    }
    
    public int getItemQuantity(int index) {
        return cartItems.get(index).getQuantity();
    }

    public String getItemName(int index) {
        return cartItems.get(index).getName();
    }

    public float getItemAccumulatedPrice(int index) {
        return cartItems.get(index).getAccumulatedPrice();
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }
}