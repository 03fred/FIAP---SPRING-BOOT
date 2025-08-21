package br.com.fiap.infrastructure.gateways;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fiap.config.security.JwtTokenUtil;
import br.com.fiap.domain.gateways.TokenGateway;

@Component
public class TokenGatewayImpl implements TokenGateway {
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Override
    public String generatePasswordResetToken() {
        return UUID.randomUUID().toString();
    }
    
    @Override
    public String generateJwtToken(String username, Long restaurantId) {
        return jwtTokenUtil.createToken(username, restaurantId);
    }
    
    @Override
    public boolean validateJwtToken(String token) {
        return jwtTokenUtil.parseToken(token) != null;
    }
    
    @Override
    public String extractUsernameFromJwt(String token) {
        return jwtTokenUtil.getLogin(token);
    }
    
    @Override
    public Long extractRestaurantIdFromJwt(String token) {
        return jwtTokenUtil.getRestaurantId(token);
    }
    
    @Override
    public LocalDateTime calculateExpirationTime(int hoursFromNow) {
        return LocalDateTime.now().plusHours(hoursFromNow);
    }
}