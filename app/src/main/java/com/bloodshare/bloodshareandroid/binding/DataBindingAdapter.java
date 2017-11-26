package com.bloodshare.bloodshareandroid.binding;

import android.databinding.BindingAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.bloodshare.bloodshareandroid.R;
import com.jokerlab.jokerstool.DateUtil;

import java.util.Date;

/**
 * Created by sayem on 11/21/2017.
 */

public class DataBindingAdapter {
    @BindingAdapter("bloodGroup")
    public static void setBloodGroupToSpinner(Spinner spinner, String bloodGroup) {
        String[] bloodGroups = spinner.getResources().getStringArray(R.array.blood_groups);
        for (int i = 0; i < bloodGroups.length; i++) {
            if (bloodGroups[i].equals(bloodGroup)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    @BindingAdapter("dob")
    public static void setDateOfBirth(TextView textView, Date dateOfBirth) {
        if (dateOfBirth != null) {
            textView.setText(DateUtil.getDateByFormat(dateOfBirth, DateUtil.DATE_FORMAT_1));
        }
    }
}
