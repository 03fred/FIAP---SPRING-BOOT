package br.com.fiap.infrastructure.persistence.user;

import java.util.Date;
import java.util.Objects;

import br.com.fiap.dto.UserDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "users") 
public class JpaUserEntity {
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

    public JpaUserEntity(UserDTO userDto, String passwordCrypto) {
        this.email = userDto.email();
        this.name = userDto.name();
        this.login = userDto.login();
        this.password = passwordCrypto;
        this.dtUpdateRow = new Date();

   
    }

    public JpaUserEntity(UserDTO userDto) {
        this.email = userDto.email();
        this.name = userDto.name();
        this.login = userDto.login();
        this.dtUpdateRow = new Date();
    }


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
		JpaUserEntity other = (JpaUserEntity) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id) && Objects.equals(login, other.login);
	}
    
    
}