package br.com.fiap.domain.entities;

import br.com.fiap.dto.AddressDTO;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String neighborhood;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false, name = "zip_code")
    private String zipCode;

    @JsonIgnore
    @OneToOne(mappedBy = "address", fetch = FetchType.LAZY)
    private User user;

    public AddressDTO toDTO() {
        return new AddressDTO(
                this.street,
                this.number,
                this.neighborhood,
                this.city,
                this.state,
                this.zipCode
        );
    }

	@Override
	public int hashCode() {
		return Objects.hash(id, state, zipCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		return Objects.equals(id, other.id) && Objects.equals(state, other.state)
				&& Objects.equals(zipCode, other.zipCode);
	}
    
    

}
