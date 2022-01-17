package com.htec.shelffunction.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(catalog = "shelf_function", name = "function")
public class FunctionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @Column(name = "shelf_id")
    private Long shelfId;

    @Column(name = "custom")
    private Boolean custom;

    @Column(name = "language")
    private String language;

    @OneToOne
    private EventEntity event;

    public static EventEntity createEvent(Long id) {
        return new EventEntity(id);
    }
}
