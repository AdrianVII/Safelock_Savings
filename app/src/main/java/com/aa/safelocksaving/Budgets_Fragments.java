package com.aa.safelocksaving;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aa.safelocksaving.Operation.BudgetCardListAdapter;
import com.aa.safelocksaving.Operation.CardListAdapter;
import com.aa.safelocksaving.Operation.OPBasics;
import com.aa.safelocksaving.data.Budgets_Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import soup.neumorphism.NeumorphCardView;

public class Budgets_Fragments extends Fragment {
    private FloatingActionButton add;
    private RecyclerView budgets_cards;
    private NeumorphCardView payment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.budgets_fragments, container, false);
        add = view.findViewById(R.id.add);
        budgets_cards = view.findViewById(R.id.recycler);
        payment = view.findViewById(R.id.payment);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        add.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), New_Budgets_Activity.class));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        new OPBasics().getCardsBudgets().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    List<Budgets_Data> items = new ArrayList<>();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        items.add(dataSnapshot.getValue(Budgets_Data.class));
                    }
                    budgets_cards.setHasFixedSize(true);
                    budgets_cards.setLayoutManager(new LinearLayoutManager(getContext()));
                    budgets_cards.setAdapter(new BudgetCardListAdapter(items, getContext()));
                    budgets_cards.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                add.show();
                                payment.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            if (dy > 0 || dy > 0 && add.isShown()) {
                                add.hide();
                                payment.setVisibility(View.GONE);
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
}
