package br.com.fiap.infrastructure.persistence.passwordResetToken;

import java.time.LocalDateTime;

import br.com.fiap.infrastructure.persistence.user.JpaUserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "password_reset_token") 
public class JpaPasswordResetTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    private JpaUserEntity user;

    private LocalDateTime expiration;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiration);
    }
}