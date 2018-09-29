package ca.mcgill.ecse321.rideshare9.service.impl;

import ca.mcgill.ecse321.rideshare9.entity.User;
import ca.mcgill.ecse321.rideshare9.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.DigestUtils;

/**
 * Auth
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private UserService userservice;

    private BCryptPasswordEncoder encoder;

    public CustomAuthenticationProvider(BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService){
        this.encoder = bCryptPasswordEncoder;
        this.userservice = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String uname = authentication.getName();
        String pswd = authentication.getCredentials().toString();
        User usr = userservice.loadUserByUsername(uname);
        System.out.println(usr.getAuthorities());
        if(null != usr){
            String encodePassword = DigestUtils.md5DigestAsHex((pswd).getBytes());
            if(usr.getPassword().equals(encodePassword)){
                Authentication auth = new UsernamePasswordAuthenticationToken(uname, pswd, usr.getAuthorities());
                return auth;
            }else {
                throw new BadCredentialsException("wrong pswd");
            }
        } else {
            throw new UsernameNotFoundException("usr dne");
        }
    }

    /**
     * authorized?
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}