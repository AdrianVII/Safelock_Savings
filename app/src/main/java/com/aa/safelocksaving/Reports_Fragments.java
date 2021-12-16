package com.aa.safelocksaving;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aa.safelocksaving.Operation.OPBasics;
import com.aa.safelocksaving.Operation.ReportCardListAdapter;
import com.aa.safelocksaving.data.CardItem;
import com.aa.safelocksaving.data.Reminders_CardData;
import com.aa.safelocksaving.data.Reminders_ShopData;
import com.aa.safelocksaving.data.Reminders_SubscriptionData;
import com.aa.safelocksaving.data.Type;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Reports_Fragments extends Fragment {
    private FloatingActionButton open;
    private FloatingActionButton share;
    private FloatingActionButton download;
    private boolean isOpen = false;
    private Animation fabOpen;
    private Animation fabClose;
    private Animation rotateForward;
    private Animation rotateBackward;
    private RecyclerView cardList;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reports_fragments, container, false);
        open = view.findViewById(R.id.open);
        cardList = view.findViewById(R.id.report_card);
        share = view.findViewById(R.id.share);
        download = view.findViewById(R.id.download);
        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backwawrd);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        open.setOnClickListener(view -> animateFab());
        new OPBasics().getCardsReminders().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<CardItem> items = new ArrayList<>();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        switch (Objects.requireNonNull(dataSnapshot.getValue(CardItem.class)).getType()) {
                            case Type.CARD:
                                items.add(new CardItem(Type.CARD, dataSnapshot.child("item").getValue(Reminders_CardData.class), Objects.requireNonNull(dataSnapshot.getValue(CardItem.class)).getStatus()));
                                break;
                            case Type.SUBSCRIPTION:
                                items.add(new CardItem(Type.SUBSCRIPTION, dataSnapshot.child("item").getValue(Reminders_SubscriptionData.class), Objects.requireNonNull(dataSnapshot.getValue(CardItem.class)).getStatus()));
                                break;
                            case Type.SHOP:
                                items.add(new CardItem(Type.SHOP, dataSnapshot.child("item").getValue(Reminders_ShopData.class), Objects.requireNonNull(dataSnapshot.getValue(CardItem.class)).getStatus()));
                                break;
                        }
                    }
                    cardList.setHasFixedSize(true);
                    cardList.setLayoutManager(new LinearLayoutManager(getContext()));
                    ReportCardListAdapter cardListAdapter = new ReportCardListAdapter(items, getContext());
                    cardList.setAdapter(cardListAdapter);
                    download.setOnClickListener(view -> cardListAdapter.createPDF());
                    share.setOnClickListener(view -> cardListAdapter.sharePDF());
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
            share.startAnimation(fabClose);
            download.startAnimation(fabClose);
            share.setClickable(false);
            download.setClickable(false);
            isOpen = false;
        } else {
            open.startAnimation(rotateForward);
            share.startAnimation(fabOpen);
            download.startAnimation(fabOpen);
            share.setClickable(true);
            download.setClickable(true);
            isOpen = true;
        }
    }
}

