import java.util.Scanner;
import java.util.UUID;

public class Item {
    private String uniqueId;
    private String name;
    private String description;
    private double incomePrice;
    private double sellingPrice;
    private String barcode;

    public Item() {

    }

    public Item(String name, String description, double incomePrice, double sellingPrice, String barcode) {
        this.uniqueId = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.incomePrice = incomePrice;
        this.sellingPrice = sellingPrice;
        this.barcode = barcode;
    }

    // Getting ID
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    // Setting and Getting ID
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Setting and Getting Description
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // Setting and Setting Income Price
    public void setIncomePrice(double incomePrice) {
        this.incomePrice = incomePrice;
    }

    public double getIncomePrice() {
        return incomePrice;
    }

    // Setting and Getting Selling Price
    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    // Setting and Getting Barcode
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBarcode() {
        return barcode;
    }

    @Override
    public String toString() {
        return name + " " + " - " + " " + sellingPrice + "USD";
    }
}