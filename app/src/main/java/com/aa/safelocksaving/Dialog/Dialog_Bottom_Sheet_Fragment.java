package com.aa.safelocksaving.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aa.safelocksaving.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Dialog_Bottom_Sheet_Fragment extends BottomSheetDialogFragment {
    public static final String TAG = "TAG";
    private LinearLayout Folder;
    private LinearLayout Camera;
    public interface OnClickListener {
        void OnClickFolder(View view);
        void OnClickCamera(View view);
    }
    private OnClickListener listener;

    public Dialog_Bottom_Sheet_Fragment(OnClickListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        Folder = view.findViewById(R.id.Folder);
        Camera = view.findViewById(R.id.Camera);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle);
    }

    @Override
    public void onStart() {
        super.onStart();
        Folder.setOnClickListener(view -> {
            dismiss();
            listener.OnClickFolder(view);
        });
        Camera.setOnClickListener(view -> {
            dismiss();
            listener.OnClickCamera(view);
        });
    }
}
