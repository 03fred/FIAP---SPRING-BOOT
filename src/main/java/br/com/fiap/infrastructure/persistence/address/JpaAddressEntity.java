package br.com.fiap.infrastructure.persistence.address;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.fiap.infrastructure.persistence.user.JpaUserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Entity
@Table(name = "addresses")
public class JpaAddressEntity{

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
	    private JpaUserEntity user;

}