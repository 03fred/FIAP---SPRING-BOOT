package br.com.fiap.model;

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
@Table(name = "restaurants") // boa prática: nomes em minúsculo
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String tipoCozinha;

    @Column
    private String horarioFuncionamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_owner_id", nullable = false)
    private User restaurantOwner;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus;

}