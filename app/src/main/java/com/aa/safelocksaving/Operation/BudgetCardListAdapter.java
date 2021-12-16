package com.aa.safelocksaving.Operation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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

import com.aa.safelocksaving.Budgets_Edit_Card;
import com.aa.safelocksaving.DAO.DAOUser;
import com.aa.safelocksaving.Dialog.Dialog_Upload;
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

    @SuppressLint("NotifyDataSetChanged")
    public void removeAll() {
        Dialog_Upload dialogUpload = new Dialog_Upload(context);
        dialogUpload.start();
        new OPBasics().removeAllBudgets().addOnCompleteListener(task -> {
            dialogUpload.dismiss();
            if (task.isSuccessful()) {
                items.clear();
                notifyDataSetChanged();
            }
        }).addOnFailureListener(e -> {
            dialogUpload.dismiss();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
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
            menu.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(context, menu);
                popupMenu.inflate(R.menu.menu_budgets_card);
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.menuEdit:
                            Intent intent = new Intent(context, Budgets_Edit_Card.class);
                            intent.putExtra("id",item.getID());
                            intent.putExtra("name", item.getName());
                            intent.putExtra("amount", item.getAmount());
                            intent.putExtra("type", item.getType());
                            context.startActivity(intent);
                            return true;
                        case R.id.menuDelete:
                            final int position = getAdapterPosition();
                            Budgets_Data auxItem = item;
                            long id = item.getID();
                            new OPBasics().removeBudgetsCard(id).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    items.remove(position);
                                    notifyItemRemoved(position);
                                    new SnackBar_Action(context, 32, 32, view).showSBMargin(v -> {
                                        new OPBasics().addBudgetsCards(auxItem, String.valueOf(id)).addOnCompleteListener(task1 -> {
                                            items.add(position, auxItem);
                                            notifyItemInserted(position);
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


}
