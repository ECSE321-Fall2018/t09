package ca.mcgill.ecse321.carpool.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Advertisement{
   private Trip trip;
   
   @OneToOne(mappedBy="advertisement" , optional=false)
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
}
