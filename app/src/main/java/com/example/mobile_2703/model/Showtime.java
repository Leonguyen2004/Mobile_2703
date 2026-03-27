package com.example.mobile_2703.model;

/**
 * Model tương ứng với bảng showtimes.
 * Các trường transient (movieTitle, theaterName) được nạp từ JOIN query trong ShowtimeDAO.
 */
public class Showtime {

    private int    id;
    private int    movieId;
    private int    theaterId;
    private String showDate;
    private String showTime;
    private double price;
    private int    availableSeats;

    // Transient fields - từ JOIN query, không lưu trực tiếp vào bảng showtimes
    private String movieTitle;
    private String theaterName;

    // =========================================================
    // Constructors
    // =========================================================
    public Showtime() {}

    public Showtime(int movieId, int theaterId, String showDate,
                    String showTime, double price, int availableSeats) {
        this.movieId        = movieId;
        this.theaterId      = theaterId;
        this.showDate       = showDate;
        this.showTime       = showTime;
        this.price          = price;
        this.availableSeats = availableSeats;
    }

    // =========================================================
    // Getters & Setters
    // =========================================================
    public int    getId()                  { return id; }
    public void   setId(int id)            { this.id = id; }

    public int    getMovieId()             { return movieId; }
    public void   setMovieId(int v)        { this.movieId = v; }

    public int    getTheaterId()           { return theaterId; }
    public void   setTheaterId(int v)      { this.theaterId = v; }

    public String getShowDate()            { return showDate; }
    public void   setShowDate(String v)    { this.showDate = v; }

    public String getShowTime()            { return showTime; }
    public void   setShowTime(String v)    { this.showTime = v; }

    public double getPrice()               { return price; }
    public void   setPrice(double v)       { this.price = v; }

    public int    getAvailableSeats()      { return availableSeats; }
    public void   setAvailableSeats(int v) { this.availableSeats = v; }

    // Transient
    public String getMovieTitle()           { return movieTitle; }
    public void   setMovieTitle(String v)   { this.movieTitle = v; }

    public String getTheaterName()          { return theaterName; }
    public void   setTheaterName(String v)  { this.theaterName = v; }

    @Override
    public String toString() {
        return "Showtime{id=" + id + ", movieId=" + movieId
                + ", theaterId=" + theaterId + ", date='" + showDate
                + "', time='" + showTime + "'}";
    }
}
