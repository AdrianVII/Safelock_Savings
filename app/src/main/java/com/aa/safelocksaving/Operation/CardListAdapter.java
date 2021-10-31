package com.aa.safelocksaving.Operation;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aa.safelocksaving.R;
import com.aa.safelocksaving.data.DataUser_Reminder;

import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {
    private List<DataUser_Reminder> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    private FragmentManager fragmentManager;
    private int lastPosition = -1;

    public CardListAdapter(List<DataUser_Reminder> listData, Context context, FragmentManager fragmentManager) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.listData = listData;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_element, parent, false);
        return new CardListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binData(listData.get(position));

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, dateText, amountText;
        ImageView imgMenu;
        LinearLayout linearCard;

        ViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.title);
            dateText = itemView.findViewById(R.id.DateNum);
            amountText = itemView.findViewById(R.id.amountNum);
            imgMenu = itemView.findViewById(R.id.menu);
            linearCard = itemView.findViewById(R.id.card);
        }

        void binData(final DataUser_Reminder item) {
            if (item.getReminders_cardData() != null) {
                titleText.setText(item.getReminders_cardData().getName());
                dateText.setText(item.getReminders_cardData().getDeadline().toString());
                amountText.setText(String.valueOf(item.getReminders_cardData().getAmount()));
                setColor(item.getReminders_cardData().getImportance());
            }
            if (item.getReminders_subscriptionData() != null) {
                titleText.setText(item.getReminders_subscriptionData().getName());
                dateText.setText(item.getReminders_subscriptionData().getDate().toString());
                amountText.setText(String.valueOf(item.getReminders_subscriptionData().getAmount()));
                setColor(item.getReminders_subscriptionData().getImportance());

            }
            if (item.getReminders_shopData() != null) {
                titleText.setText(item.getReminders_shopData().getName());
                dateText.setText(item.getReminders_shopData().getDeadline().toString());
                amountText.setText(String.valueOf(item.getReminders_shopData().getAmount()));
                setColor(item.getReminders_shopData().getImportance());

            }
        }

        private void setColor(int color) {
            switch (color) {
                case 1: linearCard.setBackgroundColor(context.getColor(R.color.blue_grey_white)); break;
                case 2: linearCard.setBackgroundColor(context.getColor(R.color.orange_white)); break;
                case 3: linearCard.setBackgroundColor(context.getColor(R.color.orange_black)); break;
            }
        }

    }
}
