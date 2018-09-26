package ca.mcgill.ecse321.carpool.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Set;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.sql.Time;
import java.sql.Date;

@Entity
public class Trip{
   private String name;

public void setName(String value) {
    this.name = value;
}
public String getName() {
    return this.name;
}
private Driver driver;

@ManyToOne(optional=false)
public Driver getDriver() {
   return this.driver;
}

public void setDriver(Driver driver) {
   this.driver = driver;
}

private Vehicle vehicle;

@ManyToOne(optional=false)
public Vehicle getVehicle() {
   return this.vehicle;
}

public void setVehicle(Vehicle vehicle) {
   this.vehicle = vehicle;
}

private Set<Stop> stop;

@OneToMany(mappedBy="trip" )
public Set<Stop> getStop() {
   return this.stop;
}

public void setStop(Set<Stop> stops) {
   this.stop = stops;
}

private Advertisement advertisement;

@OneToOne
public Advertisement getAdvertisement() {
   return this.advertisement;
}

public void setAdvertisement(Advertisement advertisement) {
   this.advertisement = advertisement;
}

private java.sql.Time startTime;

public void setStartTime(java.sql.Time value) {
    this.startTime = value;
}
public java.sql.Time getStartTime() {
    return this.startTime;
}
private java.sql.Date startDate;

public void setStartDate(java.sql.Date value) {
    this.startDate = value;
}
public java.sql.Date getStartDate() {
    return this.startDate;
}
}
