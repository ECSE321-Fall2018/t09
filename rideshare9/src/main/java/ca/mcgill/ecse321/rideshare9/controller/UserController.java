package ca.mcgill.ecse321.rideshare9.controller;

import ca.mcgill.ecse321.rideshare9.entity.User;
import ca.mcgill.ecse321.rideshare9.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/crud")
public class UserController {

    private UserService userService;

    UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * retrive
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public List<User> userServiceList(){
        return userService.getUsers();
    }
	
    /**
     * retrive 
     * @param username
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{username}")
    public User userProfile(@PathVariable String username){
        return userService.loadUserByUsername(username);
    }
    

    /**
     * create
     * @param user
     */
    @PostMapping("/sign-up")
    public User signUp(@RequestBody User user) {
        user.setPassword(DigestUtils.md5DigestAsHex((user.getPassword()).getBytes()));
        return userService.addUser(user);
    }
    
    /**
     * delete
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/delete/{uid}")
    public int deleteUser(@PathVariable String uid){
    	Long userid = -1L; 
    	String username=""; 
    	try {
    		userid = Long.valueOf(uid); 
    		return userService.deleteUserByUID(userid);
    	} catch (Exception e){
    		username = uid; 
    		return userService.deleteUserByUname(username);
    	}
         
    }
}