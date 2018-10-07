package ca.mcgill.ecse321.rideshare9;

import static org.junit.Assert.assertEquals;
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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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


public class AdvertisementControllerTests{

	private MockMvc mvc;
	
	@Mock private AdvertisementRepository adDao;
	
	@Mock private UserRepository userDao;
	
	@InjectMocks private AdvertisementController adController;
	
	@Mock private UserService userController;
	
	@Mock private Authentication authentication;
	
	@Mock private SecurityContextHolder security;
	
	private JacksonTester<Advertisement> jsonAd;
	private JacksonTester<ArrayList<AdvBestQuery>> jsonQuery;
	private JacksonTester<AdvQuery> jsonRegQuery;
	private JacksonTester<ArrayList<AdvResponse>> jsonResponse;
	private JacksonTester<ArrayList<Advertisement>> jsonAdList;
	
	private Advertisement testAd;
	private Advertisement newAd;
	private User testDriver;
	private Set<Long> testStops =  new HashSet<Long>();
	private Date testDate = new Date(19970803);
	
	
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
		mvc = MockMvcBuilders.standaloneSetup(adController).build();
		//Set up a test Ad
		testAd = new Advertisement();
		testAd.setDriver(123);
		testAd.setId(1234);
		testAd.setSeatAvailable(5);
		testAd.setStartLocation("Montreal");
		testAd.setStartTime(testDate);
		testAd.setStatus(TripStatus.REGISTERING);
		testStops.add((long) 222);
		testAd.setStops(testStops);
		testAd.setTitle("Quebec Trip");
		testAd.setVehicle(111);
		newAd = testAd;
		newAd.setStartLocation("Vancouver");
		
