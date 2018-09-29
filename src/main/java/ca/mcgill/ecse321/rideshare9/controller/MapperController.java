package ca.mcgill.ecse321.rideshare9.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.rideshare9.entity.MapperUserAdv;
import ca.mcgill.ecse321.rideshare9.entity.User;
import ca.mcgill.ecse321.rideshare9.entity.Vehicle;
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
     * create adv
     * @param adv
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/add-map", method=RequestMethod.POST)
    @ResponseBody
    public MapperUserAdv addMap(@RequestParam("adv_id") long aid) {
    	String currentUserName = ""; 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    currentUserName = authentication.getName();
    	}
    	User current_user = userv.loadUserByUsername(currentUserName); 
    	if (userv.findUserByUsername(currentUserName) != null) {
        	return mserv.createMapper(current_user, advService.findAdv(aid)); 
    	}
    	return null; 
    }
}
