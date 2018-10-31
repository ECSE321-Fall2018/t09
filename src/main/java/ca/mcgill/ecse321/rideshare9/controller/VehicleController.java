package ca.mcgill.ecse321.rideshare9.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.rideshare9.entity.Advertisement;
import ca.mcgill.ecse321.rideshare9.entity.TripStatus;
import ca.mcgill.ecse321.rideshare9.entity.User;
import ca.mcgill.ecse321.rideshare9.entity.Vehicle;
import ca.mcgill.ecse321.rideshare9.repository.VehicleRepository;
import ca.mcgill.ecse321.rideshare9.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {
	
	@Autowired
	private VehicleRepository carService;
	@Autowired
	protected UserServiceImpl urp; 
    
    /**
     * Driver: create car
     * @param car to be added
     * @return deleted car
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/add-car", method=RequestMethod.POST)
    public Vehicle addCar(@RequestBody Vehicle car) {
    	
    	// TODO: Fix bug: parameter error
    	
    	String currentUserName = null; 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    currentUserName = authentication.getName();
    	}
    	if (car != null) {
            return carService.createVehicle(car.getLicencePlate(), car.getModel(), car.getColor(), car.getMaxSeat(), urp.findUserByUsername(currentUserName).getId());
    	} else {
    		return car; 
    	}
    }
    
    /**
     * Driver: remove car, you can change parameter to non-json type
     * @param car criteria/identity
     * @return deleted car
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('ADMIN') or hasRole('BOSSLI')")
<<<<<<< HEAD
    @RequestMapping(value = "/remove-car", method=RequestMethod.DELETE)
=======
    @RequestMapping(value = "/remove-car", method=RequestMethod.POST)
>>>>>>> a9a644d41c53ffa39d1449d41543e886e245a484
    public Vehicle removeCar(@RequestBody Vehicle car) {
    	
    	// TODO: Implement this, with principle "i can only delete my car", you can change parameter to non-json type
    	
    	String currentUserName = null; 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    currentUserName = authentication.getName();
    	}
<<<<<<< HEAD
    	if (car != null && car.getDriver() == urp.loadUserByUsername(currentUserName).getId()) {
        	carService.removeVehicle(car.getId()); 
        	return car;
    	} else {
    		return null; 
    	}
=======
    	long currdriver = urp.loadUserByUsername(currentUserName).getId(); 
    	List<Vehicle> vs = carService.findAllVehicleByUid(currdriver); 
    	for (Vehicle v: vs) {
    		if (v.getDriver() == currdriver && v.getId() == car.getId()) {
    			carService.removeVehicle(v.getId()); 
    			return v; 
    		}
    	}
    	return null; 
>>>>>>> a9a644d41c53ffa39d1449d41543e886e245a484
    }
    
    /**
     * Driver: get all his car
     * @return all his car
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('ADMIN') or hasRole('BOSSLI')")
    @RequestMapping(value = "/get-cars", method=RequestMethod.GET)
    public List<Vehicle> getAllCar() {
    	
    	// TODO: Implement this, with principle "i can only get my cars", you can change parameter to non-json type
    	
    	String currentUserName = null; 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    currentUserName = authentication.getName();
    	}
    	
    	return carService.findAllVehicleByUid(urp.loadUserByUsername(currentUserName).getId()); 
    }
    
    
    
    /**
     * Driver: change his car
     * @param change criteria/specification
     * @return change car
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
<<<<<<< HEAD
    @RequestMapping(value = "/change-cars", method=RequestMethod.PUT)
=======
    @RequestMapping(value = "/change-cars", method=RequestMethod.POST)
>>>>>>> a9a644d41c53ffa39d1449d41543e886e245a484
    public Vehicle changelCar(@RequestBody Vehicle car) {
    	
    	// TODO: Implement this, with principle "i can only change my car", you can change parameter to non-json type
    	String currentUserName = null; 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    currentUserName = authentication.getName();
<<<<<<< HEAD
    	    User curr = urp.findUserByUsername(currentUserName); 
    	    if (curr.getId() != car.getDriver()) {
        		return new Vehicle(); 
        	}
    	}
    	
    	Vehicle oldcar=carService.findVehicle(car.getId());
    	
    	if (car.getLicencePlate() != null && !car.getLicencePlate().isEmpty() && !oldcar.getLicencePlate().equals(car.getLicencePlate())) {
    		oldcar.setLicencePlate(car.getLicencePlate());
    	}
    	if (car.getColor() != null && !car.getColor().isEmpty() && !oldcar.getColor().equals(car.getColor())) {
    		oldcar.setColor(car.getColor());
    	}
    	if (car.getModel() != null && !car.getModel().isEmpty() && !oldcar.getModel().equals(car.getModel())) {
    		oldcar.setModel(car.getModel());
    	}
    	if (car.getMaxSeat() > 0 && oldcar.getMaxSeat()!=car.getMaxSeat()){
    		oldcar.setMaxSeat(car.getMaxSeat());
    	}
    	
    	return carService.updateVehicle(oldcar); 
=======
    	}
    	long currdriver = urp.loadUserByUsername(currentUserName).getId(); 
    	List<Vehicle> vs = carService.findAllVehicleByUid(currdriver); 
    	for (Vehicle v: vs) {
    		if (v.getDriver() == currdriver && v.getId() == car.getId()) {
    			car.setDriver(currdriver); 
    	    	return carService.updateVehicle(car); 
    		}
    	}
    	return null; 
    	
>>>>>>> a9a644d41c53ffa39d1449d41543e886e245a484
    }
    
}