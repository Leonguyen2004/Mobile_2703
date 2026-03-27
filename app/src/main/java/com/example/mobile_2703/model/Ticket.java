package com.example.mobile_2703.model;

/**
 * Model tương ứng với bảng tickets.
 * Các trường transient (movieTitle, showDate, showTime, theaterName) được nạp từ JOIN query trong TicketDAO.
 */
public class Ticket {

    private int    id;
    private int    userId;
    private int    showtimeId;
    private String seatNumber;
    private double totalPrice;
    private String bookingTime;
    private String status;

    // Transient fields - từ JOIN query
    private String movieTitle;
    private String showDate;
    private String showTime;
    private String theaterName;

    // =========================================================
    // Constructors
    // =========================================================
    public Ticket() {}

    public Ticket(int userId, int showtimeId, String seatNumber,
                  double totalPrice, String status) {
        this.userId      = userId;
        this.showtimeId  = showtimeId;
        this.seatNumber  = seatNumber;
        this.totalPrice  = totalPrice;
        this.status      = status;
    }

    // =========================================================
    // Getters & Setters
    // =========================================================
    public int    getId()                { return id; }
    public void   setId(int id)          { this.id = id; }

    public int    getUserId()            { return userId; }
    public void   setUserId(int v)       { this.userId = v; }

    public int    getShowtimeId()        { return showtimeId; }
    public void   setShowtimeId(int v)   { this.showtimeId = v; }

    public String getSeatNumber()        { return seatNumber; }
    public void   setSeatNumber(String v){ this.seatNumber = v; }

    public double getTotalPrice()        { return totalPrice; }
    public void   setTotalPrice(double v){ this.totalPrice = v; }

    public String getBookingTime()        { return bookingTime; }
    public void   setBookingTime(String v){ this.bookingTime = v; }

    public String getStatus()            { return status; }
    public void   setStatus(String v)    { this.status = v; }

    // Transient
    public String getMovieTitle()        { return movieTitle; }
    public void   setMovieTitle(String v){ this.movieTitle = v; }

    public String getShowDate()          { return showDate; }
    public void   setShowDate(String v)  { this.showDate = v; }

    public String getShowTime()          { return showTime; }
    public void   setShowTime(String v)  { this.showTime = v; }

    public String getTheaterName()        { return theaterName; }
    public void   setTheaterName(String v){ this.theaterName = v; }

    @Override
    public String toString() {
        return "Ticket{id=" + id + ", userId=" + userId
                + ", showtimeId=" + showtimeId + ", seat='" + seatNumber
                + "', status='" + status + "'}";
    }
}
