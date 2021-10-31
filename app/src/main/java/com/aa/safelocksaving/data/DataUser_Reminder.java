package com.aa.safelocksaving.data;

import java.util.List;

public class DataUser_Reminder {
    private Reminders_CardData reminders_cardData;
    private Reminders_ShopData reminders_shopData;
    private Reminders_SubscriptionData reminders_subscriptionData;

    public DataUser_Reminder() {  }

    public Reminders_CardData getReminders_cardData() {
        return reminders_cardData;
    }

    public void setReminders_cardData(Reminders_CardData reminders_cardData) {
        this.reminders_cardData = reminders_cardData;
    }

    public Reminders_ShopData getReminders_shopData() {
        return reminders_shopData;
    }

    public void setReminders_shopData(Reminders_ShopData reminders_shopData) {
        this.reminders_shopData = reminders_shopData;
    }

    public Reminders_SubscriptionData getReminders_subscriptionData() {
        return reminders_subscriptionData;
    }

    public void setReminders_subscriptionData(Reminders_SubscriptionData reminders_subscriptionData) {
        this.reminders_subscriptionData = reminders_subscriptionData;
    }
}
