public class CartItem extends menuItem{
    private int Quantity;    
    public CartItem(String productName, float productPrice) {
        super(productName, productPrice);
        this.Quantity = 1;
    }

    public int getQuantity() {
        return Quantity;
    }
    public void addToQuantity() {
        this.Quantity += 1;
    }
    public float getAccumulatedPrice() {
        return this.getPrice() * Quantity;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CartItem cartitem = (CartItem) obj;
        return getName().equals(cartitem.getName());
    }
}
