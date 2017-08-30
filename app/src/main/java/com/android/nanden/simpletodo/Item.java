package com.android.nanden.simpletodo;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {

    public Long _id;
    public String itemName;

    public Item() {
        this.itemName = null;
    }

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


    protected Item(Parcel in) {
        itemName = in.readString();
        _id = in.readLong();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(itemName);
        parcel.writeLong(_id);
    }
}
