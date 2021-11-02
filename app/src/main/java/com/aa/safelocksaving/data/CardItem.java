package com.aa.safelocksaving.data;

public class CardItem {
    private int type;
    private Object item;

    public CardItem() {}

    public CardItem(int type, Object item) {
        this.type = type;
        this.item = item;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }
}
