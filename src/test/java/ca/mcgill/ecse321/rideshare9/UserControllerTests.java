package ca.mcgill.ecse321.rideshare9;
import org.springframework.test.context.junit.jupiter.SpringExtension; 
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.*;

import java.sql.Date;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.mcgill.ecse321.rideshare9.controller.AdvertisementController;
import ca.mcgill.ecse321.rideshare9.controller.UserController;
import ca.mcgill.ecse321.rideshare9.entity.Advertisement;
import ca.mcgill.ecse321.rideshare9.entity.Stop;
import ca.mcgill.ecse321.rideshare9.entity.TripStatus;
import ca.mcgill.ecse321.rideshare9.entity.User;
import ca.mcgill.ecse321.rideshare9.entity.UserStatus;
import ca.mcgill.ecse321.rideshare9.entity.helper.AdvBestQuery;
import ca.mcgill.ecse321.rideshare9.entity.helper.AdvQuery;
import ca.mcgill.ecse321.rideshare9.entity.helper.AdvResponse;
import ca.mcgill.ecse321.rideshare9.repository.AdvertisementRepository;
import ca.mcgill.ecse321.rideshare9.repository.UserRepository;
import ca.mcgill.ecse321.rideshare9.service.UserService;
import ca.mcgill.ecse321.rideshare9.service.impl.UserServiceImpl;
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserControllerTests{

	private MockMvc mvc;
	
	@Mock private AdvertisementRepository adDao;
	
	@Mock private UserRepository userDao;
	
	@InjectMocks private UserController userController;
	
	@Mock private UserService userServ;
	
	@Mock private Authentication authentication;
	
	@Mock private SecurityContextHolder security;
	
	private JacksonTester<User> jsonUser;
	private JacksonTester<ArrayList<AdvBestQuery>> jsonQuery;
	private JacksonTester<AdvQuery> jsonRegQuery;
	private JacksonTester<ArrayList<AdvResponse>> jsonResponse;
	private JacksonTester<ArrayList<User>> jsonUserList;
	
	private User testUser;
	private User testDriver;
	private User testPassenger;
	private Set<Long> testStops =  new HashSet<Long>();
	private Date testDate = new Date(19970803);
	private ArrayList<User> listOfUser; 
	
	
	public static class MockSecurityContext implements SecurityContext {

        private static final long serialVersionUID = -1386535243513362694L;

        private Authentication authentication;

        public MockSecurityContext(Authentication authentication) {
            this.authentication = authentication;
        }

        @Override
        public Authentication getAuthentication() {
            return this.authentication;
        }

        @Override
        public void setAuthentication(Authentication authentication) {
            this.authentication = authentication;
        }
    }
	
	@Before
	public void setup() {
		//Init testing objects
		MockitoAnnotations.initMocks(this);
		JacksonTester.initFields(this, new ObjectMapper());
		mvc = MockMvcBuilders.standaloneSetup(userController).build();
		//Set up a test Ad
		testUser = new User();
		testUser.setId(233L);
		testUser.setPassword("ecse321");
		testUser.setRole("ROLE_BOSSLI");
		testUser.setStatus(UserStatus.STANDBY);
		testUser.setUsername("bossli"); 
		
		//Set up a test Driver 
		testDriver = new User();
		testDriver.setId(123);
		testDriver.setPassword("abcdef");
		testDriver.setRole("ROLE_DRIVER");
		testDriver.setStatus(UserStatus.STANDBY);
		testDriver.setUsername("Yudixie");
		testPassenger = new User();
		testPassenger.setId(234);
		testPassenger.setPassword("abcdef");
		testPassenger.setRole("ROLE_PASSENGER");
		testPassenger.setStatus(UserStatus.STANDBY);
		testPassenger.setUsername("Yuxiangma");
		listOfUser = new ArrayList<User>(); 
		listOfUser.add(testPassenger); 
		listOfUser.add(testUser); 
		listOfUser.add(testDriver); 
	}
	
	@Test
	public void canSignUp() throws Exception {
		String objAsJson = jsonUser.write(testUser).getJson();
		when(userServ.addUser(any())).thenReturn(testUser);
		assertEquals(userController.signUp(this.testUser), this.testUser); 
	}
	
	
	@Test
	public void canGetDriverList() throws Exception {
		when(userServ.getUsers()).thenReturn(listOfUser);
		List<HashMap<String, UserStatus>> drivers = userController.driverStatusList(); 
		for (HashMap<String, UserStatus> usr: drivers) {
			Object[] keys = usr.keySet().toArray();
			assertTrue(keys[0].equals("Yudixie")); 	 
    	}  			
	}
	@Test
	public void canGetPassengerList() throws Exception {
		when(userServ.getUsers()).thenReturn(listOfUser);
		List<HashMap<String, UserStatus>> passengers = userController.passengerStatusList(); 
		for (HashMap<String, UserStatus> usr: passengers) {
			Object[] keys = usr.keySet().toArray();
			assertTrue(keys[0].equals("Yuxiangma")); 	 
    	}  			
	}
}
