package ca.mcgill.ecse321.rideshare9.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.rideshare9.entity.Advertisement;
import ca.mcgill.ecse321.rideshare9.repository.AdvertisementRepository;
import ca.mcgill.ecse321.rideshare9.service.UserService;

// TODO: Complete this controller, DON'T hesitate to add if you would like to add more method! 

/**
 * MORE METHODS WANTED!! 
 * i think we need a new method that adds/deletes/changes stop/vehicles on an advertisement
 * @author yuxiangma
 */

@RestController
@RequestMapping("/adv")
public class AdvertisementController {
	
	@Autowired
	private AdvertisementRepository advService;
	@Autowired
	private UserService userv; 
	
    /**
     * driver: create advertisement
     * Core API endpoint: Driver-1.1, Driver-1.2, Driver-1.3, Driver-1.4 in README.md at Mark branch
     * @param adv (JSON)
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/add-adv", method=RequestMethod.POST)
    public Advertisement postAdv(@RequestBody Advertisement adv) {
    	
    	// TODO : Make this method return the added Advertisement with Advertisement ID
    	// TODO : return the added object WITH ID (originally, it return what you entered, id = 0 is always)
    	// TODO : Add stop and vehicles have bugs
    	
    	String currentUserName = ""; 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    currentUserName = authentication.getName();
    	}
    	if (userv.findUserByUsername(currentUserName) != null) {
        	return advService.createAdv(adv.getTitle(), adv.getStartDate(), adv.getStartLocation(), adv.getSeatAvailable(), adv.getVehicle(), userv.findUserByUsername(currentUserName).getId()); 
    	} else {
    		return null; 
    	}
    }
    
    /**
     * driver: delete advertisement
     * @param adv (JSON)
     * @return deleted adv
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/delete-adv", method=RequestMethod.DELETE)
    public Advertisement delAdv(@RequestBody Advertisement adv) {
    	
    	// TODO : find by relevant criteria, or just id, and delete it 
    	
    	return new Advertisement(); 
    }
    
    /**
     * driver: change advertisement content
     * @param adv (JSON)
     * @return changed advertisement
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/change-adv", method=RequestMethod.PUT)
    public Advertisement changeAdv(@RequestBody Advertisement adv) {
    	
    	// TODO : find by relevant criteria, or just id, and change parameter passed from adv json that is 1. not null and 2. different than before 
    	
    	return new Advertisement(); 
    }

    /**
     * All user: list all advertisement
     * @param void
     * @return list of all advertisements
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('DRIVER') or hasRole('ADMIN') or hasRole('BOSSLI')")
    @GetMapping("/get-all-adv")
    public List<Advertisement> searchAllAdv() {
        return advService.findAllAdv();
    }
    
    /**
     * All people: list all advertisement SELECT COUNT(*) FROM tb_adv WHERE role = 'ROLE_DRIVER' GROUPBY driver ORDERED BY COUNT(*) DESC
     * Core API endpoint: Admin-2 in README.md at Mark branch: TOP DRIVER
     * @param void
     * @return list of all advertisements
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('DRIVER') or hasRole('ADMIN') or hasRole('BOSSLI')")
    @GetMapping("/get-top-drivers")
    public List<Advertisement> getTopDriver() {
    	
    	// TODO: Top driver, do this my adding method to AdvertisementRepository
    	
        return new ArrayList<Advertisement>();
    }
    
    /**
     * All user: Search Advertisement by relavent criteria
     * Core API endpoint: Passenger-1, Passenger-2 in README.md at Mark branch
     * This method is NOT meant to be restricted by passing JSON object, you can actually pass any parameter, or use a request type other than post
     * @param adv (json)
     * @return list of (collection of) advertisement according criteria specified by user entry
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('DRIVER') or hasRole('ADMIN') or hasRole('BOSSLI')")
    @PostMapping("/get-adv-by-criteria")
    public List<Advertisement> searchAdvByCriteria(@RequestBody Advertisement advCriteria) {
        
    	// TODO: use or invent method in entityManager/repository to implement this
    	
    	return new ArrayList<Advertisement>(); 
    }
}
