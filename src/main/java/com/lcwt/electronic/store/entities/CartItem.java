package com.lcwt.electronic.store.entities;

import lombok.*;

import javax.persistence.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;
    @OneToOne
    private Product product;
    private int quantity;
    private int totalPrice;
    //mapping cart
    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;
    }
