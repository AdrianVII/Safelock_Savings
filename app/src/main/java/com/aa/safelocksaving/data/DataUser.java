package com.aa.safelocksaving.data;

import java.util.List;

public class DataUser {
    private List<Reminders_CardData> reminders_cardData;
    private List<Reminders_ShopData> reminders_shopData;
    private List<Reminders_SubscriptionData> reminders_subscriptionData;

    public DataUser() {  }

    public List<Reminders_CardData> getReminders_cardData() {
        return reminders_cardData;
    }

    public void setReminders_cardData(List<Reminders_CardData> reminders_cardData) {
        this.reminders_cardData = reminders_cardData;
    }

    public List<Reminders_ShopData> getReminders_shopData() {
        return reminders_shopData;
    }

    public void setReminders_shopData(List<Reminders_ShopData> reminders_shopData) {
        this.reminders_shopData = reminders_shopData;
    }

    public List<Reminders_SubscriptionData> getReminders_subscriptionData() {
        return reminders_subscriptionData;
    }

    public void setReminders_subscriptionData(List<Reminders_SubscriptionData> reminders_subscriptionData) {
        this.reminders_subscriptionData = reminders_subscriptionData;
    }
}
