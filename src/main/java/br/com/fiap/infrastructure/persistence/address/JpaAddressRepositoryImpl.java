package br.com.fiap.infrastructure.persistence.address;

import org.springframework.stereotype.Repository;

import br.com.fiap.domain.entities.Address;
import br.com.fiap.gateways.AddressRepository;
import br.com.fiap.mapper.AddressMapper;

@Repository
public class JpaAddressRepositoryImpl implements AddressRepository {

	JpaAddressRepository jpRepository;

	private JpaAddressRepositoryImpl(JpaAddressRepository jpRepository) {
		this.jpRepository = jpRepository;
	}

	@Override
	public Address save(Address address) {
		return AddressMapper.toDomain(jpRepository.save(AddressMapper.toEntity(address)));
	}

	@Override
	public void delete(Address address) {
		jpRepository.deleteById(address.getId());

	}

}