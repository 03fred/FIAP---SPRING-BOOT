package br.com.fiap.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConf {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter,
			AuthorizationManager<RequestAuthorizationContext> userAuthorizationManager) throws Exception {
		  return http
	                .csrf(AbstractHttpConfigurer::disable)
	                .authorizeHttpRequests(
	                        req -> req.requestMatchers(
									"/h2-console/**",
											"/auth/**",
											"/v3/api-docs/**",
											"/swagger-ui/**",
											"/swagger-ui.html",
											"/swagger-resources/**",
											"/users_type/**",
											"/webjars/**")
	                                .permitAll()
	                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.POST, "users").permitAll()
	                                .anyRequest().authenticated())
	                .sessionManagement(
	                        sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
	                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())) // Add this line
	                .build();
	}
	
	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl roleHierarchyImpl = new RoleHierarchyImpl();
		String hierarchy = "ROLE_ADMIN > ROLE_USER";
		roleHierarchyImpl.setHierarchy(hierarchy);
		return roleHierarchy();
	}

	@Bean
	public MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setRoleHierarchy(roleHierarchy);
		return expressionHandler;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}