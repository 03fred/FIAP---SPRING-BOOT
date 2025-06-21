package br.com.fiap.model;

import br.com.fiap.dto.RestauranteDTO;
import br.com.fiap.model.enums.EnumType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "restaurante") // boa prática: nomes em minúsculo
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String tipoCozinha;

    @Column
    private String horarioFuncionamento;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_owner_id", nullable = false)
    private User restauranteOwner;

    @OneToMany(mappedBy = "restaurante", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus;

    public Restaurante(RestauranteDTO restauranteDTO, User user) {
        this.endereco = restauranteDTO.endereco();
        this.horarioFuncionamento = restauranteDTO.horarioFuncionamento();
        this.nome = restauranteDTO.nome();
        this.tipoCozinha = restauranteDTO.tipoCozinha();
        this.restauranteOwner = user;
    }

    @Override
    public String toString() {
        return "Restaurante{id=" + id + ", nome='" + nome + "', endereco='" + endereco + "', tipoCozinha='" + tipoCozinha + "', restauranteOwner=" + (restauranteOwner != null ? restauranteOwner.getId() : null) + "}";
    }

}