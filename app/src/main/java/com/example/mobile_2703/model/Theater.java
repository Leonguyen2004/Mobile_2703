package com.example.mobile_2703.model;

/**
 * Model tương ứng với bảng theaters.
 */
public class Theater {

    private int    id;
    private String name;
    private String location;
    private int    totalSeats;

    // =========================================================
    // Constructors
    // =========================================================
    public Theater() {}

    public Theater(String name, String location, int totalSeats) {
        this.name       = name;
        this.location   = location;
        this.totalSeats = totalSeats;
    }

    // =========================================================
    // Getters & Setters
    // =========================================================
    public int    getId()               { return id; }
    public void   setId(int id)         { this.id = id; }

    public String getName()             { return name; }
    public void   setName(String v)     { this.name = v; }

    public String getLocation()         { return location; }
    public void   setLocation(String v) { this.location = v; }

    public int    getTotalSeats()          { return totalSeats; }
    public void   setTotalSeats(int v)     { this.totalSeats = v; }

    @Override
    public String toString() {
        return "Theater{id=" + id + ", name='" + name + "', location='" + location + "'}";
    }
}
