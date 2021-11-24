package com.aa.safelocksaving.Operation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aa.safelocksaving.DAO.DAOConfigurationData;
import com.aa.safelocksaving.Dialog.Dialog_Subscription_Notification;
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
import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CardItem> items;
    private Context context;
    private AlarmOp alarmOp;

    public CardListAdapter(List<CardItem> items, Context context) {
        this.items = items;
        this.context = context;
        alarmOp = new AlarmOp(context);
    }

    public void setNewStatus(long ID) {
        Toast.makeText(context, "getArgument isn't null", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getStatus() == Status.ACTIVE || items.get(i).getStatus() == Status.LIMIT) {
                switch (items.get(i).getType()) {
                    case Type.CARD:
                        break;
                    case Type.SUBSCRIPTION:
                        if (((Reminders_SubscriptionData) items.get(i).getItem()).getID() == ID)
                            showDialogForSubscription(i);
                        break;
                    /*case 2:
                        break;*/
                }
            }
        }
    }

    private void showDialogForSubscription(int position) {
        new Dialog_Subscription_Notification(context, new Dialog_Subscription_Notification.onButtonClickListener() {
            @Override
            public void onYesClick(View view) {
                DateBasic dateBasic = ((Reminders_SubscriptionData) items.get(position).getItem()).getDate();
                Calendar calendar = Calendar.getInstance();
                calendar.set(dateBasic.getYear(), dateBasic.getMonth(), dateBasic.getDay());
                switch (((Reminders_SubscriptionData) items.get(position).getItem()).getRepeat()) {
                    case Repeat.WEEKLY:
                        calendar.add(Calendar.DAY_OF_MONTH, 7);
                        dateBasic.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                        dateBasic.setMonth(calendar.get(Calendar.MONTH));
                        dateBasic.setYear(calendar.get(Calendar.YEAR));
                        new OPBasics().updateRemindersDate(((Reminders_SubscriptionData) items.get(position).getItem()).getID(), dateBasic).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                ((Reminders_SubscriptionData) items.get(position).getItem()).setDate(dateBasic);
                                new AlarmOp(context).cancelAlarm(((Reminders_SubscriptionData) items.get(position).getItem()).getID());
                                notifyDataSetChanged();
                            }
                        });
                        break;
                    case Repeat.BIWEEKLY:
                        calendar.add(Calendar.DAY_OF_MONTH, 14);
                        dateBasic.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                        dateBasic.setMonth(calendar.get(Calendar.MONTH));
                        dateBasic.setYear(calendar.get(Calendar.YEAR));
                        new OPBasics().updateRemindersDate(((Reminders_SubscriptionData) items.get(position).getItem()).getID(), dateBasic).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                ((Reminders_SubscriptionData) items.get(position).getItem()).setDate(dateBasic);
                                new AlarmOp(context).cancelAlarm(((Reminders_SubscriptionData) items.get(position).getItem()).getID());
                                notifyDataSetChanged();
                            }
                        });
                        break;
                    case Repeat.MONTHLY:
                        calendar.add(Calendar.MONTH, 1);
                        dateBasic.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                        dateBasic.setMonth(calendar.get(Calendar.MONTH));
                        dateBasic.setYear(calendar.get(Calendar.YEAR));
                        new OPBasics().updateRemindersDate(((Reminders_SubscriptionData) items.get(position).getItem()).getID(), dateBasic).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                ((Reminders_SubscriptionData) items.get(position).getItem()).setDate(dateBasic);
                                new AlarmOp(context).cancelAlarm(((Reminders_SubscriptionData) items.get(position).getItem()).getID());
                                notifyDataSetChanged();
                            }
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
        }).show();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new CardViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.card_element,
                            parent,
                            false
                    )
            );
        } else if (viewType == 1) {
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
        if (getItemViewType(position) == 0) {
            Reminders_CardData cardData = (Reminders_CardData) items.get(position).getItem();
            ((CardViewHolder) holder).setCardBind(cardData);
        } else if (getItemViewType(position) == 1) {
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
            setLimitStatus(getBindingAdapterPosition(), item.getDeadline().getDay(), item.getID());

            if (!isPaused(getBindingAdapterPosition())) {
                alarmOp.setAlarm(
                        item.getID(),
                        context.getString(R.string.nextPaymentOfText) + item.getName(),
                        context.getString(R.string.theAmountOfText) + item.getAmount(),
                        item.getDeadline(),
                        item.getImportance()
                );
            }

            importantColor.setText(item.getImportance() == 1 ? context.getString(R.string.lessImportantText) : item.getImportance() == 2 ? context.getString(R.string.importantText) : context.getString(R.string.veryImportantText));
            titleText.setText(item.getName());
            dateText.setText(item.getDeadline().toString());
            amountText.setText(String.valueOf(item.getAmount()));
            minAmountNum.setText(String.valueOf(item.getMinAmount()));
            settlementNum.setText(String.valueOf(item.getSettlement()));
            cutoffDateText.setText(item.getDeadline().toString());
            monthNum.setText(String.valueOf(item.getMonth()));

            setColorStatus(getBindingAdapterPosition(), CardView, item.getImportance());
            /*if (items.get(getAdapterPosition()).getStatus() != Status.PAUSED) {
                setColor(item.getImportance(), CardView);
            } else {
                CardView.setBackgroundColor(context.getColor(R.color.Background_selector_item));
            }*/
            CardView.setOnClickListener(view -> {
                int v = (cardExpansion.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                cardExpansion.setVisibility(v);
                notifyDataSetChanged();
            });
            img.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(context, img);
                if (items.get(getAdapterPosition()).getStatus() != Status.PAUSED)
                    popupMenu.inflate(R.menu.menu_card);
                else popupMenu.inflate(R.menu.menu_card_paused);
                popupMenu.setOnMenuItemClickListener(menuItem -> menuStatus(
                        new Intent(context, Reminders_Edit_Activity.class)
                                .putExtra("id", item.getID())
                                .putExtra("type", items.get(getAdapterPosition()).getType())
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
                        menuItem, CardView, getAdapterPosition(), item.getID(), view, item.getImportance()));
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
            //if status is cancelled and it's not repeated...
            /*if (items.get(getBindingAdapterPosition()).getStatus() == Status.CANCELLED && item.getRepeat() != Repeat.NO) {

            }*/

            //if (item.getDate().getDay() == Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
            setLimitStatus(getBindingAdapterPosition(), item.getDate().getDay(), item.getID());

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

            /*if (!isPaused(getBindingAdapterPosition())) {
                alarmOp.setAlarm(
                        item.getID(),
                        context.getString(R.string.nextPaymentOfText) + item.getName(),
                        context.getString(R.string.theAmountOfText) + item.getAmount(),
                        item.getDate(),
                        item.getImportance()
                );
            }*/
            importantColor.setText(item.getImportance() == 1 ? context.getString(R.string.lessImportantText) : item.getImportance() == 2 ? context.getString(R.string.importantText) : context.getString(R.string.veryImportantText));
            titleText.setText(item.getName());
            dateText.setText(item.getDate().toString());
            amountText.setText(String.valueOf(item.getAmount()));
            setColor(item.getImportance(), CardView);
            repeatText.setText((item.getRepeat() == Repeat.WEEKLY) ? context.getString(R.string.weeklyText) : (item.getRepeat() == Repeat.BIWEEKLY) ? context.getString(R.string.biweeklyText) : (item.getRepeat() == Repeat.MONTHLY) ? context.getString(R.string.monthlyText) : context.getString(R.string.noText));

            setColorStatus(getBindingAdapterPosition(), CardView, item.getImportance());
            /*if (items.get(getAdapterPosition()).getStatus() != Status.PAUSED) {
                setColor(item.getImportance(), CardView);
            } else {
                CardView.setBackgroundColor(context.getColor(R.color.Background_selector_item));
            }*/
            CardView.setOnClickListener(view -> {
                subscriptionExpansion.setVisibility((subscriptionExpansion.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
                notifyDataSetChanged();
            });
            img.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(context, img);
                if (items.get(getAdapterPosition()).getStatus() != Status.PAUSED)
                    popupMenu.inflate(R.menu.menu_card);
                else popupMenu.inflate(R.menu.menu_card_paused);
                popupMenu.setOnMenuItemClickListener(menuItem -> menuStatus(
                        new Intent(context, Reminders_Edit_Activity.class)
                                .putExtra("id", item.getID())
                                .putExtra("type", items.get(getAdapterPosition()).getType())
                                .putExtra("name", item.getName())
                                .putExtra("amount", item.getAmount())
                                .putExtra("day", item.getDate().getDay())
                                .putExtra("month", item.getDate().getMonth())
                                .putExtra("year", item.getDate().getYear())
                                .putExtra("importance", item.getImportance())
                                .putExtra("repeat", item.getRepeat()),
                        menuItem, CardView, getAdapterPosition(), item.getID(), view, item.getImportance()));
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

            /*checkAllStatus(
                    getBindingAdapterPosition(),
                    item.getID(),
                    item.getImportance(),
                    item.getCutoffDate(),
                    item.getDeadline()
            );*/

            if (!isPaused(getBindingAdapterPosition())) {
                setLimitStatus(getBindingAdapterPosition(), item.getDeadline().getDay(), item.getID());
                alarmOp.setAlarm(
                        item.getID(),
                        context.getString(R.string.nextPaymentOfText) + item.getName(),
                        context.getString(R.string.theAmountOfText) + item.getAmount(),
                        item.getDeadline(),
                        item.getImportance()
                );
            }
            importantColor.setText(item.getImportance() == 1 ? context.getString(R.string.lessImportantText) : item.getImportance() == 2 ? context.getString(R.string.importantText) : context.getString(R.string.veryImportantText));
            titleText.setText(item.getName());
            dateText.setText(item.getDeadline().toString());
            amountText.setText(String.valueOf(item.getAmount()));
            setColor(item.getImportance(), CardView);
            descriptionText.setText(item.getDescription());
            cutoffDateText.setText(item.getCutoffDate().toString());
            monthNum.setText(String.valueOf(item.getMonth()));

            setColorStatus(getBindingAdapterPosition(), CardView, item.getImportance());
            /*if (items.get(getAdapterPosition()).getStatus() != Status.PAUSED) {
                setColor(item.getImportance(), CardView);
            } else {
                if (items.get(getBindingAdapterPosition()).getStatus() == Status.PAUSED)
                    CardView.setBackgroundColor(context.getColor(R.color.Background_selector_item));
                else if (items.get(getBindingAdapterPosition()).getStatus() != Status.LIMIT)
                    CardView.setBackgroundColor(context.getColor(R.color.));
            }*/
            CardView.setOnClickListener(view -> {
                shopExpansion.setVisibility((shopExpansion.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
                notifyDataSetChanged();
            });
            img.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(context, img);
                if (items.get(getAdapterPosition()).getStatus() != Status.PAUSED)
                    popupMenu.inflate(R.menu.menu_card);
                else popupMenu.inflate(R.menu.menu_card_paused);
                popupMenu.setOnMenuItemClickListener(menuItem -> menuStatus(
                        new Intent(context, Reminders_Edit_Activity.class)
                                .putExtra("id", item.getID())
                                .putExtra("type", items.get(getAdapterPosition()).getType())
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
                        menuItem, CardView, getAdapterPosition(), item.getID(), view, item.getImportance()));
                popupMenu.show();
            });
        }
    }

    private void setLimitStatus(int position, int day, long ID) {
        if (day == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) && items.get(position).getStatus() != Status.LIMIT) {
            new OPBasics().updateRemindersStatus(ID, Status.LIMIT).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    items.get(position).setStatus(Status.LIMIT);
                    notifyDataSetChanged();
                }
            });
        }
    }

    private void setColorStatus(int position, LinearLayout CardView, int importance) {
        switch (items.get(position).getStatus()) {
            case Status.PAUSED:
                CardView.setBackgroundColor(context.getColor(R.color.Background_selector_item));
                break;
            case Status.LIMIT:
                CardView.setBackgroundColor(context.getColor(R.color.delete_night));
                break; //Change background.
            default:
                setColor(importance, CardView);
                break;
        }
    }

    private void checkAllStatus(int position, long ID, String name, Double amount, int importance, DateBasic cutoffDate, DateBasic deadlineDate, LinearLayout CardView) {
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
            case Status.LIMIT:
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

    /*public void removeElement(int pos, long elementID, View view, LinearLayout CardView) {
        final int position = pos;

        alarmOp.cancelAlarm(elementID);

        CardItem auxItem = items.get(position);
        int auxStatus = auxItem.getStatus();
        long id = elementID;
        new OPBasics().updateRemindersStatus(id, Status.DELETED).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                items.remove(position);
                notifyItemRemoved(position);
                new SnackBar_Action(context, 32, 32, view).showSBMargin(v -> {
                    new OPBasics().updateRemindersStatus(id, auxStatus).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            items.add(position, auxItem);
                            notifyItemInserted(position);
                            if (auxItem.getStatus() == Status.PAUSED)
                                CardView.setBackgroundColor(context.getColor(R.color.Background_selector_item));
                        }
                    });
                });
            }
        });
    }*/

    public void removeElement(int position, long elementID, View view, LinearLayout CardView, int statusDeleted) {
        alarmOp.cancelAlarm(elementID);
        int auxStatus = items.get(position).getStatus();
        new OPBasics().updateRemindersStatus(elementID, statusDeleted).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                CardItem auxItem = items.remove(position);
                notifyItemRemoved(position);
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
        return (items.get(pos).getType() == 0 ? ((Reminders_CardData) items.get(pos).getItem()).getImportance() : items.get(pos).getType() == 1 ? ((Reminders_SubscriptionData) items.get(pos).getItem()).getImportance() : items.get(pos).getType() == 2 ? ((Reminders_ShopData) items.get(pos).getItem()).getImportance() : 0);
    }

    public long getID(int pos) {
        return (items.get(pos).getType() == 0 ? ((Reminders_CardData) items.get(pos).getItem()).getID() : items.get(pos).getType() == 1 ? ((Reminders_SubscriptionData) items.get(pos).getItem()).getID() : items.get(pos).getType() == 2 ? ((Reminders_ShopData) items.get(pos).getItem()).getID() : 0);
    }

    public boolean isPaused(int position) {
        return items.get(position).getStatus() == Status.PAUSED;
    }

    public int getStatus(int pos) {
        return items.get(pos).getStatus();
    }

    private int getType(int pos) {
        return items.get(pos).getType();
    }

}