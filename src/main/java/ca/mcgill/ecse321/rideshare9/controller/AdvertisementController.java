package ca.mcgill.ecse321.rideshare9.controller;

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
import ca.mcgill.ecse321.rideshare9.entity.User;
import ca.mcgill.ecse321.rideshare9.entity.helper.AdvBestQuery;
import ca.mcgill.ecse321.rideshare9.entity.helper.AdvQuery;
import ca.mcgill.ecse321.rideshare9.entity.helper.AdvResponse;
import ca.mcgill.ecse321.rideshare9.repository.AdvertisementRepository;
import ca.mcgill.ecse321.rideshare9.service.UserService;


/**
 * MORE METHODS WANTED!! 
 * i think we need a new method that adds/deletes/changes stop/vehicles on an advertisement
 * @author yuxiangma
 */

/**
 * Notes for front end: Searching
 * --------------------------------------------------------------------------------------------------------------
 * Essential Query: 4 must be present 
 * --------------------------------------------------------------------------------------------------------------
 * destination: 			| startLocation: 				| startTimeLow: 			| startTimeHigh: 			
 * --------------------------------------------------------------------------------------------------------------
 * Optional Query: any one, or both, or neither present 
 * --------------------------------------------------------------------------------------------------------------
 * Car type: 			    | Car color: 				    | 
 * --------------------------------------------------------------------------------------------------------------
 * Order by: Option, choose 1
 * --------------------------------------------------------------------------------------------------------------
 * Price					| StartTime 					|
 * --------------------------------------------------------------------------------------------------------------
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
    	String currentUserName = null; 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    currentUserName = authentication.getName();
    	}
    	if (userv.findUserByUsername(currentUserName) != null) {
        	return advService.createAdv(adv.getTitle(), adv.getStartTime(), adv.getStartLocation(), adv.getSeatAvailable(), adv.getStops(), adv.getVehicle(), userv.findUserByUsername(currentUserName).getId()); 
    	} else {
    		return null; 
    	}
    }
    /**
     * show all advertisement a logged in driver posted
     * @return List
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/my-adv", method=RequestMethod.GET)
    public List<Advertisement> myAdv() {    	
    	String currentUserName = null; 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    currentUserName = authentication.getName();
    	}
    	if (userv.findUserByUsername(currentUserName) != null) {
        	return advService.findAllAdv(userv.findUserByUsername(currentUserName).getId()); 
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
    	return advService.removeAdv(adv.getId()); 
    }
    
    /**
     * driver: cancel advertisement
     * @param adv (JSON)
     * @return canceled adv
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/cancel-adv", method=RequestMethod.PUT)
    public Advertisement cancelAdv(@RequestBody Advertisement adv) {
    	return advService.cancelAdv(adv.getId()); 
    }
    
    /**
     * driver: close advertisement: don't want anyone more to join
     * @param adv (JSON)
     * @return closed adv
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/close-adv", method=RequestMethod.PUT)
    public Advertisement closeAdv(@RequestBody Advertisement adv) {
    	return advService.closeAdv(adv.getId()); 
    }
    /**
     * driver: complete advertisement journey complete
     * @param adv (JSON)
     * @return completed adv
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/complete-adv", method=RequestMethod.PUT)
    public Advertisement completeAdv(@RequestBody Advertisement adv) {
    	return advService.completeAdv(adv.getId()); 
    }
    
    /**
     * driver: ride advertisement: journey start
     * @param adv (JSON)
     * @return canceled adv
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/ride-adv", method=RequestMethod.PUT)
    public Advertisement rideAdv(@RequestBody Advertisement adv) {
    	return advService.rideAdv(adv.getId()); 
    }
    
    
    /**
     * driver: change advertisement content
     * @param adv (JSON)
     * @return changed advertisement
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/change-adv", method=RequestMethod.PUT)
    public Advertisement changeAdv(@RequestBody Advertisement adv) {
    	String currentUserName = null; 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    currentUserName = authentication.getName();
    	    User curr = userv.findUserByUsername(currentUserName); 
    	    if (curr.getId() != adv.getDriver()) {
        		return new Advertisement(); 
        	}
    	}
    	
    	
    	Advertisement oldadv = advService.findAdv(adv.getId()); 
    	if (!oldadv.getTitle().equals(adv.getTitle())) {
    		oldadv.setTitle(adv.getTitle());
    	}
    	if (!oldadv.getStartTime().equals(adv.getStartTime())) {
    		oldadv.setStartTime(adv.getStartTime());
    	}
    	if (!oldadv.getStartLocation().equals(adv.getStartLocation())) {
    		oldadv.setStartLocation(adv.getStartLocation());
    	}
    	if (oldadv.getSeatAvailable() != adv.getSeatAvailable()) {
    		oldadv.setSeatAvailable(adv.getSeatAvailable());
    	}
    	if (oldadv.getVehicle() != adv.getVehicle()) {
    		oldadv.setVehicle(adv.getVehicle());
    	}
    	oldadv.getStops().retainAll(adv.getStops()); 
    	
    	return advService.updateAdv(oldadv); 
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
    public List<AdvBestQuery> getTopDriver() {
        return advService.findBestDriver(); 
    }
    
    /**
     * All user: Search Advertisement by price criteria
     * Core API endpoint: Passenger-1, Passenger-2 in README.md at Mark branch
     * This method is NOT meant to be restricted by passing JSON object, you can actually pass any parameter, or use a request type other than post
     * @param adv (json)
     * @return list of (collection of) advertisement according criteria specified by user entry
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('DRIVER') or hasRole('ADMIN') or hasRole('BOSSLI')")
    @PostMapping("/get-adv-by-price")
    public List<AdvResponse> searchAdvByPrice(@RequestBody AdvQuery advCriteria) {
    	return advService.findAdvByCriteriaSortByPrice(advCriteria); 
    }
    /**
     * All user: Search Advertisement by datetime criteria
     * Core API endpoint: Passenger-1, Passenger-2 in README.md at Mark branch
     * This method is NOT meant to be restricted by passing JSON object, you can actually pass any parameter, or use a request type other than post
     * @param adv (json)
     * @return list of (collection of) advertisement according criteria specified by user entry
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('DRIVER') or hasRole('ADMIN') or hasRole('BOSSLI')")
    @PostMapping("/get-adv-by-time")
    public List<AdvResponse> searchAdvByTime(@RequestBody AdvQuery advCriteria) {
    	return advService.findAdvByCriteriaSortByTime(advCriteria); 
    }
    /**
     * All user: Search Advertisement by datetime criteria, and vehicle color
     * Core API endpoint: Passenger-1, Passenger-2 in README.md at Mark branch
     * This method is NOT meant to be restricted by passing JSON object, you can actually pass any parameter, or use a request type other than post
     * @param adv (json)
     * @return list of (collection of) advertisement according criteria specified by user entry
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('DRIVER') or hasRole('ADMIN') or hasRole('BOSSLI')")
    @PostMapping("/get-adv-by-time-by-color")
    public List<AdvResponse> searchAdvByTimeByColor(@RequestBody AdvQuery advCriteria) {
    	return advService.findAdvByCriteriaAndColorSortByTime(advCriteria); 
    }
    /**
     * All user: Search Advertisement by price criteria, and vehicle color
     * Core API endpoint: Passenger-1, Passenger-2 in README.md at Mark branch
     * This method is NOT meant to be restricted by passing JSON object, you can actually pass any parameter, or use a request type other than post
     * @param adv (json)
     * @return list of (collection of) advertisement according criteria specified by user entry
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('DRIVER') or hasRole('ADMIN') or hasRole('BOSSLI')")
    @PostMapping("/get-adv-by-price-by-color")
    public List<AdvResponse> searchAdvByPriceByColor(@RequestBody AdvQuery advCriteria) {
    	return advService.findAdvByCriteriaAndColorSortByPrice(advCriteria); 
    }
    /**
     * All user: Search Advertisement by datetime criteria, and vehicle model
     * Core API endpoint: Passenger-1, Passenger-2 in README.md at Mark branch
     * This method is NOT meant to be restricted by passing JSON object, you can actually pass any parameter, or use a request type other than post
     * @param adv (json)
     * @return list of (collection of) advertisement according criteria specified by user entry
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('DRIVER') or hasRole('ADMIN') or hasRole('BOSSLI')")
    @PostMapping("/get-adv-by-time-by-model")
    public List<AdvResponse> searchAdvByTimeByModel(@RequestBody AdvQuery advCriteria) {
    	return advService.findAdvByCriteriaAndModelSortByTime(advCriteria); 
    }
    /**
     * All user: Search Advertisement by price criteria, and vehicle model
     * Core API endpoint: Passenger-1, Passenger-2 in README.md at Mark branch
     * This method is NOT meant to be restricted by passing JSON object, you can actually pass any parameter, or use a request type other than post
     * @param adv (json)
     * @return list of (collection of) advertisement according criteria specified by user entry
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('DRIVER') or hasRole('ADMIN') or hasRole('BOSSLI')")
    @PostMapping("/get-adv-by-price-by-model")
    public List<AdvResponse> searchAdvByPriceByModel(@RequestBody AdvQuery advCriteria) {
    	return advService.findAdvByCriteriaAndModelSortByPrice(advCriteria); 
    }
    /**
     * All user: Search Advertisement by price criteria, and vehicle color and model
     * Core API endpoint: Passenger-1, Passenger-2 in README.md at Mark branch
     * This method is NOT meant to be restricted by passing JSON object, you can actually pass any parameter, or use a request type other than post
     * @param advhelper (json)
     * @return list of (collection of) advertisement according criteria specified by user entry
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('DRIVER') or hasRole('ADMIN') or hasRole('BOSSLI')")
    @PostMapping("/get-adv-by-price-by-model-color")
    public List<AdvResponse> searchAdvByPriceByModelAndColor(@RequestBody AdvQuery advCriteria) {
    	return advService.findAdvByCriteriaAndModelAndColorSortByPrice(advCriteria); 
    }
    /**
     * All user: Search Advertisement by datetime criteria, and vehicle color and model
     * Core API endpoint: Passenger-1, Passenger-2 in README.md at Mark branch
     * This method is NOT meant to be restricted by passing JSON object, you can actually pass any parameter, or use a request type other than post
     * @param advhelper (json)
     * @return list of (collection of) advertisement according criteria specified by user entry
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('DRIVER') or hasRole('ADMIN') or hasRole('BOSSLI')")
    @PostMapping("/get-adv-by-time-by-model-color")
    public List<AdvResponse> searchAdvByTimeByModelAndColor(@RequestBody AdvQuery advCriteria) {
    	return advService.findAdvByCriteriaAndModelAndColorSortByTime(advCriteria); 
    }
}
