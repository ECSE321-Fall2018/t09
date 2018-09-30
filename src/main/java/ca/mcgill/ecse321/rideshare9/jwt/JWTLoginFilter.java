package ca.mcgill.ecse321.rideshare9.jwt;
import com.fasterxml.jackson.databind.ObjectMapper;
import ca.mcgill.ecse321.rideshare9.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
/**
 * DO NOT EDIT IT ON YOUR OWN!!!
 * ATTENTION: DON'T EDIT ANY CLASS WHOSE NAME HAS "User" or "Security" or "service" or related! Otherwise, no one can log in this system anymore! 
 * if you have suggestions, please contact me in group chat! 
 * @author yuxiangma
 */

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {
	
	public static final String SIGNING_KEY = "spring-security-@Jwt!&Secret^#";

    private AuthenticationManager authenticationManager;

    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(req.getInputStream(), User.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
        String token = null;
        logger.info(auth.getName());
        try {
            token = Jwts.builder().setSubject(auth.getName()).setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 3 * 1000)) .signWith(SignatureAlgorithm.HS512, SIGNING_KEY) .compact();
            res.addHeader("Authorization", "Bearer " + token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*
     *   datasource:
    url : jdbc:mysql://localhost:3306/carpool
    username : group9
    password : ecse321
    driverClassName : com.mysql.jdbc.Driver
     */
}