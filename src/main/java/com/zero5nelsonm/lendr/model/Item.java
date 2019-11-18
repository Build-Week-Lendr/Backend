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

    public Item() {
    }

    public Item(String itemname,
                String itemdescription,
                String lentto,
                String lentdate,
                String lendnotes,
                Boolean beingreturned,
                User user,
                List<ItemHistory> itemHistory) {
        this.itemname = itemname;
        this.itemdescription = itemdescription;
        this.lentto = lentto;
        this.lentdate = lentdate;
        this.lendnotes = lendnotes;
        this.beingreturned = beingreturned;
        this.user = user;

        for (ItemHistory ih : itemHistory) {
            ih.setItem(this);
        }
        this.itemhistories = itemHistory;
    }

    public long getItemid() {
        return itemid;
    }

    public void setItemid(long itemid) {
        this.itemid = itemid;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemdescription() {
        return itemdescription;
    }

    public void setItemdescription(String itemdescription) {
        this.itemdescription = itemdescription;
    }

    public String getLentto() {
        return lentto;
    }

    public void setLentto(String lentto) {
        this.lentto = lentto;
    }

    public String getLentdate() {
        return lentdate;
    }

    public void setLentdate(String lentdate) {
        this.lentdate = lentdate;
    }

    public String getLendnotes() {
        return lendnotes;
    }

    public void setLendnotes(String lendnotes) {
        this.lendnotes = lendnotes;
    }

    public Boolean getBeingreturned() {
        return beingreturned;
    }

    public void setBeingreturned(Boolean beingreturned) {
        this.beingreturned = beingreturned;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ItemHistory> getItemhistories() {
        return itemhistories;
    }

    public void setItemhistories(List<ItemHistory> itemhistories) {
        this.itemhistories = itemhistories;
    }
}
