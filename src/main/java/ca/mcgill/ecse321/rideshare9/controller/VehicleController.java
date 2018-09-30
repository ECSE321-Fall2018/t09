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
    	
    	String currentUserName = ""; 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    currentUserName = authentication.getName();
    	}
    	if (car != null) {
        	carService.createVehicle(car.getLicencePlate(), car.getModel(), car.getColor(), car.getMaxSeat(), urp.findUserByUsername(currentUserName).getId()); 
            return car;
    	} else {
    		return car; 
    	}
    }
    
    /**
     * Driver: remove car, you can change parameter to non-json type
     * @param car criteria/identity
     * @return deleted car
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/remove-car", method=RequestMethod.DELETE)
    public Vehicle removeCar(@RequestBody Vehicle car) {
    	
    	// TODO: Implement this, with principle "i can only delete my car", you can change parameter to non-json type
    	
    	String currentUserName = ""; 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    currentUserName = authentication.getName();
    	}
    	if (car != null) {
        	carService.createVehicle(car.getLicencePlate(), car.getModel(), car.getColor(), car.getMaxSeat(), urp.findUserByUsername(currentUserName).getId()); 
            return car;
    	} else {
    		return car; 
    	}
    }
    
    /**
     * Driver: get all his car
     * @return all his car
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/get-cars", method=RequestMethod.GET)
    public List<Vehicle> getAllCar() {
    	
    	// TODO: Implement this, with principle "i can only get my cars", you can change parameter to non-json type
    	
    	return new ArrayList<Vehicle>(); 
    }
    
    /**
     * Driver: change his car
     * @param change criteria/specification
     * @return change car
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/change-cars", method=RequestMethod.PUT)
    public Vehicle changelCar(@RequestBody Vehicle car) {
    	
    	// TODO: Implement this, with principle "i can only change my car", you can change parameter to non-json type
    	
    	return new Vehicle(); 
    }
    
}