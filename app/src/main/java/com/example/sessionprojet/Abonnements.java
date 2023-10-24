package com.example.sessionprojet;

public class Abonnements {
    private int id;
    private String customerName;
    private String customerAddress;
    private int productId;
    private int quantity;
    private int routeId;

    public Abonnements(int id, String customerName, String customerAddress, int productId, int quantity, int routeId) {
        this.id = id;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.productId = productId;
        this.quantity = quantity;
        this.routeId = routeId;
    }

    public int getId() {
        return this.id;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public String getCustomerAddress() {
        return this.customerAddress;
    }

    public int getProductId() {
        return this.productId;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public int getRouteId() {
        return this.routeId;
    }
}
