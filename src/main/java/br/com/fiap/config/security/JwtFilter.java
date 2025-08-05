package br.com.fiap.config.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.fiap.interfaces.services.AuthService;
import br.com.fiap.model.AuthenticatedUser;
import br.com.fiap.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter  extends OncePerRequestFilter{
	
	 private AuthService authService;
	 
	 private JwtFilter (AuthService authService) {
		 this.authService = authService;
	 }
	 
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = buildRequestToken(request);

		if (token != null && JwtTokenUtil.parseToken(token) != null) {
			
			String login = JwtTokenUtil.getLogin(token);
			Long restaurantId = JwtTokenUtil.getRestaurantId(token);

			User user = this.authService.getUserByLogin(login);

			AuthenticatedUser authenticatedUser = new AuthenticatedUser(restaurantId, user.getName(), user.getLogin(),
					user.getId(), user.getUserTypesRoles());
			
			Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, "", authenticatedUser.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} else {
			SecurityContextHolder.clearContext();
		}

		filterChain.doFilter(request, response);

	}
	
	private String buildRequestToken(HttpServletRequest request){
		
		String authorizationHeader = request.getHeader("Authorization");
	
		if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer")) {
			return authorizationHeader.substring(7);
		}
		
		return null;
		
	}
}
