package com.example.kursach.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders_table")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "is_ordered")
    private boolean isFinished;
    @Column(name = "price")
    private Double price;
    @ManyToOne
    @JsonIgnore
    private User user;
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Item> items;
}
