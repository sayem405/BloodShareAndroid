package com.bloodshare.bloodshareandroid.ui.login.phoneLogin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;


import com.bloodshare.bloodshareandroid.R;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public final class CountryListSpinner extends AppCompatEditText implements
        View.OnClickListener, CountryListLoadTask.Listener {
    private String textFormat;
    private DialogPopup dialogPopup;
    private CountryListAdapter countryListAdapter;
    private OnClickListener listener;
    private String selectedCountryName;

    public CountryListSpinner(Context context) {
        this(context, null, android.R.attr.spinnerStyle);
    }

    public CountryListSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.spinnerStyle);
    }

    public CountryListSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private static void hideKeyboard(Context context, View view) {
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void init() {
        if (isInEditMode()) return;
        super.setOnClickListener(this);

        countryListAdapter = new CountryListAdapter(getContext());
        dialogPopup = new DialogPopup(countryListAdapter);
        textFormat = "%1$s  +%2$d";
        selectedCountryName = "";
        final CountryInfo countryInfo = PhoneNumberUtils.getCurrentCountryInfo(getContext());
        setSpinnerText(countryInfo.countryCode, countryInfo.locale);
    }

    public void setSpinnerText(int countryCode, Locale locale) {
        setText(String.format(textFormat, CountryInfo.localeToEmoji(locale), countryCode));
        setTag(new CountryInfo(locale, countryCode));
    }

    public void setSelectedForCountry(final Locale locale, String countryCode) {
        final String countryName = locale.getDisplayName();
        if (!TextUtils.isEmpty(countryName) && !TextUtils.isEmpty(countryCode)) {
            selectedCountryName = countryName;
            setSpinnerText(Integer.parseInt(countryCode), locale);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (dialogPopup.isShowing()) {
            dialogPopup.dismiss();
        }
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        listener = l;
    }

    @Override
    public void onClick(View view) {
        if (countryListAdapter.getCount() == 0) {
            loadCountryList();
        } else {
            dialogPopup.show(countryListAdapter.getPositionForCountry(selectedCountryName));
        }
        //hideKeyboard(getContext(), CountryListSpinner.this);
        executeUserClickListener(view);
    }

    private void loadCountryList() {
        new CountryListLoadTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void executeUserClickListener(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    @Override
    public void onLoadComplete(List<CountryInfo> result) {
        countryListAdapter.setData(result);
        dialogPopup.show(countryListAdapter.getPositionForCountry(selectedCountryName));
    }

    public class DialogPopup implements DialogInterface.OnClickListener {
        //Delay for postDelayed to set selection without showing the scroll animation
        private static final long DELAY_MILLIS = 10L;
        private final CountryListAdapter listAdapter;
        private AlertDialog dialog;

        DialogPopup(CountryListAdapter adapter) {
            listAdapter = adapter;
        }

        public void dismiss() {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
        }

        public boolean isShowing() {
            return dialog != null && dialog.isShowing();
        }

        public void show(final int selected) {
            if (listAdapter == null) {
                return;
            }

            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            dialog = builder.setSingleChoiceItems(listAdapter, 0, this).create();
            dialog.setCanceledOnTouchOutside(true);
            final ListView listView = dialog.getListView();
            listView.setFastScrollEnabled(true);
            listView.setScrollbarFadingEnabled(false);
            listView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    listView.setSelection(selected);
                }
            }, DELAY_MILLIS);
            dialog.show();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            final CountryInfo countryInfo = listAdapter.getItem(which);
            selectedCountryName = countryInfo.locale.getDisplayCountry();
            setSpinnerText(countryInfo.countryCode, countryInfo.locale);
            dismiss();

        }
    }

    final static class CountryListAdapter extends ArrayAdapter<CountryInfo> implements SectionIndexer {
        private final HashMap<String, Integer> alphaIndex = new LinkedHashMap<>();
        private final HashMap<String, Integer> countryPosition = new LinkedHashMap<>();
        private String[] sections;

        public CountryListAdapter(Context context) {
            super(context, R.layout.digits_country_row, android.R.id.text1);
        }

        // The list of countries should be sorted using locale-sensitive string comparison
        public void setData(List<CountryInfo> countries) {
            // Create index and add entries to adapter
            int index = 0;
            for (CountryInfo countryInfo : countries) {
                final String key = countryInfo.locale.getDisplayCountry().substring(0, 1).toUpperCase
                        (Locale.getDefault());

                if (!alphaIndex.containsKey(key)) {
                    alphaIndex.put(key, index);
                }
                countryPosition.put(countryInfo.locale.getDisplayCountry(), index);

                index++;
                add(countryInfo);
            }

            sections = new String[alphaIndex.size()];
            alphaIndex.keySet().toArray(sections);

            notifyDataSetChanged();
        }

        @Override
        public Object[] getSections() {
            return sections;
        }

        @Override
        public int getPositionForSection(int index) {
            if (sections == null) {
                return 0;
            }

            // Check index bounds
            if (index <= 0) {
                return 0;
            }
            if (index >= sections.length) {
                index = sections.length - 1;
            }

            // Return the position
            return alphaIndex.get(sections[index]);
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }

        public int getPositionForCountry(String country) {
            final Integer position = countryPosition.get(country);
            return position == null ? 0 : position;
        }
    }
}
