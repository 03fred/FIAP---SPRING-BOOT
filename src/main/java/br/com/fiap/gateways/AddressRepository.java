package br.com.fiap.gateways;

import br.com.fiap.domain.entities.Address;


public interface AddressRepository {
    Address save(Address address);
    void delete(Address address);
}