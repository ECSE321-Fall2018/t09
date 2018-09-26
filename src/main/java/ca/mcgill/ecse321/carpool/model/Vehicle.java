package ca.mcgill.ecse321.carpool.model;

import javax.persistence.Entity;
import java.util.Set;
import javax.persistence.OneToMany;

@Entity
public class Vehicle{
   private String licensePlate;

public void setLicensePlate(String value) {
    this.licensePlate = value;
}
public String getLicensePlate() {
    return this.licensePlate;
}
private String model;

public void setModel(String value) {
    this.model = value;
}
public String getModel() {
    return this.model;
}
private String color;

public void setColor(String value) {
    this.color = value;
}
public String getColor() {
    return this.color;
}
private Integer maximumPassengerSeats;

public void setMaximumPassengerSeats(Integer value) {
    this.maximumPassengerSeats = value;
}
public Integer getMaximumPassengerSeats() {
    return this.maximumPassengerSeats;
}
   private Set<Trip> trip;
   
   @OneToMany(mappedBy="vehicle" )
   public Set<Trip> getTrip() {
      return this.trip;
   }
   
   public void setTrip(Set<Trip> trips) {
      this.trip = trips;
   }
   
   }
