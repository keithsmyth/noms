package com.keithsmyth.noms.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.keithsmyth.noms.R;
import com.keithsmyth.noms.data.DataManager;
import com.keithsmyth.noms.model.Entry;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author keithsmyth
 */
public class EntryFragment extends Fragment {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.ENGLISH);
    private Navigatable nav;
    private Subscription sub;

    @Bind(R.id.txt_date) EditText dateText;
    @Bind(R.id.txt_category) EditText categoryText;
    @Bind(R.id.txt_amount) EditText amountText;
    @Bind(R.id.txt_description) EditText descriptionText;
    @Bind(R.id.chk_in_rules) CheckBox rulesCheck;
    @Bind(R.id.txt_comment) EditText commentText;

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        nav = (Navigatable) activity;
    }

    @Override public void onDetach() {
        super.onDetach();
        nav = null;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_entry, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        dateText.setText(DATE_FORMAT.format(new Date()));
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_save) public void onSaveClick() {
        final Entry entry = new Entry();

        if (!isValid(entry)) {
            return;
        }

        sub = DataManager.get().saveEntry(entry)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override public void call(Boolean success) {
                        nav.previous();
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable e) {
                        Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isValid(Entry entry) {
        boolean isValid = true;

        try {
            final Date date = DATE_FORMAT.parse(dateText.getText().toString());
            entry.setDate(date);
        } catch (ParseException e) {
            isValid = false;
            dateText.setError(getString(R.string.unable_to_parse_date));
        }

        entry.setCategory(categoryText.getText().toString());
        if (TextUtils.isEmpty(entry.getCategory())) {
            isValid = false;
            categoryText.setError(getString(R.string.cannot_be_empty));
        }

        try {
            entry.setAmount(Integer.parseInt(amountText.getText().toString()));
            if (entry.getAmount() < 1) {
                isValid = false;
                amountText.setError(getString(R.string.must_greater_zero));
            }
        } catch (NumberFormatException e) {
            isValid = false;
            amountText.setError(getString(R.string.cannot_be_empty));
        }

        entry.setDescription(descriptionText.getText().toString());
        if (TextUtils.isEmpty(entry.getDescription())) {
            isValid = false;
            descriptionText.setError(getString(R.string.cannot_be_empty));
        }

        entry.setInRules(rulesCheck.isChecked());

        entry.setComment(commentText.getText().toString());
        if (TextUtils.isEmpty(entry.getComment())) {
            isValid = false;
            commentText.setError(getString(R.string.cannot_be_empty));
        }

        return isValid;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        if (sub != null) {
            sub.unsubscribe();
        }
    }
}
