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
import ca.mcgill.ecse321.rideshare9.entity.TripStatus;
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
    @RequestMapping(value = "/create-adv", method=RequestMethod.POST)
    public Advertisement postAdv(@RequestBody Advertisement adv) {

    	String currentUserName = null; 
   	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        currentUserName = authentication.getName();
        if (userv.loadUserByUsername(currentUserName) != null) {
                return advService.createAdv(adv.getTitle(), adv.getStartTime(), adv.getStartLocation(), adv.getSeatAvailable(), adv.getStops(), adv.getVehicle(), userv.loadUserByUsername(currentUserName).getId());
    	} else {
    		return null; 
        }

    }
    /**
     * show all advertisement a logged in driver posted
     * @return List
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/get-logged-adv", method=RequestMethod.GET)
    public List<Advertisement> myAdv() {    	
    	String currentUserName = null; 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        currentUserName = authentication.getName();
        if (userv.loadUserByUsername(currentUserName) != null) {
                return advService.findAllAdv(userv.loadUserByUsername(currentUserName).getId());
    	} else {
    		return null; 
    	}
    }
    
    /**
     * driver: delete advertisement, but only id needed
     * @param adv (JSON)
     * @return deleted adv
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/delete-adv", method=RequestMethod.DELETE)
    public Advertisement delAdv(@RequestBody Advertisement adv) {
    	for (Advertisement a: this.myAdv()) {
    		if (a.getId() == adv.getId()) {
    			return advService.removeAdv(adv.getId()); 
    		}
    	}
    	return null; 
    }
    
    
    /**
     * driver: change advertisement content
     * @param adv (JSON)
     * @return changed advertisement
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/update-adv", method=RequestMethod.PUT)
    public Advertisement changeAdv(@RequestBody Advertisement adv) {
    	
    	String currentUserName = null; 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    currentUserName = authentication.getName();
        User curr = userv.loadUserByUsername(currentUserName);
	    if (curr.getId() != advService.findAdv(adv.getId()).getDriver()) {
    		return new Advertisement(); 
    	}
    	
    	Advertisement oldadv = advService.findAdv(adv.getId()); 
    	if (adv.getTitle() != null && !adv.getTitle().isEmpty() && !adv.getTitle().equals(oldadv.getTitle())) {
    		oldadv.setTitle(adv.getTitle());
    	}
    	if (adv.getStartTime() != null && !adv.getStartTime().equals(oldadv.getStartTime())) {
    		oldadv.setStartTime(adv.getStartTime());
    	}
    	if (adv.getStartLocation() != null && !adv.getStartLocation().isEmpty() && !adv.getStartLocation().equals(oldadv.getStartLocation())) {
    		oldadv.setStartLocation(adv.getStartLocation());
    	}
    	if ((adv.getSeatAvailable() != 0) && (oldadv.getSeatAvailable() != adv.getSeatAvailable()) 
    			|| (adv.getSeatAvailable() == 0) && (adv.getStatus() != null) && (adv.getStatus() != TripStatus.REGISTERING)) {
    		oldadv.setSeatAvailable(adv.getSeatAvailable());
    	}
    	if (adv.getStatus() != null && adv.getStatus() != oldadv.getStatus()) {
    		oldadv.setStatus(adv.getStatus());
    	}
    	if (adv.getVehicle() > 0 && (oldadv.getVehicle() != adv.getVehicle())) {
    		oldadv.setVehicle(adv.getVehicle());
    	}
    	oldadv.setStops(adv.getStops()); 
    	
    	return advService.updateAdv(oldadv); 
    }
    
    /**
     * All user: list all advertisement
     * @param void
     * @return list of all advertisements
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('DRIVER') or hasRole('ADMIN') or hasRole('BOSSLI')")
    @GetMapping("/get-list-adv")
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
     * All user: search advertisement by carrier json
     * @param must have start, stop, start time range x, start time range y; color, model are optional; default sort by price
     * @return list of all advertisements
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('DRIVER') or hasRole('ADMIN') or hasRole('BOSSLI')")
    @PostMapping("/get-adv-search")
    public List<AdvResponse> searchAdv(@RequestBody AdvQuery advCriteria) {
    	if (advCriteria.isSortByPrice()) {
    		if (advCriteria.getvColor() != null && advCriteria.getvModel() != null) {
    			return advService.findAdvByCriteriaAndModelAndColorSortByPrice(advCriteria); 
    		} else if (advCriteria.getvColor() != null && advCriteria.getvModel() == null) {
    			return advService.findAdvByCriteriaAndColorSortByPrice(advCriteria); 
    		} else if (advCriteria.getvColor() == null && advCriteria.getvModel() != null) {
    			return advService.findAdvByCriteriaAndModelSortByPrice(advCriteria); 
    		} else {
    			return advService.findAdvByCriteriaSortByPrice(advCriteria); 
    		}
    	} else {
    		if (advCriteria.getvColor() != null && advCriteria.getvModel() != null) {
    			return advService.findAdvByCriteriaAndModelAndColorSortByTime(advCriteria); 
    		} else if (advCriteria.getvColor() != null && advCriteria.getvModel() == null) {
    			return advService.findAdvByCriteriaAndColorSortByTime(advCriteria); 
    		} else if (advCriteria.getvColor() == null && advCriteria.getvModel() != null) {
    			return advService.findAdvByCriteriaAndModelSortByTime(advCriteria); 
    		} else {
    			return advService.findAdvByCriteriaSortByTime(advCriteria); 
    		}
    	}
    }
}
