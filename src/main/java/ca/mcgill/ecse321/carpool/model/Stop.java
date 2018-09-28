package ca.mcgill.ecse321.carpool.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Stop{
   private Trip trip;
   
   @ManyToOne(optional=false)
   public Trip getTrip() {
      return this.trip;
   }
   
   public void setTrip(Trip trip) {
      this.trip = trip;
   }
   
   private String name;

public void setName(String value) {
    this.name = value;
}
public String getName() {
    return this.name;
}
private String address;

public void setAddress(String value) {
    this.address = value;
}
public String getAddress() {
    return this.address;
}
private float price;

public void setPrice(float value) {
    this.price = value;
}
public float getPrice() {
    return this.price;
}
}
