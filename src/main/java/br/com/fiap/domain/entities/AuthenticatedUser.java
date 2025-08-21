package br.com.fiap.domain.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticatedUser implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	private Long restaurantId;
	private String username;
	private String password;
	private String login;
	private Long id;
	private Set<Role> userTypesRoles = new HashSet<>(); 
	
	public AuthenticatedUser(Long restaurantId, String username, String login, Long id,
			Set<Role> userTypesRoles) {
		super();
		this.restaurantId = restaurantId;
		this.username = username;
		this.login = login;
		this.id = id;
		this.userTypesRoles = userTypesRoles;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getUserTypesRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role.getName()))
		.collect(Collectors.toList());
	}

	public boolean hasRoleAdmin() {
		return getAuthorities().stream().anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()));
	}
}
