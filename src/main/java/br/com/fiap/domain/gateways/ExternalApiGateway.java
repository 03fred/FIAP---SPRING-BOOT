package br.com.fiap.domain.gateways;

import java.util.Map;
import java.util.Optional;

public interface ExternalApiGateway {
    
    Optional<String> get(String url, Map<String, String> headers);
    
    Optional<String> post(String url, String requestBody, Map<String, String> headers);
    
    Optional<String> put(String url, String requestBody, Map<String, String> headers);
    
    Optional<String> delete(String url, Map<String, String> headers);
    
    boolean isApiReachable(String url);
}