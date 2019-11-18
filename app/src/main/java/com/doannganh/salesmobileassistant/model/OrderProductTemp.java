package com.doannganh.salesmobileassistant.model;

public class OrderProductTemp {
    String ProductName;
    int Quantity;
    double UnitPrice;

    public OrderProductTemp(String productName, int quantity, double unitPrice) {
        ProductName = productName;
        Quantity = quantity;
        UnitPrice = unitPrice;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        UnitPrice = unitPrice;
    }
}
