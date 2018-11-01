package ca.mcgill.ecse321.rideshare9;

public class VehicleItem {

    private Long id;
    private String model;
    private String licencePlate;
    private String color;
    private Integer maxSeat;
    public VehicleItem() {};
    public VehicleItem(long id, String model) {
        this.id = id;
        this.model = model;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getModel() {
        return model;
    }
    public Long getId() {
        return id;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getMaxSeat() {
        return maxSeat;
    }

    public void setMaxSeat(Integer maxSeat) {
        this.maxSeat = maxSeat;
    }
}
