package ca.mcgill.ecse321.rideshare9.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.rideshare9.entity.Advertisement;
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
    	
    	if (sp != null) {
    		 return stopService.createStop(sp.getStopName(), sp.getPrice());
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
    	Stop oldsp = stopService.findStop(sp.getId()); 
    	if (sp.getPrice() > 0  && oldsp.getPrice()!=sp.getPrice()) {
    		oldsp.setPrice(sp.getPrice());;
    	}
    	if (sp.getStopName() != null && !sp.getStopName().isEmpty() && !oldsp.getStopName().equals(sp.getStopName())) {
    		oldsp.setStopName(sp.getStopName());
    	}
    	return stopService.updateStop(oldsp);
    }
    
    /**
     * Driver: Delete stop
     * Core API endpoint: Driver-1.3
     * @param stop
     * @return deleted stop
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/del-stop", method = RequestMethod.DELETE)
    public Stop delStop(@RequestBody Stop sp) {
    	if (sp != null) {
        	stopService.removeStop(sp.getId()); 
        	return sp;
    	} else {
    		return sp; 
    	}
    }
    /**
     * Driver: Find stop
     * Core API endpoint: Driver-1.3
     * @param name of the stop
     * @return found stop
     */
    @PreAuthorize("hasRole('DRIVER') or hasRole('BOSSLI')")
    @RequestMapping(value = "/get-stop-by-name/{stopName}", method = RequestMethod.GET)
    public Stop findStopByName(@PathVariable(name="stopName") String stpName) {
    	return stopService.findStopbyName(stpName);
    }
}
