package br.com.fiap.domain.gateways;

public interface CryptographyGateway {
    
    String encodePassword(String rawPassword);
    
    boolean verifyPassword(String rawPassword, String encodedPassword);
}