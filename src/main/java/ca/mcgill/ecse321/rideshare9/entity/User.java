package ca.mcgill.ecse321.rideshare9.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * DO NOT EDIT IT ON YOUR OWN!!!
 * ATTENTION: DON'T EDIT ANY CLASS WHOSE NAME HAS "User" or "Security" or "service" or related! Otherwise, no one can log in this system anymore! 
 * if you have suggestions, please contact me in group chat! 
 * @author yuxiangma
 */


@Entity
@Table(name = "tb_user")
public class User implements Serializable, UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String username;
	private String password;
	private String role;
	@Enumerated(EnumType.STRING)
	@Column(name="userStatus")
	private UserStatus status;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
		if (role != null) {
			auths.add(new SimpleGrantedAuthority(this.getRole()));
		}
		return auths;
	}

	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setStatus(UserStatus value) {
		this.status = value;
	}
	
	public UserStatus getStatus() {
		return this.status;
	}
}
