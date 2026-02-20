public class OrderItem {
    private Item item;
    private int quantity;

    public OrderItem(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Price Calculating
    public double getTotalPrice() {
        return item.getSellingPrice() * quantity;
    }

    @Override
    public String toString() {
        return item.getName() + " x " + quantity + " = " + getTotalPrice();
    }
}
