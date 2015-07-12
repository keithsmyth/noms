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
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author keithsmyth
 */
public class EntryFragment extends Fragment {

    private static final String KEY_OBJECT_ID = "object-id";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.ENGLISH);

    @Bind(R.id.txt_date) EditText dateText;
    @Bind(R.id.txt_category) EditText categoryText;
    @Bind(R.id.txt_amount) EditText amountText;
    @Bind(R.id.txt_description) EditText descriptionText;
    @Bind(R.id.chk_in_rules) CheckBox rulesCheck;
    @Bind(R.id.txt_comment) EditText commentText;

    private Navigatable nav;
    private final SubscriptionManager subscriptionManager = new SubscriptionManager();
    private String objectId;
    private Entry entry;

    public static EntryFragment createForNew() {
        return new EntryFragment();
    }

    public static EntryFragment createForExisting(String objectId) {
        final Bundle args = new Bundle();
        args.putString(KEY_OBJECT_ID, objectId);
        final EntryFragment fragment = new EntryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        nav = (Navigatable) activity;
    }

    @Override public void onDetach() {
        super.onDetach();
        nav = null;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objectId = getArguments().getString(KEY_OBJECT_ID, null);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_entry, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (TextUtils.isEmpty(objectId)) {
            dateText.setText(DATE_FORMAT.format(new Date()));
            return;
        }

        if (savedInstanceState != null) {
            return;
        }

        // load existing
        subscriptionManager.add(DataManager.get().getEntry(objectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(new Action1<Entry>() {
                    @Override public void call(Entry entry) {
                        populateExisting(entry);
                    }
                }));
    }

    private void populateExisting(Entry entry) {
        this.entry = entry;
        dateText.setText(DATE_FORMAT.format(entry.getDate()));
        categoryText.setText(entry.getCategory());
        amountText.setText(String.valueOf(entry.getAmount()));
        descriptionText.setText(entry.getDescription());
        rulesCheck.setChecked(entry.isInRules());
        commentText.setText(entry.getComment());
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_save) public void onSaveClick() {
        final Entry entry = this.entry != null ? this.entry : new Entry();

        if (!isValid(entry)) {
            return;
        }

        subscriptionManager.add(DataManager.get().saveEntry(entry)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Entry>() {
                    @Override public void call(Entry savedEntry) {
                        nav.previous();
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable e) {
                        Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                }));
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

        return isValid;
    }

    @Override public void onStop() {
        super.onStop();
        UiUtils.closeKeyboard(getActivity(), getView());
    }

    @Override public void onDestroy() {
        super.onDestroy();
        subscriptionManager.unsubscribe();
    }
}
