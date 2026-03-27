// FILE: app/src/main/java/com/example/mobile_2703/model/Movie.java
package com.example.mobile_2703.model;

public class Movie {

    private long   id;
    private String title;
    private String genre;
    private int    duration;
    private String description;
    private String posterUrl;
    private double rating;

    public Movie() {}

    /** Constructor đầy đủ (kèm id — dùng khi đọc từ DB). */
    public Movie(long id, String title, String genre, int duration,
                 String description, String posterUrl, double rating) {
        this.id          = id;
        this.title       = title;
        this.genre       = genre;
        this.duration    = duration;
        this.description = description;
        this.posterUrl   = posterUrl;
        this.rating      = rating;
    }

    /** Constructor không id (dùng khi insert mới). */
    public Movie(String title, String genre, int duration,
                 String description, String posterUrl, double rating) {
        this.title       = title;
        this.genre       = genre;
        this.duration    = duration;
        this.description = description;
        this.posterUrl   = posterUrl;
        this.rating      = rating;
    }

    // =========================================================
    // Getters & Setters
    // =========================================================
    public long   getId()              { return id; }
    public void   setId(long id)       { this.id = id; }

    public String getTitle()           { return title; }
    public void   setTitle(String v)   { this.title = v; }

    public String getGenre()           { return genre; }
    public void   setGenre(String v)   { this.genre = v; }

    public int    getDuration()        { return duration; }
    public void   setDuration(int v)   { this.duration = v; }

    public String getDescription()           { return description; }
    public void   setDescription(String v)   { this.description = v; }

    public String getPosterUrl()             { return posterUrl; }
    public void   setPosterUrl(String v)     { this.posterUrl = v; }

    public double getRating()                { return rating; }
    public void   setRating(double v)        { this.rating = v; }

    @Override
    public String toString() {
        return "Movie{id=" + id + ", title='" + title + "', genre='" + genre + "'}";
    }
}
