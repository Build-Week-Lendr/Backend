package com.zero5nelsonm.lendr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zero5nelsonm.lendr.logging.Loggable;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;

@Loggable
@Entity
@Table(name = "itemhistory")
@ApiModel(value = "ItemHistory", description = "A list of an item's history of being lent")
public class ItemHistory extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long itemhistoryid;

    private String lentto;
    private String lentdate;
    private String lendnotes;
    private String datereturned;

    @ManyToOne
    @JoinColumn(name = "itemid", nullable = false)
    @JsonIgnoreProperties("itemhistories")
    private Item item;
}
