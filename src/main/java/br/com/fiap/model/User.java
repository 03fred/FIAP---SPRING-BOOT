package br.com.fiap.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.fiap.dto.UserDTO;
import br.com.fiap.model.enums.EnumUserType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity 
@Table(name = "users") 
public class User implements UserDetails {
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
    
    @Column(nullable = false)
    private String address;

    @ManyToMany(fetch=  FetchType.EAGER, cascade = CascadeType.PERSIST)
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
    
    
    public User(UserDTO userDto, EnumUserType enumType, String passwordCrypto) {
    	this.email = userDto.email();
    	this.name = userDto.name();
    	this.login = userDto.login();
    	this.password = passwordCrypto;
    	this.address = userDto.address();
    	this.dtUpdateRow = new Date();
    }


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getUserTypesRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role.getName()))
				.collect(Collectors.toList());
	}


	@Override
	public String getUsername() {
		return getLogin();
	}
}