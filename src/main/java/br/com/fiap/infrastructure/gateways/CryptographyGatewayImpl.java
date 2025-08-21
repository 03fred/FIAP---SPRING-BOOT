package br.com.fiap.infrastructure.gateways;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.fiap.domain.gateways.CryptographyGateway;

@Component
public class CryptographyGatewayImpl implements CryptographyGateway {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
    
    @Override
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}