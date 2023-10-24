package com.example.sessionprojet;

public class Livreur {
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private int routeNumber;

    public Livreur(String name, String address, String phoneNumber, int routeNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.routeNumber = routeNumber;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public int getRouteNumber() {
        return routeNumber;
    }
}
