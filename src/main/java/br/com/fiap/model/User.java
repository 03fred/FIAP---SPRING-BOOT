package br.com.fiap.model;

import java.util.Date;
import java.util.List;

import br.com.fiap.dto.UserDTO;
import br.com.fiap.model.enums.UserType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
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
public class User {
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

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "restaurantOwner", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Restaurant> restaurants;
    
    
    public User(UserDTO userDto, UserType userType, String passwordCrypto) {
    	this.email = userDto.email();
    	this.name = userDto.name();
    	this.login = userDto.login();
    	this.password = passwordCrypto;
    	this.address = userDto.address();
    	this.dtUpdateRow = new Date();
    	this.userType = userType;
    	
    }
}