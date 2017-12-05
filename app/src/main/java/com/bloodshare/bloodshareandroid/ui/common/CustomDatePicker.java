package com.bloodshare.bloodshareandroid.ui.common;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

import static com.bloodshare.bloodshareandroid.utils.ExtraConstants.EXTRA_DATE_MILLIS;

/**
 * Created by sayem on 10/20/2017.
 */

public class CustomDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    private Calendar calendar;

    public static CustomDatePicker newInstance(@Nullable Date date) {
        if (date == null) {
            date = Calendar.getInstance().getTime();
        }

        CustomDatePicker customDatePicker = new CustomDatePicker();
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_DATE_MILLIS, date.getTime());
        customDatePicker.setArguments(bundle);
        return customDatePicker;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
        calendar.setTime(new Date(getArguments().getLong(EXTRA_DATE_MILLIS)));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}
