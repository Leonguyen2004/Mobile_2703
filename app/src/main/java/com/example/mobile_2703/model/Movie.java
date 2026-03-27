package com.example.mobile_2703.model;

/**
 * Model tương ứng với bảng movies.
 */
public class Movie {

    private int    id;
    private String title;
    private String genre;
    private int    durationMin;
    private String description;
    private String posterUrl;
    private double rating;
    private String releaseDate;

    // =========================================================
    // Constructors
    // =========================================================
    public Movie() {}

    public Movie(String title, String genre, int durationMin,
                 String description, String posterUrl, double rating, String releaseDate) {
        this.title       = title;
        this.genre       = genre;
        this.durationMin = durationMin;
        this.description = description;
        this.posterUrl   = posterUrl;
        this.rating      = rating;
        this.releaseDate = releaseDate;
    }

    // =========================================================
    // Getters & Setters
    // =========================================================
    public int    getId()              { return id; }
    public void   setId(int id)        { this.id = id; }

    public String getTitle()           { return title; }
    public void   setTitle(String v)   { this.title = v; }

    public String getGenre()           { return genre; }
    public void   setGenre(String v)   { this.genre = v; }

    public int    getDurationMin()        { return durationMin; }
    public void   setDurationMin(int v)   { this.durationMin = v; }

    public String getDescription()        { return description; }
    public void   setDescription(String v){ this.description = v; }

    public String getPosterUrl()          { return posterUrl; }
    public void   setPosterUrl(String v)  { this.posterUrl = v; }

    public double getRating()             { return rating; }
    public void   setRating(double v)     { this.rating = v; }

    public String getReleaseDate()        { return releaseDate; }
    public void   setReleaseDate(String v){ this.releaseDate = v; }

    @Override
    public String toString() {
        return "Movie{id=" + id + ", title='" + title + "', genre='" + genre + "'}";
    }
}
