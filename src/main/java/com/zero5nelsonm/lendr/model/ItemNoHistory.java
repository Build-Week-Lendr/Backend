package com.zero5nelsonm.lendr.model;

import java.util.ArrayList;
import java.util.List;

public class ItemNoHistory {
    private long itemid;
    private String itemname;
    private String itemdescription;
    private String lentto;
    private String lentdate;
    private String lendnotes;
    private List<ItemHistory> itemhistories;

    public ItemNoHistory(long itemid,
                         String itemname,
                         String itemdescription,
                         String lentto,
                         String lentdate,
                         String lendnotes) {
        this.itemid = itemid;
        this.itemname = itemname;
        this.itemdescription = itemdescription;
        this.lentto = lentto;
        this.lentdate = lentdate;
        this.lendnotes = lendnotes;
        this.itemhistories = new ArrayList<>();
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

    public List<ItemHistory> getItemhistories() {
        return itemhistories;
    }

    public void setItemhistories(List<ItemHistory> itemhistories) {
        this.itemhistories = itemhistories;
    }
}
