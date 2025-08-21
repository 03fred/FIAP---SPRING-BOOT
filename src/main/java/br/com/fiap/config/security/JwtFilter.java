package br.com.fiap.config.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.fiap.application.useCases.AuthService;
import br.com.fiap.domain.entities.AuthenticatedUser;
import br.com.fiap.domain.entities.User;
import br.com.fiap.domain.gateways.TokenGateway;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter  extends OncePerRequestFilter{
	
	 private AuthService authService;
	 private TokenGateway tokenGateway;
	 
	 private JwtFilter (AuthService authService, TokenGateway tokenGateway) {
		 this.authService = authService;
		 this.tokenGateway = tokenGateway;
	 }
	 
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = buildRequestToken(request);

		if (token != null && tokenGateway.validateJwtToken(token)) {
			
			String login = tokenGateway.extractUsernameFromJwt(token);
			Long restaurantId = tokenGateway.extractRestaurantIdFromJwt(token);

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
