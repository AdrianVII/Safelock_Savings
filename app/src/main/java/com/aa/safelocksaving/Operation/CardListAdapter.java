package com.aa.safelocksaving.Operation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aa.safelocksaving.DAO.DAOConfigurationData;
import com.aa.safelocksaving.Dialog.Card_Store_Notification;
import com.aa.safelocksaving.Dialog.Dialog_Subscription_Notification;
import com.aa.safelocksaving.Dialog.Dialog_Upload;
import com.aa.safelocksaving.Dialog.Progress_Alert_Dialog;
import com.aa.safelocksaving.Dialog.Store_Notification_Dialog;
import com.aa.safelocksaving.R;
import com.aa.safelocksaving.Reminders_Edit_Activity;
import com.aa.safelocksaving.data.CardItem;
import com.aa.safelocksaving.data.DateBasic;
import com.aa.safelocksaving.data.Importance;
import com.aa.safelocksaving.data.Reminders_CardData;
import com.aa.safelocksaving.data.Reminders_ShopData;
import com.aa.safelocksaving.data.Reminders_SubscriptionData;
import com.aa.safelocksaving.data.Repeat;
import com.aa.safelocksaving.data.Status;
import com.aa.safelocksaving.data.Type;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<CardItem> items;
    private final Context context;
    private final AlarmOp alarmOp;

    public CardListAdapter(List<CardItem> items, Context context) {
        this.items = items;
        this.context = context;
        alarmOp = new AlarmOp(context);
    }

    public void setNewStatus(long ID) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getStatus() == Status.ACTIVE || items.get(i).getStatus() == Status.LIMIT) {
                switch (items.get(i).getType()) {
                    case Type.CARD: if (((Reminders_CardData)items.get(i).getItem()).getID() == ID) showDialogForCard(i); break;
                    case Type.SUBSCRIPTION: if (((Reminders_SubscriptionData)items.get(i).getItem()).getID() == ID) showDialogForSubscription(i); break;
                    case Type.SHOP: if (((Reminders_ShopData)items.get(i).getItem()).getID() == ID) showDialogForShop(i); break;
                }
            }
        }
    }

    private void showDialogForShop(int position) {
        new Store_Notification_Dialog(context, new Store_Notification_Dialog.onButtonClickListener() {
            @Override
            public void OnYesClick(View view) {
                if ( ((Reminders_ShopData)items.get(position).getItem()).getMonth() > 0 ) {
                    DateBasic dateBasic = ((Reminders_ShopData) items.get(position).getItem()).getCutoffDate();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(dateBasic.getYear(), dateBasic.getMonth(), dateBasic.getDay());
                    setNewDate(((Reminders_ShopData) items.get(position).getItem()).getID(), position, calendar, Calendar.MONTH, 1, dateBasic, "cutoffDate");
                    dateBasic = ((Reminders_ShopData) items.get(position).getItem()).getDeadline();
                    calendar.set(dateBasic.getYear(), dateBasic.getMonth(), dateBasic.getDay());
                    setNewDate(((Reminders_ShopData) items.get(position).getItem()).getID(), position, calendar, Calendar.MONTH, 1, dateBasic, "deadline");
                    HashMap<String, Object> dataUpdate = new HashMap<>();
                    int newMonth = ((Reminders_ShopData)items.get(position).getItem()).getProgressMonth() + 1;
                    double newAmount = ((Reminders_ShopData)items.get(position).getItem()).getAmount() + ((Reminders_ShopData)items.get(position).getItem()).getProgressAmount();
                    dataUpdate.put("progressMonth", newMonth);
                    dataUpdate.put("progressAmount", newAmount);
                    Dialog_Upload dialogUpload = new Dialog_Upload(context);
                    dialogUpload.start();
                    new OPBasics().updateCard(((Reminders_ShopData)items.get(position).getItem()).getID(), dataUpdate).addOnCompleteListener(task -> {
                        dialogUpload.dismiss();
                        if (task.isSuccessful()) {
                            ((Reminders_ShopData)items.get(position).getItem()).setProgressMonth(newMonth);
                            ((Reminders_ShopData)items.get(position).getItem()).setProgressAmount(newAmount);
                            notifyItemChanged(position);
                            if (newMonth == ((Reminders_ShopData)items.get(position).getItem()).getMonth()) {
                                dialogUpload.start();
                                new OPBasics().updateRemindersStatus(((Reminders_ShopData)items.get(position).getItem()).getID(), Status.FINISHED).addOnCompleteListener(task2 -> {
                                    dialogUpload.dismiss();
                                    if (task2.isSuccessful()) {
                                        items.get(position).setStatus(Status.FINISHED);
                                        notifyItemChanged(position);
                                    }
                                }).addOnFailureListener(e -> {
                                    dialogUpload.dismiss();
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                            }
                        }
                    }).addOnFailureListener(e -> {
                        dialogUpload.dismiss();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

                }
            }

            @Override
            public void OnNoClick(View view) {

            }
        }, ((Reminders_ShopData)items.get(position).getItem()).getName(), ((Reminders_ShopData)items.get(position).getItem()).getAmount()).show();
    }

    private void showDialogForCard(int position) {
        new Card_Store_Notification(context, new Card_Store_Notification.onButtonClickListener() {
            @Override
            public void OnYesClick(View view) {

            }

            @Override
            public void OnNoClick(View view) {

            }

            @Override
            public void OnAcceptClick(View view, double amount, double accumulated) {
                if ( ((Reminders_CardData)items.get(position).getItem()).getMonth() > 0 ) {
                    DateBasic dateBasic = ((Reminders_CardData)items.get(position).getItem()).getCutoffDate();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(dateBasic.getYear(), dateBasic.getMonth(), dateBasic.getDay());
                    setNewDate(((Reminders_CardData)items.get(position).getItem()).getID(), position, calendar, Calendar.MONTH, 1, dateBasic, "cutoffDate");
                    dateBasic = ((Reminders_CardData) items.get(position).getItem()).getDeadline();
                    calendar.set(dateBasic.getYear(), dateBasic.getMonth(), dateBasic.getDay());
                    setNewDate(((Reminders_CardData) items.get(position).getItem()).getID(), position, calendar, Calendar.MONTH, 1, dateBasic, "deadline");
                    HashMap<String, Object> dataUpdate = new HashMap<>();
                    //if (amount > ((Reminders_CardData)items.get(position).getItem()).getAmount() + ((Reminders_CardData)items.get(position).getItem()).getAccumulatedAmount()) ;
                    int newMonth = ((Reminders_CardData)items.get(position).getItem()).getProgressMonth() + 1;
                    double newAmount = ((Reminders_CardData)items.get(position).getItem()).getProgressAmount() + amount;
                    //double newAccumulated = ((Reminders_CardData)items.get(position).getItem()).getAccumulatedAmount() + accumulated;
                    double newAccumulated = (amount < ((Reminders_CardData)items.get(position).getItem()).getAmount() + ((Reminders_CardData)items.get(position).getItem()).getAccumulatedAmount()) ?
                            ((Reminders_CardData)items.get(position).getItem()).getAccumulatedAmount() + accumulated : 0;
                    dataUpdate.put("progressMonth", newMonth);
                    dataUpdate.put("progressAmount", newAmount);
                    dataUpdate.put("accumulatedAmount", newAccumulated);
                    Dialog_Upload dialogUpload = new Dialog_Upload(context);
                    dialogUpload.start();
                    new OPBasics().updateCard(((Reminders_CardData)items.get(position).getItem()).getID(), dataUpdate).addOnCompleteListener(task -> {
                        dialogUpload.dismiss();
                        if (task.isSuccessful()) {
                            ((Reminders_CardData)items.get(position).getItem()).setProgressMonth(newMonth);
                            ((Reminders_CardData)items.get(position).getItem()).setProgressAmount(newAmount);
                            notifyItemChanged(position);
                            if (newMonth == ((Reminders_CardData)items.get(position).getItem()).getMonth() || ((Reminders_CardData)items.get(position).getItem()).getProgressAmount() >= ((Reminders_CardData)items.get(position).getItem()).getSettlement()) {
                                dialogUpload.start();
                                new OPBasics().updateRemindersStatus(((Reminders_CardData)items.get(position).getItem()).getID(), Status.FINISHED).addOnCompleteListener(task2 -> {
                                    dialogUpload.dismiss();
                                    if (task2.isSuccessful()) {
                                        items.get(position).setStatus(Status.FINISHED);
                                        notifyItemChanged(position);
                                    }
                                }).addOnFailureListener(e -> {
                                    dialogUpload.dismiss();
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                            }
                        }
                    }).addOnFailureListener(e -> {
                        dialogUpload.dismiss();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void OnCancelClick(View view) {
            }
        },
                ((Reminders_CardData)items.get(position).getItem()).getSettlement() - ((Reminders_CardData)items.get(position).getItem()).getProgressAmount(),
                ((Reminders_CardData)items.get(position).getItem()).getAmount(),
                ((Reminders_CardData)items.get(position).getItem()).getMinAmount(),
                ((Reminders_CardData)items.get(position).getItem()).getAccumulatedAmount()
        ).show();
    }

    private void showDialogForSubscription(int position) {
        long ID = ((Reminders_SubscriptionData)items.get(position).getItem()).getID();
        new Dialog_Subscription_Notification(context, new Dialog_Subscription_Notification.onButtonClickListener() {
            @Override
            public void onYesClick(View view) {
                DateBasic dateBasic = ((Reminders_SubscriptionData) items.get(position).getItem()).getDate();
                Calendar calendar = Calendar.getInstance();
                calendar.set(dateBasic.getYear(), dateBasic.getMonth(), dateBasic.getDay());
                switch (((Reminders_SubscriptionData) items.get(position).getItem()).getRepeat()) {
                    case Repeat.WEEKLY:
                        setNewDate( ((Reminders_SubscriptionData)items.get(position).getItem()).getID(), position, calendar, Calendar.DAY_OF_MONTH, 7, dateBasic, "date");
                        break;
                    case Repeat.BIWEEKLY:
                        setNewDate( ((Reminders_SubscriptionData)items.get(position).getItem()).getID(), position, calendar, Calendar.DAY_OF_MONTH, 14, dateBasic, "date");
                        break;
                    case Repeat.MONTHLY:
                        setNewDate( ((Reminders_SubscriptionData)items.get(position).getItem()).getID(), position, calendar, Calendar.MONTH, 1, dateBasic, "date");
                        break;
                    case Repeat.NO:
                        Dialog_Upload dialogUpload = new Dialog_Upload(context);
                        dialogUpload.start();
                        new OPBasics().updateRemindersStatus(ID, Status.CANCELED).addOnCompleteListener(task -> {
                            dialogUpload.dismiss();
                            if (task.isSuccessful()) {
                                items.get(position).setStatus(Status.CANCELED);
                                notifyDataSetChanged();
                            }
                        }).addOnFailureListener(e -> {
                            dialogUpload.dismiss();
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                        break;
                }
            }

            @Override
            public void onNoClick(View view) {
                //dismiss
            }

            @Override
            public void onCancelledClick(View view) {
                items.get(position).setStatus(Status.CANCELED);
                notifyDataSetChanged();
            }
        }, cancel -> {
            if (((Reminders_SubscriptionData) items.get(position).getItem()).getRepeat() == Repeat.NO)
                cancel.setVisibility(View.GONE);
        }, ((Reminders_SubscriptionData)items.get(position).getItem()).getName(), ((Reminders_SubscriptionData)items.get(position).getItem()).getAmount()).show();
    }

    private void setNewDate(long ID, int position, Calendar calendar, int DATE, int NEW, DateBasic dateBasic, String dateID) {
        calendar.add(DATE, NEW);
        dateBasic.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        dateBasic.setMonth(calendar.get(Calendar.MONTH));
        dateBasic.setYear(calendar.get(Calendar.YEAR));
        Dialog_Upload dialogUpload = new Dialog_Upload(context);
        dialogUpload.start();
        new OPBasics().updateRemindersDate(ID, dateID, dateBasic).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                switch (items.get(position).getType()) {
                    case Type.CARD: ((Reminders_CardData) items.get(position).getItem()).setDeadline(dateBasic); break;
                    case Type.SUBSCRIPTION: ((Reminders_SubscriptionData) items.get(position).getItem()).setDate(dateBasic); break;
                    case Type.SHOP: ((Reminders_ShopData) items.get(position).getItem()).setDeadline(dateBasic); break;
                }
                new AlarmOp(context).cancelAlarm(ID);
                new OPBasics().updateRemindersStatus(ID, Status.ACTIVE).addOnCompleteListener(task1 -> {
                    dialogUpload.dismiss();
                    if (task1.isSuccessful()) {
                        items.get(position).setStatus(Status.ACTIVE);
                        notifyItemChanged(position);
                    }
                }).addOnFailureListener(e -> {
                    dialogUpload.dismiss();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).addOnFailureListener(e -> {
            dialogUpload.dismiss();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Type.CARD) {
            return new CardViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.card_element,
                            parent,
                            false
                    )
            );
        } else if (viewType == Type.SUBSCRIPTION) {
            return new SubscriptionViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.card_element,
                            parent,
                            false
                    )
            );
        } else {
            return new ShopViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.card_element,
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == Type.CARD) {
            Reminders_CardData cardData = (Reminders_CardData) items.get(position).getItem();
            ((CardViewHolder) holder).setCardBind(cardData);
        } else if (getItemViewType(position) == Type.SUBSCRIPTION) {
            Reminders_SubscriptionData subscriptionData = (Reminders_SubscriptionData) items.get(position).getItem();
            ((SubscriptionViewHolder) holder).setSubscriptionBind(subscriptionData);
        } else {
            Reminders_ShopData shopData = (Reminders_ShopData) items.get(position).getItem();
            ((ShopViewHolder) holder).setShopBind(shopData);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText, dateText, amountText, importantColor;
        private ImageView img;
        private LinearLayout CardView;
        private TextView minAmountNum, settlementNum, cutoffDateText, monthNum;
        private LinearLayout cardExpansion;

        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.title);
            dateText = itemView.findViewById(R.id.DateNum);
            amountText = itemView.findViewById(R.id.amountNum);
            img = itemView.findViewById(R.id.menuCard);
            CardView = itemView.findViewById(R.id.linearLayoutCard);
            minAmountNum = itemView.findViewById(R.id.minAmountNum);
            settlementNum = itemView.findViewById(R.id.settlementNum);
            cutoffDateText = itemView.findViewById(R.id.cutoffDateText);
            monthNum = itemView.findViewById(R.id.monthNum);
            cardExpansion = itemView.findViewById(R.id.cardExpansion);
            importantColor = itemView.findViewById(R.id.importantColor);
        }

        void setCardBind(@NonNull Reminders_CardData item) {
            checkAllStatus(
                    getBindingAdapterPosition(),
                    item.getID(),
                    item.getName(),
                    item.getAmount(),
                    item.getImportance(),
                    item.getCutoffDate(),
                    item.getDeadline(),
                    CardView
            );

            importantColor.setText(item.getImportance() == Importance.LESS ? context.getString(R.string.lessImportantText) : item.getImportance() == Importance.MEDIUM ? context.getString(R.string.importantText) : context.getString(R.string.veryImportantText));
            titleText.setText(item.getName());
            dateText.setText(item.getDeadline().toString());
            amountText.setText(String.valueOf(item.getAmount() + item.getAccumulatedAmount()));
            minAmountNum.setText(String.valueOf(item.getMinAmount() + item.getAccumulatedAmount()));
            settlementNum.setText(String.valueOf(item.getSettlement() - item.getProgressAmount() + item.getAccumulatedAmount()));
            cutoffDateText.setText(item.getDeadline().toString());
            monthNum.setText(String.valueOf(item.getMonth() - item.getProgressMonth()));
            setColorStatus(getBindingAdapterPosition(), CardView, item.getImportance());
            CardView.setOnClickListener(view -> {
                cardExpansion.setVisibility((cardExpansion.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
                notifyDataSetChanged();
            });
            img.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(context, img);
                if (items.get(getBindingAdapterPosition()).getStatus() != Status.PAUSED)
                    popupMenu.inflate(R.menu.menu_card);
                else popupMenu.inflate(R.menu.menu_card_paused);
                popupMenu.setOnMenuItemClickListener(menuItem -> menuStatus(
                        new Intent(context, Reminders_Edit_Activity.class)
                                .putExtra("id", item.getID())
                                .putExtra("type", items.get(getBindingAdapterPosition()).getType())
                                .putExtra("name", item.getName())
                                .putExtra("amount", item.getAmount())
                                .putExtra("minAmount", item.getMinAmount())
                                .putExtra("settlement", item.getSettlement())
                                .putExtra("dayCutoffDate", item.getCutoffDate().getDay())
                                .putExtra("monthCutoffDate", item.getCutoffDate().getMonth())
                                .putExtra("yearCutoffDate", item.getCutoffDate().getYear())
                                .putExtra("day", item.getDeadline().getDay())
                                .putExtra("month", item.getDeadline().getMonth())
                                .putExtra("year", item.getDeadline().getYear())
                                .putExtra("importance", item.getImportance())
                                .putExtra("months", item.getMonth()),
                        menuItem, CardView, getBindingAdapterPosition(), item.getID(), view, item.getImportance()));
                popupMenu.show();
            });

        }
    }

    class SubscriptionViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText, dateText, amountText, importantColor;
        private ImageView img;
        private LinearLayout CardView;
        private TextView repeatText;
        private LinearLayout subscriptionExpansion;

        SubscriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            importantColor = itemView.findViewById(R.id.importantColor);
            titleText = itemView.findViewById(R.id.title);
            dateText = itemView.findViewById(R.id.DateNum);
            amountText = itemView.findViewById(R.id.amountNum);
            img = itemView.findViewById(R.id.menuCard);
            CardView = itemView.findViewById(R.id.linearLayoutCard);
            repeatText = itemView.findViewById(R.id.repeatText);
            subscriptionExpansion = itemView.findViewById(R.id.subscriptionExpansion);
        }

        void setSubscriptionBind(@NonNull Reminders_SubscriptionData item) {
            checkAllStatus(
                    getBindingAdapterPosition(),
                    item.getID(),
                    item.getName(),
                    item.getAmount(),
                    item.getImportance(),
                    null,
                    item.getDate(),
                    CardView
            );
            importantColor.setText(item.getImportance() == Importance.LESS ? context.getString(R.string.lessImportantText) : item.getImportance() == Importance.MEDIUM ? context.getString(R.string.importantText) : context.getString(R.string.veryImportantText));
            titleText.setText(item.getName());
            dateText.setText(item.getDate().toString());
            amountText.setText(String.valueOf(item.getAmount()));
            repeatText.setText((item.getRepeat() == Repeat.WEEKLY) ? context.getString(R.string.weeklyText) : (item.getRepeat() == Repeat.BIWEEKLY) ? context.getString(R.string.biweeklyText) : (item.getRepeat() == Repeat.MONTHLY) ? context.getString(R.string.monthlyText) : context.getString(R.string.noText));

            setColorStatus(getBindingAdapterPosition(), CardView, item.getImportance());
            CardView.setOnClickListener(view -> {
                subscriptionExpansion.setVisibility((subscriptionExpansion.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
                notifyDataSetChanged();
            });
            img.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(context, img);
                if (items.get(getBindingAdapterPosition()).getStatus() != Status.PAUSED)
                    popupMenu.inflate(R.menu.menu_card);
                else popupMenu.inflate(R.menu.menu_card_paused);
                popupMenu.setOnMenuItemClickListener(menuItem -> menuStatus(
                        new Intent(context, Reminders_Edit_Activity.class)
                                .putExtra("id", item.getID())
                                .putExtra("type", items.get(getBindingAdapterPosition()).getType())
                                .putExtra("name", item.getName())
                                .putExtra("amount", item.getAmount())
                                .putExtra("day", item.getDate().getDay())
                                .putExtra("month", item.getDate().getMonth())
                                .putExtra("year", item.getDate().getYear())
                                .putExtra("importance", item.getImportance())
                                .putExtra("repeat", item.getRepeat()),
                        menuItem, CardView, getBindingAdapterPosition(), item.getID(), view, item.getImportance()));
                popupMenu.show();
            });
        }
    }

    class ShopViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText, dateText, amountText, importantColor;
        private ImageView img;
        private LinearLayout CardView;
        private TextView descriptionText, cutoffDateText, monthNum;
        private LinearLayout shopExpansion;

        ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            importantColor = itemView.findViewById(R.id.importantColor);
            titleText = itemView.findViewById(R.id.title);
            dateText = itemView.findViewById(R.id.DateNum);
            amountText = itemView.findViewById(R.id.amountNum);
            img = itemView.findViewById(R.id.menuCard);
            CardView = itemView.findViewById(R.id.linearLayoutCard);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            cutoffDateText = itemView.findViewById(R.id.cutoffDateText1);
            monthNum = itemView.findViewById(R.id.monthNum1);
            shopExpansion = itemView.findViewById(R.id.shopExpansion);
        }

        void setShopBind(@NonNull Reminders_ShopData item) {
            checkAllStatus(
                    getBindingAdapterPosition(),
                    item.getID(),
                    item.getName(),
                    item.getAmount(),
                    item.getImportance(),
                    item.getCutoffDate(),
                    item.getDeadline(),
                    CardView
            );

            importantColor.setText(item.getImportance() == Importance.LESS ? context.getString(R.string.lessImportantText) : item.getImportance() == Importance.MEDIUM ? context.getString(R.string.importantText) : context.getString(R.string.veryImportantText));
            titleText.setText(item.getName());
            dateText.setText(item.getDeadline().toString());
            amountText.setText(String.valueOf(item.getAmount()));
            descriptionText.setText(item.getDescription());
            cutoffDateText.setText(item.getCutoffDate().toString());
            monthNum.setText(String.valueOf(item.getMonth() - item.getProgressMonth()));
            setColorStatus(getBindingAdapterPosition(), CardView, item.getImportance());
            CardView.setOnClickListener(view -> {
                shopExpansion.setVisibility((shopExpansion.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
                notifyDataSetChanged();
            });
            img.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(context, img);
                if (items.get(getBindingAdapterPosition()).getStatus() != Status.PAUSED)
                    popupMenu.inflate(R.menu.menu_card);
                else popupMenu.inflate(R.menu.menu_card_paused);
                popupMenu.setOnMenuItemClickListener(menuItem -> menuStatus(
                        new Intent(context, Reminders_Edit_Activity.class)
                                .putExtra("id", item.getID())
                                .putExtra("type", items.get(getBindingAdapterPosition()).getType())
                                .putExtra("name", item.getName())
                                .putExtra("amount", item.getAmount())
                                .putExtra("dayCutoffDate", item.getCutoffDate().getDay())
                                .putExtra("monthCutoffDate", item.getCutoffDate().getMonth())
                                .putExtra("yearCutoffDate", item.getCutoffDate().getYear())
                                .putExtra("day", item.getDeadline().getDay())
                                .putExtra("month", item.getDeadline().getMonth())
                                .putExtra("year", item.getDeadline().getYear())
                                .putExtra("description", item.getDescription())
                                .putExtra("importance", item.getImportance())
                                .putExtra("months", item.getMonth()),
                        menuItem, CardView, getBindingAdapterPosition(), item.getID(), view, item.getImportance()));
                popupMenu.show();
            });
        }
    }

    private void setLimitStatus(int position, DateBasic dateBasic, long ID) {
        Calendar calendar = Calendar.getInstance();
        if (dateBasic.getDay() == calendar.get(Calendar.DAY_OF_MONTH) &&
                dateBasic.getMonth() == calendar.get(Calendar.MONTH) &&
                dateBasic.getYear() == calendar.get(Calendar.YEAR) &&
                items.get(position).getStatus() != Status.LIMIT &&
                items.get(position).getStatus() != Status.PAUSED) {
            new OPBasics().updateRemindersStatus(ID, Status.LIMIT).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    items.get(position).setStatus(Status.LIMIT);
                    notifyDataSetChanged();
                }
            });
        }
    }

    private void setIndeterminateStatus(int position, DateBasic date) {
        if (
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH) > date.getDay() &&
                        Calendar.getInstance().get(Calendar.MONTH) > date.getMonth() &&
                        Calendar.getInstance().get(Calendar.YEAR) > date.getYear()
        ) items.get(position).setStatus(Status.INDETERMINATE);
    }

    private void setColorStatus(int position, LinearLayout CardView, int importance) {
        switch (items.get(position).getStatus()) {
            case Status.PAUSED: CardView.setBackgroundColor(context.getColor(R.color.Background_selector_item)); break;
            case Status.LIMIT: CardView.setBackgroundColor(context.getColor(R.color.delete_night)); break;
            default: setColor(importance, CardView); break;
        }
    }

    private void checkAllStatus(int position, long ID, String name, Double amount, int importance, DateBasic cutoffDate, DateBasic deadlineDate, LinearLayout CardView) {
        setLimitStatus(position, deadlineDate, ID);
        setIndeterminateStatus(position, deadlineDate);
        switch (items.get(position).getStatus()) {
            case Status.ACTIVE:
                alarmOp.setAlarm(
                        ID,
                        context.getString(R.string.nextPaymentOfText) + name,
                        context.getString(R.string.theAmountOfText) + amount,
                        getType(position) == Type.CARD && getType(position) == Type.SHOP ? cutoffDate : deadlineDate,
                        importance
                );
                break;
            case Status.CANCELED:
                removeElement(position, ID, CardView, CardView, Status.CANCELED);
                break;
            case Status.FINISHED:
                removeElement(position, ID, CardView, CardView, Status.FINISHED);
                break;
            case Status.INDETERMINATE:
                removeElement(position, ID, CardView, CardView, Status.INDETERMINATE);
                break;
        }
    }

    private void setColor(int color, LinearLayout card) {
        int colorAux = 0;
        switch (color) {
            case Importance.LESS:
                if (!new DAOConfigurationData(context).verifyNightMode()) {
                    colorAux = context.getColor(R.color.blue_grey_white);
                } else colorAux = context.getColor(R.color.lessimportant);
                break;

            case Importance.MEDIUM:
                if (!new DAOConfigurationData(context).verifyNightMode()) {
                    colorAux = context.getColor(R.color.orange_white);
                } else colorAux = context.getColor(R.color.important);
                break;
            case Importance.VERY:
                if (!new DAOConfigurationData(context).verifyNightMode()) {
                    colorAux = context.getColor(R.color.orange_black);
                } else colorAux = context.getColor(R.color.veryimportant);
                break;
        }
        card.setBackgroundColor(colorAux);
    }

    @SuppressLint("NonConstantResourceId")
    private boolean menuStatus(Intent intent, MenuItem menuItem, LinearLayout CardView, int pos, long elementID, View view, int importantColor) {
        switch (menuItem.getItemId()) {
            case R.id.menuEdit:
                context.startActivity(intent);
                return true;
            case R.id.menuStop:
                pauseElement(pos, elementID, CardView);
                return true;
            case R.id.menuResume:
                resumeElement(pos, elementID, importantColor, CardView);
                return true;
            case R.id.menuDelete:
                removeElement(pos, elementID, view, CardView, Status.DELETED);
                return true;
        }
        return false;
    }

    public void removeElement(int position, long elementID, View view, LinearLayout CardView, int statusDeleted) {
        alarmOp.cancelAlarm(elementID);
        int auxStatus = items.get(position).getStatus();
        new OPBasics().updateRemindersStatus(elementID, statusDeleted).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                CardItem auxItem = items.remove(position);
                notifyItemRemoved(position);
                if (statusDeleted == Status.DELETED) {
                    new SnackBar_Action(context, 32, 32, view).showSBMargin(v ->
                            new OPBasics().updateRemindersStatus(elementID, auxStatus).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    items.add(position, auxItem);
                                    notifyItemInserted(position);
                                    if (auxItem.getStatus() == Status.PAUSED)
                                        CardView.setBackgroundColor(context.getColor(R.color.Background_selector_item));
                                }
                            }));
                }
            }
        });
    }

    public void pause_resume(int position, long elementID, LinearLayout CardView, int importantColor) {
        if (items.get(position).getStatus() != Status.PAUSED)
            pauseElement(position, elementID, CardView);
        else resumeElement(position, elementID, importantColor, CardView);
    }

    public void pauseElement(int position, long elementID, LinearLayout CardView) {
        new OPBasics().updateRemindersStatus(elementID, Status.PAUSED).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                items.get(position).setStatus(Status.PAUSED);
                alarmOp.cancelAlarm(elementID);
                CardView.setBackgroundColor(context.getColor(R.color.Background_selector_item));
                Toast.makeText(context, context.getString(R.string.cardPausedText), Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });
    }

    public void resumeElement(int position, long elementID, int importantColor, LinearLayout CardView) {
        new OPBasics().updateRemindersStatus(elementID, Status.ACTIVE).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                items.get(position).setStatus(Status.ACTIVE);
                setColor(importantColor, CardView);
                Toast.makeText(context, context.getString(R.string.cardResumeText), Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });
    }

    public int getImportantColor(int pos) {
        return (items.get(pos).getType() == Type.CARD ? ((Reminders_CardData) items.get(pos).getItem()).getImportance() : items.get(pos).getType() == Type.SUBSCRIPTION ? ((Reminders_SubscriptionData) items.get(pos).getItem()).getImportance() : items.get(pos).getType() == Type.SHOP ? ((Reminders_ShopData) items.get(pos).getItem()).getImportance() : 0);
    }

    public long getID(int pos) {
        return (items.get(pos).getType() == Type.CARD ? ((Reminders_CardData) items.get(pos).getItem()).getID() : items.get(pos).getType() == Type.SUBSCRIPTION ? ((Reminders_SubscriptionData) items.get(pos).getItem()).getID() : items.get(pos).getType() == Type.SHOP ? ((Reminders_ShopData) items.get(pos).getItem()).getID() : 0);
    }

    public boolean isPaused(int position) {
        return items.get(position).getStatus() == Status.PAUSED;
    }

    /*public int getStatus(int pos) {
        return items.get(pos).getStatus();
    }*/

    private int getType(int pos) {
        return items.get(pos).getType();
    }

}