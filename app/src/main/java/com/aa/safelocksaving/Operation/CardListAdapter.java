package com.aa.safelocksaving.Operation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aa.safelocksaving.R;
import com.aa.safelocksaving.data.CardItem;
import com.aa.safelocksaving.data.Reminders_CardData;
import com.aa.safelocksaving.data.Reminders_ShopData;
import com.aa.safelocksaving.data.Reminders_SubscriptionData;

import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CardItem> items;
    private Context context;

    public CardListAdapter(List<CardItem> items, Context context) {
        this.items = items;
        this.context = context;
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
        private TextView titleText, dateText, amountText;
        private ImageView img;
        private LinearLayout CardView;
        //All Data
        private TextView minAmountNum, settlementNum, cutoffDateText, monthNum;
        private LinearLayout cardExpansion;

        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.title);
            dateText = itemView.findViewById(R.id.DateNum);
            amountText = itemView.findViewById(R.id.amountNum);
            img = itemView.findViewById(R.id.menuCard);
            CardView = itemView.findViewById(R.id.linearLayoutCard);

            //All Data
            minAmountNum = itemView.findViewById(R.id.minAmountNum);
            settlementNum = itemView.findViewById(R.id.settlementNum);
            cutoffDateText = itemView.findViewById(R.id.cutoffDateText);
            monthNum = itemView.findViewById(R.id.monthNum);
            cardExpansion = itemView.findViewById(R.id.cardExpansion);
        }
        void setCardBind(@NonNull Reminders_CardData item) {
            titleText.setText(item.getName());
            dateText.setText(item.getDeadline().toString());
            amountText.setText(String.valueOf(item.getAmount()));
            minAmountNum.setText(String.valueOf(item.getMinAmount()));
            settlementNum.setText(String.valueOf(item.getSettlement()));
            cutoffDateText.setText(item.getDeadline().toString());
            monthNum.setText(String.valueOf(item.getMonth()));
            if(item.getStatus() != 2) setColor(item.getImportance(), CardView);

            CardView.setOnClickListener(view -> {
                int v = (cardExpansion.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                //TransitionManager.beginDelayedTransition(linearLayout, new AutoTransition());
                cardExpansion.setVisibility(v);
                //notifyItemChanged(getAdapterPosition());
                notifyDataSetChanged();
            });
            img.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(context, img);
                if (item.getStatus() != 2) popupMenu.inflate(R.menu.menu_card);
                else popupMenu.inflate(R.menu.menu_card_paused);
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.menuEdit:
                            item.setStatus(3);
                            Toast.makeText(context, context.getString(R.string.editedCardText), Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.menuStop:
                            new OPBasics().updateRemindersStatus(item.getID(), 2).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    item.setStatus(2);
                                    CardView.setBackgroundColor(context.getColor(R.color.Background_selector_item));
                                    Toast.makeText(context, context.getString(R.string.cardPausedText), Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                }
                            });
                            return true;
                        case R.id.menuResume:
                            new OPBasics().updateRemindersStatus(item.getID(), 1).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    item.setStatus(1);
                                    setColor(item.getImportance(), CardView);
                                    Toast.makeText(context, context.getString(R.string.cardResumeText), Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                }
                            });
                            return true;
                        case R.id.menuDelete:
                            final int position = getAdapterPosition();
                            CardItem auxItem = items.get(position);
                            int auxStatus = item.getStatus();
                            long id = item.getID();
                            new OPBasics().updateRemindersStatus(id, 0).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    item.setStatus(0);
                                    items.remove(position);
                                    notifyItemRemoved(position);
                                    new SnackBar_Action(context, 32, 32, view).showSBMargin(v -> {
                                        new OPBasics().updateRemindersStatus(id, auxStatus).addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                items.add(position, auxItem);
                                                notifyItemInserted(position);
                                            }
                                        });
                                    });
                                }
                            });
                            return true;
                    }
                    return false;
                });
                popupMenu.show();
            });

        }
    }

    class SubscriptionViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText, dateText, amountText;
        private ImageView img;
        private LinearLayout CardView;
        //AllData
        private TextView repeatText;
        private LinearLayout subscriptionExpansion;

        SubscriptionViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.title);
            dateText = itemView.findViewById(R.id.DateNum);
            amountText = itemView.findViewById(R.id.amountNum);
            img = itemView.findViewById(R.id.menuCard);
            CardView = itemView.findViewById(R.id.linearLayoutCard);
            //AllData
            repeatText = itemView.findViewById(R.id.repeatText);
            subscriptionExpansion = itemView.findViewById(R.id.subscriptionExpansion);
        }
        void setSubscriptionBind(@NonNull Reminders_SubscriptionData item) {
            titleText.setText(item.getName());
            dateText.setText(item.getDate().toString());
            amountText.setText(String.valueOf(item.getAmount()));
            setColor(item.getImportance(), CardView);
            //All Data
            repeatText.setText(
                    (item.getRepeat() == 1)? context.getString(R.string.weeklyText): (item.getRepeat()== 2)? context.getString(R.string.biweeklyText): (item.getRepeat() == 3)? context.getString(R.string.monthlyText): context.getString(R.string.noText)
            );
            CardView.setOnClickListener(view -> {
                subscriptionExpansion.setVisibility((subscriptionExpansion.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
                notifyDataSetChanged();
            });
            img.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(context, img);
                popupMenu.inflate(R.menu.menu_card);
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.menuEdit:
                            Toast.makeText(context, "Editatela c:", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.menuStop:
                            item.setStatus(2);
                            CardView.setBackgroundColor(context.getColor(R.color.Background_selector_item));
                            Toast.makeText(context, context.getString(R.string.cardPausedText), Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.menuDelete:
                            final int position = getAdapterPosition();
                            CardItem auxItem = items.get(position);
                            item.setStatus(0);
                            //Codigo para actualizarlo la base de datos
                            items.remove(position);
                            notifyItemRemoved(position);
                            new SnackBar_Action(context, 32, 32, view).showSBMargin(v -> {
                                items.add(position, auxItem);
                                item.setStatus(1);
                                //Codigo para actualizarlo la base de datos
                                notifyItemInserted(position);
                            });
                            notifyDataSetChanged();
                            return true;
                    }
                    return false;
                });
                popupMenu.show();
            });
        }
    }

    class ShopViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText, dateText, amountText;
        private ImageView img;
        private LinearLayout CardView;
        //AllData
        private TextView descriptionText, cutoffDateText, monthNum;
        private LinearLayout shopExpansion;

        ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.title);
            dateText = itemView.findViewById(R.id.DateNum);
            amountText = itemView.findViewById(R.id.amountNum);
            img = itemView.findViewById(R.id.menuCard);
            CardView = itemView.findViewById(R.id.linearLayoutCard);
            //AllData
            descriptionText = itemView.findViewById(R.id.descriptionText);
            cutoffDateText = itemView.findViewById(R.id.cutoffDateText1);
            monthNum = itemView.findViewById(R.id.monthNum1);
            shopExpansion = itemView.findViewById(R.id.shopExpansion);
        }
        void setShopBind(@NonNull Reminders_ShopData item) {
            titleText.setText(item.getName());
            dateText.setText(item.getDeadline().toString());
            amountText.setText(String.valueOf(item.getAmount()));
            setColor(item.getImportance(), CardView);
            //AllData
            descriptionText.setText(item.getDescription());
            cutoffDateText.setText(item.getCutoffDate().toString());
            monthNum.setText(String.valueOf(item.getMonth()));
            CardView.setOnClickListener(view -> {
                shopExpansion.setVisibility((shopExpansion.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
                notifyDataSetChanged();
            });
            img.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(context, img);
                popupMenu.inflate(R.menu.menu_card);
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.menuEdit:
                            Toast.makeText(context, context.getString(R.string.editedCardText), Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.menuStop:
                            item.setStatus(2);
                            CardView.setBackgroundColor(context.getColor(R.color.Background_selector_item));
                            Toast.makeText(context, context.getString(R.string.cardPausedText), Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.menuDelete:
                            final int position = getAdapterPosition();
                            CardItem auxItem = items.get(position);
                            item.setStatus(0);
                            //Codigo para actualizarlo la base de datos
                            items.remove(position);
                            notifyItemRemoved(position);
                            new SnackBar_Action(context, 32, 32, view).showSBMargin(v -> {
                                items.add(position, auxItem);
                                item.setStatus(1);
                                //Codigo para actualizarlo la base de datos
                                notifyItemInserted(position);
                            });
                            notifyDataSetChanged();
                            return true;
                    }
                    return false;
                });
                popupMenu.show();
            });
        }
    }

    private void setColor(int color, LinearLayout card) {
        switch (color) {
            case 1: card.setBackgroundColor(context.getColor(R.color.blue_grey_white)); break;
            case 2: card.setBackgroundColor(context.getColor(R.color.orange_white)); break;
            case 3: card.setBackgroundColor(context.getColor(R.color.orange_black)); break;
        }
    }
}
