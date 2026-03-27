package com.example.mobile_2703.model;

/**
 * Model tương ứng với bảng users.
 */
public class User {

    private int    id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private String createdAt;

    // =========================================================
    // Constructors
    // =========================================================
    public User() {}

    public User(String username, String password, String fullName,
                String email, String phone, String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email    = email;
        this.phone    = phone;
        this.role     = role;
    }

    // =========================================================
    // Getters & Setters
    // =========================================================
    public int    getId()        { return id; }
    public void   setId(int id)  { this.id = id; }

    public String getUsername()           { return username; }
    public void   setUsername(String v)   { this.username = v; }

    public String getPassword()           { return password; }
    public void   setPassword(String v)   { this.password = v; }

    public String getFullName()           { return fullName; }
    public void   setFullName(String v)   { this.fullName = v; }

    public String getEmail()              { return email; }
    public void   setEmail(String v)      { this.email = v; }

    public String getPhone()              { return phone; }
    public void   setPhone(String v)      { this.phone = v; }

    public String getRole()               { return role; }
    public void   setRole(String v)       { this.role = v; }

    public String getCreatedAt()          { return createdAt; }
    public void   setCreatedAt(String v)  { this.createdAt = v; }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', role='" + role + "'}";
    }
}
