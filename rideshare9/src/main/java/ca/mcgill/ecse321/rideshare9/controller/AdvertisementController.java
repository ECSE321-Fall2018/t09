package ca.mcgill.ecse321.rideshare9.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
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

@RestController
@RequestMapping("/adv")
public class AdvertisementController {
	private AdvertisementRepository advService;
    /**
     * create adv
     * @param adv
     */
    @PreAuthorize("hasRole('DRIVER')")
    @RequestMapping(value = "/add-adv", method=RequestMethod.GET)
    public String postAdv() {
        return "Driver can add adertisement";
    }

    /**
     * create
     * @param adv
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('DRIVER') or hasRole('ADMIN')")
    @GetMapping("/passenger/get-all-adv")
    public String searchAdv() {
        return "Everyone can query advertisment";
    }
}
