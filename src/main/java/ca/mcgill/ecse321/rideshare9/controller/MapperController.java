package ca.mcgill.ecse321.rideshare9.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.rideshare9.entity.Advertisement;
import ca.mcgill.ecse321.rideshare9.entity.MapperUserAdv;
import ca.mcgill.ecse321.rideshare9.entity.TripStatus;
import ca.mcgill.ecse321.rideshare9.entity.User;
import ca.mcgill.ecse321.rideshare9.entity.helper.AdvQuery;
import ca.mcgill.ecse321.rideshare9.entity.helper.MapperBestQuery;
import ca.mcgill.ecse321.rideshare9.repository.AdvertisementRepository;
import ca.mcgill.ecse321.rideshare9.repository.MapperUserAdvRepository;
import ca.mcgill.ecse321.rideshare9.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/map")
public class MapperController {
	@Autowired
	private AdvertisementRepository advService;
	@Autowired
	private UserService userv; 
	@Autowired
	private MapperUserAdvRepository mserv; 
	
	/**
     * Passenger: register for a journey/advertisement
     * Core API endpoint: Passenger-1, Passenger-2 in README.md at Mark branch
     * @param advertisement id
     * @return registration/mapper object
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('ADMIN') or hasRole('BOSSLI')")
    @RequestMapping(value = "/add-map", method=RequestMethod.POST)
    @ResponseBody
    public MapperUserAdv addMap(@RequestParam("adv_id") long aid) {
    	
    	// TODO : Make this method return the added Mapper with Mapper ID
    	
    	String currentUserName = ""; 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    currentUserName = authentication.getName();
    	}
    	User current_user = userv.loadUserByUsername(currentUserName); 
    	if (userv.findUserByUsername(currentUserName) != null) {
    		Advertisement advFound = advService.findAdv(aid); 
        	if (1 == (advFound.getSeatAvailable())) {
        		advFound.setStatus(TripStatus.CLOSED); 
        		advFound.setSeatAvailable(advFound.getSeatAvailable() - 1); 
        		advService.updateAdv(advFound); 
        		return mserv.createMapper(current_user.getId(), advFound.getId());
        	}
        	if (0 < (advFound.getSeatAvailable())) {
        		advFound.setSeatAvailable(advFound.getSeatAvailable() - 1); 
    			return mserv.createMapper(current_user.getId(), advFound.getId()); 
    		}
    		if (mserv.findRegCountById(advFound.getId()) >= (advFound.getSeatAvailable())) {
    			return null; 
    		}
    	}
    	return null; 
    }
    /**
     * Passenger: get his registered journey
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('ADMIN') or hasRole('BOSSLI')")
    @RequestMapping(value = "/get-map", method=RequestMethod.GET)
    @ResponseBody
    public List<MapperUserAdv> getLoggedMap() {
    	    	
    	String currentUserName = ""; 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    currentUserName = authentication.getName();
    	}
    	
    	return mserv.findNamedAdv(userv.loadUserByUsername(currentUserName).getId()); 
    }
    /**
     * Passenger: delete journey/mapper
     * @param journey/mapper (JSON)
     * @return deleted mapper
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('BOSSLI') or hasRole('ADMIN')")
    @DeleteMapping("/admin/delete/{mapperid}")
    	public MapperUserAdv delMap(@PathVariable String mapperid) {
        	try {
        		Long mapperid_long = Long.valueOf(mapperid);
            	MapperUserAdv mapperuseradv = mserv.findMap(mapperid_long);
            	mserv.removeVehicle(mapperid_long);
            	return mapperuseradv; 
        	}
        	catch (Exception e) {
        		return null;
        	}
    }
    /**
     * Admin: list all advertisement SELECT COUNT(*) FROM tb_mapper WHERE role = 'ROLE_PASSENGER' GROUPBY user ORDERED BY COUNT(*) DESC
     * Core API endpoint: Admin-2 in README.md at Mark branch
     * @return List<User>
     */
    //@PreAuthorize("hasRole('DRIVER') or hasRole('PASSENGER') or hasRole('BOSSLI') or hasRole('ADMIN')")
    @PostMapping("/get-top-passengers")
    public List<MapperBestQuery> getTopPassengers(@RequestBody AdvQuery advCriteria){   
        return mserv.findBestPassenger(advCriteria);
    }
}
