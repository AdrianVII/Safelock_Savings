package com.aa.safelocksaving;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.aa.safelocksaving.Operation.CardListAdapter;
import com.aa.safelocksaving.Operation.OPBasics;
import com.aa.safelocksaving.data.DataUser_Reminder;
import com.aa.safelocksaving.data.Reminders_CardData;
import com.aa.safelocksaving.data.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Reminders_Fragments extends Fragment {
    private FloatingActionButton add;
    private RecyclerView reminder_cards;
    private CardListAdapter cardListAdapter;
    private List<Reminders_CardData> cardData = new ArrayList<>();

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        add.setOnClickListener(view -> startActivity(new Intent(getContext(), New_Reminders_Activity.class)));

    }

    private void getCards() {
        new OPBasics().getCardsReminders().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
