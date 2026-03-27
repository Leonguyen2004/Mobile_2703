package com.example.mobile_2703.model;

public class Product {

    private int      id;
    private String   name;
    private double   price;
    private String   description;
    private String   imageUrl;
    private int      stock;
    private int      categoryId;
    private String   createdAt;

    // Transient field - không lưu vào DB, join từ bảng category
    private String   categoryName;

    public Product() {}

    public Product(int id, String name, double price, String description,
                   String imageUrl, int stock, int categoryId, String createdAt) {
        this.id          = id;
        this.name        = name;
        this.price       = price;
        this.description = description;
        this.imageUrl    = imageUrl;
        this.stock       = stock;
        this.categoryId  = categoryId;
        this.createdAt   = createdAt;
    }

    public Product(String name, double price, String description,
                   String imageUrl, int stock, int categoryId) {
        this.name        = name;
        this.price       = price;
        this.description = description;
        this.imageUrl    = imageUrl;
        this.stock       = stock;
        this.categoryId  = categoryId;
    }

    public int    getId()                        { return id; }
    public void   setId(int id)                  { this.id = id; }

    public String getName()                      { return name; }
    public void   setName(String name)           { this.name = name; }

    public double getPrice()                     { return price; }
    public void   setPrice(double price)         { this.price = price; }

    public String getDescription()               { return description; }
    public void   setDescription(String desc)    { this.description = desc; }

    public String getImageUrl()                  { return imageUrl; }
    public void   setImageUrl(String imageUrl)   { this.imageUrl = imageUrl; }

    public int    getStock()                     { return stock; }
    public void   setStock(int stock)            { this.stock = stock; }

    public int    getCategoryId()                { return categoryId; }
    public void   setCategoryId(int categoryId)  { this.categoryId = categoryId; }

    public String getCreatedAt()                 { return createdAt; }
    public void   setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getCategoryName()              { return categoryName; }
    public void   setCategoryName(String name)   { this.categoryName = name; }

    public boolean isInStock() {
        return stock > 0;
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + price + "}";
    }
}
