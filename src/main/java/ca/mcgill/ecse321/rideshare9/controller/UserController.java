package ca.mcgill.ecse321.rideshare9.controller;

import ca.mcgill.ecse321.rideshare9.entity.User;
import ca.mcgill.ecse321.rideshare9.entity.UserStatus;
import ca.mcgill.ecse321.rideshare9.jwt.JWTLoginFilter;
import ca.mcgill.ecse321.rideshare9.repository.UserRepository;
import ca.mcgill.ecse321.rideshare9.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * DO NOT EDIT IT ON YOUR OWN!!! THIS API DOES NOT SUPPORT UPDATE
 * ATTENTION: DON'T EDIT ANY CLASS WHOSE NAME HAS "User" or "Security" or "service" or related! Otherwise, no one can log in this system anymore! 
 * if you have suggestions, please contact me in group chat! 
 * @author yuxiangma
 */

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
    private UserService userService;
	@Autowired
	private UserRepository uRepo; 
    
    
    /**
     * Admin: Retrive all user profiles
     * Core API endpoint: Admin-1 in README.md at Mark branch
     * @return List<User>
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSSLI')")
    @GetMapping("/get-list-users")
    public List<User> userServiceList(){    	
        return userService.getUsers();
    }
    
    /**
     * Admin: Retrive all user profiles, Modify this if you would like just user status
     * Core API endpoint: Admin-1 in README.md at Mark branch
     * @param query Name
     * @return List<User>
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSSLI')")
    @GetMapping("/get-list-driver-active/{name}")
    public List<HashMap<String, UserStatus>> driverStatusList(@PathVariable(value = "name") String name){

    	ArrayList<HashMap<String, UserStatus>> arrl = new ArrayList<HashMap<String, UserStatus>>(); 
    	List<User> allU = uRepo.findByUsernameWithStatus(name); 
    	for (User usr: allU) {
    		if (usr.getRole().equals("ROLE_DRIVER")) {
    			HashMap<String, UserStatus> curr = new HashMap<String, UserStatus>();
    			curr.put(usr.getUsername(), usr.getStatus()); 
    			if (usr.getStatus().equals(UserStatus.ON_RIDE)) {
    				arrl.add(curr); 
    			}
    		}
    			 
    	}  	
        return arrl;
    }
    /**
     * Admin: Retrive all user profiles, Modify this if you would like just user status
     * Core API endpoint: Admin-1 in README.md at Mark branch
     * @param query Name
     * @return List<User>
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSSLI')")
    @GetMapping("/get-list-passenger-active/{name}")
    public List<HashMap<String, UserStatus>> passengerStatusList(@PathVariable(value = "name") String name){

    	ArrayList<HashMap<String, UserStatus>> arrl = new ArrayList<HashMap<String, UserStatus>>(); 
    	List<User> allU = uRepo.findByUsernameWithStatus(name); 
    	for (User usr: allU) {
    		if (usr.getRole().equals("ROLE_PASSENGER")) {
    			HashMap<String, UserStatus> curr = new HashMap<String, UserStatus>();
    			curr.put(usr.getUsername(), usr.getStatus()); 
    			if (usr.getStatus().equals(UserStatus.ON_RIDE)) {
    				arrl.add(curr); 
    			}
    		}
    			 
    	}  	
        return arrl;
    }
	
    /**
     * Admin: retrive user profile of a user, only username needed
     * @param username
     * @return User
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSSLI')")
    @PostMapping("/get-user-by-uname")
    public User userProfile(@RequestBody User usr){
        return userService.loadUserByUsername(usr.getUsername());
    }
    
    /**
     * All registered user: retrive current user profile of current logged in user
     * @param void
     * @return User
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('DRIVER') or hasRole('ADMIN') or hasRole('BOSSLI')")
    @GetMapping("/get-logged-user")
    public User userIDnow(){
    	String currentUserName = ""; 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    currentUserName = authentication.getName();
    	}
        return userService.loadUserByUsername(currentUserName);
    }
    
    /**
     * All registered user: change status to on ride
     * @param void
     * @return User
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('DRIVER') or hasRole('ADMIN') or hasRole('BOSSLI')")
    @PutMapping("/update-status")
    public User userStatus(@RequestBody User u){
    	String currentUserName = ""; 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    currentUserName = authentication.getName();
    	}
        return userService.changeUserStatus(userService.loadUserByUsername(currentUserName).getId(), u.getStatus());
    }
    
    
    /**
     * HelloWorld, to test role intercepting/authorization 
     * @param 
     * @return str("Ok")
     */
    
    @PreAuthorize("hasRole('PASSENGER') or hasRole('BOSSLI')")
    @GetMapping("/hello")
    public String helloW(){
        return "Ok!";
    }
   
    
    /**
     * HelloWorld, greet to everyone (new users)!  
     * @param 
     * @return hi 
     */
    @GetMapping("/mainpg")
    public String reservedMainpage(){
        return "Hi, welcome to RideShare9! ";
    }
    

    /**
     * All people: Signup
     * Bonus Poing
     * @param User
     */
    @PostMapping("/sign-up")
    public User signUp(@RequestBody User user) {
        user.setPassword(DigestUtils.md5DigestAsHex((user.getPassword()).getBytes()));
        user.setStatus(UserStatus.STANDBY);
        return userService.addUser(user);
    }
    
    /**
     * All people: before sign-up, check if username is valid (not duplicated)
     * Bonus Point
     * @param User
     */
    @PostMapping("/get-is-unique")
    public boolean checkValidUname(@RequestBody User user) {
    	try {
    		userService.loadUserByUsername(user.getUsername()); 
    	} catch (Exception e) {
    		return true; 
    	}
        return false; 
    }
    
    /**
     * Admin: delete user by uid
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSSLI')")
    @DeleteMapping("/delete-usr")
    public int deleteUser(@RequestBody User u){
    	String username=""; 
		username = u.getUsername(); 
		return userService.deleteUserByUname(username);
         
    }
    
}