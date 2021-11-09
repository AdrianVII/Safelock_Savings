package com.aa.safelocksaving;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aa.safelocksaving.DAO.DAOBudgets;
import com.aa.safelocksaving.Operation.BudgetCardListAdapter;
import com.aa.safelocksaving.Operation.OPBasics;
import com.aa.safelocksaving.Operation.SnackBar_Action;
import com.aa.safelocksaving.Operation.ViewAnimation;
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
    private FloatingActionButton trash;
    private FloatingActionButton open;
    private EditText paymentNumber;
    private TextView PriceTotal;
    private RecyclerView budgets_cards;
    private NeumorphCardView payment;
    private boolean isOpen = false;
    private Animation fabOpen;
    private Animation fabClose;
    private Animation rotateForward;
    private Animation rotateBackward;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.budgets_fragments, container, false);
        add = view.findViewById(R.id.add);
        budgets_cards = view.findViewById(R.id.recycler);
        payment = view.findViewById(R.id.payment);
        paymentNumber = view.findViewById(R.id.paymentNumber);
        PriceTotal = view.findViewById(R.id.PriceTotal);
        trash = view.findViewById(R.id.trash);
        open = view.findViewById(R.id.open);
        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backwawrd);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        add.setOnClickListener(view -> {
            startActivity(new Intent(getContext(), New_Budgets_Activity.class));
        });
        open.setOnClickListener(view -> {
            animateFab();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        paymentNumber.setText(String.valueOf(new DAOBudgets(getActivity()).getAmount()));
        new OPBasics().getCardsBudgets().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Budgets_Data> items = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        items.add(dataSnapshot.getValue(Budgets_Data.class));
                    }
                    float paymentY = payment.getTranslationY();
                    float openY = open.getTranslationY();
                    float addY = add.getTranslationY();
                    float trashY = trash.getTranslationY();
                    budgets_cards.setHasFixedSize(true);
                    budgets_cards.setLayoutManager(new LinearLayoutManager(getContext()));
                    BudgetCardListAdapter budgetCardListAdapter = new BudgetCardListAdapter(items, getContext());
                    sum(items);
                    paymentNumber.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (!paymentNumber.getText().toString().isEmpty()) {
                                new DAOBudgets(getActivity()).setAmount(Long.parseLong(paymentNumber.getText().toString()));
                            }
                            sum(items);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    budgets_cards.setAdapter(budgetCardListAdapter);
                    trash.setOnClickListener(view -> {
                        budgetCardListAdapter.removeAll();
                        paymentNumber.setText("");
                        /*new SnackBar_Action(getContext(), 32, 32, view).showSBMargin(v -> {
                            new OPBasics().addAllBudgetsCards(auxItem);
                            budgets_cards.setAdapter(new BudgetCardListAdapter(auxItem, getContext()));
                        });*/
                    });

                    budgets_cards.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                ViewAnimation.showUpAnimation(open, openY);
                                ViewAnimation.showUpAnimation(payment, paymentY);
                                if (isOpen) {
                                    ViewAnimation.showUpAnimation(add, addY);
                                    ViewAnimation.showUpAnimation(trash, trashY);
                                }
                            }
                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            if (dy > 0 || dy < 0 && open.isShown()) {
                                ViewAnimation.showDownAnimation(open);
                                ViewAnimation.showDownAnimation(payment);
                                if (isOpen) {
                                    ViewAnimation.showDownAnimation(add);
                                    ViewAnimation.showDownAnimation(trash);
                                }
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

    private void animateFab() {
        if (isOpen) {
            open.startAnimation(rotateBackward);
            add.startAnimation(fabClose);
            trash.startAnimation(fabClose);
            add.setClickable(false);
            trash.setClickable(false);
            isOpen = false;
        } else {
            open.startAnimation(rotateForward);
            add.startAnimation(fabOpen);
            trash.startAnimation(fabOpen);
            add.setClickable(true);
            trash.setClickable(true);
            isOpen = true;
        }

    }

    private void sum(List<Budgets_Data> items) {
        double sum = 0;
        for (int i = 0; i < items.size(); i++) {
            sum += items.get(i).getAmount();
        }
        PriceTotal.setText(String.valueOf(sum));
        if (paymentNumber.getText().toString().isEmpty()) {
            paymentNumber.setError("Metele una cantidad, pobre c:");
            paymentNumber.requestFocus();
            return;
        }
        if (sum > Double.parseDouble(paymentNumber.getText().toString())) {
            payment.setBackgroundColor(getContext().getColor(R.color.red_black));
        } else payment.setBackgroundColor(getContext().getColor(R.color.white_background));
    }
}
