package ca.mcgill.ecse321.rideshare9.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class Advertisement implements Parcelable {
    private long id;
    private int availableSeats;

    public static final Creator<Advertisement> CREATOR = new Creator<Advertisement>() {
        @Override
        public Advertisement createFromParcel(Parcel in) {
            return new Advertisement(in);
        }

        @Override
        public Advertisement[] newArray(int size) {
            return new Advertisement[size];
        }
    };

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    private Vehicle vehicle;
    private int driverId;
    private String title;
    private String startTime;
    private String startLocation;
    private String status;
    private List<Stop> stops;

    public Advertisement() {
    }

    public Advertisement(Parcel parcel){
        this.id = parcel.readLong();
        this.availableSeats = parcel.readInt();
        this.vehicle = parcel.readParcelable(Vehicle.class.getClassLoader());
        this.driverId = parcel.readInt();
        this.title = parcel.readString();
        this.startTime = parcel.readString();
        this.startLocation = parcel.readString();
        this.status = parcel.readString();
        this.stops = new ArrayList<>();
        this.stops = parcel.readArrayList(Stop.class.getClassLoader());
    }

    public Advertisement(int id, int availableSeats, Vehicle vehicle, int driverId, String title,
                         String startTime, String startLocation, String status, List<Stop> stops) {
        this.id = id;
        this.availableSeats = availableSeats;
        this.vehicle = vehicle;
        this.driverId = driverId;
        this.title = title;
        this.startTime = startTime;
        this.startLocation = startLocation;
        this.status = status;
        this.stops = stops;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(availableSeats);
        dest.writeParcelable(vehicle, flags);
        dest.writeInt(driverId);
        dest.writeString(title);
        dest.writeString(startTime);
        dest.writeString(startLocation);
        dest.writeString(status);
        dest.writeTypedList(stops);
    }
}
