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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.rideshare9.entity.Advertisement;
import ca.mcgill.ecse321.rideshare9.entity.Stop;
import ca.mcgill.ecse321.rideshare9.entity.User;
import ca.mcgill.ecse321.rideshare9.entity.Vehicle;
import ca.mcgill.ecse321.rideshare9.repository.AdvertisementRepository;
import ca.mcgill.ecse321.rideshare9.repository.StopRepository;
import ca.mcgill.ecse321.rideshare9.repository.VehicleRepository;
import ca.mcgill.ecse321.rideshare9.service.UserService;

@RestController
@RequestMapping("/adv")
public class AdvertisementController {
	@Autowired
	private AdvertisementRepository advService;
	@Autowired
	private UserService userv; 
	
    /**
     * create adv
     * @param adv
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/add-adv", method=RequestMethod.POST)
    public Advertisement postAdv(@RequestBody Advertisement adv) {
    	String currentUserName = ""; 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    currentUserName = authentication.getName();
    	}
    	if (userv.findUserByUsername(currentUserName) != null) {
        	return advService.createAdv(adv.getTitle(), adv.getStartDate(), adv.getStartLocation(), adv.getSeatAvailable(), adv.getVehicle(), userv.findUserByUsername(currentUserName)); 
    	} else {
    		return null; 
    	}
    }

    /**
     * create
     * @param adv
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('DRIVER') or hasRole('ADMIN') or hasRole('BOSSLI')")
    @GetMapping("/passenger/get-all-adv")
    public List<Advertisement> searchAdv() {
        return advService.findAllAdv();
    }
}
