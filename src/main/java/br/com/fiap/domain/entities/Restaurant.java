package br.com.fiap.domain.entities;

import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.fiap.dto.RestaurantDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "restaurants") // boa prática: nomes em minúsculo
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String typeKitchen;

    @JsonFormat(pattern = "HH:mm")
    @Column(name = "opening_time", nullable = false)
    private LocalTime openingTime;

    @JsonFormat(pattern = "HH:mm")
    @Column(name = "closing_time", nullable = false)
    private LocalTime closingTime;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_owner_id", nullable = false)
    private User restaurantOwner;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus;

    public Restaurant(RestaurantDTO restaurantDTO, User user) {
        this.address = restaurantDTO.adress();
        this.openingTime = restaurantDTO.openingTime();
        this.closingTime = restaurantDTO.closingTime();
        this.name = restaurantDTO.name();
        this.typeKitchen = restaurantDTO.typeKitchen();
        this.restaurantOwner = user;
    }

    @Override
    public String toString() {
        return "Restaurante{id=" + id + ", nome='" + name + "', endereco='" + address + "', tipoCozinha='" + typeKitchen + "', restauranteOwner=" + (restaurantOwner != null ? restaurantOwner.getId() : null) + "}";
    }

}