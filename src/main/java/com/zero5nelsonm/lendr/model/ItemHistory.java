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

    public ItemHistory() {
    }

    public ItemHistory(String lentto, String lentdate, String lendnotes, String datereturned, Item item) {
        this.lentto = lentto;
        this.lentdate = lentdate;
        this.lendnotes = lendnotes;
        this.datereturned = datereturned;
        this.item = item;
    }

    public long getItemhistoryid() {
        return itemhistoryid;
    }

    public void setItemhistoryid(long itemhistoryid) {
        this.itemhistoryid = itemhistoryid;
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

    public String getDatereturned() {
        return datereturned;
    }

    public void setDatereturned(String datereturned) {
        this.datereturned = datereturned;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
