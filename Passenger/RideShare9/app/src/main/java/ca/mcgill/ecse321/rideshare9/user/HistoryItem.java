package ca.mcgill.ecse321.rideshare9.user;

public class HistoryItem {
    private long id;
    private long advertisement;
    private long vehicle;
    private String start;
    private String time;

    public HistoryItem(long id, long advertisement, long vehicle, String start, String time) {
        this.id = id;
        this.advertisement = advertisement;
        this.vehicle = vehicle;
        this.start = start;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(long advertisement) {
        this.advertisement = advertisement;
    }

    public long getVehicle() {
        return vehicle;
    }

    public void setVehicle(long vehicle) {
        this.vehicle = vehicle;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
