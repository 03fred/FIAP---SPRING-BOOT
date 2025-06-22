package br.com.fiap.model;

import br.com.fiap.dto.UserDTO;
import br.com.fiap.model.enums.EnumType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

import java.util.Date;
import java.util.List;

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

    @Enumerated(jakarta.persistence.EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private EnumType enumType;

    @JsonBackReference
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "restaurantOwner", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Restaurant> restaurant;
    
    
    public User(UserDTO userDto, EnumType enumType, String passwordCrypto) {
    	this.email = userDto.email();
    	this.name = userDto.name();
    	this.login = userDto.login();
    	this.password = passwordCrypto;
    	this.address = userDto.address();
    	this.dtUpdateRow = new Date();
    	this.enumType = enumType;
    	
    }
}