package com.example.mobile_2703.model;

/**
 * Model class đại diện cho bảng "user" trong database.
 *
 * Quy tắc đặt tên:
 *   - Tên field Java: camelCase  (vd: fullName)
 *   - Tên cột DB:     snake_case (vd: full_name) - khai báo trong DBConstants
 */
public class User {

    private int    id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String role;
    private String createdAt;

    // =========================================================
    // CONSTRUCTORS
    // =========================================================

    public User() {}

    /** Constructor đầy đủ (dùng khi đọc từ database) */
    public User(int id, String username, String password,
                String fullName, String email, String role, String createdAt) {
        this.id        = id;
        this.username  = username;
        this.password  = password;
        this.fullName  = fullName;
        this.email     = email;
        this.role      = role;
        this.createdAt = createdAt;
    }

    /** Constructor tạo user mới (chưa có id và createdAt) */
    public User(String username, String password, String fullName, String email, String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email    = email;
        this.role     = role;
    }

    // =========================================================
    // GETTERS & SETTERS
    // =========================================================

    public int getId()                    { return id; }
    public void setId(int id)             { this.id = id; }

    public String getUsername()           { return username; }
    public void setUsername(String v)     { this.username = v; }

    public String getPassword()           { return password; }
    public void setPassword(String v)     { this.password = v; }

    public String getFullName()           { return fullName; }
    public void setFullName(String v)     { this.fullName = v; }

    public String getEmail()              { return email; }
    public void setEmail(String v)        { this.email = v; }

    public String getRole()               { return role; }
    public void setRole(String v)         { this.role = v; }

    public String getCreatedAt()          { return createdAt; }
    public void setCreatedAt(String v)    { this.createdAt = v; }

    // =========================================================
    // HELPERS
    // =========================================================

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', role='" + role + "'}";
    }
}
