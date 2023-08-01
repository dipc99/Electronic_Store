package com.lcwt.electronic.store.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @Column(name = "id")
    private Long categoryId;
    @Column(name = "category_title", length = 60)
    private String title;
    @Column(name = "category_desc", length = 50)
    private String description;

    private String coverImage;
    @OneToMany(mappedBy = "category" ,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Product> products=new ArrayList<>();


}
