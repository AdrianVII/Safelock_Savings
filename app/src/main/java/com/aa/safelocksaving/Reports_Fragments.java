package com.aa.safelocksaving;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Reports_Fragments extends Fragment {
    private FloatingActionButton open;
    private FloatingActionButton share;
    private FloatingActionButton download;
    private boolean isOpen = false;
    private Animation fabOpen;
    private Animation fabClose;
    private Animation rotateForward;
    private Animation rotateBackward;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reports_fragments, container, false);
        open = view.findViewById(R.id.open);
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
        open.setOnClickListener(view -> {
            animateFab();
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
