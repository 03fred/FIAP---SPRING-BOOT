package br.com.fiap.domain.gateways;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Generic database gateway interface for data persistence operations
 * @param <T> Entity type
 * @param <ID> Entity ID type
 */
public interface DatabaseGateway<T, ID> {
    
    T save(T entity);
    
    Optional<T> findById(ID id);
    
    List<T> findAll();
    
    Page<T> findAll(Pageable pageable);
    
    void deleteById(ID id);
    
    void delete(T entity);
    
    boolean existsById(ID id);
    
    long count();
}