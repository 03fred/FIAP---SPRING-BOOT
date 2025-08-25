package br.com.fiap.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Address {

	 private Long id;
	    private String street;
	    private String number;
	    private String neighborhood;
	    private String city;
	    private String state;
	    private String zipCode;
        private User user;
}
