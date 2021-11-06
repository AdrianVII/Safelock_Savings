package com.aa.safelocksaving.Operation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aa.safelocksaving.R;
import com.aa.safelocksaving.data.Budgets_Data;

import java.util.List;

public class BudgetCardListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Budgets_Data> items;
    private Context context;

    public BudgetCardListAdapter(List<Budgets_Data> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CardViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.budgets_card_element,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Budgets_Data item = items.get(position);
        ((CardViewHolder) holder).setCardBind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView amount;
        private TextView type;
        private ImageView menu;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            amount = itemView.findViewById(R.id.amountNum);
            type = itemView.findViewById(R.id.Type);
            menu = itemView.findViewById(R.id.menu);

        }

        void setCardBind(@NonNull Budgets_Data item) {
            title.setText(item.getName().trim());
            amount.setText(String.valueOf(item.getAmount()));
            type.setText(item.getType().trim());


        }

    }

}
