package ca.mcgill.ecse321.rideshare9.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.rideshare9.entity.Stop;
import ca.mcgill.ecse321.rideshare9.repository.StopRepository;

@RestController
@RequestMapping("/stop")
public class StopController {
	@Autowired
	private StopRepository stopService;
	
    /**
     * Driver: Add stop
     * Core API endpoint: Driver-1.3
     * @param stop
     * @return added stop
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/add-stop", method = RequestMethod.POST)
    public Stop addStop(@RequestBody Stop sp) {
    	
    	// TODO : return the added object WITH ID (originally, it return what you entered, id = 0 is always)
    	
    	if (sp != null) {
    		stopService.createStop(sp.getStopName(), sp.getPrice()); 
    		return sp;
    	} else {
    		return sp; 
    	}
    }
    
    /**
     * Driver: Change
     * Core API endpoint: Driver-1.3
     * @param stop
     * @return changed stop
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/change-stop", method = RequestMethod.PUT)
    public Stop changeStop(@RequestBody Stop sp) {
    	
    	// TODO : return the changed object WITH ID (originally, it return what you entered, id = 0 is always)
    	
    	if (sp != null) {
    		stopService.createStop(sp.getStopName(), sp.getPrice()); 
    		return sp;
    	} else {
    		return sp; 
    	}
    }
    
    /**
     * Driver: Delete stop
     * Core API endpoint: Driver-1.3
     * @param stop
     * @return added stop
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/del-stop", method = RequestMethod.DELETE)
    public Stop delStop(@RequestBody Stop sp) {
    	
    	// TODO : return the added object WITH ID (originally, it return what you entered, id = 0 is always)
    	
    	if (sp != null) {
    		stopService.createStop(sp.getStopName(), sp.getPrice()); 
    		return sp;
    	} else {
    		return sp; 
    	}
    }
}
