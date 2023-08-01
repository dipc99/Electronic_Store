package com.lcwt.electronic.store.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cart {
    @Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    private String cartId;
    private Date createdAt;
    @OneToOne
    private User user;

    //mapping cart-items
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> items = new ArrayList<>();
}
