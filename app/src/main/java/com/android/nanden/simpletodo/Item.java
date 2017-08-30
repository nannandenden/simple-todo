package com.android.nanden.simpletodo;

public class Item {

    public Long _id;
    public String itemName;

    public Item(String itemName) {
        this.itemName = itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public Long get_id() {
        return _id;
    }

}
