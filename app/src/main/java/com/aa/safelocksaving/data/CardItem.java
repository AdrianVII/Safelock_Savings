package com.aa.safelocksaving.data;

public class CardItem {
    private int type;
    private Object item;
    private int Status;

    public CardItem() {}

    public CardItem(int type, Object item, int Status) {
        this.type = type;
        this.item = item;
        this.Status = Status;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
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
