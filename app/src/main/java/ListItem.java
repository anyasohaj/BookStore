public class ListItem {
    private String productName;
    private String price;
    private String quantity;
    private String supplierName;
    private String supplierPhone;

    public ListItem(String productName, String price, String quantity, String supplierName, String supplierPhone) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.supplierName = supplierName;
        this.supplierPhone = supplierPhone;
    }

    public String getProductName() {
        return productName;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }
}
