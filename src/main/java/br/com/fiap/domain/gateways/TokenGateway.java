package br.com.fiap.domain.gateways;

import java.time.LocalDateTime;

public interface TokenGateway {
    
    String generatePasswordResetToken();
    
    String generateJwtToken(String username, Long restaurantId);
    
    boolean validateJwtToken(String token);
    
    String extractUsernameFromJwt(String token);
    
    Long extractRestaurantIdFromJwt(String token);
    
    LocalDateTime calculateExpirationTime(int hoursFromNow);
}