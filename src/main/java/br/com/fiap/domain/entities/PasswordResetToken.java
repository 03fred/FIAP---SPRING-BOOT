package br.com.fiap.domain.entities;

import java.util.Date;
import java.util.Objects;

import br.com.fiap.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PasswordResetToken {
	private static final long serialVersionUID = 1L;
	
	private Long id;

	private String email;

	private String name;

	private String login;

	private String password;

	private Date dtUpdateRow;

	public void onUpdate() {
		this.dtUpdateRow = new Date();
	}

	//private Address address;

	//private Set<Role> userTypesRoles = new HashSet<>();

	//private List<Restaurant> restaurant;

	public PasswordResetToken(UserDTO userDto, String passwordCrypto) {
		this.email = userDto.email();
		this.name = userDto.name();
		this.login = userDto.login();
		this.password = passwordCrypto;
		this.dtUpdateRow = new Date();

//		AddressDTO ad = userDto.address();
		/*
		 * this.address = new Address( null, ad.street(), ad.number(),
		 * ad.neighborhood(), ad.city(), ad.state(), ad.zipCode(), this );
		 */
	}

	public PasswordResetToken(UserDTO userDto) {
		this.email = userDto.email();
		this.name = userDto.name();
		this.login = userDto.login();
		this.dtUpdateRow = new Date();

		/*
		 * AddressDTO ad = userDto.address(); this.address = new Address( null,
		 * ad.street(), ad.number(), ad.neighborhood(), ad.city(), ad.state(),
		 * ad.zipCode(), this );
		 */
	}

	/*public void removeRole(Role role) {
		this.userTypesRoles.remove(role);
	}*/

	@Override
	public int hashCode() {
		return Objects.hash(email, id, login);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PasswordResetToken other = (PasswordResetToken) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id) && Objects.equals(login, other.login);
	}

	public PasswordResetToken(Long id, String email, String name, String login, String password, Date dtUpdateRow) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.login = login;
		this.password = password;
		this.dtUpdateRow = dtUpdateRow;
	
	}

}