package br.com.fiap.services;

import br.com.fiap.dto.RestauranteDTO;
import br.com.fiap.exceptions.ResourceNotFoundException;
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
		Optional<User> user = userService.getUser(Long.valueOf(codigoProprietario));
		if (user.isPresent()) {
			Restaurante restaurante = new Restaurante(restauranteDTO, user.get());
			restauranteRepository.save(restaurante);
		}
	}

	@Override
	public Optional<RestauranteDTO> getRestauranteById(Long id) {
		return Optional.empty();
	}

	@Override
	public void update(RestauranteDTO restauranteDTO, Long id) {
		Restaurante restaurante = restauranteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado com o id: " + id));

		restaurante.setEndereco(restauranteDTO.endereco());
		restaurante.setNome(restauranteDTO.endereco());
		restaurante.setHorarioFuncionamento(restauranteDTO.endereco());
		restaurante.setTipoCozinha(restauranteDTO.tipoCozinha());

		restauranteRepository.save(restaurante);
	}

	@Override
	public void delete(Long id) {
		Restaurante restaurante = restauranteRepository.findById(id)
				.orElseThrow(
						() -> new ResourceNotFoundException("Restaurante não encontrado com o id: " + id + "."));
		restauranteRepository.delete(restaurante);
	}
}
