package com.example.younho.clinic.model;

public class Laundry {
    private int Id;
    private String Name;
    private double Lat;
    private double Lon;
    private String Address;

    public Laundry(int id, String name, double Lat, double Lon, String address)
    {
        this.Id = id;
        this.Name = name;
        this.Lat = Lat;
        this.Lon = Lon;
        this.Address = address;
    }

    public double getLat() {
        return Lat;
    }

    public double getLon() {
        return Lon;
    }

    public String getAddress() {
        return Address;
    }

    public String getName() {
        return Name;
    }

    public int getId() {
        return Id;
    }
}
