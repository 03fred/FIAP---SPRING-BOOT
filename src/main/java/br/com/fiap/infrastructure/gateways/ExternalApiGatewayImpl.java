package br.com.fiap.infrastructure.gateways;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import br.com.fiap.domain.gateways.ExternalApiGateway;

import java.util.Map;
import java.util.Optional;

@Component
public class ExternalApiGatewayImpl implements ExternalApiGateway {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Optional<String> get(String url, Map<String, String> headers) {
        try {
            HttpHeaders httpHeaders = createHeaders(headers);
            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);
            
            return response.getStatusCode().is2xxSuccessful() ? 
                Optional.ofNullable(response.getBody()) : Optional.empty();
        } catch (RestClientException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> post(String url, String requestBody, Map<String, String> headers) {
        try {
            HttpHeaders httpHeaders = createHeaders(headers);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, httpHeaders);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class);
            
            return response.getStatusCode().is2xxSuccessful() ? 
                Optional.ofNullable(response.getBody()) : Optional.empty();
        } catch (RestClientException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> put(String url, String requestBody, Map<String, String> headers) {
        try {
            HttpHeaders httpHeaders = createHeaders(headers);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, httpHeaders);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.PUT, entity, String.class);
            
            return response.getStatusCode().is2xxSuccessful() ? 
                Optional.ofNullable(response.getBody()) : Optional.empty();
        } catch (RestClientException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> delete(String url, Map<String, String> headers) {
        try {
            HttpHeaders httpHeaders = createHeaders(headers);
            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.DELETE, entity, String.class);
            
            return response.getStatusCode().is2xxSuccessful() ? 
                Optional.ofNullable(response.getBody()) : Optional.empty();
        } catch (RestClientException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isApiReachable(String url) {
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.HEAD, null, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (RestClientException e) {
            return false;
        }
    }

    private HttpHeaders createHeaders(Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        
        if (headers != null) {
            headers.forEach(httpHeaders::set);
        }
        
        return httpHeaders;
    }
}