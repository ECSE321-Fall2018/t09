package ca.mcgill.ecse321.rideshare9.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.rideshare9.entity.MapperUserAdv;
import ca.mcgill.ecse321.rideshare9.entity.Vehicle;
import ca.mcgill.ecse321.rideshare9.repository.AdvertisementRepository;
import ca.mcgill.ecse321.rideshare9.repository.MapperUserAdvRepository;
import ca.mcgill.ecse321.rideshare9.service.UserService;

@RestController
@RequestMapping("/map")
public class MapperController {
	@Autowired
	private AdvertisementRepository advService;
	private UserService userv; 
	private MapperUserAdvRepository mserv; 
	/**
     * create adv
     * @param adv
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/add-map", method=RequestMethod.POST)
    public MapperUserAdv addCar(@RequestBody MapperUserAdv map) {
    	if (map != null) {
    		mserv.createMapper(map.getPassenger(), map.getAdvertisement()); 
            return map;
    	} else {
    		return map; 
    	}
    }
}
