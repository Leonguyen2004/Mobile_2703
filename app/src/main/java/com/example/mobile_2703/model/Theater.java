// FILE: app/src/main/java/com/example/mobile_2703/model/Theater.java
package com.example.mobile_2703.model;

public class Theater {

    private long   id;
    private String name;
    private String address;
    private String city;
    private int    totalSeats;

    public Theater() {}

    /** Constructor đầy đủ (kèm id — dùng khi đọc từ DB). */
    public Theater(long id, String name, String address, String city, int totalSeats) {
        this.id         = id;
        this.name       = name;
        this.address    = address;
        this.city       = city;
        this.totalSeats = totalSeats;
    }

    /** Constructor không id (dùng khi insert mới). */
    public Theater(String name, String address, String city, int totalSeats) {
        this.name       = name;
        this.address    = address;
        this.city       = city;
        this.totalSeats = totalSeats;
    }

    // =========================================================
    // Getters & Setters
    // =========================================================
    public long   getId()                 { return id; }
    public void   setId(long id)          { this.id = id; }

    public String getName()               { return name; }
    public void   setName(String v)       { this.name = v; }

    public String getAddress()            { return address; }
    public void   setAddress(String v)    { this.address = v; }

    public String getCity()               { return city; }
    public void   setCity(String v)       { this.city = v; }

    public int    getTotalSeats()         { return totalSeats; }
    public void   setTotalSeats(int v)    { this.totalSeats = v; }

    @Override
    public String toString() {
        return "Theater{id=" + id + ", name='" + name + "', address='" + address + "'}";
    }
}
