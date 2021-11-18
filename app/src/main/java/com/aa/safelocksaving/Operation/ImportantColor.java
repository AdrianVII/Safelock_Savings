package com.aa.safelocksaving.Operation;

import android.app.Activity;
import android.view.View;

import com.aa.safelocksaving.DAO.DAOConfigurationData;
import com.aa.safelocksaving.R;

public class ImportantColor {

    public ImportantColor(Activity activity, View view, int color){
        switch (color) {
            case 0:
                if (!new DAOConfigurationData(activity).verifyNightMode())
                    view.setBackgroundResource(R.drawable.box_important_unselected);
                else
                    view.setBackgroundResource(R.drawable.box_important_unselected_night);
                break;
            case 1:
                if (!new DAOConfigurationData(activity).verifyNightMode()) {
                    view.setBackgroundResource(R.drawable.box_important_less);
                } else
                    view.setBackgroundResource(R.drawable.box_important_less_night);
                break;
            case 2:
                if (!new DAOConfigurationData(activity).verifyNightMode()) {
                   view.setBackgroundResource(R.drawable.box_important_important);
                } else
                    view.setBackgroundResource(R.drawable.box_important_important_night);
                break;
            case 3:
                if (!new DAOConfigurationData(activity).verifyNightMode()) {
                    view.setBackgroundResource(R.drawable.box_important_very);
                } else
                    view.setBackgroundResource(R.drawable.box_important_very_night);
                break;
        }


    }
}
