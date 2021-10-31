package com.aa.safelocksaving.data;

import java.util.List;

public class Reminders {

    private List<Reminders_CardData> cards;
    private List<Reminders_SubscriptionData> subscriptions;
    private List<Reminders_ShopData> shop;

    public Reminders() {  }

    public Reminders(List<Reminders_CardData> cards, List<Reminders_SubscriptionData> subscriptions, List<Reminders_ShopData> shop) {
        this.cards = cards;
        this.subscriptions = subscriptions;
        this.shop = shop;
    }

    public List<Reminders_CardData> getCards() {
        return cards;
    }

    public void setCards(List<Reminders_CardData> cards) {
        this.cards = cards;
    }

    public List<Reminders_SubscriptionData> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Reminders_SubscriptionData> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<Reminders_ShopData> getShop() {
        return shop;
    }

    public void setShop(List<Reminders_ShopData> shop) {
        this.shop = shop;
    }
}
