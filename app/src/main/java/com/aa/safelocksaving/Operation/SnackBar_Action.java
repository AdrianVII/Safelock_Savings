package com.aa.safelocksaving.Operation;

import android.content.Context;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.aa.safelocksaving.R;
import com.google.android.material.snackbar.Snackbar;

    public class SnackBar_Action {
    private Context context;
    private int side;
    private int bottom;
    private View v;

    public SnackBar_Action(Context context, int side, int bottom, View v) {
        this.context = context;
        this.side = side;
        this.bottom = bottom;
        this.v = v;
    }

    public interface OnClickRedoListener {
        void OnClickRedo(View v);
    }

    public void showSBMargin(OnClickRedoListener listener) {
        Snackbar snackbar = Snackbar.make(v, context.getString(R.string.deletedCardText), Snackbar.LENGTH_LONG)
                .setAction(context.getString(R.string.undoText), listener::OnClickRedo).setActionTextColor(context.getColor(R.color.white));
        final View snackBarView = snackbar.getView();
        final CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackBarView.getLayoutParams();

        params.setMargins(params.leftMargin + side,
                params.topMargin,
                params.rightMargin + side,
                params.bottomMargin + bottom);
        snackBarView.setLayoutParams(params);
        snackbar.show();
    }
}
