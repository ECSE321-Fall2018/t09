package ca.mcgill.ecse321.rideshare9.jwt;

import ca.mcgill.ecse321.rideshare9.entity.User;
import ca.mcgill.ecse321.rideshare9.exception.TokenException;
import ca.mcgill.ecse321.rideshare9.service.UserService;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
	
	public static final String SIGNING_KEY = "spring-security-@Jwt!&Secret^#";

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private UserService userService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            // parse the token.
            String username = null;
            try {
                username = Jwts.parser()
                        .setSigningKey(SIGNING_KEY)
                        .parseClaimsJws(token.replace("Bearer ", ""))
                        .getBody()
                        .getSubject();
                logger.info(username);
                if (username != null) {
                    User user = userService.loadUserByUsername(username);
                    logger.info("---"+user.getAuthorities().toString());
                    return new UsernamePasswordAuthenticationToken(username, user.getAuthorities(), user.getAuthorities());
                }
            } catch (ExpiredJwtException e) {
                logger.error("expired {} " + e);
                throw new TokenException("Token expired");
            } catch (UnsupportedJwtException e) {
                logger.error("wrong format {} " + e);
                throw new TokenException("Token expired");
            } catch (MalformedJwtException e) {
                logger.error("Token malformed: {} " + e);
                throw new TokenException("Token malformed");
            } catch (SignatureException e) {
                logger.error("failed signature: {} " + e);
                throw new TokenException("failed signatur");
            } catch (IllegalArgumentException e) {
                logger.error("illegal arg: {} " + e);
                throw new TokenException("illegal arg");
            }
        }
        return null;
    }
}