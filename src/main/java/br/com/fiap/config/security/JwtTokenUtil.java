package br.com.fiap.config.security;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtTokenUtil {

	private final static String JWT_SECRET = "AA";
	
	public static String createToken(String login) {
		
		return JWT.create()
				.withSubject(login)
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
	
	public static String getAuthentication(String token) {
		DecodedJWT decodedJWT = JwtTokenUtil.parseToken(token);
		return  decodedJWT.getSubject();
	}
}
