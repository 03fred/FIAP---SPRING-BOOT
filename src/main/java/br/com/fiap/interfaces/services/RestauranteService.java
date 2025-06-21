package br.com.fiap.interfaces.services;

import br.com.fiap.dto.RestauranteDTO;

import java.util.Optional;

public interface RestauranteService {
	
	void save(RestauranteDTO restauranteDTO, Long codigoProprietario);
	
	Optional<RestauranteDTO> getRestauranteById(Long id);

	void update(RestauranteDTO restauranteDTO, Long id);

	void delete(Long id);
}
