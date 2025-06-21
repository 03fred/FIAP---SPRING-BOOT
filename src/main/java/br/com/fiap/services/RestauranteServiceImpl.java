package br.com.fiap.services;

import br.com.fiap.dto.RestauranteDTO;
import br.com.fiap.dto.UserResponseDTO;
import br.com.fiap.interfaces.repositories.RestauranteRepository;
import br.com.fiap.interfaces.services.RestauranteService;
import br.com.fiap.interfaces.services.UserService;
import br.com.fiap.model.Restaurante;
import br.com.fiap.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RestauranteServiceImpl implements RestauranteService {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@Override
	public void save(RestauranteDTO restauranteDTO, Long codigoProprietario) {
		UserResponseDTO userResponseDTO = userService.getUserById(Long.valueOf(codigoProprietario));
		//Criar um tratamento de excecao caso não existe proprietario cadastrado no banco.
		if (userResponseDTO != null) {
			// converter para entity
			//Restaurante restaurante = new Restaurante(restauranteDTO, userResponseDTO);
			//restauranteRepository.save(restaurante);
		}
	}


	@Override
	public Optional<RestauranteDTO> getRestauranteById(Long id) {
		return Optional.empty();
	}

	@Override
	public void update(RestauranteDTO restauranteDTO, Long id) {

	}

	@Override
	public void delete(Long id) {

	}
}
