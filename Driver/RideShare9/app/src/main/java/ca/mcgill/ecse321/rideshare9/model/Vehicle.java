package ca.mcgill.ecse321.rideshare9.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Vehicle implements Parcelable {

    private long id;
    private String licencePlate;
    private String model;
    private String color;
    private int maxSeat;
    private long driver;

    public static final Parcelable.Creator<Vehicle> CREATOR = new Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel source) {
            return new Vehicle(source);
        }
            @Override
            public Vehicle[] newArray(int size) {
                return new Vehicle[size];
            }
        };

    public Vehicle()
    {

    }

    public Vehicle(long id, String licencePlate, String model, String color, int maxSeat, long driver) {
        this.id = id;
        this.licencePlate = licencePlate;
        this.model = model;
        this.color = color;
        this.maxSeat = maxSeat;
        this.driver = driver;
    }

    protected Vehicle(Parcel in) {
        id = in.readLong();
        licencePlate = in.readString();
        model = in.readString();
        color = in.readString();
        maxSeat = in.readInt();
        driver = in.readLong();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(licencePlate);
        dest.writeString(model);
        dest.writeString(color);
        dest.writeInt(maxSeat);
        dest.writeLong(driver);
    }
}
