package ca.mcgill.ecse321.rideshare9.service.impl;

import org.springframework.security.core.GrantedAuthority;

/**
 * Authority
 */

public class GrantedAuthorityImpl implements GrantedAuthority {

    private String authority;

    public GrantedAuthorityImpl(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}