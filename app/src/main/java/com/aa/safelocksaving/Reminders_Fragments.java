package com.aa.safelocksaving;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aa.safelocksaving.Dialog.Dialog_Subscription_Notification;
import com.aa.safelocksaving.Operation.AlarmOp;
import com.aa.safelocksaving.Operation.CardListAdapter;
import com.aa.safelocksaving.Operation.OPBasics;
import com.aa.safelocksaving.Operation.ViewAnimation;
import com.aa.safelocksaving.data.CardItem;
import com.aa.safelocksaving.data.Reminders_CardData;
import com.aa.safelocksaving.data.Reminders_ShopData;
import com.aa.safelocksaving.data.Reminders_SubscriptionData;
import com.aa.safelocksaving.data.Status;
import com.aa.safelocksaving.data.Type;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Reminders_Fragments extends Fragment {
    private FloatingActionButton add;
    private RecyclerView reminder_cards;
    private List<CardItem> cardItems;
    private CardListAdapter cardListAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reminders_fragments, container, false);
        add = view.findViewById(R.id.add);
        reminder_cards = view.findViewById(R.id.reminder_cards);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        add.setOnClickListener(view -> startActivity(new Intent(getContext(), New_Reminders_Activity.class)));
        new OPBasics().getCardsReminders().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    cardItems = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        int status = Objects.requireNonNull(dataSnapshot.getValue(CardItem.class)).getStatus();
                        if (status == Status.ACTIVE || status == Status.PAUSED || status == Status.LIMIT)
                        {
                            switch (Objects.requireNonNull(dataSnapshot.getValue(CardItem.class)).getType()) {
                                case 0:
                                    cardItems.add(new CardItem(Type.CARD, dataSnapshot.child("item").getValue(Reminders_CardData.class), Objects.requireNonNull(dataSnapshot.getValue(CardItem.class)).getStatus()));
                                    break;
                                case 1:
                                    cardItems.add(new CardItem(Type.SUBSCRIPTION, dataSnapshot.child("item").getValue(Reminders_SubscriptionData.class), Objects.requireNonNull(dataSnapshot.getValue(CardItem.class)).getStatus()));
                                    break;
                                case 2:
                                    cardItems.add(new CardItem(Type.SHOP, dataSnapshot.child("item").getValue(Reminders_ShopData.class), Objects.requireNonNull(dataSnapshot.getValue(CardItem.class)).getStatus()));
                                    break;
                            }
                        }
                    }
                    if (cardItems.size() == 0) new AlarmOp(getContext()).cancelAllAlarms();
                    float addY = add.getTranslationY();
                    reminder_cards.setHasFixedSize(true);
                    reminder_cards.setLayoutManager(new LinearLayoutManager(getContext()));
                    new ItemTouchHelper(swipe).attachToRecyclerView(reminder_cards);
                    cardListAdapter = new CardListAdapter(cardItems, getContext());
                    if (getArguments() != null) cardListAdapter.setNewStatus(getArguments().getLong("Id"));
                    reminder_cards.setAdapter(cardListAdapter);
                    reminder_cards.addOnScrollListener(new RecyclerView.OnScrollListener() {

                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            ViewAnimation.showUpAnimation(add, addY);
                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            if (dy > 0 || dy < 0 && add.isShown()) {
                                ViewAnimation.showDownAnimation(add);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    ItemTouchHelper.SimpleCallback swipe = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            LinearLayout CardView = viewHolder.itemView.findViewById(R.id.linearLayoutCard);
            int position = viewHolder.getBindingAdapterPosition();
            if (direction == ItemTouchHelper.RIGHT)
                cardListAdapter.removeElement(position, cardListAdapter.getID(position), CardView, CardView, Status.DELETED);
            else if (direction == ItemTouchHelper.LEFT)
                cardListAdapter.pause_resume(position, cardListAdapter.getID(position), CardView, cardListAdapter.getImportantColor(position));
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            RecyclerViewSwipeDecorator.Builder builder = new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            int position = viewHolder.getBindingAdapterPosition();
            if (position > -1) {
                if (!cardListAdapter.isPaused(position)) {
                    builder
                            .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pause))
                            .addSwipeLeftActionIcon(R.drawable.ic_pause)
                            .addSwipeLeftLabel(getString(R.string.pauseText))
                            .setSwipeLeftLabelColor(ContextCompat.getColor(requireContext(), R.color.white));
                } else {
                    builder
                            .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(), R.color.resume))
                            .addSwipeLeftActionIcon(R.drawable.ic_resume)
                            .addSwipeLeftLabel(getString(R.string.resumeText))
                            .setSwipeLeftLabelColor(ContextCompat.getColor(requireContext(), R.color.white));
                }

                builder
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(requireContext(), R.color.delete))
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_delete_outline_24)
                        .addSwipeRightLabel(getString(R.string.deleteText))
                        .setSwipeRightLabelColor(ContextCompat.getColor(requireContext(), R.color.white));

                builder
                        .create()
                        .decorate();
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

}