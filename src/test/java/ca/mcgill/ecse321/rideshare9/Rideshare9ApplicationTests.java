package ca.mcgill.ecse321.rideshare9;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.mcgill.ecse321.rideshare9.controller.StopController;
import ca.mcgill.ecse321.rideshare9.entity.Stop;
import ca.mcgill.ecse321.rideshare9.repository.StopRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class Rideshare9ApplicationTests {
	private MockMvc mvc;
	@Mock
	private StopRepository stopDao;
	@InjectMocks
	private StopController stopController;
	private JacksonTester<Stop> jsonStop;
	
	@BeforeEach
	public void setup() {
		JacksonTester.initFields(this, new ObjectMapper());
		mvc = MockMvcBuilders.standaloneSetup(stopController).build();
	}
	
	@Test
	public void contextLoads(){
		assertTrue(true);
	}
	
	@Test
	public void canCreateStop() throws Exception {
		Stop s = new Stop();
		s.setId(1);
		s.setPrice(1.1f);
		s.setStopName("test");
		when(stopDao.createStop(anyString(), anyFloat())).thenReturn(new Stop(1l, 1.1f, "test"));
		Stop s2 = new Stop();
		s2.setPrice(1.1f);
		s2.setStopName("test");
		MvcResult response = mvc.perform(post("/stop/add-stop")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(jsonStop.write(s2).getJson())
				).andReturn();
		System.out.println(response.getClass().toString());
	}
}