		//Set up a test Driver 
		testDriver = new User();
		testDriver.setId(123);
		testDriver.setPassword("abc");
		testDriver.setRole("BOSSLI");
		testDriver.setStatus(UserStatus.STANDBY);
		testDriver.setUsername("Thomas");
	}
	
	//Trouble setting up authentication for this test
	@Test
	public void canCreateAd() throws Exception {
		testDriver.setRole("BOSSLI");
		
		 UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken ("Thomas","abc", testDriver.getAuthorities());
		   MockHttpSession session = new MockHttpSession();
	        session.setAttribute(
	                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
	                new MockSecurityContext(principal));
	     
	    SecurityContextHolder.getContext().setAuthentication(principal);  
	       
		//strinify Ad to json
		String objAsJson = jsonAd.write(testAd).getJson();
		//set repository response
		when(adDao.createAdv(anyString(), any(), anyString(), anyInt(), any(), anyLong(), anyLong())).thenReturn(testAd);
		when(userController.findUserByUsername(anyString())).thenReturn(testDriver);
		when(userController.loadUserByUsername(anyString())).thenReturn(testDriver);
	
		//start the actual test
		MvcResult result = mvc.perform(
				
				post("/adv/create-adv")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(objAsJson)
					.session(session))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();
		//get the response body
		String responseContent = result.getResponse().getContentAsString();
		//make sure it is equal to what we sent
		assertEquals(responseContent, objAsJson);
	}
	
	@Test
	public void canDeleteAd() throws Exception {
		
		testDriver.setRole("BOSSLI");
		
		 UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken ("Thomas","abc", testDriver.getAuthorities());
		   MockHttpSession session = new MockHttpSession();
	        session.setAttribute(
	                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
	                new MockSecurityContext(principal));
	     
	    SecurityContextHolder.getContext().setAuthentication(principal);  
	    
		//strinify Ad to json
		String objAsJson = jsonAd.write(testAd).getJson();
		//set repository response
		when(adDao.createAdv(anyString(), any(), anyString(), anyInt(), any(), anyLong(), anyLong())).thenReturn(testAd);
		when(userController.findUserByUsername(anyString())).thenReturn(testDriver);
		when(userController.loadUserByUsername(anyString())).thenReturn(testDriver);
		
	
		//start the actual test
		MvcResult result = mvc.perform(
				delete("/adv/delete-adv")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(objAsJson)
					.session(session))
				.andExpect(status().isOk())
				.andReturn();
	
	}
	
	@Test
	public void canChangeAd() throws Exception {
		
		testDriver.setRole("BOSSLI");
		
		 UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken ("Thomas","abc", testDriver.getAuthorities());
		   MockHttpSession session = new MockHttpSession();
	        session.setAttribute(
	                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
	                new MockSecurityContext(principal));
	     
	    SecurityContextHolder.getContext().setAuthentication(principal);  
	    
		//strinify Ad to json
		String objAsJson = jsonAd.write(newAd).getJson();
		//set repository response
		when(adDao.findAdv(anyLong())).thenReturn(testAd);
		when(userController.findUserByUsername(anyString())).thenReturn(testDriver);
		when(adDao.updateAdv(any())).thenReturn(testAd);
		when(userController.loadUserByUsername(anyString())).thenReturn(testDriver);
	
		//start the actual test
		MvcResult result = mvc.perform(
				put("/adv/update-adv")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(objAsJson)
					.session(session))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();
		
				//get the response body
				String responseContent = result.getResponse().getContentAsString();
				//make sure it is equal to what we updated
				assertEquals(responseContent, objAsJson);
	
	}
	
	@Test
	public void canGetTopDriver() throws Exception {
		
		testDriver.setRole("BOSSLI");
		
		 UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken ("Thomas","abc", testDriver.getAuthorities());
		   MockHttpSession session = new MockHttpSession();
	        session.setAttribute(
	                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
	                new MockSecurityContext(principal));
	     
	    SecurityContextHolder.getContext().setAuthentication(principal);  
	    
	    ArrayList<AdvBestQuery> testQueries = new ArrayList<AdvBestQuery>();
	    AdvBestQuery testQuery = new AdvBestQuery();
	    testQuery.setBest(testDriver);
	    testQuery.setCount((long)22);
	    testQueries.add(testQuery);
		//strinify Query to json
		String objAsJson = jsonQuery.write(testQueries).getJson();
		//set repository response
		when(adDao.findBestDriver()).thenReturn(testQueries);
		
	
		//start the actual test
		MvcResult result = mvc.perform(
				get("/adv/get-top-drivers")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(objAsJson)
					.session(session))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();
		
				//get the response body
				String responseContent = result.getResponse().getContentAsString();
				//make sure it is equal to what we updated
				assertEquals(responseContent, objAsJson);
	
	}
	
	@Test
	public void canFindMyAds() throws Exception {
		
		testDriver.setRole("BOSSLI");
		
		 UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken ("Thomas","abc", testDriver.getAuthorities());
		   MockHttpSession session = new MockHttpSession();
	        session.setAttribute(
	                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
	                new MockSecurityContext(principal));
	     
	    SecurityContextHolder.getContext().setAuthentication(principal);  
	    
	    ArrayList<Advertisement> adList = new ArrayList<Advertisement>();
	    adList.add(testAd);
	    
		//strinify Ad to json
		String objAsJson = jsonAdList.write(adList).getJson();
		//set repository response
		when(adDao.findAllAdv(anyLong())).thenReturn(adList);
		when(userController.findUserByUsername(anyString())).thenReturn(testDriver);
		when(userController.loadUserByUsername(anyString())).thenReturn(testDriver);
		when(adDao.updateAdv(any())).thenReturn(testAd);
		
	
		//start the actual test
		MvcResult result = mvc.perform(
				get("/adv/get-logged-adv")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(objAsJson)
					.session(session))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();
		
				//get the response body
				String responseContent = result.getResponse().getContentAsString();
				//make sure it is equal to what we updated
				assertEquals(responseContent, objAsJson);
	
	}
	
	@Test
	public void canSearchAdv() throws Exception {
		
		
		 UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken ("Thomas","abc", testDriver.getAuthorities());
		   MockHttpSession session = new MockHttpSession();
	        session.setAttribute(
	                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
	                new MockSecurityContext(principal));
	     
	    SecurityContextHolder.getContext().setAuthentication(principal);  
	    
	    //Test Queries

	    AdvQuery testAsk = new AdvQuery();
	    testAsk.setSortByPrice(true);
	    testAsk.setvColor("Blue");
		//strinify Query to json
	    
	    //Test Response
	    ArrayList<AdvResponse> testQueries = new ArrayList<AdvResponse>();
	    AdvResponse testQuery = new AdvResponse(testAd, (float) 30.0);
	    testQueries.add(testQuery);
		//strinify Query to json
		String queryAsJson = jsonRegQuery.write(testAsk).getJson();
		String adAsJson = jsonResponse.write(testQueries).getJson();
		//set repository response
		when(adDao.findAdvByCriteriaAndColorSortByPrice(any())).thenReturn(testQueries);
		/*when(adDao.findAdvByCriteriaAndColorSortByTime(any())).thenReturn(testQueries);
		when(adDao.findAdvByCriteriaAndModelAndColorSortByPrice(any())).thenReturn(testQueries);
		when(adDao.findAdvByCriteriaAndModelAndColorSortByTime(any())).thenReturn(testQueries);
		when(adDao.findAdvByCriteriaAndModelSortByPrice(any())).thenReturn(testQueries);
		when(adDao.findAdvByCriteriaAndModelSortByTime(any())).thenReturn(testQueries);
		when(adDao.findAdvByCriteriaSortByPrice(any())).thenReturn(testQueries);
		when(adDao.findAdvByCriteriaSortByTime(any())).thenReturn(testQueries); */
		//start the actual test
		MvcResult result = mvc.perform(
				post("/adv/get-adv-search")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(queryAsJson)
					.session(session))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();
		
				//get the response body
				String responseContent = result.getResponse().getContentAsString();
				//make sure it is equal to what we updated
				assertEquals(responseContent, adAsJson);
	
	}
	
	@Test
	public void canFindAllAds() throws Exception {
		
		 UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken ("Thomas","abc", testDriver.getAuthorities());
		   MockHttpSession session = new MockHttpSession();
	        session.setAttribute(
	                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
	                new MockSecurityContext(principal));
	     
	    SecurityContextHolder.getContext().setAuthentication(principal);  
	    
	    ArrayList<Advertisement> adList = new ArrayList<Advertisement>();
	    adList.add(testAd);
	    
		//strinify Ad to json
		String objAsJson = jsonAdList.write(adList).getJson();
		//set repository response
		when(adDao.findAllAdv()).thenReturn(adList);
	
		
	
		//start the actual test
		MvcResult result = mvc.perform(
				get("/adv/get-list-adv")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(objAsJson)
					.session(session))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();
		
				//get the response body
				String responseContent = result.getResponse().getContentAsString();
				//make sure it is equal to what we updated
				assertEquals(responseContent, objAsJson);
	
	}
	
	
	
	
}
