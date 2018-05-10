package com.bagirapp.bookstore.Data;

public class Product {
    private int _ID;
    private String mName;
    private String mPrice;
    private int mQuantity;
    private String mSupplierName;
    private String mSupplierPhone;

    public Product(int _ID, String mName, String mPrice, int mQuantity) {
        this._ID = _ID;
        this.mName = mName;
        this.mPrice = mPrice;
        this.mQuantity = mQuantity;
        mSupplierName = "Unknown";
        mSupplierPhone = "Unknown";
    }

    public Product(String mName, String mPrice, int mQuantity, String mSupplierName, String mSupplierPhone) {
        this.mName = mName;
        this.mPrice = mPrice;
        this.mQuantity = mQuantity;
        this.mSupplierName = mSupplierName;
        this.mSupplierPhone = mSupplierPhone;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public void setmQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    public void setmSupplierName(String mSupplierName) {
        this.mSupplierName = mSupplierName;
    }

    public void setmSupplierPhone(String mSupplierPhone) {
        this.mSupplierPhone = mSupplierPhone;
    }

    public String getmPrice() {
        return mPrice;
    }

    public int getmQuantity() {
        return mQuantity;
    }

    public String getmSupplierName() {
        return mSupplierName;
    }

    public String getmSupplierPhone() {
        return mSupplierPhone;
    }
}
