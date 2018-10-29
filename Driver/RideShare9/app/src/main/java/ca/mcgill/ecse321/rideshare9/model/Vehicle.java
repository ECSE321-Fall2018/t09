package ca.mcgill.ecse321.rideshare9.model;

public class Vehicle {

    private long id;
    private String licencePlate;
    private String model;
    private String color;
    private int maxSeat;
    private long driver;

    public Vehicle(){

    }

    public Vehicle(long id, String licencePlate, String model, String color, int maxSeat, long driver) {
        this.id = id;
        this.licencePlate = licencePlate;
        this.model = model;
        this.color = color;
        this.maxSeat = maxSeat;
        this.driver = driver;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getMaxSeat() {
        return maxSeat;
    }

    public void setMaxSeat(int maxSeat) {
        this.maxSeat = maxSeat;
    }

    public long getDriver() {
        return driver;
    }

    public void setDriver(long driver) {
        this.driver = driver;
    }
}
