package com.zero5nelsonm.lendr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zero5nelsonm.lendr.logging.Loggable;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Loggable
@Entity
@Table(name = "items", uniqueConstraints = {@UniqueConstraint(columnNames = {"userid", "itemname"})})
@ApiModel(value = "Item", description = "A list of items for the user")
public class Item extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long itemid;

    @Column(nullable = false)
    private String itemname;

    private String itemdescription;
    private String lentto;
    private String lentdate;
    private String lendnotes;
    private Boolean beingreturned = false;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    @JsonIgnoreProperties("useritems")
    private User user;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("item")
    private List<ItemHistory> itemhistories = new ArrayList<>();
}
