package ca.mcgill.ecse321.rideshare9.user;

import java.util.List;

public class Advertisement {

    private int id;
    private int availableSeats;
    private int vehicleId;
    private int driverId;
    private String title;
    private String startTime;
    private String startLocation;
    private String status;
    private List<Stop> stops;

    public Advertisement() {
    }

    public Advertisement(int id, int availableSeats, int vehicleId, int driverId, String title,
                         String startTime, String startLocation, String status, List<Stop> stops) {
        this.id = id;
        this.availableSeats = availableSeats;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.title = title;
        this.startTime = startTime;
        this.startLocation = startLocation;
        this.status = status;
        this.stops = stops;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public void addStop(Stop stop) {
        stops.add(stop);
    }
}
