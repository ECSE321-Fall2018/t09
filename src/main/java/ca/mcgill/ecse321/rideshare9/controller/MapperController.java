package ca.mcgill.ecse321.rideshare9.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.rideshare9.entity.MapperUserAdv;
import ca.mcgill.ecse321.rideshare9.entity.User;
import ca.mcgill.ecse321.rideshare9.repository.AdvertisementRepository;
import ca.mcgill.ecse321.rideshare9.repository.MapperUserAdvRepository;
import ca.mcgill.ecse321.rideshare9.service.UserService;

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
    @PreAuthorize("hasRole('PASSENGER') or hasRole('BOSSLI')")
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
        	return mserv.createMapper(current_user.getId(), advService.findAdv(aid).getId()); 
    	}
    	return null; 
    }
    
    /**
     * Passenger: delete journey/mapper
     * @param journey/mapper (JSON)
     * @return deleted mapper
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/delete-map", method=RequestMethod.DELETE)
    public MapperUserAdv delMap(@RequestBody MapperUserAdv map) {
    	
    	// TODO : find by relevant criteria, or just id, and delete it 
    	
    	return new MapperUserAdv(); 
    }
    
    /**
     * Admin: list all advertisement SELECT COUNT(*) FROM tb_mapper WHERE role = 'ROLE_PASSENGER' GROUPBY user ORDERED BY COUNT(*) DESC
     * Core API endpoint: Admin-2 in README.md at Mark branch
     * @return List<User>
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSSLI')")
    @GetMapping("/list-top-passengers")
    public List<User> getTopPassengers(){   
    	
    	// TODO: do this by query 
    	
        return userv.getUsers();
    }
}