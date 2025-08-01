package br.com.fiap.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.fiap.dto.AddressDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.fiap.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode
@ToString
@Entity 
@Table(name = "users") 
public class User {
    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_update_row")
    private Date dtUpdateRow;

    @PreUpdate
    public void onUpdate() {
        this.dtUpdateRow = new Date();
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;


    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
        name = "user_types_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> userTypesRoles = new HashSet<>(); 

    @JsonBackReference
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "restaurantOwner", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Restaurant> restaurant;


    public User(UserDTO userDto, String passwordCrypto) {
        this.email = userDto.email();
        this.name = userDto.name();
        this.login = userDto.login();
        this.password = passwordCrypto;
        this.dtUpdateRow = new Date();

        AddressDTO ad = userDto.address();
        this.address = new Address(
                null,
                ad.street(),
                ad.number(),
                ad.neighborhood(),
                ad.city(),
                ad.state(),
                ad.zipCode(),
                this
        );
    }

    public User(UserDTO userDto) {
        this.email = userDto.email();
        this.name = userDto.name();
        this.login = userDto.login();
        this.dtUpdateRow = new Date();

        AddressDTO ad = userDto.address();
        this.address = new Address(
                null,
                ad.street(),
                ad.number(),
                ad.neighborhood(),
                ad.city(),
                ad.state(),
                ad.zipCode(),
                this
        );
    }


    public void removeRole(Role role) {
        this.userTypesRoles.remove(role);
    }
}