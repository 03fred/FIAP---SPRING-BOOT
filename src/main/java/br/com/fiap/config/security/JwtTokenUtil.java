package br.com.fiap.config.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtTokenUtil {

	private final static String JWT_SECRET = "AA";
	
	public static String createToken() {
		
		return JWT.create()
				.withSubject("name")
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + 100 * 60 * 60))
				.sign(Algorithm.HMAC256(JWT_SECRET));
	}
	
	public static DecodedJWT parseToken(String token) {
		try {
			Algorithm alg = Algorithm.HMAC256(JWT_SECRET);
			JWTVerifier verifier = JWT.require(alg).build();
			return verifier.verify(token);
		} catch (JWTVerificationException e) {
			// TODO TRATAR ERRO
			return null;
		}

	}
	
	public static Authentication getAuthentication(String token) {
		DecodedJWT decodedJWT = JwtTokenUtil.parseToken(token);
		String userName = decodedJWT.getSubject();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		UserDetails userDetails = new User(userName, "", authorities);
		return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
	}
}
