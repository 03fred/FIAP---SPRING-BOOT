package br.com.fiap.infrastructure.persistence.address;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAddressRepository extends JpaRepository<JpaAddressEntity, Long> {

}