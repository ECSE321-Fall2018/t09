package ca.mcgill.ecse321.rideshare9;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.mcgill.ecse321.rideshare9.controller.StopController;
import ca.mcgill.ecse321.rideshare9.entity.Stop;
import ca.mcgill.ecse321.rideshare9.repository.StopRepository;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
public class StopControllerTests {
	private MockMvc mvc;
	
	@Mock private StopRepository stopDao;
	
	@InjectMocks private StopController stopController;
	
	private JacksonTester<Stop> jsonStop;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		JacksonTester.initFields(this, new ObjectMapper());
		mvc = MockMvcBuilders.standaloneSetup(stopController).build();
	}
	
	@Test
	public void canCreateStop() throws Exception {
		//create object to stringify to json
		Stop s2 = new Stop();
		s2.setPrice(1.1f);
		s2.setStopName("test");
		//stringify it to json
		String objAsJson = jsonStop.write(s2).getJson();
		//set repository response
		when(stopDao.createStop(anyString(), anyFloat())).thenReturn(s2);
		//start the actual test
		MvcResult result = mvc.perform(
				post("/stop/add-stop")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(objAsJson))
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
	public void canUpdateStop() throws Exception {
		Stop oldS = new Stop();
		oldS.setPrice(0f);
		oldS.setStopName("oldName");
		oldS.setId(1l);
		Stop newS = new Stop();
		newS.setId(1l);
		newS.setPrice(2.2f);
		newS.setStopName("newName");
		//stringify to json
		//String oldSJson = jsonStop.write(oldS).getJson();
		String newSJson = jsonStop.write(newS).getJson();
		//set repository response
		when(stopDao.findStop(1l)).thenReturn(oldS);
		when(stopDao.updateStop(oldS)).thenReturn(newS);
		//start the actual test
		MvcResult result = mvc.perform(
				put("/stop/change-stop")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(newSJson))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();
		//get the response body
		String responseContent = result.getResponse().getContentAsString();
		//make sure it is equal to what we sent
		assertEquals(responseContent, newSJson);
	}
	
	@Test
	public void canDeleteStop() throws Exception {
		Stop s = new Stop();
		s.setId(1);
		s.setPrice(1.1f);
		s.setStopName("test");
		//stringify to json
		String sJson = jsonStop.write(s).getJson();
		//set repository response
		MvcResult result = mvc.perform(
				post("/stop/del-stop")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(sJson))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();
	}
	
	@Test
	public void canFindStopByName() throws Exception {
		Stop s = new Stop();
		s.setId(1);
		s.setPrice(1.1f);
		s.setStopName("test");
		String sJson = jsonStop.write(s).getJson();
		when(stopDao.findStopbyName("test")).thenReturn(s);
		when(stopDao.findStopbyName("nope")).thenReturn(null);
		MvcResult result = mvc.perform(
				get("/stop/get-stop-by-name/test"))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();
		String responseContent = result.getResponse().getContentAsString();
		assertEquals(responseContent, sJson);
		MvcResult result2 = mvc.perform(
				get("/stop/get-stop-by-name/nope"))
				//.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		assertEquals(result2.getResponse().getContentLength(), 0);
	}
}






















