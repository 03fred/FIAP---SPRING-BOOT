package br.com.fiap.model;

import java.util.Date;

import br.com.fiap.dto.UserDTO;
import br.com.fiap.model.enums.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity; // Import the correct @Entity annotation
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id; // Import the correct @Id annotation
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table; // Optional, but good practice for naming the table
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